package ai.wapl.noteapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ai.wapl.noteapi.domain.Tag;

@Repository
public interface TagRepository extends JpaRepository<Tag, String> {

}