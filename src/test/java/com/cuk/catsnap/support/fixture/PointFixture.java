package com.cuk.catsnap.support.fixture;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;

public class PointFixture {

    private Double x = 37.579617;
    private Double y = 126.977041;
    private GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);

    public static PointFixture point() {
        return new PointFixture();
    }

    public Point build() {
        return geometryFactory.createPoint(new Coordinate(this.x, this.y));
    }
}
