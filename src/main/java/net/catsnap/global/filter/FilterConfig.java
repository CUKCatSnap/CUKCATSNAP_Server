package net.catsnap.global.filter;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

    @Bean
    public FilterRegistrationBean<RequestWrappingFilter> requestWrappingFilter() {
        FilterRegistrationBean<RequestWrappingFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new RequestWrappingFilter());
        // OrderedFilter.REQUEST_WRAPPER_FILTER_MAX_ORDER-100 (-100)은 FilterChainProxy의 Order입니다.
        // FilterChainProxy가 나중에 실행되도록 설정합니다.
        registrationBean.setOrder(1000000);
        return registrationBean;
    }
}
