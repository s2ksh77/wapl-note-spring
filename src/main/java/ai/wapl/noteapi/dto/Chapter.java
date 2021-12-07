package ai.wapl.noteapi.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Chapter {

    @JsonProperty("ID")
    private String id;

    @JsonProperty("NOTE_CHANNEL_ID")
    private String chapterId;

    @JsonProperty("TEXT")
    private String name;

    @JsonProperty("MODIFIED_DATE")
    private LocalDateTime modifiedDate;

    @JsonProperty("TYPE")
    private String type;

    @JsonProperty("COLOR")
    private String color;

    @JsonProperty("SHARED_DATE")
    private LocalDateTime sharedDate;

    @JsonProperty("SHARED_USER_ID")
    private String sharedUserId;

    @JsonProperty("SHARED_ROOM_NAME")
    private String sharedRoomId;
}
