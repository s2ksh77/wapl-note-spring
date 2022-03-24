package ai.wapl.noteapi.dto;

import lombok.Data;

import java.util.List;

@Data
public class SearchDTO {
    private List<ChapterDTO> chapterList;
    private List<PageDTO> pageList;
    private List<TagDTO> tagList;
}
