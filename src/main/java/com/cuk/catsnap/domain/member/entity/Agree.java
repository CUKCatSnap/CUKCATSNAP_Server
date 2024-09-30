package com.cuk.catsnap.domain.member.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name="agree")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Agree {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String content;
    private Boolean necessary;

    @OneToMany(mappedBy = "agree")
    private List<MemberAgree> memberAgrees;
}
