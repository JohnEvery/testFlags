package com.eve.flags.service.impl;

import com.eve.flags.service.interfaces.FlagService;
import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Locale;

@Service
public class FlagServiceImpl implements FlagService {

    @Value("${internal.flags.url}")
    private String flagsUrl;

    @Value("${internal.country.url}")
    private String countriesUrl;

    @Value("${internal.base.dir}")
    private String path;

    private static final String FORMAT = ".gif";

    @Override
    public void downloadAllFlags() throws IOException {
        String jsonString = IOUtils.toString(new URL(countriesUrl), StandardCharsets.UTF_8);
        JSONArray jsonArray = new JSONArray(jsonString);
        for (int i = 0; i < jsonArray.length(); i++) {
            String alpha2Code = jsonArray.getJSONObject(i).get("alpha2Code").toString().toLowerCase(Locale.ROOT);
            Path downloadPath = Paths.get(String.format("%s/geonames/flags/x/%s%s", path, alpha2Code, FORMAT));
            downloadFile(alpha2Code, downloadPath);
        }
    }

    private void downloadFile(String alpha2Code, Path downloadPath) throws IOException {
        try {
            Path file = Files.createFile(downloadPath);
            URL link = new URL(String.format("%s%s%s", flagsUrl, alpha2Code, FORMAT));
            ReadableByteChannel rbc = Channels.newChannel(link.openStream());
            try (FileOutputStream fos = new FileOutputStream(file.toString())) {
                fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
            }
        } catch (FileAlreadyExistsException x) {
            System.err.format("file named %s%s" +
                    " already exists.", alpha2Code, FORMAT);

        }
    }
}
