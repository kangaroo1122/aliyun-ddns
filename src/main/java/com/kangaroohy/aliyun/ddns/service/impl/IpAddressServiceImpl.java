package com.kangaroohy.aliyun.ddns.service.impl;

import com.alibaba.fastjson.JSON;
import com.aliyun.alidns20150109.Client;
import com.aliyun.alidns20150109.models.*;
import com.aliyun.teaopenapi.models.Config;
import com.kangaroohy.aliyun.ddns.config.DdnsToken;
import com.kangaroohy.aliyun.ddns.entity.DomainListVO;
import com.kangaroohy.aliyun.ddns.enums.RecordType;
import com.kangaroohy.aliyun.ddns.service.IIpAddressService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 类 IpAddressServiceImpl 功能描述：<br/>
 *
 * @author kangaroo hy
 * @version 0.0.1
 * @date 2023/1/1 23:38
 */
@Service
@Slf4j
public class IpAddressServiceImpl implements IIpAddressService {
    @Autowired
    private DdnsToken ddnsToken;

    @Autowired
    private RestTemplate restTemplate;

    private Client client;

    private Map<String, String> currentDdnsIp = new ConcurrentHashMap<>();

    @PostConstruct
    public void initClient() throws Exception {
        this.client = createClient();
    }

    /**
     * 3分钟执行一次
     */
    @Scheduled(cron = "${ddns.aliyun.cron:0 0/3 * * * ?}")
    public void configureTasks() throws Exception {
        autoRefresh();
    }

    /**
     * 强制刷新，无关缓存ip
     */
    @Override
    public String refresh(String subDomainName) throws Exception {
        List<DdnsToken.Domain> domainList = ddnsToken.getDomain();
        DdnsToken.Domain domain = domainList.stream().filter(item -> item.getSubDomainName().equals(subDomainName)).findFirst()
                .orElseThrow(() -> new IllegalArgumentException("未找到对应的二级域名配置信息：" + subDomainName));
        String currentIp = getIpAddress(domain);
        DescribeDomainRecordsResponseBody.DescribeDomainRecordsResponseBodyDomainRecordsRecord domainDnsRecord = this.getDomainDnsRecord(domain);
        if (domainDnsRecord.getValue().equals(currentIp)) {
            throw new RuntimeException(String.format("已是最新IP：%s，无需同步", currentIp));
        } else {
            this.updateDomainDnsRecord(domainDnsRecord, currentIp);
        }
        return currentIp;
    }

    @Override
    public void autoRefresh() throws Exception {
        List<DdnsToken.Domain> domainList = ddnsToken.getDomain();
        for (DdnsToken.Domain domain : domainList) {
            String currentIp = getIpAddress(domain);
            if (currentDdnsIp.containsKey(domain.getSubDomainName())) {
                if (!currentDdnsIp.get(domain.getSubDomainName()).equals(currentIp)) {
                    log.info("当前本地IP：{}，解析IP：{}，不一致，需更新", currentIp, currentDdnsIp.get(domain.getSubDomainName()));
                    // 刷新解析记录
                    this.updateDomainDnsRecord(this.getDomainDnsRecord(domain), currentIp);
                }
            } else {
                // 获取解析记录
                DescribeDomainRecordsResponseBody.DescribeDomainRecordsResponseBodyDomainRecordsRecord domainDnsRecord = this.getDomainDnsRecord(domain);
                if (!domainDnsRecord.getValue().equals(currentIp)) {
                    log.info("当前本地IP：{}，解析IP：{}，不一致，需更新", currentIp, domainDnsRecord.getValue());
                    // 刷新解析记录
                    this.updateDomainDnsRecord(domainDnsRecord, currentIp);
                }
            }
        }
    }

    @Override
    public List<DomainListVO> findDomainList() {
        List<DomainListVO> domainList = new ArrayList<>();
        List<DdnsToken.Domain> domains = ddnsToken.getDomain();
        domains.forEach(item ->
                domainList.add(DomainListVO.builder()
                        .domainName(item.getDomainName())
                        .subDomainName(item.getSubDomainName())
                        .recordType(item.getRecordType().getType())
                        .build()));
        return domainList;
    }

    private DescribeDomainRecordsResponseBody.DescribeDomainRecordsResponseBodyDomainRecordsRecord getDomainDnsRecord(DdnsToken.Domain domain) {
        DescribeDomainRecordsRequest describeDomainRecordsRequest = new DescribeDomainRecordsRequest()
                .setDomainName(domain.getDomainName())
                .setRRKeyWord(domain.getSubDomainName())
                .setType(domain.getRecordType().getType());
        DescribeDomainRecordsResponse domainRecords;
        DescribeDomainRecordsResponseBody.DescribeDomainRecordsResponseBodyDomainRecordsRecord recordsRecord = null;
        try {
            domainRecords = client.describeDomainRecords(describeDomainRecordsRequest);
            List<DescribeDomainRecordsResponseBody.DescribeDomainRecordsResponseBodyDomainRecordsRecord> records = domainRecords.getBody().getDomainRecords().getRecord();
            recordsRecord = records.get(0);
            log.info("获取解析记录：{}", JSON.toJSONString(recordsRecord));
        } catch (Exception e) {
            log.error("Error while trying to describe domain records：{}.{}，exception {}", domain.getSubDomainName(), domain.getDomainName(), e.getMessage(), e);
        }
        return Optional.ofNullable(recordsRecord).orElseThrow(() -> new RuntimeException("获取解析记录信息时出现异常"));
    }

    private void updateDomainDnsRecord(DescribeDomainRecordsResponseBody.DescribeDomainRecordsResponseBodyDomainRecordsRecord domainDnsRecord, String newIp) throws Exception {
        // 修改记录
        UpdateDomainRecordRequest updateDomainRecordRequest = new UpdateDomainRecordRequest()
                .setRecordId(domainDnsRecord.getRecordId())
                .setRR(domainDnsRecord.getRR())
                .setType(domainDnsRecord.getType())
                .setValue(newIp);
        // 修改记录
        UpdateDomainRecordResponse updateDomainRecordResponse = client.updateDomainRecord(updateDomainRecordRequest);
        log.info("修改解析结果：{}", JSON.toJSONString(updateDomainRecordResponse));
        if (updateDomainRecordResponse.getStatusCode() == 200) {
            currentDdnsIp.put(domainDnsRecord.getRR(), newIp);
        }
    }

    private Client createClient() throws Exception {
        Config config = new Config()
                .setAccessKeyId(ddnsToken.getAccessKeyId())
                .setAccessKeySecret(ddnsToken.getAccessKeySecret());
        config.endpoint = "alidns.aliyuncs.com";
        return new Client(config);
    }

    private String getIpAddress(DdnsToken.Domain domain) {
        String currentIp;
        if (domain.getRecordType().equals(RecordType.A)) {
            currentIp = this.getIpv4Address();
        } else {
            currentIp = this.getIpv6Address();
        }
        if (currentIp == null) {
            throw new IllegalArgumentException("获取本地IP失败");
        }
        return currentIp;
    }

    private String getIpv4Address() {
        return restTemplate.getForObject(ddnsToken.getIpv4QueryDomain(), String.class);
    }

    private String getIpv6Address() {
        return restTemplate.getForObject(ddnsToken.getIpv6QueryDomain(), String.class);
    }
}
