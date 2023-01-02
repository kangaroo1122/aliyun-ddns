package com.kangaroohy.aliyun.ddns.controller;

import com.kangaroohy.aliyun.ddns.service.IIpAddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.constraints.NotBlank;

/**
 * 类 DdnsController 功能描述：<br/>
 *
 * @author kangaroo hy
 * @version 0.0.1
 * @date 2023/1/1 23:50
 */
@RestController
@Validated
public class DdnsController {

    @Autowired
    private IIpAddressService ipAddressService;

    @GetMapping("/")
    public ModelAndView index(@NotBlank(message = "请输入需要刷新的二级域名") String subDomainName) {
        return new ModelAndView("redirect:/refresh?subDomainName=" + subDomainName);
    }

    @GetMapping("/refresh")
    public String refresh(@NotBlank(message = "请输入需要刷新的二级域名") String subDomainName) throws Exception {
        String ip = ipAddressService.refresh(subDomainName);
        return ip != null ? "刷新成功！当前ip地址：" + ip : "刷新失败！未知异常";
    }
}
