package cn.framework.kafka;


import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Kafka 启动类
 * @author LiuJingWei
 */
@Slf4j
public class KafkaApplication {


    private static KafkaApplication applicationServer;
    private ClassPathXmlApplicationContext context = null;
    private String configLocation;

    private KafkaApplication(String configLocation) {
        this.configLocation = configLocation;
    }

    public static KafkaApplication getApplicationServer() {
        return applicationServer;
    }

    public void start() {
        try {
            long start = System.currentTimeMillis();
            context = new ClassPathXmlApplicationContext();
            context.setConfigLocation(configLocation);
            context.refresh();
            log.info("[Kafka服务]启动成功，配置项已加载！耗时：" + (System.currentTimeMillis() - start) + "ms");
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
            log.error("[Kafka服务]启动失败，请查看异常日志", e);
        }
    }

    public void stop() {
        if (context != null) {
            context.close();
        }
    }

    public static void main(String[] args) {
        KafkaApplication server = new KafkaApplication("config/application-context.xml");
        server.start();
        // 保持服务器挂起，一直运行
        while (true) {
            try {
                Thread.sleep(1000 * 60 * 60);
            } catch (InterruptedException e) {
            }
        }
    }

}
