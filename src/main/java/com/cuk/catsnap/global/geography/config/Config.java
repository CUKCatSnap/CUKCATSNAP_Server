package com.cuk.catsnap.global.geography.config;

import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.PrecisionModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Config {

    @Bean
    public GeometryFactory geometryFactory() {
        /*
         * SRID는 좌표계를 나타내는 값으로, 자주 사용하는 값은 0(좌표평면)과 4326(위도-경도 좌표계)이다.
         */
        return new GeometryFactory(new PrecisionModel(), 4326);
    }
}
