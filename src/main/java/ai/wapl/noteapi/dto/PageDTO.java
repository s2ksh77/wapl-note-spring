package ai.wapl.noteapi.dto;

import javax.persistence.Lob;

import ai.wapl.noteapi.domain.Bookmark;
import ai.wapl.noteapi.domain.Page;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.*;
import org.springframework.beans.BeanUtils;

import java.util.List;

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
    @JsonProperty("NOTE_CONTENT")
    @Lob
    private String content;
    private String updatedUserId;
    private String type;
    private String sharedUserId;
    private String sharedRoomId;
    private String createdUserId;
    private String deletedDate;
    @JsonProperty("TEXT_CONTENT")
    @Lob
    private String textContent;
    private List<FileDTO> fileList;

    public PageDTO(Page source, String bookmark) {
        BeanUtils.copyProperties(source, this);
        if (source.getChapter() != null) this.chapterId = source.getChapter().getId();
        this.favorite = bookmark != null;
    }

    public Page toEntity() {
        Page page = new Page();
        BeanUtils.copyProperties(this, page);
        return page;
    }

    public enum Action {
        NON_EDIT, EDIT_START, MOVE, RENAME, EDIT_DONE, THROW, RESTORE,
    }
}
