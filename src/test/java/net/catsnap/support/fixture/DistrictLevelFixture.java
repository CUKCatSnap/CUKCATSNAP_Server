package net.catsnap.support.fixture;

import net.catsnap.domain.reservation.entity.DistrictLevel;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;

public class DistrictLevelFixture {

    private Long id;
    private String code = "1168000000";
    private String districtName = "강남구";
    private Point center = new GeometryFactory(new PrecisionModel(), 4326).createPoint(
        new Coordinate(127.04743, 37.5173));

    public static DistrictLevelFixture DistrictLevel() {
        return new DistrictLevelFixture();
    }

    public DistrictLevelFixture id(Long id) {
        this.id = id;
        return this;
    }

    public DistrictLevel build() {
        return DistrictLevel.builder()
            .id(id)
            .code(code)
            .districtName(districtName)
            .center(center)
            .build();
    }
}
