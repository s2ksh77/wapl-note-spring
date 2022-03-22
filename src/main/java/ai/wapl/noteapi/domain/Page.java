package ai.wapl.noteapi.domain;

import javax.persistence.Id;

import ai.wapl.noteapi.util.NoteUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static ai.wapl.noteapi.util.Constants.EMPTY_CONTENT;
import static ai.wapl.noteapi.util.Constants.SHARED_PAGE_TYPE;

@Data
@Entity
@NoArgsConstructor
@Table(name = "TB_NOTEAPP_NOTE_MST")
public class Page {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "NOTE_ID")
    private String id;

    @JsonIgnore
    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "PARENT_NOTEBOOK")
    private Chapter chapter;

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
    private String updatedUserId;

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

    @Column(name = "RESTORECHAPTERID")
    private String restoreChapterId;

    @ManyToMany(targetEntity = Tag.class, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "TB_NOTEAPP_TAG_MST", joinColumns = @JoinColumn(name = "NOTE_ID"), inverseJoinColumns = @JoinColumn(name = "TAG_ID"))
    private Set<Tag> tagSet = new HashSet<>();

//    @ManyToMany
//    @JoinTable(name = "TB_NOTEAPP_NOTE_FILE_MAP", joinColumns = @JoinColumn(name = "NOTE_ID"), inverseJoinColumns = @JoinColumn(name = "FILE_ID"))
//    private final List<File> fileList = new ArrayList<>();

    @Transient
    private String resultMsg;

    @Transient
    private String channelId;

    public void addTag(Tag tag) {
        tagSet.add(tag);
    }

    public void deleteTag(Tag tag) {
        tagSet.remove(tag);
    }

    public void updateTag(Tag from, Tag to) {
        deleteTag(from);
        addTag(to);
    }

    @Transient
    private int tagCount;

    @Builder
    public static Page createPage(String id, String name, String content, String textContent, String editingUserId, Chapter chapter, String userId, String userName, String type) {
        Page page = new Page();
        page.setId(id);
        page.setChapter(chapter);
        page.setName(name);
        page.setContent((content == null || content.isEmpty()) ? EMPTY_CONTENT : content);
        page.setTextContent(textContent);
        page.setEditingUserId(editingUserId);
        page.setUpdatedUserId(userId);
        page.setCreatedUserId(userId);
        page.setUserName(userName);
        page.setCreatedDate(NoteUtil.generateDate());
        page.setModifiedDate(NoteUtil.generateDate());
        page.setType(type);

        return page;
    }

    public static Page createPage(Page page) {
        if (page.content == null || page.content.isEmpty())
            page.setContent(EMPTY_CONTENT);
        page.setCreatedDate(NoteUtil.generateDate());
        page.setModifiedDate(NoteUtil.generateDate());

        return page;
    }

    public boolean isShared() {
        return type != null && type.equals(SHARED_PAGE_TYPE);
    }

    public static enum PageType {
        NONEDIT,EDIT_START,MOVE,RENAME, EDIT_DONE,
    }

}
