package net.catsnap.support.fixture;

import net.catsnap.domain.client.dto.LegalAddress;

public class LegalAddressFixture {

    public String city = "서울특별시";
    public String districtName = "강남구";
    public String townName = "역삼동";

    public static LegalAddressFixture legalAddress() {
        return new LegalAddressFixture();
    }

    public LegalAddressFixture cityName(String cityName) {
        this.city = cityName;
        return this;
    }

    public LegalAddressFixture districtName(String districtName) {
        this.districtName = districtName;
        return this;
    }

    public LegalAddressFixture townName(String townName) {
        this.townName = townName;
        return this;
    }

    public LegalAddress build() {
        return new LegalAddress(city, districtName, townName);
    }
}
