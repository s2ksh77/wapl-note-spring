package ai.wapl.noteapi.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class NoteUtil {

    public static String generateDate() {
        Date time = new Date();
        String dateFormat = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        sdf.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));

        return sdf.format(time);
    }
}
