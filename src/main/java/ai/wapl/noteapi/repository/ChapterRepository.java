package ai.wapl.noteapi.repository;

import java.time.LocalDate;
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
public interface ChapterRepository extends JpaRepository<Chapter, String> {

    List<Chapter> findByChannelId(String channelId);

    Chapter findByChannelIdAndType(String channelId, String type);

    @Query("select c from Chapter c join fetch c.pageList where c.id = :id")
    Optional<Chapter> findByIdFetchJoin(@Param("id") String id);

    @Transactional
    @Modifying
    @Query("update Page p\n"
        + "set p.chapter = (select c from Chapter c \n"
        + "        where c.type = 'recycle_bin' and c.channelId = :channelId),\n"
        + "p.deletedDate = :deletedAt \n"
        + "where p.chapter.id = :chapterId")
    int updateRecycleBin(@Param("chapterId") String chapterId,
            @Param("channelId") String channelId,
            @Param("deletedAt") LocalDateTime deletedAt);

}