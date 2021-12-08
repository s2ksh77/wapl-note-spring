package ai.wapl.noteapi.dto;

import java.sql.Clob;
import java.time.LocalDateTime;

import javax.persistence.Lob;

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
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;
    private String userName;
    private String editingUserId;
    private String favorite;
    @Lob
    private Clob content;
    private String userId;
    private String type;
    private String sharedUserId;
    private String sharedRoomId;
    private String createdUserId;
    private LocalDateTime deletedDate;
    @Lob
    private Clob textContent;

}
