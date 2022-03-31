package ai.wapl.noteapi.domain;

import static ai.wapl.noteapi.domain.Chapter.Type.shared_page;

import ai.wapl.noteapi.util.Constants;
import ai.wapl.noteapi.util.DateTimeConverter;
import ai.wapl.noteapi.util.NoteUtil;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

@ToString
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "TB_NOTEAPP_NOTEBOOK_MST")
public class Chapter {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "ID")
    private String id;

    @Column(name = "NOTE_CHANNEL_ID")
    private String channelId;

    @Column(name = "TEXT")
    private String name;

    @Column(name = "MODIFIED_DATE")
    @Convert(converter = DateTimeConverter.class)
    private LocalDateTime modifiedDate = NoteUtil.now();

    @Column(name = "TYPE")
    @Enumerated(EnumType.STRING)
    private Type type = Type.notebook;

    @Column(name = "COLOR")
    private String color;

    @Column(name = "CREATED_USER_ID")
    private String createdUserId;

    @Column(name = "SHARED_DATE")
    @Convert(converter = DateTimeConverter.class)
    private LocalDateTime sharedDate;

    @Column(name = "SHARED_USER_ID")
    private String sharedUserId;

    @Column(name = "SHARED_ROOM_NAME")
    private String sharedRoomId;

    @OneToMany(mappedBy = "chapter", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Page> pageList = new ArrayList<>();

    @Transient
    private String userId;

    @Transient
    private String userName;

    @Builder
    public Chapter(String id, String channelId, String name, Type type, String color,
        String createdUserId) {
        this.id = id;
        this.channelId = channelId;
        this.name = name;
        this.type = type;
        this.color = color;
        this.createdUserId = createdUserId;
    }

    public static Chapter createChapter(String userId, Chapter input) {
        Chapter chapter = Chapter.builder().channelId(input.getChannelId())
            .color(input.getColor()).name(input.getName())
            .createdUserId(userId)
            .type(Type.notebook).build();
        chapter.modifiedDate = NoteUtil.now();
        return chapter;
    }

    public static Chapter createRecycleBin(String channelId) {
        Chapter chapter = Chapter.builder().channelId(channelId)
            .name(Constants.RECYCLE_BIN_NAME)
            .type(Type.recycle_bin).build();
        chapter.modifiedDate = NoteUtil.now();
        chapter.modifiedDate = NoteUtil.now();
        return chapter;
    }

    public static Chapter createChapterForShare(String userId, Chapter input) {
        Chapter chapter = Chapter.builder().channelId(input.getChannelId())
            .color(input.getColor()).name(input.getName())
            .type(Type.shared).build();

        chapter.modifiedDate = NoteUtil.now();
        chapter.sharedDate = NoteUtil.now();
        chapter.sharedUserId = userId;
        return chapter;
    }

    public static Chapter createShareChapter(String userId, String channelId) {
        Chapter chapter = Chapter.builder().channelId(channelId).name("전달받은 페이지")
            .type(shared_page).build();
        chapter.modifiedDate = NoteUtil.now();
        chapter.sharedDate = NoteUtil.now();
        chapter.sharedUserId = userId;
        return chapter;
    }

    public void addPage(Page page) {
        pageList.add(page);
    }

    public enum Type {
        notebook, recycle_bin, shared, shared_page, allNote
    }
}
