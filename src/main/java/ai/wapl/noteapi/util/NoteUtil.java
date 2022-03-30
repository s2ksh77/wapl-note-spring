package ai.wapl.noteapi.util;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Random;

import static ai.wapl.noteapi.util.Constants.ASIA_SEOUL;

public class NoteUtil {
    static final String DATE_FORMAT = "yyyy.MM.dd HH:mm:ss";
    static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(DATE_FORMAT);

    public static LocalDateTime stringToDate(String date) {
        return date == null ? null : LocalDateTime.parse(date, DATE_TIME_FORMATTER);
    }

    public static String dateToString(LocalDateTime date) {
        return date == null ? null : DATE_TIME_FORMATTER.format(date);
    }

    public static LocalDateTime now() {
        return ZonedDateTime.now(ASIA_SEOUL).toLocalDateTime();
    }

    public static String getRandomColor() {
        String[] colorArray = {
                "#C84847", "#F29274", "#F6C750",
                "#77B69B", "#679886", "#3A7973",
                "#77BED3", "#5C83DA", "#8F91E7",
                "#DF97AA", "#CA6D6D"
        };

        int idx = new Random().nextInt(colorArray.length);

        return colorArray[idx];
    }

    public static boolean isMobile(String userAgent){
        String[] mobile = {
                "okhttp",
                "iPhone",
                "iPod",
                "Android",
                "BlackBerry",
                "Windows CE",
                "Nokia",
                "Webos",
                "Opera Mini",
                "SonyEricsson",
                "Opera Mobi",
                "IEMobile"
        };

        return Arrays.stream(mobile).anyMatch(userAgent::contains);
    }

    public static boolean isImage(String FileExtension) {
        String[] ImageExt = {"jpg", "gif", "jpeg", "jfif", "tiff", "bmp", "bpg", "png"};
        return Arrays.asList(ImageExt).contains(FileExtension.toLowerCase());
    }

}
