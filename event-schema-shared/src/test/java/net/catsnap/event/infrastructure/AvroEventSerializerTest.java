package net.catsnap.event.infrastructure;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.ByteArrayInputStream;
import net.catsnap.event.test.v1.TestEventFixture;
import net.catsnap.shared.infrastructure.AvroEventSerializer;
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
        @DisplayName("Avro 이벤트를 바이트 배열로 직렬화 가능하다")
        void it_serializes_event_to_bytes() {
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
        @DisplayName("직렬화된 바이트 배열은 역직렬화 가능하다 (round-trip)")
        void it_can_be_deserialized_back() throws Exception {
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
            SpecificDatumReader<TestEventFixture> reader = new SpecificDatumReader<>(TestEventFixture.class);
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
        @DisplayName("빈 문자열을 포함한 이벤트도 직렬화할 수 있다")
        void it_serializes_event_with_empty_strings() {
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
    }
}