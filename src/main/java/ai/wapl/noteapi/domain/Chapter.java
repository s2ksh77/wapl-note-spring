package ai.wapl.noteapi.domain;

import org.springframework.data.annotation.Id;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Table(name = "TB_NOTEAPP_NOTEBOOK_MST")
public class Chapter {

    @Id
    @Column(name = "ID")
    private String id;

    @Column(name = "NOTE_CHANNEL_ID")
    private String chapterId;

    @Column(name = "TEXT")
    private String name;

    @Column(name = "MODIFIED_DATE")
    private LocalDateTime modifiedDate;

    @Column(name = "TYPE")
    private String type;

    @Column(name = "COLOR")
    private String color;

    @Column(name = "SHARED_DATE")
    private LocalDateTime sharedDate;

    @Column(name = "SHARED_USER_ID")
    private String sharedUserId;

    @Column(name = "SHARED_ROOM_NAME")
    private String sharedRoomId;
}
