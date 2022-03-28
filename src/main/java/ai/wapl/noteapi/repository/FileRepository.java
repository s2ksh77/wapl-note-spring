package ai.wapl.noteapi.repository;

import ai.wapl.noteapi.domain.File;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FileRepository extends JpaRepository<File, File> {
    List<File> findByPageId(String pageId);

    @Query("select f from Page p inner join File f on f.pageId = p.id where p.chapter.id = ?1")
    List<File> findByChapterId(String chapterId);

    @Modifying
    int deleteByPageId(String pageId);

    @Modifying
    @Query("delete from File f where f.pageId in (select p.id from Page p where p.chapter.id = ?1)")
    int deleteByChapterId(String chapterId);

}
