package ai.wapl.noteapi.repository;

import ai.wapl.noteapi.domain.Page;
import ai.wapl.noteapi.domain.Tag;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ai.wapl.noteapi.dto.SearchDTOinterface;

@Repository
public interface PageRepository extends JpaRepository<Page, String>, QueryDslPageRepository {

        @Query(value = "SELECT DISTINCT N.note_id as id, B.id as chapterId, B.text as chapterName, B.TYPE as Type, B.color \n"
                        + "from TB_NOTEAPP_TAG as T \n"
                        + "LEFT JOIN TB_NOTEAPP_TAG_MST as M \n"
                        + "on t.tag_id = m.tag_id \n"
                        + "LEFT JOIN TB_NOTEAPP_NOTE_MST as N \n"
                        + "on m.note_id = n.note_id \n"
                        + " LEFT JOIN TB_NOTEAPP_NOTEBOOK_MST as B \n"
                        + "on n.parent_notebook = b.id \n"
                        + "where note_channel_id = :channelId AND (T.TEXT LIKE :text)", nativeQuery = true)
        List<SearchDTOinterface> searchTagInPage(@Param("channelId") String channelId,
                        @Param("text") String text);
}
