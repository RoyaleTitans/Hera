package com.royale.titans.hera.core;

import com.royale.titans.hera.Configuration;
import com.royale.titans.hera.definitions.Asset;
import com.royale.titans.hera.definitions.Fingerprint;
import com.royale.titans.hera.utils.Debugger;
import com.royale.titans.hera.utils.compression.LZMA;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

public class Downloader {

    public static void start() throws IOException {
        Fingerprint fingerprint = Resources.fingerprint;

        for (Asset file : fingerprint.getFiles()) {
            String cdn = Configuration.CDN_URL + "/" + fingerprint.getHash() + "/" + file.getName();

            URL location = new URL(cdn);
            String fileLocation = cdn.replace(Configuration.CDN_URL + "/", "").replace(fingerprint.getHash() + "/", "");

            ReadableByteChannel byteChannel = Channels.newChannel(location.openStream());

            File newFile = new File("out/compressed/" + fileLocation);
            File directory = new File(newFile.getParent());
            if (!directory.exists()) {
                directory.mkdirs();
            }
            newFile.createNewFile();

            FileOutputStream stream = new FileOutputStream(newFile);

            if (onBlacklist(newFile.getPath())) {
                Debugger.info("Skipped " + fileLocation + ".");
            } else {
                stream.getChannel().transferFrom(byteChannel, 0, Long.MAX_VALUE);
                stream.close();

                File decompFile = new File("out/decompressed/" + fileLocation);
                File decompDirectory = new File(decompFile.getParent());
                if (!decompDirectory.exists()) {
                    decompDirectory.mkdirs();
                }

                if (newFile.getPath().endsWith(".csv"))
                    LZMA.decompress(newFile.getPath(), decompFile.getPath());

                Debugger.info("Downloaded and decompressed " + fileLocation + ".");
            }
        }
    }

    private static boolean onBlacklist(String file) {
        if (Configuration.ENABLE_BLACKLIST) {
            if (file.endsWith(".ogg") || file.endsWith(".m4a"))
                return true;

            return false;
        }

        return false;
    }
}
