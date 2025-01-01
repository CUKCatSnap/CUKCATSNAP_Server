package net.catsnap.domain.review.entity;

import net.catsnap.domain.member.entity.Member;
import net.catsnap.domain.photographer.entity.Photographer;
import net.catsnap.global.entity.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "review_like")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewLike extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_like_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id")
    private Review review;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "photographer_id")
    private Photographer photographer;

    private Boolean liked = true;

    /*
     * 사용자가 리뷰에 좋아요를 누르는 경우
     */
    public ReviewLike(Review review, Member member) {
        this.review = review;
        this.member = member;
    }

    /*
     * 작가가 리뷰에 좋아요를 누르는 경우
     */
    public ReviewLike(Review review, Photographer photographer) {
        this.review = review;
        this.photographer = photographer;
    }

    public void toggleLike() {
        this.liked = !this.liked;
    }
}
