package ai.wapl.noteapi.repository;

import ai.wapl.noteapi.domain.File;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<File, File> {
}
