package net.catsnap.CatsnapReservation.program.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.catsnap.CatsnapReservation.program.domain.vo.Description;
import net.catsnap.CatsnapReservation.program.domain.vo.Duration;
import net.catsnap.CatsnapReservation.program.domain.vo.Price;
import net.catsnap.CatsnapReservation.program.domain.vo.Title;
import net.catsnap.CatsnapReservation.program.infrastructure.converter.DescriptionConverter;
import net.catsnap.CatsnapReservation.program.infrastructure.converter.DurationConverter;
import net.catsnap.CatsnapReservation.program.infrastructure.converter.PriceConverter;
import net.catsnap.CatsnapReservation.program.infrastructure.converter.TitleConverter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 * 작가 프로그램 엔티티 (Aggregate Root)
 * <p>
 * 작가가 제공하는 촬영 프로그램을 표현하는 도메인 엔티티입니다.
 * 프로그램 제목, 설명, 가격, 소요 시간 정보를 관리합니다.
 */
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Program {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long photographerId;

    @Column(nullable = false, length = 100)
    @Convert(converter = TitleConverter.class)
    private Title title;

    @Column(length = 500)
    @Convert(converter = DescriptionConverter.class)
    private Description description;

    @Column(nullable = false)
    @Convert(converter = PriceConverter.class)
    private Price price;

    @Column(nullable = false)
    @Convert(converter = DurationConverter.class)
    private Duration duration;

    private LocalDateTime deletedAt;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    private Program(
        Long photographerId,
        Title title,
        Description description,
        Price price,
        Duration duration
    ) {
        validatePhotographerId(photographerId);
        this.photographerId = photographerId;
        this.title = title;
        this.description = description;
        this.price = price;
        this.duration = duration;
    }

    /**
     * 새로운 프로그램 생성
     * <p>
     * 원시 타입을 받아 내부에서 VO를 생성하고 검증합니다.
     * 도메인 엔티티가 자신의 불변식(invariant)을 보장합니다.
     *
     * @param photographerId   작가 ID
     * @param titleValue       프로그램 제목
     * @param descriptionValue 프로그램 설명 (nullable)
     * @param priceValue       가격
     * @param durationMinutes  소요 시간 (분)
     * @return 생성된 Program
     */
    public static Program create(
        Long photographerId,
        String titleValue,
        String descriptionValue,
        Long priceValue,
        Integer durationMinutes
    ) {
        Title title = new Title(titleValue);
        Description description = new Description(descriptionValue);
        Price price = new Price(priceValue);
        Duration duration = new Duration(durationMinutes);

        return new Program(photographerId, title, description, price, duration);
    }

    /**
     * 프로그램 정보 수정
     * <p>
     * 원시 타입을 받아 내부에서 VO를 생성하고 검증합니다.
     *
     * @param titleValue       프로그램 제목
     * @param descriptionValue 프로그램 설명 (nullable)
     * @param priceValue       가격
     * @param durationMinutes  소요 시간 (분)
     */
    public void update(
        String titleValue,
        String descriptionValue,
        Long priceValue,
        Integer durationMinutes
    ) {
        this.title = new Title(titleValue);
        this.description = new Description(descriptionValue);
        this.price = new Price(priceValue);
        this.duration = new Duration(durationMinutes);
    }

    /**
     * 프로그램 소프트 삭제
     *
     * @param deletedAt 삭제 시간
     * @throws IllegalArgumentException 삭제 시간이 null인 경우
     */
    public void delete(LocalDateTime deletedAt) {
        if (deletedAt == null) {
            throw new IllegalArgumentException("삭제 시간은 필수입니다.");
        }
        this.deletedAt = deletedAt;
    }

    /**
     * 삭제 여부 확인
     */
    public boolean isDeleted() {
        return deletedAt != null;
    }

    /**
     * 작가 소유권 확인
     */
    public boolean isOwnedBy(Long photographerId) {
        return this.photographerId.equals(photographerId);
    }

    private void validatePhotographerId(Long photographerId) {
        if (photographerId == null) {
            throw new IllegalArgumentException("작가 ID는 필수입니다.");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Program program = (Program) o;
        return Objects.equals(id, program.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return String.format("Program{id=%d, photographerId=%d, title=%s}",
            id, photographerId, title);
    }
}
