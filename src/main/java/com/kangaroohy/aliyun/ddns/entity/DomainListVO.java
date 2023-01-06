package com.kangaroohy.aliyun.ddns.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 类 DomainListVO 功能描述：<br/>
 *
 * @author kangaroo hy
 * @version 0.0.1
 * @date 2023/1/6 10:23
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DomainListVO implements Serializable {

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
    private String recordType;
}
