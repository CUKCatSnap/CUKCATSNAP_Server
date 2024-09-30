package com.cuk.catsnap.domain.notification.entity;

import com.cuk.catsnap.domain.member.entity.Member;
import com.cuk.catsnap.domain.photographer.entity.Photographer;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name="notification_type")
@Table(name = "notification")
@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public abstract class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notification_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="photographer_id")
    private Photographer photographer;

    private String title;
    private String content;
}
