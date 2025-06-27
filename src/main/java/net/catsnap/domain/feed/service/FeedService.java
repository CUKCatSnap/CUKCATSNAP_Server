package net.catsnap.domain.feed.service;

import java.net.URL;
import java.util.List;
import net.catsnap.domain.feed.dto.request.FeedPostRequest;
import net.catsnap.domain.feed.dto.response.FeedDetailResponse;
import net.catsnap.domain.feed.dto.response.FeedPhotoPresignedURLResponse;
import net.catsnap.domain.feed.entity.Feed;
import net.catsnap.domain.feed.entity.FeedPhoto;
import net.catsnap.domain.feed.repository.FeedPhotoRepository;
import net.catsnap.domain.feed.repository.FeedRepository;
import net.catsnap.domain.user.photographer.entity.Photographer;
import net.catsnap.domain.user.photographer.repository.PhotographerRepository;
import net.catsnap.global.Exception.authority.ResourceNotFoundException;
import net.catsnap.global.aws.s3.ImageDownloadClient;
import net.catsnap.global.aws.s3.ImageUploadClient;
import net.catsnap.global.aws.s3.dto.PresignedUrlResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FeedService {

    private final FeedRepository feedRepository;
    private final FeedLikeService feedLikeService;
    private final FeedCommentService feedCommentService;
    private final ImageUploadClient feedImageUploadClient;
    private final ImageDownloadClient feedImageDownloadClient;
    private final PhotographerRepository photographerRepository;
    private final FeedPhotoRepository feedPhotoRepository;

    public FeedService(FeedRepository feedRepository, FeedLikeService feedLikeService,
        FeedCommentService feedCommentService,
        @Qualifier("feedImageUploadClient") ImageUploadClient feedImageUploadClient,
        @Qualifier("feedImageDownloadClient") ImageDownloadClient feedImageDownloadClient,
        PhotographerRepository photographerRepository,
        FeedPhotoRepository feedPhotoRepository) {
        this.feedRepository = feedRepository;
        this.feedLikeService = feedLikeService;
        this.feedCommentService = feedCommentService;
        this.feedImageUploadClient = feedImageUploadClient;
        this.feedImageDownloadClient = feedImageDownloadClient;
        this.photographerRepository = photographerRepository;
        this.feedPhotoRepository = feedPhotoRepository;
    }

    @Transactional(readOnly = true)
    public FeedDetailResponse getFeedDetail(Long feedId, Long userId) {
        Feed feed = feedRepository.findById(feedId)
            .orElseThrow(() -> new ResourceNotFoundException("해당 피드를 찾을 수 없습니다."));
        long likeCount = feedLikeService.getFeedLikeCount(feedId);
        boolean isLiked = feedLikeService.getFeedLikes(feedId, userId);
        long commentCount = feedCommentService.getFeedCommentCount(feedId);
        return FeedDetailResponse.of(feed, likeCount, isLiked, commentCount);
    }

    @Transactional
    public FeedPhotoPresignedURLResponse postFeed(Long photographerId,
        FeedPostRequest feedPostRequest) {
        Photographer photographer = photographerRepository.getReferenceById(photographerId);
        Feed feed = new Feed(photographer, feedPostRequest.title(), feedPostRequest.content());

        Feed savedFeed = feedRepository.save(feed);

        List<PresignedUrlResponse> presignedUrlResponseList = generatePresignedUrl(
            feedPostRequest.photoFileNameList());

        presignedUrlResponseList.stream()
            .map(PresignedUrlResponse::uuidFileName)
            .peek(fileName -> feedPhotoRepository.save(
                new FeedPhoto(savedFeed, fileName)
            ));

        List<URL> presignedUrls = presignedUrlResponseList.stream()
            .map(PresignedUrlResponse::presignedURL)
            .toList();

        List<String> photoUrls = presignedUrlResponseList.stream()
            .map(PresignedUrlResponse::uuidFileName)
            .map(feedImageDownloadClient::getDownloadImageUrl)
            .toList();

        return FeedPhotoPresignedURLResponse.of(
            savedFeed.getId(),
            presignedUrls,
            photoUrls
        );
    }

    private List<PresignedUrlResponse> generatePresignedUrl(List<String> fileNameList) {
        return fileNameList.stream()
            .map(feedImageUploadClient::getUploadImageUrl)
            .toList();
    }
}
