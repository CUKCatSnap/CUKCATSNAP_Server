package net.catsnap.domain.user.photographer.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import net.catsnap.domain.feed.entity.Feed;
import net.catsnap.domain.feed.entity.FeedComment;
import net.catsnap.domain.feed.entity.FeedLike;
import net.catsnap.domain.notification.entity.Notification;
import net.catsnap.domain.reservation.entity.Program;
import net.catsnap.domain.reservation.entity.Reservation;
import net.catsnap.domain.reservation.entity.WeekdayReservationTimeMapping;
import net.catsnap.domain.review.entity.Review;
import net.catsnap.domain.review.entity.ReviewLike;
import net.catsnap.domain.user.entity.User;

@Entity
@Table(name = "photograph")
@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Photographer extends User {

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
