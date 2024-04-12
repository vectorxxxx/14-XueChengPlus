package com.xuecheng.media;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author VectorX
 * @version V1.0
 * @description
 * @date 2024-04-12 11:05:35
 */
public class JobTest
{
    public static void main(String[] agrs) {
        // testTimer();
        testScheduledExecutor();
    }

    private static void testTimer() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask()
        {
            @Override
            public void run() {
                System.out.println("todo sth.");
            }
        }, 1000, 2000);  //1秒后开始调度，每2秒执行一次
    }

    private static void testScheduledExecutor() {
        final ScheduledExecutorService service = Executors.newScheduledThreadPool(10);
        service.scheduleAtFixedRate(() -> System.out.println("todo sth."), 1, 2, TimeUnit.SECONDS);
    }
}
