package ai.wapl.noteapi.service;

import ai.wapl.noteapi.domain.Chapter;
import ai.wapl.noteapi.domain.Page;
import ai.wapl.noteapi.repository.PageRepository;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import javax.transaction.TransactionScoped;
import javax.transaction.Transactional;

import java.util.List;
import java.util.Optional;

import static ai.wapl.noteapi.domain.Chapter.Type.notebook;
import static ai.wapl.noteapi.domain.Chapter.Type.recycle_bin;
import static ai.wapl.noteapi.util.Constants.KOREAN;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class ChapterServiceTest {

    @Resource
    ChapterService service;
    @Resource
    PageService pageService;

    @Test
    public void getChapterInfoList() {
        // given
        String id = "330a2cfb-037e-4d4e-a35f-ef0a561d1299";

        // when
        Chapter chapter = service.getChapterInfoList(id);

        // then
        assertThat(chapter.getPageList().size()).isEqualTo(20);
    }

    @Test
    public void createChapter() {
        // given
        String channelId = "channelId";
        String userId = "userId";
        String color = "#5C83DA";
        Chapter chapter = Chapter.builder()
                .name("새 챕터").channelId(channelId).color(color)
                .build();

        // when
        Chapter chapter1 = service.createChapter(userId, chapter, KOREAN);

        // then
        assertThat(chapter1.getType()).isEqualTo(notebook);
        assertThat(chapter1.getColor()).isEqualTo(color);
        assertThat(chapter1.getUserId()).isEqualTo(userId);
        assertThat(chapter1.getModifiedDate()).isNotNull();
        assertThat(chapter1.getPageList().size()).isEqualTo(1);
    }

    @Test
    @Transactional
    public void deleteChapter() throws Exception {
        // given
        String channelId = "channelId";
        String userId = "userId";
        String color = "#5C83DA";
        Chapter chapter = Chapter.builder()
                .name("새 챕터").channelId(channelId).color(color)
                .build();
        Chapter chapter1 = service.createChapter(userId, chapter, KOREAN);

        // when
        service.deleteChapter(channelId, chapter1.getId());

        // then
        Page page = pageService.getPageInfo(chapter1.getPageList().get(0).getId());
        assertThat(page).isNotNull();
    }

}