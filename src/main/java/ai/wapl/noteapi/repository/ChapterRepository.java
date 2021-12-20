package ai.wapl.noteapi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ai.wapl.noteapi.domain.Chapter;

@Repository
public interface ChapterRepository extends JpaRepository<Chapter, String> {

    List<Chapter> findByChannelId(String channelId);

}