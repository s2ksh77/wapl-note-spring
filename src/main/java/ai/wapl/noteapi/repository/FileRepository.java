package ai.wapl.noteapi.repository;

import ai.wapl.noteapi.domain.File;
import ai.wapl.noteapi.dto.FileDTO;
import ai.wapl.noteapi.dto.FileDTOinterface;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FileRepository extends JpaRepository<File, String> {
    List<File> findByPageId(String pageId);

    @Query("select f from Page p inner join File f on f.pageId = p.id where p.chapter.id = ?1")
    List<File> findByChapterId(String chapterId);

    @Modifying
    int deleteByPageId(String pageId);

    @Modifying
    @Query("delete from File f where f.pageId in (select p.id from Page p where p.chapter.id = ?1)")
    int deleteByChapterId(String chapterId);

    @Query(value = "select s.log_file_id as fileId, s.file_size as fileSize, s.file_extension as fileExtension, \n"
            + "s.file_name as fileName, s.created_at as createdAt, s.updated_at as updatedAt, s.last_update_user_id as uploadUserId \n"
            + "from TB_NOTEAPP_NOTE_FILE_MAP as f \n"
            + "LEFT JOIN TB_DRIVE_MST as s \n"
            + "on f.file_id = s.log_file_id \n"
            + "where f.file_id = :fileId", nativeQuery = true)
    FileDTOinterface findFileInfo(@Param("fileId") String fileId);
}
