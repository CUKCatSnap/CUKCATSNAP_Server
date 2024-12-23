package net.catsnap.domain.client.dto;

import lombok.Getter;

@Getter
public class LegalAddress {

    private String level0; //시도명
    private String level1; //시군구명
    private String level2; //읍면동명

    public LegalAddress(String level0, String level1, String level2) {
        this.level0 = level0;
        this.level1 = level1;
        this.level2 = level2;
    }
}
