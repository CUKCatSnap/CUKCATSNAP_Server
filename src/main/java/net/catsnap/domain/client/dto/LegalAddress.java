package net.catsnap.domain.client.dto;

import lombok.Getter;

@Getter
public class LegalAddress {

    private String cityName; //시도명
    private String districtName; //시군구명
    private String townName; //읍면동명

    public LegalAddress(String cityName, String districtName, String townName) {
        this.cityName = cityName;
        this.districtName = districtName;
        this.townName = townName;
    }
}
