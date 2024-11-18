package com.cuk.catsnap.domain.reservation.repository;

import com.cuk.catsnap.domain.member.entity.Member;
import com.cuk.catsnap.domain.member.repository.MemberRepository;
import com.cuk.catsnap.domain.photographer.entity.Photographer;
import com.cuk.catsnap.domain.photographer.repository.PhotographerRepository;
import com.cuk.catsnap.domain.reservation.entity.Program;
import com.cuk.catsnap.domain.reservation.entity.Reservation;
import com.cuk.catsnap.domain.reservation.entity.ReservationState;
import com.cuk.catsnap.global.data.jpa.confiog.JpaConfig;
import com.cuk.catsnap.support.fixture.MemberFixture;
import com.cuk.catsnap.support.fixture.PhotographerFixture;
import com.cuk.catsnap.support.fixture.ProgramFixture;
import com.cuk.catsnap.support.fixture.ReservationFixture;
import java.time.LocalDateTime;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

@DataJpaTest
@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@Import(JpaConfig.class)
class ReservationRepositoryTest {

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private PhotographerRepository photographerRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ProgramRepository programRepository;

    @Test
    void 작가의_예약시간_시작시간을_범위로_조회() {
        //given
        Member member = MemberFixture.member().build();
        Photographer photographer = PhotographerFixture.photographer().build();
        photographerDataFixture(member, photographer);

        //when
        List<Reservation> lazyLoading = reservationRepository.findAllReservationByPhotographerIdAndStartTimeBetween(
            photographer.getId(), LocalDateTime.now().minusDays(15),
            LocalDateTime.now().plusDays(15));

        //then
        Assertions.assertThat(lazyLoading).hasSize(2);
    }

    @Test
    void 작가의_예약시간_시작시간을_범위로_조회_즉시_로딩() {
        //given
        Member member = MemberFixture.member().build();
        Photographer photographer = PhotographerFixture.photographer().build();
        photographerDataFixture(member, photographer);

        //when
        List<Reservation> eagerLoading = reservationRepository.findAllReservationWithEagerByPhotographerIdAndStartTimeBetween(
            photographer.getId(), LocalDateTime.now().minusDays(15),
            LocalDateTime.now().plusDays(25));

        //then
        Assertions.assertThat(eagerLoading).hasSize(3);
    }

    @Test
    void 작가의_예약시간_시작시간을_범위로_조회_시작시간_오름차순() {
        //gien
        Member member = MemberFixture.member().build();
        Photographer photographer = PhotographerFixture.photographer().build();
        photographerDataFixture(member, photographer);

        //when
        List<Reservation> asc = reservationRepository.findAllByPhotographerIdAndStartTimeBetweenOrderByStartTimeAsc(
            photographer.getId(), LocalDateTime.now().minusDays(30),
            LocalDateTime.now().plusDays(30));

        //then
        for (int i = 0; i < asc.size() - 1; ++i) {
            Assertions.assertThat(asc.get(i).getStartTime())
                .isBeforeOrEqualTo(asc.get(i + 1).getStartTime());
        }
    }

    @Test
    void 회원의_예약_조회_생성일_내림차순() {
        //given
        Member member = MemberFixture.member().build();
        Photographer photographer = PhotographerFixture.photographer().build();
        photographerDataFixture(member, photographer);
        Pageable pageable = PageRequest.of(0, 3);

        //when
        Slice<Reservation> desc = reservationRepository.findAllByMemberIdOrderByCreatedAtDesc(
            member.getId(), pageable);

        //then
        List<Reservation> reservationList = desc.getContent();
        Assertions.assertThat(reservationList).hasSize(3);
        for (int i = 0; i < reservationList.size() - 1; ++i) {
            Assertions.assertThat(reservationList.get(i).getCreatedAt())
                .isAfterOrEqualTo(reservationList.get(i + 1).getCreatedAt());
        }
    }

    @Test
    void 회원의_예약_특정_시작_날짜조회_특정_예약_상태만_오름차순() {
        //given
        Member member = MemberFixture.member().build();
        Photographer photographer = PhotographerFixture.photographer().build();
        memberDataFixture(member, photographer);

        //when
        Slice<Reservation> slice = reservationRepository.findAllByMemberIdAndStartTimeAfterAndReservationStateInOrderByStartTimeAsc(
            member.getId(), LocalDateTime.now(),
            List.of(ReservationState.APPROVED, ReservationState.PENDING), PageRequest.of(0, 5));

        //then
        List<Reservation> reservationList = slice.getContent();
        Assertions.assertThat(reservationList).hasSize(3);
        for (int i = 0; i < reservationList.size() - 1; ++i) {
            Assertions.assertThat(reservationList.get(i).getStartTime())
                .isBeforeOrEqualTo(reservationList.get(i + 1).getStartTime());
        }
    }

