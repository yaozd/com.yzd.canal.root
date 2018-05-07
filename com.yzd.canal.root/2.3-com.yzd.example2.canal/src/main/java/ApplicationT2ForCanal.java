import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import java.util.concurrent.CountDownLatch;

@SpringBootApplication
@ComponentScan("com.yzd.example2.canal")
public class ApplicationT2ForCanal {
    private static final Logger logger = LoggerFactory.getLogger(ApplicationT2ForCanal.class);

    @Bean
    public CountDownLatch closeLatch() {
        return new CountDownLatch(1);
    }

    public static void main(String[] args) throws InterruptedException {

        logger.info("项目启动--BEGIN");
        SpringApplication app = new SpringApplication(ApplicationT2ForCanal.class);
        app.setBannerMode(Banner.Mode.OFF);
        ApplicationContext ctx = app.run(args);
        logger.info("项目启动--END");
        CountDownLatch closeLatch = ctx.getBean(CountDownLatch.class);
        closeLatch.await();
    }
}

