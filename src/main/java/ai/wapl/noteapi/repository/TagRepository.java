package ai.wapl.noteapi.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import ai.wapl.noteapi.domain.Tag;

@Repository
public interface TagRepository extends JpaRepository<Tag, String> {

    public Tag findByName(String name);

    @Query(value = "SELECT DISTINCT T.TAG_ID, T.TEXT \n"
            + "FROM TB_NOTEAPP_TAG as T \n"
            + "LEFT JOIN TB_NOTEAPP_TAG_MST as TM \n"
            + "ON T.TAG_ID = TM.TAG_ID \n"
            + "LEFT JOIN TB_NOTEAPP_NOTE_MST as P \n"
            + "ON P.NOTE_ID = TM.NOTE_ID \n"
            + "LEFT JOIN TB_NOTEAPP_NOTEBOOK_MST as B \n"
            + "ON P.PARENT_NOTEBOOK = B.ID \n"
            + "WHERE NOTE_CHANNEL_ID = :NOTE_CHANNEL_ID", nativeQuery = true)
    List<Tag> findByChannelId(@Param("NOTE_CHANNEL_ID") String channelId);

    @Query(value = "SELECT DISTINCT T.TAG_ID, T.TEXT \n"
            + "FROM TB_NOTEAPP_TAG as T \n"
            + "LEFT JOIN TB_NOTEAPP_TAG_MST as TM \n"
            + "ON T.TAG_ID = TM.TAG_ID \n"
            + "LEFT JOIN TB_NOTEAPP_NOTE_MST as P \n"
            + "ON P.NOTE_ID = TM.NOTE_ID \n"
            + "WHERE P.NOTE_ID = :NOTE_ID", nativeQuery = true)
    public List<Tag> pageforTagList(@Param("NOTE_ID") String pageId);

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO TB_NOTEAPP_TAG_MST (TAG_ID, NOTE_ID) VALUES (:TAG_ID, :NOTE_ID)", nativeQuery = true)
    public int createMapping(@Param("TAG_ID") String tagId, @Param("NOTE_ID") String pageId);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM TB_NOTEAPP_TAG_MST WHERE TAG_ID = :TAG_ID and NOTE_ID = :NOTE_ID", nativeQuery = true)
    public int deleteMapping(@Param("TAG_ID") String tagId, @Param("NOTE_ID") String pageId);
}