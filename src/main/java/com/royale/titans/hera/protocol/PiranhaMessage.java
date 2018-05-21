package com.royale.titans.hera.protocol;

import com.royale.titans.hera.utils.binary.ByteStream;

import java.lang.reflect.Field;

public class PiranhaMessage {

    private short mId;
    private int mLength;
    private int mVersion;

    private ByteStream mStream;

    public PiranhaMessage(short id) {
        setId(id);
    }

    public PiranhaMessage(ByteStream stream) {
        mStream = stream;
    }

    public short getId() {
        return mId;
    }

    public void setId(short id) {
        mId = id;
    }

    public int getLength() {
        return mLength;
    }

    public void setLength(int length) {
        mLength = length;
    }

    public int getVersion() {
        return mVersion;
    }

    public void setVersion(int version) {
        mVersion = version;
    }

    public ByteStream encode() {
        return null;
    }

    public void process() {
    }

    public byte[] toBytes(byte[] data) {
        ByteStream stream = new ByteStream();

        mLength = data.length;

        stream.writeShort(mId);
        stream.write((byte) (mLength >>> 16));
        stream.write((byte) (mLength >>> 8));
        stream.write((byte) mLength);
        stream.writeShort(mVersion);
        stream.write(data);

        return stream.obtain().array();
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        Class<?> clazz = null;
        try {
            clazz = Class.forName(this.getClass().getName());

            Field[] fields = clazz.getDeclaredFields();

            builder.append(clazz.getSimpleName() + " Fields: \n\n");

            for (Field field : fields) {
                String name = field.getName();

                if (!name.contains("unk"))
                    builder.append(name + " : " + field.get(this) + "\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return builder.toString();
    }
}
