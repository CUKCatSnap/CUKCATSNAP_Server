package net.catsnap.support.fixture;

import net.catsnap.domain.reservation.entity.TownLevel;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;

public class TownLevelFixture {

    private Long id;
    private String code = "1168010100";
    private String townName = "역삼동";
    private Point center = new GeometryFactory(new PrecisionModel(), 4326).createPoint(
        new Coordinate(127.0367, 37.4949));

    public static TownLevelFixture TownLevel() {
        return new TownLevelFixture();
    }

    public TownLevelFixture id(Long id) {
        this.id = id;
        return this;
    }

    public TownLevel build() {
        return TownLevel.builder()
            .id(id)
            .code(code)
            .townName(townName)
            .center(center)
            .build();
    }
}
