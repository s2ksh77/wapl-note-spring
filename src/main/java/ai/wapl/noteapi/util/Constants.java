package ai.wapl.noteapi.util;

import java.time.ZoneId;

public class Constants {
    public static final ZoneId ASIA_SEOUL = ZoneId.of("Asia/Seoul");

    public static final String EMPTY_CONTENT = "<p></br></p>";
    public static final String SHARED_PAGE_TYPE = "shared";

    public static final String RETURN_MSG_SUCCESS = "Success";
    public static final String RETURN_MSG_FAIL = "Fail";

    public static final String DEFAULT_API_URI = "/apis/v1";

    public static final String KOREAN = "ko";
    public static final String ENGLISH = "en";

    public static final String NEW_PAGE_KOREAN = "새 페이지";
    public static final String NEW_PAGE_ENGLISH = "New Page";
    public static final String NEW_CHAPTER_KOREAN = "새 챕터";
    public static final String NEW_CHAPTER_ENGLISH = "New Chapter";
    public static final String RECYCLE_BIN_NAME = "휴지통";

    public static final String NOTE_CHANNEL_CODE = "CHN0003";
}
