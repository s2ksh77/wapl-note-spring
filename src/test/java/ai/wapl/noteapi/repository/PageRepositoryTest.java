package ai.wapl.noteapi.repository;

import ai.wapl.noteapi.domain.Page;
import ai.wapl.noteapi.dto.PageDTO;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class PageRepositoryTest {

    @Resource
    PageRepository pageRepository;

    @Test
    public void findById()  {
        // given
        String pageId = "2cd6dc17-229a-4ba2-9942-49d085ce0778";
        String userId = "caf1a998-c39e-49d4-81c7-719f6cc624d9"; // 오다은

        // when
        PageDTO page = pageRepository.findById(userId, pageId);

        // then
        assertThat(page.getCreatedUserId()).isEqualTo(userId);
        assertThat(page.isFavorite()).isTrue();
    }

}