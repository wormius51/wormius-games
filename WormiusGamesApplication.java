package wormius.games.wormiusgames;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import wormius.games.wormiusgames.filters.ApiKeyFilter;
import wormius.games.wormiusgames.helpers.IPageMaker;
import wormius.games.wormiusgames.helpers.IValidator;
import wormius.games.wormiusgames.helpers.PageMaker;
import wormius.games.wormiusgames.helpers.Validator;

@SpringBootApplication
@EnableAsync
@EnableScheduling
public class WormiusGamesApplication {

	public static void main(String[] args) {
		SpringApplication.run(WormiusGamesApplication.class, args);
	}
	
	@Bean
	public FilterRegistrationBean<ApiKeyFilter> apiKeyFilter() {
		FilterRegistrationBean<ApiKeyFilter> filter = new FilterRegistrationBean<>();
		filter.setFilter(new ApiKeyFilter());
		filter.addUrlPatterns("/*");
		return filter;
	}
	
	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean(name = "iValidator")
	public IValidator iValidator() {
		return new Validator();
	}
	
	@Bean(name = "iPageMaker")
	public IPageMaker iPageMaker() {
		return new PageMaker();
	}
	
	@Value(value = "${mail.smtp.host}")
	private String smtpHost;
	@Value(value = "${mail.smtp.port}")
	private String smtpPort;
	@Value(value = "${mail.smtp.user}")
	private String smtpUser;
	@Value(value = "${mail.smtp.password}")
	private String smtpPassword;
	
	@Bean
	public JavaMailSender javaMailSender() {
		JavaMailSenderImpl sender = new JavaMailSenderImpl();

	    Properties p = new Properties();
	    p.setProperty("mail.smtp.auth", "true");
	    p.setProperty("mail.smtp.host", smtpHost);
	    p.setProperty("mail.smtp.port", smtpPort);
	    p.put("mail.smtp.starttls.enable", "true");
	    sender.setUsername(smtpUser);
	    sender.setPassword(smtpPassword);
	    sender.setJavaMailProperties(p);

	    return sender;
	}
	
	@Bean
	public FilterRegistrationBean corsFilter() {
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		CorsConfiguration config = new CorsConfiguration();
		config.setAllowCredentials(true);
		config.addAllowedOrigin("https://v6p9d9t4.ssl.hwcdn.net");
		config.addAllowedHeader("*");
		config.addAllowedMethod("*");
		source.registerCorsConfiguration("/**", config);
		FilterRegistrationBean bean = new FilterRegistrationBean (new CorsFilter(source));
		bean.setOrder(0);
		return bean;
	}
	
	/*@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/**").allowedOrigins("https://v6p9d9t4.ssl.hwcdn.net");
			}
		};
	}*/
	
	/* code for redirecting http requests to https
	@Bean
	  public TomcatServletWebServerFactory servletContainer() {
	    TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory() {
	        protected void postProcessContext(Context context) {
	          SecurityConstraint securityConstraint = new SecurityConstraint();
	          securityConstraint.setUserConstraint("CONFIDENTIAL");
	          SecurityCollection collection = new SecurityCollection();
	          collection.addPattern("/*");
	          securityConstraint.addCollection(collection);
	          context.addConstraint(securityConstraint);
	        }
	      };
	    
	    tomcat.addAdditionalTomcatConnectors(initiateHttpConnector());
	    return tomcat;
	  }
	  
	  private Connector initiateHttpConnector() {
	    Connector connector = new Connector("org.apache.coyote.http11.Http11NioProtocol");
	    connector.setScheme("http");
	    connector.setPort(8000);
	    connector.setSecure(false);
	    connector.setRedirectPort(8080);
	    
	    return connector;
	  }
	  */
}
