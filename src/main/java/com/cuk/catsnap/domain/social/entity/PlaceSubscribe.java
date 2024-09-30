package com.cuk.catsnap.domain.social.entity;

import com.cuk.catsnap.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name="place_subscribe")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlaceSubscribe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="place_subscribe_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="member_id")
    private Member member;

    private String keyword;
}
