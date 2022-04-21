package ai.wapl.noteapi.domain;

import static ai.wapl.noteapi.domain.Chapter.Type.NOTEBOOK;
import static ai.wapl.noteapi.domain.Chapter.Type.RECYCLE_BIN;
import static ai.wapl.noteapi.domain.Chapter.Type.SHARED;
import static ai.wapl.noteapi.domain.Chapter.Type.SHARED_PAGE;

import ai.wapl.noteapi.util.Constants;
import ai.wapl.noteapi.util.DateTimeConverter;
import ai.wapl.noteapi.util.NoteUtil;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.persistence.AttributeConverter;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Converter;
import javax.persistence.Entity;
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
    @Convert(converter = EnumConverter.class)
    private Type type = NOTEBOOK;

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
                .type(NOTEBOOK).build();
        chapter.modifiedDate = NoteUtil.now();
        return chapter;
    }

    public static Chapter createRecycleBin(String channelId) {
        Chapter chapter = Chapter.builder().channelId(channelId)
                .name(Constants.RECYCLE_BIN_NAME)
                .type(RECYCLE_BIN).build();
        chapter.modifiedDate = NoteUtil.now();
        chapter.modifiedDate = NoteUtil.now();
        return chapter;
    }

    public static Chapter createChapterForShare(String userId, Chapter input) {
        Chapter chapter = Chapter.builder().channelId(input.getChannelId())
                .color(input.getColor()).name(input.getName())
                .type(SHARED).build();

        chapter.modifiedDate = NoteUtil.now();
        chapter.sharedDate = NoteUtil.now();
        chapter.sharedUserId = userId;
        return chapter;
    }

    public static Chapter createShareChapter(String userId, String channelId) {
        Chapter chapter = Chapter.builder().channelId(channelId).name("전달받은 페이지")
                .type(SHARED_PAGE).build();
        chapter.modifiedDate = NoteUtil.now();
        chapter.sharedDate = NoteUtil.now();
        chapter.sharedUserId = userId;
        return chapter;
    }

    public void addPage(Page page) {
        pageList.add(page);
    }

    public enum Type {
        NOTEBOOK("notebook"), RECYCLE_BIN("recycle_bin"), SHARED("shared"), SHARED_PAGE(
                "shared_page"),
        ALL_NOTE("allNote"), DEFAULT(
                "default");

        String value;

        Type(String value) {
            this.value = value;
        }

        public static Type find(String value) {
            return Arrays.stream(Type.values()).filter(type -> type.value.equals(value)).findFirst()
                    .orElse(null);
        }

        public String getValue() {
            return value;
        }
    }

    @Converter
    public static class EnumConverter implements AttributeConverter<Type, String> {

        @Override
        public String convertToDatabaseColumn(Type attribute) {
            return attribute.getValue();
        }

        @Override
        public Type convertToEntityAttribute(String dbData) {
            return Type.find(dbData);
        }

    }
}
