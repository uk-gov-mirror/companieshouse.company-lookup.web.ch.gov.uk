package uk.gov.companieshouse.lookup.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import uk.gov.companieshouse.common.web.interceptor.TemplateNameInterceptor;

@Configuration
public class PiwikInterceptorConfig implements WebMvcConfigurer {

    /**
     * Setup the interceptors to run against endpoints when the endpoints are called
     * Interceptors are executed in the order they are added to the registry
     *
     * @param registry The spring interceptor registry
     */
    @Override
    public void addInterceptors(@NonNull InterceptorRegistry registry) {
        // Add interceptor to get template names for matomo events
        registry.addInterceptor(new TemplateNameInterceptor());
    }
}
