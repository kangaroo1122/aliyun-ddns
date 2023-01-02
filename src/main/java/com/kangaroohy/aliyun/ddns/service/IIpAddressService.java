package com.kangaroohy.aliyun.ddns.service;

/**
 * 类 IIpAddressService 功能描述：<br/>
 *
 * @author kangaroo hy
 * @version 0.0.1
 * @date 2023/1/1 23:37
 */
public interface IIpAddressService {
    /**
     * 刷新IP解析，无关本地ip缓存
     *
     * @param subDomainName
     * @return
     * @throws Exception
     */
    String refresh(String subDomainName) throws Exception;

    /**
     * 刷新IP解析
     *
     * @throws Exception
     */
    void autoRefresh() throws Exception;
}
