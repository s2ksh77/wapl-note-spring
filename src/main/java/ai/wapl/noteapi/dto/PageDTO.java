package ai.wapl.noteapi.dto;

import javax.persistence.Lob;

import ai.wapl.noteapi.domain.Page;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.*;
import org.springframework.beans.BeanUtils;

@Getter
@Setter
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
    private String favorite;
    @JsonProperty("NOTE_CONTENT")
    @Lob
    private String content;
    private String userId;
    private String type;
    private String sharedUserId;
    private String sharedRoomId;
    private String createdUserId;
    private String deletedDate;
    @JsonProperty("TEXT_CONTENT")
    @Lob
    private String textContent;

    public PageDTO(Page source) {
        BeanUtils.copyProperties(source, this);
        if (source.getChapter() != null) this.chapterId = source.getChapter().getId();
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
