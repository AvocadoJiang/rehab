package xin.nbjzj.rehab.core.config;

import java.io.File;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class StaticResConfig extends WebMvcConfigurerAdapter {
	
	
	
	@Value("${userphoto.savepath}")
	private String usersavepath;
	
	@Value("${userphoto.urlprefix}")
	private String userurlprefix;
	
	
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		File file = null;
		file = new File(usersavepath);
		if(!file.exists()) {
			file.mkdirs();
		}
		
		registry.addResourceHandler("/users/**").addResourceLocations("file:"+usersavepath);
		
		
		
		registry.addResourceHandler("/**").addResourceLocations("classpath:/static/");
        registry.addResourceHandler("swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
	}
}
