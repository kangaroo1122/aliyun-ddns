package com.kangaroohy.aliyun.ddns.controller;

import com.kangaroohy.aliyun.ddns.entity.DomainListVO;
import com.kangaroohy.aliyun.ddns.result.RestResult;
import com.kangaroohy.aliyun.ddns.service.IIpAddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.constraints.NotBlank;
import java.util.List;

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
    public ModelAndView index() {
        return new ModelAndView("/index");
    }

    @GetMapping("/domains")
    public ResponseEntity<RestResult<List<DomainListVO>>> domains() {
        return RestResult.ok(ipAddressService.findDomainList());
    }

    @PostMapping("/refresh")
    public ResponseEntity<RestResult<String>> refresh(@NotBlank(message = "请输入需要刷新的二级域名") @RequestParam String subDomainName) throws Exception {
        String ip = ipAddressService.refresh(subDomainName);
        return ip != null ? RestResult.ok("刷新成功！当前ip地址：" + ip) : RestResult.error("刷新失败！未知异常");
    }
}
