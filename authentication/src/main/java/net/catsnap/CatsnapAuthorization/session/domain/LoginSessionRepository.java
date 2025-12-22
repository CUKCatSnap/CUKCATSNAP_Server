package net.catsnap.CatsnapAuthorization.session.domain;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * LoginSession 애그리거트의 Repository 인터페이스
 *
 * <p>Redis 기반 세션 저장소입니다.</p>
 */
@Repository
public interface LoginSessionRepository extends CrudRepository<LoginSession, String> {

}