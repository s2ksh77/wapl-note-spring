package ai.wapl.noteapi.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChapterDTO {
    private String id;
    private String channelId;
    private String name;
    private LocalDateTime modifiedDate;
    private String type;
    private String color;
    private LocalDateTime sharedDate;
    private String sharedUserId;
    private String sharedRoomId;
}
