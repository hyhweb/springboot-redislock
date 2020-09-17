package com.springboot.reidslock.controller;

import com.springboot.reidslock.dao.SecKillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Create by hyhweb on 2020/9/17 17:05
 */
@RestController
@RequestMapping("/skill")
public class SecKillController {
    @Autowired
    private SecKillService secKillService;

    @GetMapping("/getProduct/{productId}")
    public String getProduct(@PathVariable String productId) {
        return secKillService.querySecKillProductInfo(productId);
    }

    @GetMapping("/getOrder/{productId}")
    public String getOrder(@PathVariable String productId) {
        secKillService.orderProductMockDiffUser(productId);
       return secKillService.querySecKillProductInfo(productId);
    }
}
