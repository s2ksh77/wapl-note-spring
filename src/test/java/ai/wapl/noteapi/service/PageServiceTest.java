package ai.wapl.noteapi.service;

import ai.wapl.noteapi.dto.SearchDTO;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class PageServiceTest {
    @Resource
    PageService pageService;

    @Test
    public void search() {
        // given
        String channelId = "deef09e9-9f67-4e24-aef7-23b6be588cd2";
        String text = "aa";

        // when
        SearchDTO search = pageService.search(channelId, text);

        // then
        assertThat(search.getChapterList().size()).isEqualTo(1);
        assertThat(search.getPageList().size()).isEqualTo(17);
        assertThat(search.getTagList().size()).isEqualTo(1);
    }

}
