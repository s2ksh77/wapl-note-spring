package ai.wapl.noteapi.domain;

import org.springframework.data.annotation.Id;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import java.sql.Clob;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Table(name = "TB_NOTEAPP_NOTE_MST")
public class Page {

    @Id
    @Column(name = "NOTE_ID")
    private String id;

    @Column(name = "PARENT_NOTEBOOK")
    private String chapterId;

    @Column(name = "NOTE_TITLE")
    private String name;

    @Column(name = "CREATED_DATE")
    private LocalDateTime createdDate;

    @Column(name = "MODIFIED_DATE")
    private LocalDateTime modifiedDate;

    @Column(name = "USER_NAME")
    private String userName;

    @Column(name = "IS_EDIT")
    private String editingUserId;

    @Column(name = "IS_FAVORITE")
    private String favorite;

    @Column(name = "NOTE_CONTENT")
    @Lob
    private Clob content;

    @Column(name = "USER_ID")
    private String userId;

    @Column(name = "TYPE")
    private String type;

    @Column(name = "SHARED_USER_ID")
    private String sharedUserId;

    @Column(name = "SHARED_ROOM_NAME")
    private String sharedRoomId;

    @Column(name = "CREATED_USER_ID")
    private String createdUserId;

    @Column(name = "NOTE_DELETED_AT")
    private LocalDateTime deletedDate;

    @Column(name = "TEXT_CONTEXT")
    @Lob
    private Clob textContent;

    @OneToMany(mappedBy = "page")
    private List<PageTag> pageTagList;

    @OneToMany(mappedBy = "tag")
    private List<PageTag> tagPageList;
}
