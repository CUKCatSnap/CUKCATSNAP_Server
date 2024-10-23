package com.cuk.catsnap.domain.reservation.entity;

import com.cuk.catsnap.domain.photographer.entity.Photographer;
import com.cuk.catsnap.global.entity.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;


/*
*Program은 작가가 삭제를 하더라도 해당 프로그램을 예약한 사람들에게는 영향을 주지 않아야 한다.
*따라서 삭제 여부를 표시하는 deleted 필드를 추가하고, 이 필드가 true일 경우 삭제된 프로그램으로 간주한다.
*그러나 해당 프로그램을 과거에 예약한 사람들에게는 여전히 예약 내역이 보여야 하므로, 삭제된 프로그램에 대한 예약 내역도 보여야 한다.
* 또한 삭제는 @SQLDelete을 통해 하지만, 조회는 @Where가 아닌 별도의 쿼리를 통해 조회해야 한다.
 */
@Entity
@Table(name = "program")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Program extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "program_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "photographer_id")
    private Photographer photographer;

    private String title;
    private String content;
    private Long price;

    @Column(name = "duration_minutes")
    private Long durationMinutes;
    /*
    * 삭제 여부
    * 작가가 삭제를 하더라도 과거에 예약한 사람들에게는 여전히 예약 내역이 보여야 하므로, 삭제된 프로그램에 대한 예약 내역도 보여야 한다.
     */
    private Boolean deleted = false;


    //======= @OneToMany ========

    @OneToMany(mappedBy = "program")
    private List<Reservation> reservationList;
}
