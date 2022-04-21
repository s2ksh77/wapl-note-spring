package ai.wapl.noteapi.dto;

import javax.persistence.Lob;

import ai.wapl.noteapi.domain.Page;
import ai.wapl.noteapi.domain.Tag;
import ai.wapl.noteapi.domain.Chapter.Type;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.*;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PageDTO {
    private String id;
    private String chapterId;
    private String channelId;
    private String restoreChapterId;
    private String name;
    private String createdDate;
    private String modifiedDate;
    private String userName;
    private String editingUserId;
    private boolean favorite;
    @JsonProperty("noteContent")
    @Lob
    private String content;
    private String updatedUserId;
    private boolean shared;
    private String sharedUserId;
    private String sharedRoomId;
    private String createdUserId;
    private String deletedDate;
    @JsonProperty("textContent")
    @Lob
    private String textContent;
    private boolean read;
    private List<FileDTO> fileList;
    private List<Tag> tagList;

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
