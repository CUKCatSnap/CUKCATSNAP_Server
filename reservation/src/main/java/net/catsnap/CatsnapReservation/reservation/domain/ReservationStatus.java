package net.catsnap.CatsnapReservation.reservation.domain;

/**
 * 예약 상태
 */
public enum ReservationStatus {
    PENDING,   // 임시예약(HOLD)
    CONFIRMED, // 결제 성공 및 예약 확정
    CANCELED,  // 사용자/작가/시스템 취소
    EXPIRED    // 홀드 만료
}
