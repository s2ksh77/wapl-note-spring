package ai.wapl.noteapi.repository;

import ai.wapl.noteapi.domain.Chapter.Type;
import ai.wapl.noteapi.dto.ChapterDTO;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import ai.wapl.noteapi.domain.Chapter;

@Repository
public interface ChapterRepository extends JpaRepository<Chapter, String>, QueryDslChapterRepository {

    List<Chapter> findByChannelId(String channelId);

    Optional<Chapter> findByChannelIdAndType(String channelId, Type type);

    @Query("select c from Chapter c join fetch c.pageList where c.id = :id")
    Optional<Chapter> findByIdJoin(@Param("id") String id);

    ChapterDTO findByIdFetchJoin(@Param("id") String id, String userId, String pageId);

    @Modifying
    @Query("update Page p\n"
            + "set p.chapter = (select c from Chapter c \n"
            + "        where c.type = RECYCLE_BIN"
            + " and c.channelId = :channelId),\n"
            + "p.deletedDate = :deletedAt \n"
            + "where p.chapter.id = :chapterId")
    int updateRecycleBin(@Param("chapterId") String chapterId,
            @Param("channelId") String channelId,
            @Param("deletedAt") LocalDateTime deletedAt);

    @Modifying
    @Query("delete from Chapter c where c.channelId = :channelId")
    int deleteAllByChannelId(@Param("channelId") String channelId);

}