package dk.magenta.databroker;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.embedded.ServletRegistrationBean;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.WebApplicationContext;

@Configuration
@EnableAutoConfiguration
@ComponentScan
public class DatabrokerWebXml extends SpringBootServletInitializer {

    private WebApplicationContext context;

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(Application.class);
    }


    @Bean
    public ServletRegistrationBean cxfServlet() {
        org.apache.cxf.transport.servlet.CXFServlet cxfServlet = new org.apache.cxf.transport.servlet.CXFServlet();
        ServletRegistrationBean servletDef = new ServletRegistrationBean(cxfServlet, "/services/*");
        servletDef.setLoadOnStartup(1);
        return servletDef;
    }

}