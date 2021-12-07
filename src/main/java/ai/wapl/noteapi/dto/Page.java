package ai.wapl.noteapi.dto;

import java.sql.Clob;
import java.time.LocalDateTime;

import javax.persistence.Lob;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Page {

    @JsonProperty("NOTE_ID")
    private String id;

    @JsonProperty("PARENT_NOTEBOOK")
    private String chapterId;

    @JsonProperty("NOTE_TITLE")
    private String name;

    @JsonProperty("CREATED_DATE")
    private LocalDateTime createdDate;

    @JsonProperty("MODIFIED_DATE")
    private LocalDateTime modifiedDate;

    @JsonProperty("USER_NAME")
    private String userName;

    @JsonProperty("IS_EDIT")
    private String editingUserId;

    @JsonProperty("IS_FAVORITE")
    private String favorite;

    @JsonProperty("NOTE_CONTENT")
    @Lob
    private Clob content;

    @JsonProperty("USER_ID")
    private String userId;

    @JsonProperty("TYPE")
    private String type;

    @JsonProperty("SHARED_USER_ID")
    private String sharedUserId;

    @JsonProperty("SHARED_ROOM_NAME")
    private String sharedRoomId;

    @JsonProperty("CREATED_USER_ID")
    private String createdUserId;

    @JsonProperty("NOTE_DELETED_AT")
    private LocalDateTime deletedDate;

    @JsonProperty("TEXT_CONTEXT")
    @Lob
    private Clob textContent;

}
