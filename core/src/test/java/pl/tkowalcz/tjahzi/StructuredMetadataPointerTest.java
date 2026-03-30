package pl.tkowalcz.tjahzi;

import org.agrona.concurrent.UnsafeBuffer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class StructuredMetadataPointerTest {

    private StructuredMetadataPointer pointer;
    private UnsafeBuffer buffer;

    @BeforeEach
    public void setUp() {
        pointer = new StructuredMetadataPointer();
        buffer = new UnsafeBuffer(new byte[256]);
    }

    @Test
    public void shouldWrapBufferCorrectly() {
        // Given
        int index = 10;
        int size = 20;

        // When
        pointer.wrap(buffer, index, size);

        // Then
        assertThat(pointer.getBuffer()).isEqualTo(buffer);
        assertThat(pointer.getIndex()).isEqualTo(index);
        assertThat(pointer.getSize()).isEqualTo(size);
    }

    @Test
    public void shouldCreateWrappingBufferCorrectly() {
        // Given
        int index = 10;
        int size = 20;

        // When
        pointer = new StructuredMetadataPointer(buffer, index, size);

        // Then
        assertThat(pointer.getBuffer()).isEqualTo(buffer);
        assertThat(pointer.getIndex()).isEqualTo(index);
        assertThat(pointer.getSize()).isEqualTo(size);
    }

    @Test
    public void shouldReturnTrueWhenHasBytes() {
        // Given
        pointer.wrap(buffer, 0, 10);

        // When
        boolean result = pointer.hasBytes();

        // Then
        assertThat(result).isTrue();
    }

    @Test
    public void shouldReturnFalseWhenHasNoBytes() {
        // Given
        pointer.wrap(buffer, 0, 0);

        // When
        boolean result = pointer.hasBytes();

        // Then
        assertThat(result).isFalse();
    }

    @Test
    public void shouldReadIntCorrectly() {
        // Given
        int index = 0;
        int value = 12345;
        buffer.putInt(index, value);
        pointer.wrap(buffer, index, Integer.BYTES);

        // When
        int result = pointer.readInt();

        // Then
        assertThat(result).isEqualTo(value);
        assertThat(pointer.getIndex()).isEqualTo(index + Integer.BYTES);
    }

    @Test
    public void shouldGetStringUtf8Correctly() {
        // Given
        int index = 0;
        String value = "test";
        buffer.putStringUtf8(index, value);
        int byteLength = buffer.getInt(index);
        pointer.wrap(buffer, index, byteLength + Integer.BYTES);

        // When
        String result = pointer.getStringUtf8();

        // Then
        assertThat(result).isEqualTo(value);
        assertThat(pointer.getIndex()).isEqualTo(index + byteLength + Integer.BYTES);
    }
}
