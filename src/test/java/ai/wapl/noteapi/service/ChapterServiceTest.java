package ai.wapl.noteapi.service;

import ai.wapl.noteapi.domain.Chapter;
import ai.wapl.noteapi.domain.Page;
import ai.wapl.noteapi.dto.ChapterDTO;
import ai.wapl.noteapi.dto.PageDTO;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import static ai.wapl.noteapi.domain.Chapter.Type.NOTEBOOK;
import static ai.wapl.noteapi.util.Constants.KOREAN;
import static org.assertj.core.api.Assertions.assertThat;

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
//        String id = "330a2cfb-037e-4d4e-a35f-ef0a561d1299";
//        String userId = "userId";
//
//        // when
//        ChapterDTO chapter = service.getChapterInfoList(userId,id);
//
//        // then
//        assertThat(chapter.getPageList().size()).isEqualTo(20);
//        chapter.getPageList().stream()
//            .filter(pageDTO -> pageDTO.getId().equals("8438123a-f11b-4225-b8e2-b2c93b8a1db4"))
//            .forEach(pageDTO -> assertThat(pageDTO.isRead()).isTrue());
    }

    @Test
    public void createChapter() {
        // given
//        String channelId = "channelId";
//        String userId = "userId";
//        String color = "#5C83DA";
//        Chapter chapter = Chapter.builder()
//                .name("새 챕터").channelId(channelId).color(color)
//                .build();
//
//        // when
//        Chapter chapter1 = service.createChapter(userId, chapter, KOREAN,false);
//
//        // then
////        assertThat(chapter1.getType()).isEqualTo(NOTEBOOK);
//        assertThat(chapter1.getColor()).isEqualTo(color);
//        assertThat(chapter1.getUserId()).isEqualTo(userId);
//        assertThat(chapter1.getModifiedDate()).isNotNull();
//        assertThat(chapter1.getPageList().size()).isEqualTo(1);
    }

    @Test
    public void deleteChapter() {
        // given
//        String channelId = "channelId";
//        String userId = "userId";
//        String color = "#5C83DA";
//        Chapter chapter = Chapter.builder()
//                .name("새 챕터").channelId(channelId).color(color)
//                .build();
//        Chapter chapter1 = service.createChapter(userId, chapter, KOREAN, false);
//
//        // when
//        service.deleteChapter(userId, channelId, chapter1.getId(), false);
//
//        // then
//        PageDTO page = pageService.getPageInfo(userId, chapter1.getPageList().get(0).getId());
//        assertThat(page).isNotNull();
    }

}