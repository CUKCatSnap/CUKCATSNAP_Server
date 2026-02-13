package net.catsnap.domain.user.photographer.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.catsnap.global.entity.BaseTimeEntity;

@Entity
@Table(name = "photographer_introduction")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PhotographerIntroduction extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @OneToOne
    @JoinColumn(name = "photographer_id")
    Photographer photographer;

    String content;

    public void updateIntroduction(String content) {
        this.content = content;
    }
}
