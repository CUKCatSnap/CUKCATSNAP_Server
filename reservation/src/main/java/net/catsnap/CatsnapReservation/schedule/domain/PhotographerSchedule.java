package net.catsnap.CatsnapReservation.schedule.domain;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.catsnap.CatsnapReservation.schedule.domain.vo.WeekdayScheduleRule;
import net.catsnap.CatsnapReservation.schedule.infrastructure.converter.WeekdayRulesConverter;

/**
 * 작가 예약 가능 시간 Aggregate Root
 * <p>
 * 작가의 예약 가능 시간 규칙(템플릿)을 관리하는 집합 루트입니다. - 요일별 기본 규칙 관리 - 예외 규칙 관리 - 일관성 경계 유지
 */
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PhotographerSchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private Long photographerId;

    /**
     * 요일별 기본 규칙 JSON으로 저장: {"MONDAY": {...}, "TUESDAY": {...}}
     */
    @Column(columnDefinition = "jsonb")
    @Convert(converter = WeekdayRulesConverter.class)
    private Map<DayOfWeek, WeekdayScheduleRule> weekdayRules = new EnumMap<>(DayOfWeek.class);

    /**
     * 예외 규칙들 (Aggregate 내부 관리)
     */
    @OneToMany(
        mappedBy = "photographerId",
        cascade = CascadeType.ALL,
        orphanRemoval = true,
        fetch = FetchType.LAZY
    )
    private List<ScheduleOverride> overrides = new ArrayList<>();

    private PhotographerSchedule(
        Long photographerId,
        Map<DayOfWeek, WeekdayScheduleRule> weekdayRules
    ) {
        if (photographerId == null) {
            throw new IllegalArgumentException("작가 ID는 필수입니다.");
        }

        this.photographerId = photographerId;
        this.weekdayRules = weekdayRules != null
            ? new EnumMap<>(weekdayRules)
            : initializeDefaultWeekdayRules();
    }

    /**
     * 기본 요일 규칙 초기화 (모두 휴무)
     */
    private Map<DayOfWeek, WeekdayScheduleRule> initializeDefaultWeekdayRules() {
        Map<DayOfWeek, WeekdayScheduleRule> rules = new EnumMap<>(DayOfWeek.class);
        for (DayOfWeek day : DayOfWeek.values()) {
            rules.put(day, WeekdayScheduleRule.dayOff(day));
        }
        return rules;
    }

    public PhotographerSchedule initSchedule(Long photographerId) {
        return new PhotographerSchedule(photographerId, null);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PhotographerSchedule that = (PhotographerSchedule) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return String.format("ReservationAvailability{id=%d, photographerId=%d}",
            id, photographerId);
    }
}
