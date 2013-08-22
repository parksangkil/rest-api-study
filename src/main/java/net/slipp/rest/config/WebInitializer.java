package net.slipp.rest.config;

import com.opensymphony.sitemesh.webapp.SiteMeshFilter;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.context.support.XmlWebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.filter.HiddenHttpMethodFilter;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;


/**
 * Created with IntelliJ IDEA.
 *
 * @author: ihoneymon
 * Date: 13. 8. 22
 * Reference<br/>
 * <ul>
 *     <li><a href="http://www.java-allandsundry.com/2012/05/spring-webapplicationinitializer-and.html">http://www.java-allandsundry.com/2012/05/spring-webapplicationinitializer-and.html</a></li>
 *     <li><a href="http://zeroturnaround.com/rebellabs/your-next-java-web-app-less-xml-no-long-restarts-fewer-hassles-part-1/">Your Next Java Web App: Less XML, No Long Restarts, Fewer Hassles (Part 1)</a></li>
 * </ul>
 */
public class WebInitializer implements WebApplicationInitializer {
    @Override
    public void onStartup(ServletContext container) throws ServletException {
        /**
         * applicationContext 등록
         */
        XmlWebApplicationContext applicationContext = new XmlWebApplicationContext();
        applicationContext.setConfigLocation("classpath*:/META-INF/spring/*.xml");
        container.addListener(new ContextLoaderListener(applicationContext));

        /**
         * appApiServlet 생성
         */
        //appApiServlet
//        AnnotationConfigWebApplicationContext appApiServletContext = new AnnotationConfigWebApplicationContext();
//        appApiServletContext.register(WebApplicationApiConfig.class);
//
//        ServletRegistration.Dynamic appApiServlet = container.addServlet("appApiServlet", new DispatcherServlet(appApiServletContext));
        ServletRegistration.Dynamic appApiServlet = container.addServlet("appApiServlet", DispatcherServlet.class);
        appApiServlet.setInitParameter("contextConfigLocation", "/WEB-INF/spring/webApplication-api-context.xml");
        appApiServlet.addMapping("/api/*");
        appApiServlet.setLoadOnStartup(1);

        /**
         * appViewServlet 생성
         */
//        AnnotationConfigWebApplicationContext appViewServletContext = new AnnotationConfigWebApplicationContext();
//        appViewServletContext.register(WebApplicationViewConfig.class);
//
//        ServletRegistration.Dynamic appViewServlet = container.addServlet("appViewServlet", new DispatcherServlet(appViewServletContext));
        ServletRegistration.Dynamic appViewServlet = container.addServlet("appViewServlet", DispatcherServlet.class);
        appViewServlet.setInitParameter("contextConfigLocation", "/WEB-INF/spring/webApplication-view-context.xml");
        appViewServlet.addMapping("/view/*");
        appViewServlet.setLoadOnStartup(2);

        /**
         * HiddenHttpMethodFilter
         */
        container.addFilter("hiddenHttpMethodFilter", HiddenHttpMethodFilter.class).addMappingForUrlPatterns(null, false, "/*");

        /**
         * CharacterEncodingFilter
         */
        FilterRegistration encodingFilterRegistration = container.addFilter("encodingFilter", CharacterEncodingFilter.class);
        encodingFilterRegistration.setInitParameter("encoding","UTF-8");
        encodingFilterRegistration.setInitParameter("forceEncoding","true");
        encodingFilterRegistration.addMappingForUrlPatterns(null, false, "/*");

        FilterRegistration siteMeshFilterRegistration = container.addFilter("sitemesh", SiteMeshFilter.class);
        siteMeshFilterRegistration.addMappingForUrlPatterns(null, false, "/view/*");
    }
}
