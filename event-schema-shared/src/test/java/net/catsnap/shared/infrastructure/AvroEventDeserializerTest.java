package net.catsnap.shared.infrastructure;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import net.catsnap.event.test.v1.TestEventFixture;
import net.catsnap.shared.infrastructure.exception.EventDeserializationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@DisplayName("AvroEventDeserializer 통합 테스트")
class AvroEventDeserializerTest {

    private AvroEventSerializer serializer;
    private AvroEventDeserializer deserializer;

    @BeforeEach
    void setUp() {
        serializer = new AvroEventSerializer();
        deserializer = new AvroEventDeserializer();
    }

    @Nested
    class deserialize_테스트 {

        @Test
        void 바이트_배열을_이벤트_객체로_역직렬화_가능하다() {
            // given
            TestEventFixture originalEvent = TestEventFixture.newBuilder()
                .setEventId("test-event-id-123")
                .setEventType("TEST_EVENT")
                .setOccurredAt(1704067200000L)
                .setUserId(12345L)
                .setIsActive(true)
                .build();
            byte[] serialized = serializer.serialize(originalEvent);

            // when
            TestEventFixture deserialized = deserializer.deserialize(serialized, TestEventFixture.class);

            // then
            assertThat(deserialized).isNotNull();
            assertThat(deserialized.getEventId()).isEqualTo("test-event-id-123");
            assertThat(deserialized.getEventType()).isEqualTo("TEST_EVENT");
            assertThat(deserialized.getOccurredAt()).isEqualTo(1704067200000L);
            assertThat(deserialized.getUserId()).isEqualTo(12345L);
            assertThat(deserialized.getIsActive()).isTrue();
        }

        @Test
        void 직렬화_역직렬화_round_trip이_정상_동작한다() {
            // given
            String expectedEventId = "test-event-id-456";
            String expectedEventType = "USER_CREATED";
            long expectedOccurredAt = 1704153600000L;
            long expectedUserId = 67890L;
            boolean expectedIsActive = false;

            TestEventFixture originalEvent = TestEventFixture.newBuilder()
                .setEventId(expectedEventId)
                .setEventType(expectedEventType)
                .setOccurredAt(expectedOccurredAt)
                .setUserId(expectedUserId)
                .setIsActive(expectedIsActive)
                .build();

            // when
            byte[] serialized = serializer.serialize(originalEvent);
            TestEventFixture deserialized = deserializer.deserialize(serialized, TestEventFixture.class);

            // then
            assertThat(deserialized.getEventId()).isEqualTo(expectedEventId);
            assertThat(deserialized.getEventType()).isEqualTo(expectedEventType);
            assertThat(deserialized.getOccurredAt()).isEqualTo(expectedOccurredAt);
            assertThat(deserialized.getUserId()).isEqualTo(expectedUserId);
            assertThat(deserialized.getIsActive()).isEqualTo(expectedIsActive);
        }

        @Test
        void 빈_문자열을_포함한_이벤트도_역직렬화할_수_있다() {
            // given
            TestEventFixture originalEvent = TestEventFixture.newBuilder()
                .setEventId("")
                .setEventType("")
                .setOccurredAt(0L)
                .setUserId(0L)
                .setIsActive(false)
                .build();
            byte[] serialized = serializer.serialize(originalEvent);

            // when
            TestEventFixture deserialized = deserializer.deserialize(serialized, TestEventFixture.class);

            // then
            assertThat(deserialized).isNotNull();
            assertThat(deserialized.getEventId()).isEmpty();
            assertThat(deserialized.getEventType()).isEmpty();
        }

        @Test
        void null_데이터는_IllegalArgumentException을_발생시킨다() {
            // when & then
            assertThatThrownBy(() -> deserializer.deserialize(null, TestEventFixture.class))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("데이터는 null일 수 없습니다.");
        }

        @Test
        void null_대상_클래스는_IllegalArgumentException을_발생시킨다() {
            // given
            byte[] data = new byte[]{1, 2, 3};

            // when & then
            assertThatThrownBy(() -> deserializer.deserialize(data, null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("대상 클래스는 null일 수 없습니다.");
        }

        @Test
        void 지원되지_않는_타입은_IllegalArgumentException을_발생시킨다() {
            // given
            byte[] data = new byte[]{1, 2, 3};

            // when & then
            assertThatThrownBy(() -> deserializer.deserialize(data, String.class))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("지원되지 않는 이벤트 타입입니다");
        }

        @Test
        void 잘못된_바이트_배열은_EventDeserializationException을_발생시킨다() {
            // given
            byte[] invalidData = new byte[]{0, 1, 2, 3, 4, 5};

            // when & then
            assertThatThrownBy(() -> deserializer.deserialize(invalidData, TestEventFixture.class))
                .isInstanceOf(EventDeserializationException.class)
                .hasMessageContaining("이벤트 역직렬화에 실패했습니다");
        }
    }
}