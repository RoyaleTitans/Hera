package com.royale.titans.hera.utils.binary;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class ByteStream {

    private ByteBuffer mBuffer;

    private final ByteArrayOutputStream mStream = new ByteArrayOutputStream();

    public ByteStream() {
    }

    public ByteStream(ByteBuffer byteBuffer) {
        mBuffer = byteBuffer;
    }

    public ByteStream obtain() {
        return ByteStream.wrap(mStream.toByteArray());
    }

    public ByteStream write(byte... bytes) {
        try {
            mStream.write(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return this;
    }

    public ByteStream write(byte[] bytes, int off, int len) {
        mStream.write(bytes, off, len);
        return this;
    }

    public ByteStream writeInt(int value) {
        try {
            mStream.write(ByteBuffer.allocate(4).putInt(value).array());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return this;
    }

    public ByteStream writeIntLe(int value) {
        try {
            mStream.write(ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN).putInt(value).array());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return this;
    }

    public ByteStream writeLong(long value) {
        try {
            mStream.write(ByteBuffer.allocate(8).putLong(value).array());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return this;
    }

    public int writeVInt(long value) {
        if (value == 0) {
            write((byte) 0);
            return 1;
        }

        int c = 0;
        boolean rotate = true;
        byte b;

        value = (value << 1) ^ (value >> 31);
        value >>>= 0;

        while (value != 0) {
            b = (byte) (value & 0x7f);
            if (value < 0 || value >= 0x80)
                b |= 0x80;
            if (rotate) {
                rotate = false;
                byte lsb = (byte) (b & 0x1);
                byte msb = (byte) ((b & 0x80) >> 7);

                b = (byte) (b >> 1);
                b = (byte) (b & ~(0xC0));
                b = (byte) (b | (msb << 7) | (lsb << 6));
            }
            write(b);
            value >>>= 7;
            c++;
        }
        return c;
    }

    public ByteStream writeShort(int value) {
        try {
            mStream.write(ByteBuffer.allocate(2).putShort((short) value).array());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return this;
    }

    public ByteStream writeString(String value) {
        writeInt(value.length());

        if (value.length() > 0) {
            try {
                mStream.write(value.getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return this;
    }

    public static ByteStream allocate(int size) {
        ByteBuffer byteBuffer = ByteBuffer.allocate(size);
        return new ByteStream(byteBuffer);
    }

    public static ByteStream wrap(byte[] bytes) {
        ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);
        return new ByteStream(byteBuffer);
    }

    public byte[] array() {
        return mBuffer.array();
    }

    public int capacity() {
        return mBuffer.capacity();
    }

    public void clear() {
        mBuffer.clear();
    }

    public void flip() {
        mBuffer.flip();
    }

    public ByteBuffer getByteBuffer() {
        return mBuffer;
    }

    public int position() {
        return mBuffer.position();
    }

    public void position(int newPosition) {
        mBuffer.position(newPosition);
    }

    public byte read() {
        return mBuffer.get();
    }

    public byte[] readArray() {
        int len = readInt();
        byte[] b = read(len);
        return b;
    }

    public byte[] read(int len) {
        byte[] b = new byte[len];
        read(b, 0, len);
        return b;
    }

    public void read(byte[] dest, int offset, int len) {
        mBuffer.get(dest, offset, len);
    }

    public int readInt() {
        return mBuffer.getInt();
    }

    public VInt readVInt() {
        int c = 0;
        int value = 0;
        int seventh;
        int msb;
        int b;
        do {
            b = read();

            if (c == 0) {
                seventh = (b & 0x40) >> 6;
                msb = (b & 0x80) >> 7;
                b = b << 1;
                b = b & ~(0x181);
                b = b | (msb << 7) | (seventh);
            }

            value |= (b & 0x7f) << (7 * c);
            ++c;
        } while ((b & 0x80) != 0);

        value = ((value >>> 1) ^ -(value & 1));

        return new VInt(value, c);
    }

    public int readShort() {
        return mBuffer.getShort();
    }

    public long readLong() {
        return mBuffer.getLong();
    }

    public String readString() {
        int len = readInt();

        if (len < 0) {
            return "";
        }

        byte[] b = new byte[len];
        read(b, 0, len);

        return new String(b);
    }

    public ByteStream rewind() {
        mBuffer.rewind();
        return this;
    }
}
