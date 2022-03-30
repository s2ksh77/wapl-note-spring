package ai.wapl.noteapi.domain;

import ai.wapl.noteapi.domain.Chapter.Type;
import javax.persistence.Id;

import ai.wapl.noteapi.util.DateTimeConverter;
import ai.wapl.noteapi.util.NoteUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

import java.time.LocalDateTime;
import java.util.*;

import static ai.wapl.noteapi.domain.Chapter.Type.shared;
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
    @Convert(converter = DateTimeConverter.class)
    private LocalDateTime createdDate;

    @Column(name = "MODIFIED_DATE")
    @Convert(converter = DateTimeConverter.class)
    private LocalDateTime modifiedDate;

    @Column(name = "USER_NAME")
    private String userName;

    @Column(name = "IS_EDIT")
    private String editingUserId;

    @Column(name = "NOTE_CONTENT")
    @Lob
    private String content = EMPTY_CONTENT;

    @Column(name = "USER_ID")
    private String updatedUserId;

    @Column(name = "TYPE")
    @Enumerated(EnumType.STRING)
    private Type type;

    @Column(name = "SHARED_USER_ID")
    private String sharedUserId;

    @Column(name = "SHARED_ROOM_NAME")
    private String sharedRoomId;

    @Column(name = "CREATED_USER_ID")
    private String createdUserId;

    @Column(name = "NOTE_DELETED_AT")
    @Convert(converter = DateTimeConverter.class)
    private LocalDateTime deletedDate;

    @Column(name = "TEXT_CONTENT")
    @Lob
    private String textContent;

    @Column(name = "RESTORECHAPTERID")
    private String restoreChapterId;

    @ManyToMany(targetEntity = Tag.class, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "TB_NOTEAPP_TAG_MST", joinColumns = @JoinColumn(name = "NOTE_ID"), inverseJoinColumns = @JoinColumn(name = "TAG_ID"))
    private Set<Tag> tagSet = new HashSet<>();

    public void addTag(Tag tag) {
        tagSet.add(tag);
    }

    public void deleteTag(Tag tag) {
        tagSet.remove(tag);
    }

    @Transient
    private int tagCount;

    @Builder
    public static Page createPage(String id, String name, String content, String textContent, String editingUserId, Chapter chapter, String userId, String userName, Type type) {
        Page page = new Page();
        page.setId(id);
        page.setChapter(chapter);
        page.setName(name);
        page.setContent(content);
        page.setTextContent(textContent);
        page.setEditingUserId(editingUserId);
        page.setUpdatedUserId(userId);
        page.setCreatedUserId(userId);
        page.setUserName(userName);
        page.setCreatedDate(NoteUtil.now());
        page.setModifiedDate(NoteUtil.now());
        page.setType(type);

        return page;
    }

    public static Page createPage(Chapter chapter, Page input) {
        Page page = Page.builder().chapter(chapter)
                .name(input.getName()).content(input.getContent())
                .userId(input.getCreatedUserId()).userName(input.getUserName())
                .textContent(input.getTextContent()).build();

        page.setCreatedDate(NoteUtil.now());
        page.setModifiedDate(NoteUtil.now());

        return page;
    }

    public static Page createSharedPage(String userId, Chapter chapter, Page input, String sharedRoomName) {
        Page page = Page.builder().chapter(chapter)
            .name(input.getName()).content(input.getContent()).type(shared)
            .userId(userId).userName(input.getUserName())
            .textContent(input.getTextContent()).build();

        page.setSharedRoomId(sharedRoomName);
        page.setSharedUserId(userId);
        page.setCreatedDate(NoteUtil.now());
        page.setModifiedDate(NoteUtil.now());

        return page;
    }

    public boolean isEditing() {
        return editingUserId != null && !editingUserId.isEmpty();
    }

    public boolean isShared() {
        return type != null && type.equals(SHARED_PAGE_TYPE);
    }

}
