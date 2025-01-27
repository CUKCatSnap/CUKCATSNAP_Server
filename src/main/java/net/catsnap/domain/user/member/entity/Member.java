package net.catsnap.domain.user.member.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import net.catsnap.domain.feed.entity.FeedComment;
import net.catsnap.domain.feed.entity.FeedLike;
import net.catsnap.domain.notification.entity.Notification;
import net.catsnap.domain.reservation.entity.Reservation;
import net.catsnap.domain.review.entity.Review;
import net.catsnap.domain.social.entity.PhotographerBlock;
import net.catsnap.domain.social.entity.PhotographerSubscribe;
import net.catsnap.domain.social.entity.PlaceSubscribe;
import net.catsnap.domain.user.entity.User;
import net.catsnap.global.security.authority.CatsnapAuthority;
import org.springframework.security.core.GrantedAuthority;

@Entity
@Table(name = "model")
@Getter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class Member extends User {

    @Enumerated(EnumType.STRING)
    private SnsType snstype;

    private String snsId;
    private LocalDateTime snsConnectDate;

    // OneToMany

    @OneToMany(mappedBy = "member")
    private List<MemberAgree> memberAgrees;

    @OneToMany(mappedBy = "member")
    private List<PhotographerSubscribe> photographerSubscribeList;

    @OneToMany(mappedBy = "member")
    private List<PhotographerBlock> photographerBlockList;

    @OneToMany(mappedBy = "member")
    private List<PlaceSubscribe> placeSubscribeList;

    @OneToMany(mappedBy = "member")
    private List<Reservation> ReservationList;

    @OneToMany(mappedBy = "member")
    private List<Review> reviewList;

    @OneToMany(mappedBy = "member")
    private List<FeedLike> feedLikeList;

    @OneToMany(mappedBy = "member")
    private List<FeedComment> feedCommentList;

    @OneToMany(mappedBy = "member")
    private List<Notification> notificationList;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(CatsnapAuthority.MEMBER);
    }
}
