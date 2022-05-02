package ai.wapl.noteapi.repository;

import java.util.List;

import ai.wapl.noteapi.dto.ChapterDTO;
import ai.wapl.noteapi.dto.PageDTO;

public interface QueryDslChapterRepository {

    ChapterDTO findByIdFetchJoin(String id, String userId);

    List<PageDTO> findByChapterIdWithBookmark(String chapterId, String userId);

}
