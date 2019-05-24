package cn.com.chinaunicom;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 启动类
 * @author dell
 */
@SpringBootApplication(scanBasePackages = {"cn.com.chinaunicom"})
public class ElasticSearchApiServerApp {

    public static void main( String[] args )
    {
        SpringApplication application = new SpringApplication(ElasticSearchApiServerApp.class);
        application.setWebEnvironment(true);
        application.run();
    }

}
