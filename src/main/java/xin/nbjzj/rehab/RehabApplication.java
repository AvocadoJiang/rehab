package xin.nbjzj.rehab;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
/**
 * 启动类包级别要大于或等于其它类，以保证其它类的注解可以被扫描到
 * @author Jason Chiang
 *
 */
@SpringBootApplication
public class RehabApplication {

	public static void main(String[] args) {
		SpringApplication.run(RehabApplication.class, args);
	}
}
