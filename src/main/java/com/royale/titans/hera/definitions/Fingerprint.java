package com.royale.titans.hera.definitions;

import com.google.gson.*;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Fingerprint {
    private String mHash;

    private String mVersion;

    private List<Asset> mFiles;

    public String getHash() {
        return mHash;
    }

    public void setHash(String hash) {
        mHash = hash;
    }

    public String getVersion() {
        return mVersion;
    }

    public void setVersion(String version) {
        mVersion = version;
    }

    public List<Asset> getFiles() {
        return mFiles;
    }

    public void setFiles(List<Asset> files) {
        mFiles = files;
    }

    public static Fingerprint read() {
        JsonDeserializer<Fingerprint> deserializer = new JsonDeserializer<Fingerprint>() {
            @Override
            public Fingerprint deserialize(JsonElement json, Type type, JsonDeserializationContext context) throws JsonParseException {
                final JsonObject jsonObject = json.getAsJsonObject();

                final JsonArray filesArray = jsonObject.get("files").getAsJsonArray();

                final String hash = jsonObject.get("sha").getAsString();

                final String version = jsonObject.get("version").getAsString();

                final List<Asset> files = new ArrayList<Asset>(filesArray.size());

                for (JsonElement file : filesArray) {
                    final JsonObject fileObj = file.getAsJsonObject();
                    final String fileElement = fileObj.get("file").getAsString();
                    final String hashElement = fileObj.get("sha").getAsString();

                    files.add(new Asset(hashElement, fileElement));
                }

                final Fingerprint fingerprint = new Fingerprint();
                fingerprint.setHash(hash);
                fingerprint.setVersion(version);
                fingerprint.setFiles(files);

                return fingerprint;
            }
        };

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Fingerprint.class, deserializer);
        Gson gson = gsonBuilder.create();

        Fingerprint fingerprint = null;

        try (Reader reader = new InputStreamReader(new FileInputStream("fingerprint.json"), "UTF-8")) {
            fingerprint = gson.fromJson(reader, Fingerprint.class);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return fingerprint;
    }
}
