package net.catsnap.domain.feed.repository;

import java.util.List;
import net.catsnap.domain.feed.entity.FeedCommentLike;
import net.catsnap.domain.feed.repository.dto.FeedCommentLikeCountDto;
import net.catsnap.domain.feed.repository.dto.FeedCommentLikeMeDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface FeedCommentLikeRepository extends JpaRepository<FeedCommentLike, Long> {

    @Query("""
            SELECT new net.catsnap.domain.feed.repository.dto.FeedCommentLikeCountDto(fcl.feedComment.id, COUNT(fcl))
            FROM FeedCommentLike fcl
            WHERE fcl.feedComment.id IN :commentIdList
            GROUP BY fcl.feedComment.id
        """)
    List<FeedCommentLikeCountDto> countLikesByFeedCommentIdList(
        @Param("commentIdList") List<Long> commentIdList);

    @Query("""
            SELECT new net.catsnap.domain.feed.repository.dto.FeedCommentLikeMeDto(fcl.feedComment.id, true)
            FROM FeedCommentLike fcl
            WHERE fcl.feedComment.id IN :commentIdList AND fcl.likeUser.id = :userId
        """)
    List<FeedCommentLikeMeDto> findLikedCommentsByUser(
        @Param("commentIdList") List<Long> commentIdList,
        @Param("userId") Long userId
    );
}
