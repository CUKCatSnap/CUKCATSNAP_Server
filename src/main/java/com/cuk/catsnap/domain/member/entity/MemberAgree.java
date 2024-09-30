package com.cuk.catsnap.domain.member.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name="member_agree")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberAgree {

    @Id
    @Column(name = "user_agree_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="member_id")
    private Member member;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="agree_id")
    private Agree agree;
}
