package ai.wapl.noteapi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import ai.wapl.noteapi.domain.Tag;

@Repository
public interface TagRepository extends JpaRepository<Tag, String> {

    @Query(value = "SELECT DISTINCT T.TAG_ID, T.TEXT \n"
            + "FROM TB_NOTEAPP_TAG as T \n"
            + "LEFT JOIN TB_NOTEAPP_TAG_MST as TM \n"
            + "ON T.TAG_ID = TM.TAG_ID \n"
            + "LEFT JOIN TB_NOTEAPP_NOTE_MST as P \n"
            + "ON P.NOTE_ID = TM.NOTE_ID \n"
            + "WHERE P.NOTE_ID = :NOTE_ID", nativeQuery = true)
    public List<Tag> pageforTagList(@Param("NOTE_ID") String pageId);
}