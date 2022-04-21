package ai.wapl.noteapi.dto;

import java.time.LocalDateTime;

import ai.wapl.noteapi.domain.Chapter;
import ai.wapl.noteapi.domain.Chapter.Type;

import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChapterDTO {
    private String id;
    private String channelId;
    private String name;
    private LocalDateTime modifiedDate;
    private Type type;
    private String color;
    private String createdUserId;
    private LocalDateTime sharedDate;
    private String sharedUserId;
    private String sharedRoomId;
    private boolean read;
    private List<PageDTO> pageList = new ArrayList<>();

    public ChapterDTO(Chapter source) {
        BeanUtils.copyProperties(source, this);
        if (source.getPageList() != null && !source.getPageList().isEmpty())
            source.getPageList().forEach(page -> pageList.add(new PageDTO(page)));
    }
}
