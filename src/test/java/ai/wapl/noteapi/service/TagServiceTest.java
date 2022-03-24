package ai.wapl.noteapi.service;

import ai.wapl.noteapi.domain.Tag;
import ai.wapl.noteapi.dto.TagDTO;
import ai.wapl.noteapi.repository.QueryDslPageRepositoryImpl;
import ai.wapl.noteapi.repository.TagRepository;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class TagServiceTest {
    @Resource
    TagService tagService;
    @Resource
    QueryDslPageRepositoryImpl queryDslPageRepository;
    @Resource
    TagRepository tagRepository;

    @Test
    public void findByChannelId() {
        // given
        String pageId = "3c266f61-e80b-4123-87dc-4ff5c598ac07";

        // when
        Set<Tag> tagList = tagService.getTagList(pageId);

        // then
        assertThat(tagList.size()).isEqualTo(1);
    }

}
