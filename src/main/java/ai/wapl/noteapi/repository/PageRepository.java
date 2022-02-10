package ai.wapl.noteapi.repository;

import ai.wapl.noteapi.domain.Page;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PageRepository extends JpaRepository<Page, String>, PageRepositoryInterface {
}
