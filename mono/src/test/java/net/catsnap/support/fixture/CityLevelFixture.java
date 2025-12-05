package net.catsnap.support.fixture;

import net.catsnap.domain.reservation.entity.CityLevel;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;

public class CityLevelFixture {

    private Long id;
    private String code = "1100000000";
    private String CityName = "서울특별시";
    private Point center = new GeometryFactory(new PrecisionModel(), 4326).createPoint(
        new Coordinate(126.9780, 37.5665));

    public static CityLevelFixture CityLevel() {
        return new CityLevelFixture();
    }

    public CityLevelFixture id(Long id) {
        this.id = id;
        return this;
    }

    public CityLevel build() {
        return CityLevel.builder()
            .id(id)
            .code(code)
            .cityName(CityName)
            .center(center)
            .build();
    }

}
