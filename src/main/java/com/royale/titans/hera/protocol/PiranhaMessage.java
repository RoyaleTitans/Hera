package com.royale.titans.hera.protocol;

import com.royale.titans.hera.utils.binary.ByteStream;

import java.lang.reflect.Field;

public class PiranhaMessage {

    public short id;
    public int length;
    public int version;

    public ByteStream stream;

    public PiranhaMessage(short id) {
        this.id = id;
    }

    public PiranhaMessage(ByteStream stream) {
        this.stream = stream;
    }

    public ByteStream encode() {
        return null;
    }

    public void process() {
    }

    public byte[] toBytes(byte[] data) {
        ByteStream stream = new ByteStream();

        this.length = data.length;

        stream.writeShort(this.id);
        stream.write((byte) (this.length >>> 16));
        stream.write((byte) (this.length >>> 8));
        stream.write((byte) this.length);
        stream.writeShort(this.version);
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
