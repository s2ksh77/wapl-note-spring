package ai.wapl.noteapi.repository;

import ai.wapl.noteapi.domain.File;
import ai.wapl.noteapi.dto.ChapterDTO;
import ai.wapl.noteapi.dto.PageDTO;
import ai.wapl.noteapi.dto.TagDTO;

import java.time.LocalDateTime;
import java.util.List;

public interface QueryDslPageRepository {

    PageDTO findById(String userId, String pageId);

    boolean isBookMark(String pageId, String userId);

    List<PageDTO> findByChannelIdOrderByModifiedDate(String userId, String channelId, int count);

    List<PageDTO> findAllPageByChannelId(String userId, String channelId);

    long moveToRecycleBin(String channelId, String chapterId);

    List<ChapterDTO> searchChapter(String channelId, String text);

    List<PageDTO> searchPage(String channelId, String text);

    List<TagDTO> searchTag(String channelId, String text, String pageId);

    List<File> getFileInRecycleBin(LocalDateTime targetDateTime);

    long deleteInRecycleBin(LocalDateTime targetDateTime);

    long updatePageToNonEdit(LocalDateTime targetDateTime);

    List<PageDTO> findBookmarkedPageByChannel(String userId, String channelId);

    List<PageDTO> findBookmarkedPageByUser(String userId);

    long deleteAllByChannelId(String channelId);

    List<PageDTO> findByChapterIdWithBookmark(String chapterId, String userId);
}
