package net.catsnap.CatsnapReservation.reservation.domain;

/**
 * 예약 취소 주체
 */
public enum CanceledBy {
    MODEL,        // 모델(고객) 취소
    PHOTOGRAPHER, // 작가 취소
    SYSTEM        // 시스템 자동 취소
}
