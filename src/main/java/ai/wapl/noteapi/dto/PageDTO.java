package ai.wapl.noteapi.dto;

import javax.persistence.Lob;

import ai.wapl.noteapi.domain.Page;
import ai.wapl.noteapi.domain.Tag;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.*;

import org.hibernate.annotations.Nationalized;
import org.springframework.beans.BeanUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PageDTO {
    private String id;
    private String chapterId;
    private String channelId;
    private String restoreChapterId;
    private String name;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;
    private String userName;
    private String editingUserId;
    private boolean favorite;

    @Lob
    @JsonProperty("content")
    private String content;
    private String updatedUserId;
    private boolean shared;
    private String sharedUserId;
    private String sharedRoomId;
    private String createdUserId;
    private LocalDateTime deletedDate;

    @Lob
    @JsonProperty("textContent")
    private String textContent;
    private boolean read;
    private List<FileDTOinterface> fileList;
    private List<Tag> tagList;

    private String chapterColor;
    private String chapterType;

    public PageDTO(Page source) {
        BeanUtils.copyProperties(source, this);
        if (source.getChapter() != null)
            this.chapterId = source.getChapter().getId();
    }

    public PageDTO(Page source, Set<Tag> tagSet) {
        this(source);
        if (tagSet != null && !tagSet.isEmpty())
            this.tagList = new ArrayList<>(tagSet);
    }

    public PageDTO(Page source, String bookmark) {
        this(source);
        this.favorite = bookmark != null;
    }

    public Page toEntity() {
        Page page = new Page();
        BeanUtils.copyProperties(this, page);
        return page;
    }

    public enum Action {
        NON_EDIT, EDIT_START, MOVE, RENAME, EDIT_DONE, THROW, RESTORE, EDITING,
    }
}
