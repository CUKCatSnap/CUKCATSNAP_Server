package net.catsnap.shared.infrastructure;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.io.ByteArrayInputStream;
import net.catsnap.event.test.v1.TestEventFixture;
import org.apache.avro.io.BinaryDecoder;
import org.apache.avro.io.DecoderFactory;
import org.apache.avro.specific.SpecificDatumReader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@DisplayName("AvroEventSerializer 통합 테스트")
class AvroEventSerializerTest {

    private AvroEventSerializer serializer;

    @BeforeEach
    void setUp() {
        serializer = new AvroEventSerializer();
    }

    @Nested
    class serialize_테스트 {

        @Test
        void Avro_이벤트를_바이트_배열로_직렬화_가능하다() {
            // given
            TestEventFixture event = TestEventFixture.newBuilder()
                .setEventId("test-event-id-123")
                .setEventType("TEST_EVENT")
                .setOccurredAt(1704067200000L)
                .setUserId(12345L)
                .setIsActive(true)
                .build();

            // when
            byte[] serialized = serializer.serialize(event);

            // then
            assertThat(serialized).isNotNull();
            assertThat(serialized).isNotEmpty();
        }

        @Test
        void 직렬화된_바이트_배열은_역직렬화_가능하다_round_trip() throws Exception {
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

            // then - 역직렬화하여 원본과 동일한지 확인
            SpecificDatumReader<TestEventFixture> reader = new SpecificDatumReader<>(
                TestEventFixture.class);
            BinaryDecoder decoder = DecoderFactory.get()
                .binaryDecoder(new ByteArrayInputStream(serialized), null);
            TestEventFixture deserialized = reader.read(null, decoder);

            assertThat(deserialized.getEventId()).isEqualTo(expectedEventId);
            assertThat(deserialized.getEventType()).isEqualTo(expectedEventType);
            assertThat(deserialized.getOccurredAt()).isEqualTo(expectedOccurredAt);
            assertThat(deserialized.getUserId()).isEqualTo(expectedUserId);
            assertThat(deserialized.getIsActive()).isEqualTo(expectedIsActive);
        }

        @Test
        void 빈_문자열을_포함한_이벤트도_직렬화할_수_있다() {
            // given
            TestEventFixture event = TestEventFixture.newBuilder()
                .setEventId("")
                .setEventType("")
                .setOccurredAt(0L)
                .setUserId(0L)
                .setIsActive(false)
                .build();

            // when
            byte[] serialized = serializer.serialize(event);

            // then
            assertThat(serialized).isNotNull();
            assertThat(serialized).isNotEmpty();
        }

        @Test
        void null_이벤트는_IllegalArgumentException을_발생시킨다() {
            // when & then
            assertThatThrownBy(() -> serializer.serialize(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("이벤트는 null일 수 없습니다.");
        }

        @Test
        void 지원되지_않는_타입은_IllegalArgumentException을_발생시킨다() {
            // given
            String invalidEvent = "This is not an Avro event";

            // when & then
            assertThatThrownBy(() -> serializer.serialize(invalidEvent))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("지원되지 않는 이벤트 타입입니다");
        }
    }
}