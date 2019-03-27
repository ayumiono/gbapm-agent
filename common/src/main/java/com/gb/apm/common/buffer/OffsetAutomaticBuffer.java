package com.gb.apm.common.buffer;

import java.nio.ByteBuffer;

/**
 * @author emeroad
 */
public class OffsetAutomaticBuffer extends AutomaticBuffer {

    protected int startOffset;
    protected int endOffset;

    /**
     * Unsafe API
     * unsafe array access of HBase Cell
     * @deprecated Since 1.6.0. Use {@link OffsetAutomaticBuffer(byte[], int, int)}
     */
    @Deprecated
    public OffsetAutomaticBuffer(final byte[] buffer, final int startOffset) {
        this(buffer, startOffset, buffer.length);
    }


    public OffsetAutomaticBuffer(final byte[] buffer) {
        this(buffer, 0, buffer.length);
    }


    public OffsetAutomaticBuffer(final byte[] buffer, final int startOffset, int length) {
        if (buffer == null) {
            throw new NullPointerException("buffer must not be null");
        }
        if (startOffset < 0) {
            throw new IndexOutOfBoundsException("negative offset:" + startOffset);
        }
        if (length < 0) {
            throw new IndexOutOfBoundsException("negative length:" + length);
        }
        if (startOffset > buffer.length) {
            throw new IndexOutOfBoundsException("startOffset:" + startOffset + " > buffer.length:" + buffer.length);
        }
        final int endOffset = startOffset + length;
        if (endOffset > buffer.length) {
            throw new IndexOutOfBoundsException("too large length buffer.length:" + buffer.length + " endOffset:" + endOffset);
        }
        this.buffer = buffer;
        this.offset = startOffset;
        this.startOffset = startOffset;
        this.endOffset = endOffset;
    }

    protected void checkExpand(final int size) {
        super.checkExpand(size);
        this.endOffset = buffer.length;
    }

    @Override
    public byte[] getBuffer() {
        if (startOffset == 0 && offset == buffer.length) {
            return this.buffer;
        } else {
            return copyBuffer();
        }
    }

    @Override
    public byte[] copyBuffer() {
        final int length = offset - startOffset;
        final byte[] copy = new byte[length];
        System.arraycopy(buffer, startOffset, copy, 0, length);
        return copy;
    }

    @Override
    public ByteBuffer wrapByteBuffer() {
        final int length = offset - startOffset;
        return ByteBuffer.wrap(this.buffer, startOffset, length);
    }

    @Override
    public int remaining() {
        return endOffset - offset;
    }

    @Override
    public boolean hasRemaining() {
        return offset < endOffset;
    }
}
