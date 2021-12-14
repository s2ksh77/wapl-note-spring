package ai.wapl.noteapi.repository;

import ai.wapl.noteapi.domain.Page;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PageRepository extends JpaRepository<Page, String> {

        // @Modifying(clearAutomatically = true)
        // @Query(value = "UPDATE TB_NOTEAPP_NOTE_MST "
        // + "SET note_title = nvl2(:note_title, :note_title, note_title), "
        // + "parent_notebook = :parent_notebook, "
        // + "is_edit = :is_edit, "
        // + "USER_ID = nvl2(:USER_ID, :USER_ID, USER_ID) "
        // + "WHERE note_id = ("
        // + "SELECT note_id FROM TB_NOTEAPP_NOTE_MST "
        // + "WHERE is_edit IS NULL AND note_id = :note_id "
        // + ")", nativeQuery = true)
        // public int editStartPage(
        // @Param("note_id") String id,
        // @Param("note_title") String name,
        // @Param("parent_notebook") String chapterId,
        // @Param("is_edit") String editingUserId,
        // @Param("USER_ID") String userId);

        // @Query(value = "UPDATE TB_NOTEAPP_NOTE_MST "
        // + "SET "
        // + "parent_notebook = :parent_notebook, "
        // + "is_edit = :is_edit "
        // + "WHERE note_id = ("
        // + "SELECT note_id FROM TB_NOTEAPP_NOTE_MST "
        // + "WHERE is_edit IS NOT NULL AND note_id = :note_id "
        // + ")", nativeQuery = true)
        // public int nonEditPage(
        // @Param("note_id") String id,
        // @Param("parent_notebook") String chapterId,
        // @Param("is_edit") String editingUserId);

        // @Query(value = "UPDATE TB_NOTEAPP_NOTE_MST "
        // + "set note_title = nvl2(:note_title, :note_title, note_title), "
        // + "modified_date = TO_CHAR(SYSTIMESTAMP, 'YYYY.MM.DD HH24:MI:SS'), "
        // + "parent_notebook = :parent_notebook, "
        // + "user_name = nvl2(:user_name, :user_name, user_name), "
        // + "is_edit = :is_edit, "
        // + "USER_ID = nvl2(:USER_ID, :USER_ID, USER_ID) "
        // + "where note_id = :note_id", nativeQuery = true)
        // public int moveRenamePage(
        // @Param("note_id") String id,
        // @Param("note_title") String name,
        // @Param("parent_notebook") String chapterId,
        // @Param("is_edit") String editingUserId,
        // @Param("USER_ID") String userId,
        // @Param("user_name") String userName);

        // @Query(value = "UPDATE TB_NOTEAPP_NOTE_MST "
        // + "set note_title = nvl2(:note_title, :note_title, note_title), "
        // + "note_content = :note_content, "
        // + "text_content = :text_content, "
        // + "modified_date = TO_CHAR(SYSTIMESTAMP, 'YYYY.MM.DD HH24:MI:SS'), "
        // + "parent_notebook = :parent_notebook, "
        // + "user_name = nvl2(:user_name, :user_name, user_name), "
        // + "is_favorite = nvl2(:is_favorite,:is_favorite,is_favorite), "
        // + "is_edit = :is_edit, "
        // + "USER_ID = nvl2(:USER_ID, :USER_ID, USER_ID) "
        // + "where note_id = :note_id", nativeQuery = true)
        // public int editDonePage(
        // @Param("note_id") String id,
        // @Param("note_title") String name,
        // @Param("note_content") String content,
        // @Param("text_content") String textContent,
        // @Param("user_name") String userName,
        // @Param("is_favorite") String favorite,
        // @Param("parent_notebook") String chapterId,
        // @Param("is_edit") String editingUserId,
        // @Param("USER_ID") String userId);

}
