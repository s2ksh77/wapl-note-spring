package ai.wapl.noteapi.repository;

import java.util.List;

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

    @Transactional
    @Modifying
    @Query(value = "UPDATE TB_NOTEAPP_NOTE_MST \n"
            + "SET parent_notebook = \n"
            + "(SELECT ID FROM TB_NOTEAPP_NOTEBOOK_MST \n"
            + "WHERE type = 'recycle_bin' AND note_channel_id = :note_channel_id), \n"
            + "note_deleted_at = :note_delete_at \n"
            + "WHERE NOTE_ID IN ( \n"
            + "SELECT NOTE_ID \n"
            + "FROM TB_NOTEAPP_NOTE_MST \n"
            + "WHERE parent_notebook = :parent_notebook \n"
            + ");", nativeQuery = true)
    public int updateRecycleBin(@Param("parent_notebook") String chapterId,
            @Param("note_channel_id") String channelId,
            @Param("note_delete_at") String deletedAt);

}