package com.snailmann.demo.redis.rate;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author liwenjie
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class TRateTest {

    @Autowired
    TRate rate = new TRate();

    @Test
    public void test() throws InterruptedException {
        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(15);
        for(int i = 0; i < 1000; i++ ){
            executor.execute(() -> {
                boolean res = rate.getToken("rate-1");
                //System.out.println(Thread.currentThread().getName() + " : res = " + res);
            });
        }

        Thread.sleep(100000);

    }

}