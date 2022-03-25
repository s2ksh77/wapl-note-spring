package ai.wapl.noteapi.repository;

import ai.wapl.noteapi.domain.Page;
import ai.wapl.noteapi.dto.ChapterDTO;
import ai.wapl.noteapi.dto.PageDTO;
import ai.wapl.noteapi.dto.TagDTO;

import java.util.List;

public interface QueryDslPageRepository {
    Page findById(String userId, String pageId);

    long moveToRecycleBin(String channelId, String chapterId);

    List<ChapterDTO> searchChapter(String channelId, String text);

    List<PageDTO> searchPage(String channelId, String text);

    List<TagDTO> searchTag(String channelId, String text);
}
