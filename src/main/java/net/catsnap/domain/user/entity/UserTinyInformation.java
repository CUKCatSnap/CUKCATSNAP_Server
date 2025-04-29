package net.catsnap.domain.user.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Immutable;


/*
 * 해당 엔티티는 유저 조회용 뷰와 매핑된 엔티티입니다.
 */
@Entity
@Immutable
@Table(name = "user_tiny_information")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserTinyInformation {

    @Id
    private Long id;

    private String nickname;
    private String profilePhotoUrl;
    private UserType userType;
}
