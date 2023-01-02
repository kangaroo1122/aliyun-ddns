package com.kangaroohy.aliyun.ddns.config;

import com.kangaroohy.aliyun.ddns.enums.RecordType;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 类 DdnsToken 功能描述：<br/>
 *
 * @author kangaroo hy
 * @version 0.0.1
 * @date 2022/11/7 14:59
 */
@Data
@Component
@ConfigurationProperties(prefix = "ddns.aliyun")
public class DdnsToken {

    /**
     * 查询本机外网IP信息，ipv4：<a href="https://4.ipw.cn">https://4.ipw.cn</a>，ipv6：<a href="https://6.ipw.cn">https://6.ipw.cn</a>
     */
    private  String ipv4QueryDomain = "https://4.ipw.cn";

    /**
     * 查询本机外网IP信息，ipv4：<a href="https://4.ipw.cn">https://4.ipw.cn</a>，ipv6：<a href="https://6.ipw.cn">https://6.ipw.cn</a>
     */
    private  String ipv6QueryDomain = "https://6.ipw.cn";

    /**
     * 刷新时间，默认每3分钟刷新一次
     */
    private String cron = "0 0/3 * * * ?";

    /**
     * accessKeyId
     */
    private String accessKeyId;

    /**
     * accessKeySecret
     */
    private String accessKeySecret;

    /**
     * 域名列表
     */
    private List<Domain> domain = new ArrayList<>();

    @Data
    public static class Domain {

        /**
         * 顶级域名
         */
        private String domainName;

        /**
         * 二级域名，如：@，www等
         */
        private String subDomainName;

        /**
         * 记录类型，ipv4：A，ipv6：AAAA
         */
        private RecordType recordType = RecordType.AAAA;
    }
}
