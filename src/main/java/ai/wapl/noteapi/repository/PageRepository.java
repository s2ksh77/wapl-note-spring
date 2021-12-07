package ai.wapl.noteapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ai.wapl.noteapi.domain.Page;

@Repository
public interface PageRepository extends JpaRepository<Page, String> {

}
