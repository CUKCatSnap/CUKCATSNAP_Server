package net.catsnap.global.filter;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.filter.OrderedFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

    @Bean
    public FilterRegistrationBean<RequestWrappingFilter> requestWrappingFilter() {
        FilterRegistrationBean<RequestWrappingFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new RequestWrappingFilter());
        // OrderedFilter.REQUEST_WRAPPER_FILTER_MAX_ORDER-100은 FilterChainProxy의 Order입니다.
        // 따라서 FilterChainProxy 이전에 실행되도록 이보다 1낮은 값으로 설정합니다.
        registrationBean.setOrder(OrderedFilter.REQUEST_WRAPPER_FILTER_MAX_ORDER - 101);
        return registrationBean;
    }
}
