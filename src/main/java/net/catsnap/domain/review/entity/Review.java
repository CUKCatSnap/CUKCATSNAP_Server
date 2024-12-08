package net.catsnap.domain.review.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.catsnap.domain.member.entity.Member;
import net.catsnap.domain.notification.entity.PlaceSubscribeNotification;
import net.catsnap.domain.photographer.entity.Photographer;
import net.catsnap.domain.reservation.entity.Reservation;
import net.catsnap.global.entity.BaseTimeEntity;

@Entity
@Table(name = "review")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Review extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "photographer_id")
    private Photographer photographer;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reservation_id")
    private Reservation reservation;

    @Column(name = "place_score")
    private Integer placeScore;

    @Column(name = "photographer_score")
    private Integer photographerScore;

    private String content;

    // OneToMany

    @OneToMany(mappedBy = "review")
    private List<ReviewLike> reviewLikeList;

    @OneToMany(mappedBy = "review")
    private List<ReviewPhoto> reviewPhotoList;

    @OneToMany(mappedBy = "review")
    private List<PlaceSubscribeNotification> placeSubscribeNotificationList;

    public List<String> getReivewPhotoFileNameList() {
        return reviewPhotoList.stream()
            .map(ReviewPhoto::getPhotoFileName)
            .toList();
    }
}
