package io.nats.client;

import com.fasterxml.jackson.core.JsonProcessingException;

public interface Schema<T> extends Cloneable{

    /**
     * Check if the message is a valid object for this schema.
     *
     * <p>The implementation can choose what its most efficient approach to validate the schema.
     * If the implementation doesn't provide it, it will attempt to use {@link #decode(String, byte[])}
     * to see if this schema can decode this message or not as a validation mechanism to verify
     * the bytes.
     *
     * @param message the messages to verify
     * @throws RuntimeException if it is not a valid message
     */
    default void validate(String subject, byte[] message) {
        decode(subject, message);
    }

    /**
     * Encode an object representing the message content into a byte array.
     *
     * @param message
     *            the message object
     * @return a byte array with the serialized content
     * @throws RuntimeException
     *             if the serialization fails
     */
    byte[] encode(T message) throws JsonProcessingException;

    /**
     * Decode a byte array into an object using the schema definition and deserializer implementation.
     *
     * @param bytes
     *            the byte array to decode
     * @return the deserialized object
     */
    T decode(String subject, byte[] bytes);
}
