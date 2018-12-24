package xin.nbjzj.rehab;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
/**
 * 启动类包级别要大于或等于其它类，以保证其它类的注解可以被扫描到
 * @author Jason Chiang
 *
 */
@SpringBootApplication
@EnableScheduling
@EnableAsync
public class RehabApplication {

	public static void main(String[] args) {
		SpringApplication.run(RehabApplication.class, args);
	}
}
