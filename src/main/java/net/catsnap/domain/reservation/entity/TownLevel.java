package net.catsnap.domain.reservation.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.locationtech.jts.geom.Point;

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
    private String TownName;

    @Column(columnDefinition = "geometry(Point, 4326)")
    private Point center;

    // ManyToOne
    @OneToMany(mappedBy = "townLevel")
    private List<Reservation> reservationList;
}
