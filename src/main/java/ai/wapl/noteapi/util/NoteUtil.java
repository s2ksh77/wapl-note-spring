package ai.wapl.noteapi.util;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Random;
import java.util.TimeZone;

public class NoteUtil {

    public static String generateDate() {
        Date time = new Date();
        String dateFormat = "yyyy.MM.dd HH:mm:ss";
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        sdf.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));

        return sdf.format(time) + " Asia/Seoul";
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
