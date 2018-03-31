package pl.piomin.microservices.edge;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.DefaultErrorAttributes;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.WebApplicationContext;

import javax.annotation.PostConstruct;

@SpringBootApplication
@EnableZuulProxy
@EnableHystrix
@EnableCircuitBreaker
public class Application {



	public static void main(String[] args) {
		new SpringApplicationBuilder(Application.class).web(true).run(args);
	}

	@Autowired
	ApplicationContext context;

	@PostConstruct
	public void retryRetryListener(){
		System.out.println("%%%%  PostConstruct  %%%%%%");
		RetryTemplate retryTemplate = (RetryTemplate) context.getBean("retryTemplate");
		retryTemplate.registerListener(new ZuulRetryListener());
	}

//	@Bean
//	public AlwaysSampler defaultSampler() {
//	  return new AlwaysSampler();
//	}

	@Bean
	public DefaultErrorAttributes errorAttributes() {
		return new ErrorAttributes();
	}

	@Bean
	public AccessFilter accessFilter() {
		return new AccessFilter();
	}

	@Bean
	public FilterRegistrationBean indexFilterRegistration() {
		FilterRegistrationBean registration = new FilterRegistrationBean(new IndexFilter());
		registration.addUrlPatterns("/");
		return registration;
	}
	
}
