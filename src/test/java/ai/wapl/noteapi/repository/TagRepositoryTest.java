package ai.wapl.noteapi.repository;

import ai.wapl.noteapi.domain.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class TagRepositoryTest {
    @Resource
    TagRepository tagRepository;

    @Test
    public void findByChannelId() throws Exception {
        // given
        String channelId = "0042daaf-bd40-45e1-b4fa-88c6236d2d9d";

        // when
        Set<Tag> tags = tagRepository.findByChannelId(channelId);

        // then
        assertThat(tags.size()).isEqualTo(28);
    }

    @Test
    public void findByPageId() throws Exception {
        // given
        String pageId = "90cdabb3-187f-4476-bf3b-75d68ddcdb1d";

        // when
        Set<Tag> tags = tagRepository.findByPageId(pageId);

        // then
        assertThat(tags.size()).isEqualTo(24);
    }

}