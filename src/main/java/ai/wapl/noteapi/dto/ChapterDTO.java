package ai.wapl.noteapi.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ChapterDTO {
    private String id;
    private String chapterId;
    private String name;
    private LocalDateTime modifiedDate;
    private String type;
    private String color;
    private LocalDateTime sharedDate;
    private String sharedUserId;
    private String sharedRoomId;
}