    @Test
    void 회원의_예약_특정_시작_날짜_범위_조회() {
        //given
        Member member = MemberFixture.member().build();
        Photographer photographer = PhotographerFixture.photographer().build();
        memberDataFixture(member, photographer);

        //when
        List<Reservation> reservationList = reservationRepository.findAllReservationByMemberIdAndStartTimeBetween(
            member.getId(), LocalDateTime.now().minusDays(15),
            LocalDateTime.now().plusDays(15));

        //then
        Assertions.assertThat(reservationList).hasSize(4);
    }

    @Test
    void 회원의_예약_특정_시작_날짜_범위_조회_즉시_로딩() {
        //given
        Member member = MemberFixture.member().build();
        Photographer photographer = PhotographerFixture.photographer().build();
        memberDataFixture(member, photographer);

        //when
        List<Reservation> reservationList = reservationRepository.findAllReservationWithEagerByMemberIdAndStartTimeBetween(
            member.getId(), LocalDateTime.now().minusDays(15),
            LocalDateTime.now().plusDays(15));

        //then
        Assertions.assertThat(reservationList).hasSize(4);
    }

    private void photographerDataFixture(Member member, Photographer photographer) {
        Program program = ProgramFixture.program()
            .photographer(photographer)
            .build();

        Reservation reservation1 = ReservationFixture.reservation()
            .photographer(photographer)
            .member(member)
            .program(program)
            .startTime(LocalDateTime.now().minusDays(20))
            .build();
        Reservation reservation2 = ReservationFixture.reservation()
            .photographer(photographer)
            .member(member)
            .program(program)
            .startTime(LocalDateTime.now().minusDays(10))
            .build();
        Reservation reservation3 = ReservationFixture.reservation()
            .photographer(photographer)
            .member(member)
            .program(program)
            .startTime(LocalDateTime.now().plusDays(10))
            .build();
        Reservation reservation4 = ReservationFixture.reservation()
            .photographer(photographer)
            .member(member)
            .program(program)
            .startTime(LocalDateTime.now().plusDays(20))
            .reservationState(ReservationState.PENDING)
            .build();
        photographerRepository.save(photographer);
        memberRepository.save(member);
        programRepository.save(program);
        reservationRepository.save(reservation1);
        reservationRepository.save(reservation2);
        reservationRepository.save(reservation4);
        reservationRepository.save(reservation3);
    }

    private void memberDataFixture(Member member, Photographer photographer) {
        Program program = ProgramFixture.program()
            .photographer(photographer)
            .build();

        Reservation reservation1 = ReservationFixture.reservation()
            .photographer(photographer)
            .member(member)
            .program(program)
            .startTime(LocalDateTime.now().plusDays(10))
            .build();
        Reservation reservation2 = ReservationFixture.reservation()
            .photographer(photographer)
            .member(member)
            .program(program)
            .startTime(LocalDateTime.now().plusDays(10))
            .build();
        Reservation reservation3 = ReservationFixture.reservation()
            .photographer(photographer)
            .member(member)
            .startTime(LocalDateTime.now().minusDays(10))
            .program(program)
            .reservationState(ReservationState.PENDING)
            .build();
        Reservation reservation4 = ReservationFixture.reservation()
            .photographer(photographer)
            .member(member)
            .program(program)
            .startTime(LocalDateTime.now().plusDays(10))
            .reservationState(ReservationState.PENDING)
            .build();
        Reservation reservation5 = ReservationFixture.reservation()
            .photographer(photographer)
            .member(member)
            .program(program)
            .startTime(LocalDateTime.now().plusDays(20))
            .reservationState(ReservationState.PHOTOGRAPHY_CANCELLED)
            .build();
        photographerRepository.save(photographer);
        memberRepository.save(member);
        programRepository.save(program);
        reservationRepository.save(reservation1);
        reservationRepository.save(reservation2);
        reservationRepository.save(reservation4);
        reservationRepository.save(reservation3);
        reservationRepository.save(reservation5);
    }
}
