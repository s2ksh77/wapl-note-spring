package ai.wapl.noteapi.domain;

import static ai.wapl.noteapi.util.Constants.EMPTY_CONTENT;

import ai.wapl.noteapi.dto.PageDTO;
import ai.wapl.noteapi.util.DateTimeConverter;
import ai.wapl.noteapi.util.NoteUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.AttributeConverter;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

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
  @Convert(converter = PageTypeConverter.class)
  private boolean shared;

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
  @Transient
  private int tagCount;

  @Transient
  private String chapterId;

  @Builder
  public static Page createPage(String id, String name, String content, String textContent,
      String editingUserId, Chapter chapter, String userId, String userName, boolean shared, String chapterId) {
    Page page = new Page();
    page.setId(id);
    page.setChapter(chapter);
    page.setChapterId(chapterId);
    page.setName(name);
    page.setContent(content);
    page.setTextContent(textContent);
    page.setEditingUserId(editingUserId);
    page.setUpdatedUserId(userId);
    page.setCreatedUserId(userId);
    page.setUserName(userName);
    page.setCreatedDate(NoteUtil.now());
    page.setModifiedDate(NoteUtil.now());
    page.setShared(shared);

    return page;
  }

  public static Page createPage(Chapter chapter, Page input) {
    Page page = Page.builder().chapter(chapter)
        .chapterId(chapter.getId())
        .name(input.getName()).content(input.getContent())
        .editingUserId(input.getCreatedUserId())
        .userId(input.getCreatedUserId()).userName(input.getUserName())
        .textContent(input.getTextContent()).build();

    page.setCreatedDate(NoteUtil.now());
    page.setModifiedDate(NoteUtil.now());

    return page;
  }

  public static Page createSharedPage(String userId, Chapter chapter, Page input,
      String sharedRoomName) {
    Page page = Page.builder().chapter(chapter)
        .name(input.getName()).content(input.getContent()).shared(true)
        .userId(userId).userName(input.getUserName())
        .textContent(input.getTextContent()).build();

    page.setSharedRoomId(sharedRoomName);
    page.setSharedUserId(userId);
    page.setCreatedDate(NoteUtil.now());
    page.setModifiedDate(NoteUtil.now());

    return page;
  }

  public static PageDTO convertDTO(Page input) {
    PageDTO page = new PageDTO();
    page.setId(input.getId());
    page.setChapterId(input.chapter.getId());
    page.setName(input.getName());
    page.setContent(EMPTY_CONTENT);
    page.setTextContent(input.getTextContent());
    page.setEditingUserId(input.getEditingUserId());
    page.setUpdatedUserId(input.getUpdatedUserId());
    page.setCreatedUserId(input.getCreatedUserId());
    page.setUserName(input.getUserName());
    page.setCreatedDate(input.getCreatedDate());
    page.setModifiedDate(input.getModifiedDate());
    page.setShared(input.isShared());
    return page;
  }

  public void addTag(Tag tag) {
    tagSet.add(tag);
  }

  public void deleteTag(Tag tag) {
    tagSet.remove(tag);
  }

  public boolean isEditing() {
    return editingUserId != null && !editingUserId.isEmpty();
  }

  public String getShared() {
    return shared ? "shared" : null;
  }

  public boolean isShared() {
    return shared;
  }

  public static class PageTypeConverter implements AttributeConverter<Boolean, String> {

    @Override
    public String convertToDatabaseColumn(Boolean attribute) {
      return attribute ? "shared" : null;
    }

    @Override
    public Boolean convertToEntityAttribute(String dbData) {
      return dbData != null && dbData.equals("shared");
    }
  }

}
