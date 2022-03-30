package ai.wapl.noteapi.repository;

import ai.wapl.noteapi.domain.Bookmark;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookmarkRepository extends JpaRepository<Bookmark, Bookmark> {
}
