package net.catsnap.CatsnapReservation.program.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import net.catsnap.CatsnapReservation.program.fixture.ProgramFixture;
import net.catsnap.CatsnapReservation.shared.domain.error.DomainException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

@DisplayName("Program 엔티티 테스트")
@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class ProgramTest {

    @Test
    void 프로그램_생성에_성공한다() {
        // given
        Long photographerId = 1L;
        String title = "웨딩 스냅 촬영";
        String description = "아름다운 웨딩 스냅입니다.";
        Long price = 150000L;
        Integer duration = 90;

        // when
        Program program = Program.create(photographerId, title, description, price, duration);

        // then
        assertThat(program.getPhotographerId()).isEqualTo(photographerId);
        assertThat(program.getTitle().getValue()).isEqualTo(title);
        assertThat(program.getDescription().getValue()).isEqualTo(description);
        assertThat(program.getPrice().getValue()).isEqualTo(price);
        assertThat(program.getDuration().getValue()).isEqualTo(duration);
        assertThat(program.isDeleted()).isFalse();
    }

    @Test
    void 설명_없이_프로그램_생성에_성공한다() {
        // given
        Long photographerId = 1L;

        // when
        Program program = Program.create(photographerId, "프로필 촬영", null, 100000L, 60);

        // then
        assertThat(program.getDescription().isEmpty()).isTrue();
    }

    @Test
    void null_작가ID로_생성_시_예외가_발생한다() {
        // when & then
        assertThatThrownBy(() -> Program.create(null, "웨딩 스냅", "설명", 150000L, 90))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("작가 ID는 필수입니다");
    }

    @Test
    void 빈_제목으로_생성_시_예외가_발생한다() {
        // when & then
        assertThatThrownBy(() -> Program.create(1L, "", "설명", 150000L, 90))
            .isInstanceOf(DomainException.class);
    }

    @Test
    void 음수_가격으로_생성_시_예외가_발생한다() {
        // when & then
        assertThatThrownBy(() -> Program.create(1L, "제목", "설명", -1L, 90))
            .isInstanceOf(DomainException.class);
    }

    @Test
    void 영분_소요시간으로_생성_시_예외가_발생한다() {
        // when & then
        assertThatThrownBy(() -> Program.create(1L, "제목", "설명", 150000L, 0))
            .isInstanceOf(DomainException.class);
    }

    @Test
    void 프로그램_정보_수정에_성공한다() {
        // given
        Program program = ProgramFixture.createDefault();

        // when
        program.update("수정된 제목", "수정된 설명", 200000L, 120);

        // then
        assertThat(program.getTitle().getValue()).isEqualTo("수정된 제목");
        assertThat(program.getDescription().getValue()).isEqualTo("수정된 설명");
        assertThat(program.getPrice().getValue()).isEqualTo(200000L);
        assertThat(program.getDuration().getValue()).isEqualTo(120);
    }

    @Test
    void 프로그램_삭제에_성공한다() {
        // given
        Program program = ProgramFixture.createDefault();
        LocalDateTime deletedAt = LocalDateTime.of(2024, 1, 1, 12, 0, 0);

        // when
        program.delete(deletedAt);

        // then
        assertThat(program.isDeleted()).isTrue();
        assertThat(program.getDeletedAt()).isEqualTo(deletedAt);
    }

    @Test
    void null_삭제_시간으로_삭제_시_예외가_발생한다() {
        // given
        Program program = ProgramFixture.createDefault();

        // when & then
        assertThatThrownBy(() -> program.delete(null))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("삭제 시간은 필수입니다");
    }

    @Test
    void 이미_삭제된_프로그램을_다시_삭제해도_멱등성이_보장된다() {
        // given
        Program program = ProgramFixture.createDefault();
        LocalDateTime firstDeletedAt = LocalDateTime.of(2024, 1, 1, 12, 0, 0);
        LocalDateTime secondDeletedAt = LocalDateTime.of(2024, 1, 2, 14, 0, 0);

        program.delete(firstDeletedAt);

        // when - 이미 삭제된 프로그램을 다시 삭제 시도
        program.delete(secondDeletedAt);

        // then - 첫 번째 삭제 시간이 유지됨 (덮어씌워지지 않음)
        assertThat(program.isDeleted()).isTrue();
        assertThat(program.getDeletedAt()).isEqualTo(firstDeletedAt);
    }

    @Test
    void 삭제되지_않은_프로그램의_isDeleted는_false를_반환한다() {
        // given
        Program program = ProgramFixture.createDefault();

        // when & then
        assertThat(program.isDeleted()).isFalse();
    }

    @Test
    void 활성_프로그램은_ensureBookable이_성공한다() {
        // given
        Program program = ProgramFixture.createDefault();

        // when & then
        org.assertj.core.api.Assertions.assertThatCode(program::ensureBookable)
            .doesNotThrowAnyException();
    }

    @Test
    void 삭제된_프로그램은_ensureBookable에서_예외가_발생한다() {
        // given
        Program program = ProgramFixture.createDefaultDeleted();

        // when & then
        assertThatThrownBy(program::ensureBookable)
            .isInstanceOf(DomainException.class)
            .hasMessageContaining("삭제된 프로그램은 예약할 수 없습니다");
    }

    @Test
    void 소유권_확인에_성공한다() {
        // given
        Long photographerId = 1L;
        Program program = ProgramFixture.createWithPhotographerId(photographerId);

        // when & then
        assertThat(program.isOwnedBy(1L)).isTrue();
        assertThat(program.isOwnedBy(2L)).isFalse();
    }

    @Test
    void toString이_올바르게_동작한다() {
        // given
        Program program = ProgramFixture.create(1L, "웨딩 스냅", "설명", 150000L, 90);

        // when
        String result = program.toString();

        // then
        assertThat(result).contains("Program");
        assertThat(result).contains("photographerId=1");
        assertThat(result).contains("웨딩 스냅");
    }
}
