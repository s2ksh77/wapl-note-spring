package ai.wapl.noteapi.dto;

import javax.persistence.Lob;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PageDTO {
    private String id;
    private String chapterId;
    private String name;
    private String createdDate;
    private String modifiedDate;
    private String userName;
    private String editingUserId;
    private String favorite;
    @JsonProperty("NOTE_CONTENT")
    @Lob
    private String content;
    private String userId;
    private String type;
    private String sharedUserId;
    private String sharedRoomId;
    private String createdUserId;
    private String deletedDate;
    @JsonProperty("TEXT_CONTENT")
    @Lob
    private String textContent;
}
