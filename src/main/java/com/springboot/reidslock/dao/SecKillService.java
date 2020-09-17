package com.springboot.reidslock.dao;

import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Create by hyhweb on 2020/9/17 16:30
 */
public interface SecKillService {
    /**
     * 查询秒杀活动特价商品的信息
     *
     * @param productId
     * @return
     */
    String querySecKillProductInfo(String productId);

    /**
     * 模拟不同用户秒杀同一商品的请求
     *
     * @param productId
     * @return
     */
    void orderProductMockDiffUser(String productId);
}
