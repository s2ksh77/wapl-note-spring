package ai.wapl.noteapi.repository;

import java.util.List;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import ai.wapl.noteapi.domain.Tag;
import ai.wapl.noteapi.dto.TagDTOInterface;

@Repository
public interface TagRepository extends JpaRepository<Tag, String> {

        Tag findByName(String name);

        @Query(value = "SELECT T.TAG_ID as id, T.TEXT as name, count(*) as tagCount \n"
                        + "FROM TB_NOTEAPP_TAG as T \n"
                        + "LEFT JOIN TB_NOTEAPP_TAG_MST as TM \n"
                        + "ON T.TAG_ID = TM.TAG_ID \n"
                        + "LEFT JOIN TB_NOTEAPP_NOTE_MST as P \n"
                        + "ON P.NOTE_ID = TM.NOTE_ID \n"
                        + "LEFT JOIN TB_NOTEAPP_NOTEBOOK_MST as B \n"
                        + "ON P.PARENT_NOTEBOOK = B.ID \n"
                        + "WHERE NOTE_CHANNEL_ID = :NOTE_CHANNEL_ID \n"
                        + "GROUP BY T.TAG_ID, T.TEXT", nativeQuery = true)
        List<TagDTOInterface> findByChannelIdForCount(@Param("NOTE_CHANNEL_ID") String channelId);

        @Query(value = "select p.tagSet from Page p " +
                        "inner join p.chapter c where c.channelId = :NOTE_CHANNEL_ID")
        Set<Tag> findByChannelId(@Param("NOTE_CHANNEL_ID") String channelId);

        @Query("select p.tagSet from Page p where p.id = :NOTE_ID")
        Set<Tag> findByPageId(@Param("NOTE_ID") String pageId);

        // @Modifying
        // @Query(value = "INSERT INTO TB_NOTEAPP_TAG_MST (TAG_ID, NOTE_ID) VALUES
        // (:TAG_ID, :NOTE_ID)", nativeQuery = true)
        // int createMapping(@Param("TAG_ID") String tagId, @Param("NOTE_ID") String
        // pageId);

        // @Modifying
        // @Query(value = "DELETE FROM TB_NOTEAPP_TAG_MST WHERE TAG_ID = :TAG_ID and
        // NOTE_ID = :NOTE_ID", nativeQuery = true)
        // int deleteMapping(@Param("TAG_ID") String tagId, @Param("NOTE_ID") String
        // pageId);

        // @Modifying
        // @Query(value = "UPDATE TB_NOTEAPP_TAG_MST SET TAG_ID = :UPDATE_TAG_ID WHERE
        // TAG_ID = :TAG_ID AND NOTE_ID = :NOTE_ID", nativeQuery = true)
        // int updateMapping(@Param("UPDATE_TAG_ID") String updateTagId,
        // @Param("TAG_ID") String tagId,
        // @Param("NOTE_ID") String pageId);
}
