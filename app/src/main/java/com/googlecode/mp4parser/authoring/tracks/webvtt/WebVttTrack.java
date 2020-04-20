package com.googlecode.mp4parser.authoring.tracks.webvtt;

import com.bumptech.glide.load.Key;
import com.coremedia.iso.boxes.Box;
import com.coremedia.iso.boxes.SampleDescriptionBox;
import com.googlecode.mp4parser.authoring.AbstractTrack;
import com.googlecode.mp4parser.authoring.Sample;
import com.googlecode.mp4parser.authoring.TrackMetaData;
import com.googlecode.mp4parser.authoring.tracks.webvtt.sampleboxes.CuePayloadBox;
import com.googlecode.mp4parser.authoring.tracks.webvtt.sampleboxes.CueSettingsBox;
import com.googlecode.mp4parser.authoring.tracks.webvtt.sampleboxes.VTTCueBox;
import com.googlecode.mp4parser.authoring.tracks.webvtt.sampleboxes.VTTEmptyCueBox;
import com.googlecode.mp4parser.util.ByteBufferByteChannel;
import com.googlecode.mp4parser.util.CastUtils;
import com.googlecode.mp4parser.util.Mp4Arrays;
import com.mp4parser.iso14496.part30.WebVTTConfigurationBox;
import com.mp4parser.iso14496.part30.WebVTTSampleEntry;
import com.mp4parser.iso14496.part30.WebVTTSourceLabelBox;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.WritableByteChannel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WebVttTrack extends AbstractTrack {
    private static final Sample EMPTY_SAMPLE = new Sample() {
        ByteBuffer vtte;

        {
            VTTEmptyCueBox vTTEmptyCueBox = new VTTEmptyCueBox();
            this.vtte = ByteBuffer.allocate(CastUtils.l2i(vTTEmptyCueBox.getSize()));
            try {
                vTTEmptyCueBox.getBox(new ByteBufferByteChannel(this.vtte));
                this.vtte.rewind();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        public void writeTo(WritableByteChannel writableByteChannel) throws IOException {
            writableByteChannel.write(this.vtte.duplicate());
        }

        public long getSize() {
            return (long) this.vtte.remaining();
        }

        public ByteBuffer asByteBuffer() {
            return this.vtte.duplicate();
        }
    };
    private static final Pattern WEBVTT_CUE_IDENTIFIER = Pattern.compile(WEBVTT_CUE_IDENTIFIER_STRING);
    private static final String WEBVTT_CUE_IDENTIFIER_STRING = "^(?!.*(-->)).*$";
    private static final Pattern WEBVTT_CUE_SETTING = Pattern.compile(WEBVTT_CUE_SETTING_STRING);
    private static final String WEBVTT_CUE_SETTING_STRING = "\\S*:\\S*";
    private static final Pattern WEBVTT_FILE_HEADER = Pattern.compile(WEBVTT_FILE_HEADER_STRING);
    private static final String WEBVTT_FILE_HEADER_STRING = "^﻿?WEBVTT((\\u0020|\t).*)?$";
    private static final Pattern WEBVTT_METADATA_HEADER = Pattern.compile(WEBVTT_METADATA_HEADER_STRING);
    private static final String WEBVTT_METADATA_HEADER_STRING = "\\S*[:=]\\S*";
    private static final Pattern WEBVTT_TIMESTAMP = Pattern.compile(WEBVTT_TIMESTAMP_STRING);
    private static final String WEBVTT_TIMESTAMP_STRING = "(\\d+:)?[0-5]\\d:[0-5]\\d\\.\\d{3}";
    long[] sampleDurations = new long[0];
    WebVTTSampleEntry sampleEntry;
    List<Sample> samples = new ArrayList();
    SampleDescriptionBox stsd;
    TrackMetaData trackMetaData = new TrackMetaData();

    public void close() throws IOException {
    }

    public String getHandler() {
        return "text";
    }

    private static class BoxBearingSample implements Sample {
        List<Box> boxes;

        public BoxBearingSample(List<Box> list) {
            this.boxes = list;
        }

        public void writeTo(WritableByteChannel writableByteChannel) throws IOException {
            for (Box box : this.boxes) {
                box.getBox(writableByteChannel);
            }
        }

        public long getSize() {
            long j = 0;
            for (Box size : this.boxes) {
                j += size.getSize();
            }
            return j;
        }

        public ByteBuffer asByteBuffer() {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            try {
                writeTo(Channels.newChannel(byteArrayOutputStream));
                return ByteBuffer.wrap(byteArrayOutputStream.toByteArray());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public WebVttTrack(InputStream inputStream, String str, Locale locale) throws IOException {
        super(str);
        this.trackMetaData.setTimescale(1000);
        this.trackMetaData.setLanguage(locale.getISO3Language());
        this.stsd = new SampleDescriptionBox();
        this.sampleEntry = new WebVTTSampleEntry();
        this.stsd.addBox(this.sampleEntry);
        WebVTTConfigurationBox webVTTConfigurationBox = new WebVTTConfigurationBox();
        this.sampleEntry.addBox(webVTTConfigurationBox);
        this.sampleEntry.addBox(new WebVTTSourceLabelBox());
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, Key.STRING_CHARSET_NAME));
        String readLine = bufferedReader.readLine();
        if (readLine == null || !WEBVTT_FILE_HEADER.matcher(readLine).matches()) {
            throw new IOException("Expected WEBVTT. Got " + readLine);
        }
        webVTTConfigurationBox.setConfig(String.valueOf(webVTTConfigurationBox.getConfig()) + "\n" + readLine);
        while (true) {
            String readLine2 = bufferedReader.readLine();
            if (readLine2 == null) {
                throw new IOException("Expected an empty line after webvtt header");
            } else if (readLine2.isEmpty()) {
                long j = 0;
                while (true) {
                    String readLine3 = bufferedReader.readLine();
                    if (readLine3 != null) {
                        if (!"".equals(readLine3.trim())) {
                            readLine3 = WEBVTT_CUE_IDENTIFIER.matcher(readLine3).find() ? bufferedReader.readLine() : readLine3;
                            Matcher matcher = WEBVTT_TIMESTAMP.matcher(readLine3);
                            if (matcher.find()) {
                                long parseTimestampUs = parseTimestampUs(matcher.group());
                                if (matcher.find()) {
                                    String group = matcher.group();
                                    long parseTimestampUs2 = parseTimestampUs(group);
                                    Matcher matcher2 = WEBVTT_CUE_SETTING.matcher(readLine3.substring(readLine3.indexOf(group) + group.length()));
                                    String str2 = null;
                                    while (matcher2.find()) {
                                        str2 = matcher2.group();
                                    }
                                    StringBuilder sb = new StringBuilder();
                                    while (true) {
                                        String readLine4 = bufferedReader.readLine();
                                        if (readLine4 != null && !readLine4.isEmpty()) {
                                            if (sb.length() > 0) {
                                                sb.append("\n");
                                            }
                                            sb.append(readLine4.trim());
                                        }
                                    }
                                    if (parseTimestampUs != j) {
                                        this.sampleDurations = Mp4Arrays.copyOfAndAppend(this.sampleDurations, parseTimestampUs - j);
                                        this.samples.add(EMPTY_SAMPLE);
                                    }
                                    this.sampleDurations = Mp4Arrays.copyOfAndAppend(this.sampleDurations, parseTimestampUs2 - parseTimestampUs);
                                    VTTCueBox vTTCueBox = new VTTCueBox();
                                    if (str2 != null) {
                                        CueSettingsBox cueSettingsBox = new CueSettingsBox();
                                        cueSettingsBox.setContent(str2);
                                        vTTCueBox.setCueSettingsBox(cueSettingsBox);
                                    }
                                    CuePayloadBox cuePayloadBox = new CuePayloadBox();
                                    cuePayloadBox.setContent(sb.toString());
                                    vTTCueBox.setCuePayloadBox(cuePayloadBox);
                                    this.samples.add(new BoxBearingSample(Collections.singletonList(vTTCueBox)));
                                    j = parseTimestampUs2;
                                } else {
                                    throw new IOException("Expected cue end time: " + readLine3);
                                }
                            } else {
                                throw new IOException("Expected cue start time: " + readLine3);
                            }
                        }
                    } else {
                        return;
                    }
                }
            } else if (WEBVTT_METADATA_HEADER.matcher(readLine2).find()) {
                webVTTConfigurationBox.setConfig(String.valueOf(webVTTConfigurationBox.getConfig()) + "\n" + readLine2);
            } else {
                throw new IOException("Expected WebVTT metadata header. Got " + readLine2);
            }
        }
    }

    private static long parseTimestampUs(String str) throws NumberFormatException {
        if (str.matches(WEBVTT_TIMESTAMP_STRING)) {
            String[] split = str.split("\\.", 2);
            long j = 0;
            for (String parseLong : split[0].split(":")) {
                j = (j * 60) + Long.parseLong(parseLong);
            }
            return (j * 1000) + Long.parseLong(split[1]);
        }
        throw new NumberFormatException("has invalid format");
    }

    public SampleDescriptionBox getSampleDescriptionBox() {
        return this.stsd;
    }

    public long[] getSampleDurations() {
        long[] jArr = new long[this.sampleDurations.length];
        for (int i = 0; i < jArr.length; i++) {
            jArr[i] = (this.sampleDurations[i] * this.trackMetaData.getTimescale()) / 1000;
        }
        return jArr;
    }

    public TrackMetaData getTrackMetaData() {
        return this.trackMetaData;
    }

    public List<Sample> getSamples() {
        return this.samples;
    }
}
