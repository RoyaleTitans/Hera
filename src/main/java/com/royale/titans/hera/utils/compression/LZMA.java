package com.royale.titans.hera.utils.compression;

import org.apache.commons.compress.utils.IOUtils;
import org.tukaani.xz.LZMAInputStream;

import java.io.*;
import java.util.Arrays;

public class LZMA {
    public static void decompress(String compressed, String target) {
        byte[] buffer = null;
        try {
            buffer = IOUtils.toByteArray(new FileInputStream(compressed));
        } catch (IOException e) {
            e.printStackTrace();
        }

        byte[] tempBuffer = new byte[buffer.length + 4];
        System.arraycopy(buffer, 0, tempBuffer, 0, 9);
        Arrays.fill(tempBuffer, 9, 13, (byte) 0x00);
        System.arraycopy(buffer, 9, tempBuffer, 9 + 4, buffer.length - 9);

        ByteArrayOutputStream out = null;
        try {
            LZMAInputStream in = new LZMAInputStream(new ByteArrayInputStream(tempBuffer));
            out = new ByteArrayOutputStream();

            tempBuffer = new byte[256];
            int i;
            while ((i = in.read(tempBuffer)) > 0) {
                out.write(tempBuffer, 0, i);
            }

            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            FileOutputStream decompStream = new FileOutputStream(target);
            decompStream.write(out.toByteArray());
            decompStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
