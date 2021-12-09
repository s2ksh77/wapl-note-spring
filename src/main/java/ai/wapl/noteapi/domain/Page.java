package ai.wapl.noteapi.domain;

import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import java.sql.Clob;
import java.time.LocalDateTime;
import java.util.ArrayList;
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
    private String createdDate;

    @Column(name = "MODIFIED_DATE")
    private String modifiedDate;

    @Column(name = "USER_NAME")
    private String userName;

    @Column(name = "IS_EDIT")
    private String editingUserId;

    @Column(name = "IS_FAVORITE")
    private String favorite;

    @Column(name = "NOTE_CONTENT")
    @Lob
    private String content;

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
    private String deletedDate;

    @Column(name = "TEXT_CONTENT")
    @Lob
    private String textContent;

    @ManyToMany
    @JoinTable(name = "TB_NOTEAPP_TAG_MST", joinColumns = @JoinColumn(name = "NOTE_ID"), inverseJoinColumns = @JoinColumn(name = "TAG_ID"))
    private final List<Tag> tagList = new ArrayList<Tag>();

    @ManyToMany
    @JoinTable(name = "TB_NOTEAPP_NOTE_FILE_MAP", joinColumns = @JoinColumn(name = "NOTE_ID"), inverseJoinColumns = @JoinColumn(name = "FILE_ID"))
    private final List<File> fileList = new ArrayList<File>();

}
