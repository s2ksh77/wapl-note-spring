package ai.wapl.noteapi.service;

import ai.wapl.noteapi.domain.Chapter;
import ai.wapl.noteapi.domain.Page;
import ai.wapl.noteapi.domain.Tag;
import ai.wapl.noteapi.dto.PageDTO;
import ai.wapl.noteapi.repository.ChapterRepository;
import ai.wapl.noteapi.repository.PageRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.Optional;

import static ai.wapl.noteapi.domain.Page.PageType.*;
import static ai.wapl.noteapi.dto.PageDTO.Type.THROW;
import static ai.wapl.noteapi.util.Constants.RETURN_MSG_FAIL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PageServiceTest {

    @Mock
    PageRepository pageRepository;
    @Mock
    ChapterRepository chapterRepository;
    PageService pageService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        pageService = new PageService(pageRepository, chapterRepository);
    }

    @Test
    @DisplayName("createPage from user")
    public void createPageFromUser() throws Exception {
        // given
        String userId = "userId";
        String channelId = "channelId";
        String chapterId = "chapterId";
        Page input = Page.builder()
                .userId(userId).content("<p><br></p>").userName("오다은").editingUserId(userId)
                .name("no title").chapter(Chapter.builder().channelId(channelId).id(chapterId).build())
                .build();

        // when
        Page result = pageService.createPage(input);

        // then
        verify(pageRepository).save(input);
    }

    @Test
    @DisplayName("createPage default")
    public void createPageNew() throws Exception {
        // given
        String userId = "userId";
        String channelId = "channelId";
        String chapterId = "chapterId";
        Page input = Page.builder()
                .userId(userId).content("<p><br></p>").userName("오다은")
                .name("no title").chapter(Chapter.builder().channelId(channelId).id(chapterId).build())
                .build();

        // when
        pageService.createPage(input);

        // then
        verify(pageRepository).save(input);
    }

    @Test
    public void getPageInfo() throws Exception {
        // given
        String pageId = "pageId";
        String userId = "6f30ca06-bff9-4534-aa13-727efb0a1f22";

        Page page = Page.builder().id(pageId).userId(userId)
                .name("no title")
                .chapter(Chapter.builder().id("chapterId").channelId("channelId").build())
                .build();
        page.addTag(new Tag("tagId", "name", null));
        when(pageRepository.findById(pageId)).thenReturn(Optional.of(page));

        // when
        Page pageInfo = pageService.getPageInfo(pageId);

        // then
        assertThat(pageInfo).isEqualTo(page);
    }

    @Test
    public void deletePage() throws Exception {
        // given
        String pageId = "pageId";
        String userId = "6f30ca06-bff9-4534-aa13-727efb0a1f22";
        Page page = Page.builder().id(pageId).userId(userId)
                .name("no title")
                .chapter(Chapter.builder().id("chapterId").channelId("channelId").build())
                .build();
        page.addTag(new Tag("tagId", "name", null));
        when(pageRepository.findById(pageId)).thenReturn(Optional.of(page));

        // when

        // then
        verify(pageRepository).deleteById(pageId);
        //TODO: check if file was deleted by page id
    }

    @Test
    @DisplayName("update NONEDIT")
    public void updatePage_NONEDIT() throws Exception {
        // given
        String pageId = "pageId";
        String userId = "6f30ca06-bff9-4534-aa13-727efb0a1f22";
        Page page = Page.builder().id(pageId).userId(userId)
                .name("no title").type(NONEDIT.toString())
                .chapter(Chapter.builder().id("chapterId").channelId("channelId").build())
                .build();
        page.addTag(new Tag("tagId", "name", null));
        when(pageRepository.findById(pageId)).thenReturn(Optional.of(page));

        // when
        pageService.updatePage(page);

        // then
        page.setType(null);
        verify(pageRepository).save(page);
    }

    @Test
    @DisplayName("update EDIT_START")
    public void updatePage_EDIT_START() throws Exception {
        // given
        String pageId = "pageId";
        String userId = "6f30ca06-bff9-4534-aa13-727efb0a1f22";
        Page page = Page.builder().id(pageId).userId(userId)
                .name("no title").type(EDIT_START.toString())
                .chapter(Chapter.builder().id("chapterId").channelId("channelId").build())
                .build();
        page.addTag(new Tag("tagId", "name", null));
        when(pageRepository.findById(pageId)).thenReturn(Optional.of(page));

        // when
        Page output = pageService.updatePage(page);

        // then
        assertThat(output.getResultMsg()).isEqualTo(RETURN_MSG_FAIL);
    }

    @Test
    @DisplayName("update MOVE")
    public void updatePage_MOVE() throws Exception {
        // given
        String pageId = "pageId";
        String userId = "6f30ca06-bff9-4534-aa13-727efb0a1f22";
        Page page = Page.builder().id(pageId).userId(userId)
                .name("no title").type(MOVE.toString())
                .chapter(Chapter.builder().id("chapterId").channelId("channelId").build())
                .build();
        page.addTag(new Tag("tagId", "name", null));
        when(pageRepository.findById(pageId)).thenReturn(Optional.of(page));

        Page input = Page.createPage(page);
        input.setChapter(Chapter.builder().id("new chapter id").channelId("new channel id").build());

        // when
        Page output = pageService.updatePage(input);

        // then
        input.setType(null);
        verify(pageRepository).save(input);
    }

    @Test
    @DisplayName("update RENAME")
    public void updatePage_RENAME() throws Exception {
        // given
        String pageId = "pageId";
        String userId = "6f30ca06-bff9-4534-aa13-727efb0a1f22";
        Page page = Page.builder().id(pageId).userId(userId)
                .name("no title").type(RENAME.toString())
                .chapter(Chapter.builder().id("chapterId").channelId("channelId").build())
                .build();
        page.addTag(new Tag("tagId", "name", null));
        when(pageRepository.findById(pageId)).thenReturn(Optional.of(page));

        Page input = Page.createPage(page);
        input.setName("rename");

        // when
        Page output = pageService.updatePage(input);

        // then
        input.setType(null);
        verify(pageRepository).save(input);
    }

    @Test
    @DisplayName("update EDIT_DONE")
    public void updatePage_EDIT_DONE() throws Exception {
        // given
        String pageId = "pageId";
        String userId = "6f30ca06-bff9-4534-aa13-727efb0a1f22";
        Page page = Page.builder().id(pageId).userId(userId)
                .editingUserId(userId)
                .name("no title").type(EDIT_DONE.toString())
                .chapter(Chapter.builder().id("chapterId").channelId("channelId").build())
                .build();
        page.addTag(new Tag("tagId", "name", null));
        when(pageRepository.findById(pageId)).thenReturn(Optional.of(page));

        Page input = Page.createPage(page);
        input.setContent("updated content");
        input.setUpdatedUserId("updated user id");

        // when
        Page output = pageService.updatePage(input);

        // then
        input.setType(null);
        verify(pageRepository).save(input);
    }

    @Test
    @DisplayName("THROW")
    public void updateRecyclePage_THROW() throws Exception {
        // given
        String channelId = "channelId";
        String pageId = "pageId";
        String userId = "6f30ca06-bff9-4534-aa13-727efb0a1f22";
        PageDTO dto = PageDTO.builder().channelId(channelId).type(THROW.name())
                .id(pageId).build();
        Chapter chapter = Chapter.builder().id("chapterId").channelId(channelId)
                .type("recycle_bin")
                .build();

        Page page = Page.builder().id(pageId).userId(userId)
                .editingUserId(userId)
                .name("no title").type(EDIT_DONE.toString())
                .chapter(chapter).build();
        page.addTag(new Tag("tagId", "name", null));
        chapter.getPageList().add(page);

        when(pageRepository.getById(pageId)).thenReturn(page);
        when(chapterRepository.findByChannelIdAndType(channelId, "recycle_bin")).thenReturn(chapter);

        // when
        pageService.updateRecyclePage(Arrays.asList(dto));

        // then
        assertThat(page.getType()).isEqualTo("");
        assertThat(page.getChapter().getType()).isEqualTo("recycle_bin");
    }

    @Test
    @DisplayName("RESTORE")
    public void updateRecyclePage_RESTORE() throws Exception {
        // given
        String channelId = "channelId";
        String pageId = "pageId";
        String userId = "6f30ca06-bff9-4534-aa13-727efb0a1f22";
        PageDTO dto = PageDTO.builder().channelId(channelId).type(THROW.name())
                .id(pageId).build();
        Chapter chapter = Chapter.builder().id("chapterId").channelId(channelId)
                .type("recycle_bin")
                .build();

        Page page = Page.builder().id(pageId).userId(userId)
                .editingUserId(userId)
                .name("no title").type(EDIT_DONE.toString())
                .chapter(chapter).build();
        page.addTag(new Tag("tagId", "name", null));
        chapter.getPageList().add(page);

        when(pageRepository.getById(pageId)).thenReturn(page);
        when(chapterRepository.findByChannelIdAndType(channelId, "recycle_bin")).thenReturn(chapter);

        // when
        pageService.updateRecyclePage(Arrays.asList(dto));

        // then
        assertThat(page.getType()).isEqualTo("");
        assertThat(page.getChapter().getType()).isNull();
    }
}