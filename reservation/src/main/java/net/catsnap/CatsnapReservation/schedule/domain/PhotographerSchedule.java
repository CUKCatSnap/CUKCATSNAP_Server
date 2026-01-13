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
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.catsnap.CatsnapReservation.schedule.domain.vo.AvailableStartTimes;
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
     * 요일별 예약 가능 시간 JSON으로 저장: {"MONDAY": [...], "TUESDAY": [...]}
     */
    @Column(columnDefinition = "jsonb")
    @Convert(converter = WeekdayRulesConverter.class)
    private Map<DayOfWeek, AvailableStartTimes> weekdayRules;

    /**
     * 예외 규칙들 (Aggregate 내부 관리)
     */
    @OneToMany(
        mappedBy = "photographerSchedule",
        cascade = CascadeType.ALL,
        orphanRemoval = true,
        fetch = FetchType.EAGER
    )
    private List<ScheduleOverride> overrides;

    private PhotographerSchedule(
        Long photographerId,
        Map<DayOfWeek, AvailableStartTimes> weekdayRules,
        List<ScheduleOverride> overrides
    ) {
        if (photographerId == null) {
            throw new IllegalArgumentException("작가 ID는 필수입니다.");
        }

        this.photographerId = photographerId;
        this.weekdayRules = weekdayRules != null
            ? new EnumMap<>(weekdayRules)
            : initializeDefaultWeekdayRules();
        this.overrides = overrides != null ? new ArrayList<>(overrides) : new ArrayList<>();
    }

    /**
     * 기본 요일 규칙 초기화 (모두 휴무)
     */
    private Map<DayOfWeek, AvailableStartTimes> initializeDefaultWeekdayRules() {
        Map<DayOfWeek, AvailableStartTimes> rules = new EnumMap<>(DayOfWeek.class);
        for (DayOfWeek day : DayOfWeek.values()) {
            rules.put(day, AvailableStartTimes.empty());
        }
        return rules;
    }

    public static PhotographerSchedule initSchedule(Long photographerId) {
        return new PhotographerSchedule(photographerId, null, null);
    }

    /**
     * 과거 예외 규칙 정리
     * 수정 작업 전에 호출하여 과거 데이터를 자동 삭제
     */
    private void cleanupPastOverrides() {
        LocalDate today = LocalDate.now();
        overrides.removeIf(override -> override.getTargetDate().isBefore(today));
    }

    /**
     * 예외 규칙 추가
     */
    public void addOverride(ScheduleOverride override) {
        cleanupPastOverrides();
        validateOverride(override);
        overrides.add(override);
        override.setPhotographerSchedule(this);
    }

    /**
     * 예외 규칙 제거
     */
    public void removeOverride(ScheduleOverride override) {
        cleanupPastOverrides();
        overrides.remove(override);
    }

    /**
     * 요일 규칙 업데이트
     */
    public void updateWeekdayRule(DayOfWeek day, AvailableStartTimes availableTimes) {
        cleanupPastOverrides();
        if (day == null) {
            throw new IllegalArgumentException("요일은 필수입니다.");
        }
        if (availableTimes == null) {
            throw new IllegalArgumentException("예약 가능 시간은 필수입니다.");
        }
        this.weekdayRules.put(day, availableTimes);
    }

    /**
     * 특정 날짜에 예약 가능한지 확인
     */
    public boolean isAvailableAt(LocalDate targetDate) {
        if (targetDate.isBefore(LocalDate.now())) {
            return false;
        }

        // 1. 예외 규칙 먼저 확인
        ScheduleOverride override = overrides.stream()
            .filter(o -> o.getTargetDate().equals(targetDate))
            .findFirst()
            .orElse(null);

        if (override != null) {
            return override.hasAvailableTimes();
        }

        // 2. 기본 요일 규칙 확인
        AvailableStartTimes times = weekdayRules.get(targetDate.getDayOfWeek());
        return times != null && !times.isEmpty();
    }

    private void validateOverride(ScheduleOverride override) {
        if (override == null) {
            throw new IllegalArgumentException("예외 규칙은 필수입니다.");
        }

        boolean exists = overrides.stream()
            .anyMatch(o -> o.getTargetDate().equals(override.getTargetDate()));

        if (exists) {
            throw new IllegalArgumentException(
                String.format("해당 날짜(%s)의 예외가 이미 존재합니다.", override.getTargetDate())
            );
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
