package com.kangaroohy.aliyun.ddns.config;

import com.kangaroohy.aliyun.ddns.service.IIpAddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * 类 CustomApplicationRunner 功能描述：<br/>
 *
 * @author kangaroo hy
 * @version 0.0.1
 * @date 2023/1/2 22:29
 */
@Component
public class CustomApplicationRunner implements ApplicationRunner {
    @Autowired
    private IIpAddressService ipAddressService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        try {
            ipAddressService.autoRefresh();
        } catch (Exception ignored) {
        }
    }
}
