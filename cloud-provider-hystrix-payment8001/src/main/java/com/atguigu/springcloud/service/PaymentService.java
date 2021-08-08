package com.atguigu.springcloud.service;

import cn.hutool.core.util.IdUtil;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.concurrent.TimeUnit;

@Service
public class PaymentService {

    /**
     * 正常访问,肯定ok
     *
     * @param id
     * @return
     */
    public String paymentInfo_OK(Integer id) {
        return "线程池：  " + Thread.currentThread().getName() + "  paymentInfo_OK,id:  " + id + "\t" + "O(∩_∩)O哈哈~";
    }

    // fallbackMethod代表如果目标方法出错了，由哪个方法进行兜底 超时或报错都会走兜底方法
    @HystrixCommand(fallbackMethod = "paymentInfo_TimeOutHandler", commandProperties = {
            // name是使用单独的线程池进行处理 线程隔离 规定超时等待时长，单位为毫秒,超过设定的时长后，就会走兜底的方法
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "5000")
    })
    public String paymentInfo_TimeOut(Integer id) {
        int timeNumber = 3000;
//         int age = 10/0;
        try {
            TimeUnit.MILLISECONDS.sleep(timeNumber);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "线程池：  " + Thread.currentThread().getName() + "  paymentInfo_timeout,id:  " + id + "\t" + "O(∩_∩)O哈哈~" + "耗时毫秒";
    }

    public String paymentInfo_TimeOutHandler(Integer id) {
        return "线程池：  " + Thread.currentThread().getName() + "  8001系统繁忙或运行报错，请稍后再试,id:  " + id + "\t" + "o(╥﹏╥)o";
    }

    // =====服务熔断=====
    //服务熔断 属性名怕出错可以使用HystrixPropertiesManager类中的对应常量
    @HystrixCommand(fallbackMethod = "paymentCircuitBreaker_fallback", commandProperties = {
            // 如果在10000秒内请求10次，有60%失败了，则开启断路器
            @HystrixProperty(name = "circuitBreaker.enabled", value = "true"),  //是否开启断路器
            // circuitBreaker.requestVolumeThreshold：滑动窗口大小，即触发熔断的最小请求数量，即在一定的时间窗口内请求达到一定的次数,默认为 20。举个例子，一共只有 19 个请求落在窗口内，全都失败了，也不会触发熔断
            @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "10"),   //请求次数
            // 熔断多长时间后，尝试放一次请求进来，默认5秒 时间窗口期是指保险丝开启后经过的一段时间再转换为半开状态
            @HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "10000"),  //时间范围(窗口期)  窗口期是经过多久后恢复一次尝试
            // 失败率达到多少百分比后熔断 默认值：50
            @HystrixProperty(name = "circuitBreaker.errorThresholdPercentage", value = "60"), //失败率达到多少后跳闸
            // 配置类里面的default_metricsRollingStatisticalWindow（默认10秒）表示在该时间内达到阈值则打开断路器
    })
    public String paymentCircuitBreaker(@PathVariable("id") Integer id) {
        if (id < 0) {
            throw new RuntimeException("*****id 不能负数");
        }
        String serialNumber = IdUtil.simpleUUID();

        return Thread.currentThread().getName() + "\t" + "调用成功,流水号：" + serialNumber;
    }

    public String paymentCircuitBreaker_fallback(@PathVariable("id") Integer id) {
        return "id 不能负数，请稍候再试,(┬＿┬)/~~     id: " + id;
    }


}
