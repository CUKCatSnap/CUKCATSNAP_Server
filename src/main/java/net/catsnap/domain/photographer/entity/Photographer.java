package net.catsnap.domain.photographer.entity;

import net.catsnap.domain.feed.entity.Feed;
import net.catsnap.domain.feed.entity.FeedComment;
import net.catsnap.domain.feed.entity.FeedLike;
import net.catsnap.domain.notification.entity.Notification;
import net.catsnap.domain.reservation.entity.Program;
import net.catsnap.domain.reservation.entity.Reservation;
import net.catsnap.domain.reservation.entity.WeekdayReservationTimeMapping;
import net.catsnap.domain.review.entity.Review;
import net.catsnap.domain.review.entity.ReviewLike;
import net.catsnap.global.entity.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "photograph")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Photographer extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "photographer_id")
    private Long id;

    private String identifier;
    private String password;
    private String nickname;
    private LocalDate birthday;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "profile_photo_url")
    private String profilePhotoUrl;

    // OneToMany

    @OneToMany(mappedBy = "photographer")
    private List<Reservation> ReservationList;

    @OneToMany(mappedBy = "photographer")
    private List<Review> ReviewList;

    @OneToMany(mappedBy = "photographer")
    private List<Feed> feedList;

    @OneToMany(mappedBy = "photographer")
    private List<FeedLike> feedLikeList;

    @OneToMany(mappedBy = "photographer")
    private List<FeedComment> feedCommentList;

    @OneToMany(mappedBy = "photographer")
    private List<Notification> notificationList;

    @OneToMany(mappedBy = "photographer")
    private List<WeekdayReservationTimeMapping> weekdayReservationTimeMappingList;

    @OneToMany(mappedBy = "photographer")
    private List<Program> programList;

    @OneToMany(mappedBy = "photographer")
    private List<ReviewLike> reviewLikeList;
}
