package com.springboot.reidslock.service.serviceImpl;

import com.springboot.reidslock.component.RedisLock;
import com.springboot.reidslock.dao.SecKillService;
import com.sun.org.apache.xml.internal.security.keys.KeyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Create by hyhweb on 2020/9/17 16:32
 */
@Service
public class SecKillServiceImpl implements SecKillService {
    @Autowired
    private RedisLock redisLock;
    //超时时间10秒
    private static final int TIMEOUT = 1 * 1 ;

    static Map<String,Integer> products;
    static Map<String,Integer> stock;
    static Map<String,String> orders;
    static {
        products =new HashMap<>();
        stock =new HashMap<>();
        orders =new HashMap<>();
        products.put("123456789", 10000);
        stock.put("123456789", 10000);
    }

    private  String queryMap(String productId) {
        return "猪肉特价，限量份数："
                +products.get(productId)+"份,还剩："
                +stock.get(productId)+"份,"
                +"目前该商品成功下单用户数量为："
                +orders.size()+"人";
    }

    @Override
    public String querySecKillProductInfo(String productId) {
        return queryMap(productId);
    }

    @Override
    public void orderProductMockDiffUser(String productId) {
        //加锁
        long time = System.currentTimeMillis()+TIMEOUT;
        if (!redisLock.lock(productId, String.valueOf(time))) {
            throw new RuntimeException("没抢到哦，继续努力");
        }
        int stockNum = stock.get(productId);
        if (stockNum == 0) {
            throw new RuntimeException("活动已结束");
        }else{
            orders.put(UUID.randomUUID().toString(), productId);
            stockNum =stockNum -1;
            try {
                Thread.sleep(100);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            stock.put(productId, stockNum);
        }
        redisLock.unlock(productId, String.valueOf(stockNum));
    }
}
