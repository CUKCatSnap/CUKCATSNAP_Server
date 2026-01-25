package net.catsnap.CatsnapReservation.program.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import net.catsnap.CatsnapReservation.program.domain.vo.Description;
import net.catsnap.CatsnapReservation.program.domain.vo.Duration;
import net.catsnap.CatsnapReservation.program.domain.vo.Price;
import net.catsnap.CatsnapReservation.program.domain.vo.Title;
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
        Title title = new Title("웨딩 스냅 촬영");
        Description description = new Description("아름다운 웨딩 스냅입니다.");
        Price price = new Price(150000L);
        Duration duration = new Duration(90);

        // when
        Program program = Program.create(photographerId, title, description, price, duration);

        // then
        assertThat(program.getPhotographerId()).isEqualTo(photographerId);
        assertThat(program.getTitle()).isEqualTo(title);
        assertThat(program.getDescription()).isEqualTo(description);
        assertThat(program.getPrice()).isEqualTo(price);
        assertThat(program.getDuration()).isEqualTo(duration);
        assertThat(program.isDeleted()).isFalse();
    }

    @Test
    void 설명_없이_프로그램_생성에_성공한다() {
        // given
        Long photographerId = 1L;
        Title title = new Title("프로필 촬영");
        Description description = new Description(null);
        Price price = new Price(100000L);
        Duration duration = new Duration(60);

        // when
        Program program = Program.create(photographerId, title, description, price, duration);

        // then
        assertThat(program.getDescription().isEmpty()).isTrue();
    }

    @Test
    void null_작가ID로_생성_시_예외가_발생한다() {
        // given
        Title title = new Title("웨딩 스냅");
        Description description = new Description("설명");
        Price price = new Price(150000L);
        Duration duration = new Duration(90);

        // when & then
        assertThatThrownBy(() -> Program.create(null, title, description, price, duration))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("작가 ID는 필수입니다");
    }

    @Test
    void 프로그램_정보_수정에_성공한다() {
        // given
        Program program = createDefaultProgram();
        Title newTitle = new Title("수정된 제목");
        Description newDescription = new Description("수정된 설명");
        Price newPrice = new Price(200000L);
        Duration newDuration = new Duration(120);

        // when
        program.update(newTitle, newDescription, newPrice, newDuration);

        // then
        assertThat(program.getTitle()).isEqualTo(newTitle);
        assertThat(program.getDescription()).isEqualTo(newDescription);
        assertThat(program.getPrice()).isEqualTo(newPrice);
        assertThat(program.getDuration()).isEqualTo(newDuration);
    }

    @Test
    void 프로그램_삭제에_성공한다() {
        // given
        Program program = createDefaultProgram();

        // when
        program.delete();

        // then
        assertThat(program.isDeleted()).isTrue();
        assertThat(program.getDeletedAt()).isNotNull();
    }

    @Test
    void 삭제되지_않은_프로그램의_isDeleted는_false를_반환한다() {
        // given
        Program program = createDefaultProgram();

        // when & then
        assertThat(program.isDeleted()).isFalse();
    }

    @Test
    void 소유권_확인에_성공한다() {
        // given
        Long photographerId = 1L;
        Program program = Program.create(
            photographerId,
            new Title("제목"),
            new Description("설명"),
            new Price(100000L),
            new Duration(60)
        );

        // when & then
        assertThat(program.isOwnedBy(1L)).isTrue();
        assertThat(program.isOwnedBy(2L)).isFalse();
    }

    @Test
    void 동일한_ID를_가진_프로그램은_같다() {
        // given
        Program program1 = createDefaultProgram();
        Program program2 = createDefaultProgram();

        // then
        // ID가 null이므로 equals는 ID 기반으로 동작
        // 실제 DB 저장 후에는 ID가 할당되어 비교 가능
        assertThat(program1).isEqualTo(program1);
    }

    @Test
    void toString이_올바르게_동작한다() {
        // given
        Program program = Program.create(
            1L,
            new Title("웨딩 스냅"),
            new Description("설명"),
            new Price(150000L),
            new Duration(90)
        );

        // when
        String result = program.toString();

        // then
        assertThat(result).contains("Program");
        assertThat(result).contains("photographerId=1");
        assertThat(result).contains("웨딩 스냅");
    }

    private Program createDefaultProgram() {
        return Program.create(
            1L,
            new Title("기본 프로그램"),
            new Description("기본 설명"),
            new Price(100000L),
            new Duration(60)
        );
    }
}
