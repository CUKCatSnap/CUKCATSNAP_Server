package net.catsnap.domain.reservation.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.locationtech.jts.geom.Point;

/*
 * 읍면동명을 저장하는 테이블입니다. (ex, 역삼동, 신사동)
 */
@Entity
@Table(name = "town_level")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TownLevel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String code;
    private String townName;

    @Column(columnDefinition = "geometry(Point, 4326)")
    private Point center;

    @ManyToOne(fetch = FetchType.EAGER)
    private DistrictLevel districtLevel;

    // ManyToOne
    @OneToMany(mappedBy = "townLevel")
    private List<Reservation> reservationList;
}
