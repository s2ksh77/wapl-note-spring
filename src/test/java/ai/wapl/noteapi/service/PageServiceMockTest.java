package ai.wapl.noteapi.service;

import ai.wapl.noteapi.domain.Chapter;
import ai.wapl.noteapi.domain.Page;
import ai.wapl.noteapi.domain.Tag;
import ai.wapl.noteapi.dto.FileDTO;
import ai.wapl.noteapi.dto.PageDTO;
import ai.wapl.noteapi.dto.PageDTO.Action;
import ai.wapl.noteapi.repository.BookmarkRepository;
import ai.wapl.noteapi.repository.ChapterRepository;
import ai.wapl.noteapi.repository.LogRepository;
import ai.wapl.noteapi.repository.PageRepository;
import java.util.Collections;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.Optional;

import static ai.wapl.noteapi.domain.Chapter.Type.*;
import static ai.wapl.noteapi.dto.PageDTO.Action.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PageServiceMockTest {

//    @Mock
//    ChapterRepository chapterRepository;
//    @Mock
//    PageRepository pageRepository;
//    @Mock
//    FileService fileService;
//    @Mock
//    BookmarkRepository bookmarkRepository;
//    @Mock
//    LogRepository logRepository;
//
//    PageService pageService;
//
//    @BeforeEach
//    public void setUp() {
//        MockitoAnnotations.openMocks(this);
//        pageService = new PageService(chapterRepository, pageRepository, fileService,
//            bookmarkRepository, logRepository);
    }

    @Test
    @DisplayName("createPage from user")
    public void createPageFromUser() {
        // given
        String userId = "userId";
        String channelId = "channelId";
        String chapterId = "chapterId";
        Page input = Page.builder()
                .userId(userId).content("<p><br></p>").userName("오다은").editingUserId(userId)
                .name("no title").chapter(Chapter.builder().channelId(channelId).id(chapterId).build())
                .build();

        // when
        Page result = pageService.createPage(input, false);

        // then
        verify(pageRepository).save(input);
    }

    @Test
    @DisplayName("createPage default")
    public void createPageNew() {
        // given
        String userId = "userId";
        String channelId = "channelId";
        String chapterId = "chapterId";
        Page input = Page.builder()
                .userId(userId).content("<p><br></p>").userName("오다은")
                .name("no title").chapter(Chapter.builder().channelId(channelId).id(chapterId).build())
                .build();

        // when
        pageService.createPage(input, false);

        // then
        verify(pageRepository).save(input);
    }

    @Test
    public void getPageInfo() {
        // given
//        String pageId = "pageId";
//        String userId = "6f30ca06-bff9-4534-aa13-727efb0a1f22";
//
//        PageDTO page = PageDTO.builder().id(pageId).createdUserId(userId)
//                .name("no title").chapterId("chapterId").build();
//        when(pageRepository.findById(userId, pageId)).thenReturn(page);
//        when(fileService.getFileListByPageId(pageId)).thenReturn(Collections.singletonList(
//            FileDTO.builder().id("logicalFileId").name("image").extension("jpg").createdUser(userId)
//                .build()
//        ));
//
//        // when
//        PageDTO pageInfo = pageService.getPageInfo(userId, pageId);
//
//        // then
//        assertThat(pageInfo).isEqualTo(page);
    }

    @Test
    public void deletePage() {
        // given
        String pageId = "pageId";
        String userId = "6f30ca06-bff9-4534-aa13-727efb0a1f22";
        Page page = Page.builder().id(pageId).userId(userId)
                .name("no title")
                .chapter(Chapter.builder().id("chapterId").channelId("channelId").build())
                .build();
        page.addTag(new Tag("tagId", "name"));
        when(pageRepository.findById(pageId)).thenReturn(Optional.of(page));

        // when

        // then
        verify(pageRepository).deleteById(pageId);
        //TODO: check if file was deleted by page id
    }

    @Test
    @DisplayName("update NONEDIT")
    public void updatePage_NONEDIT() {
        // given
        String pageId = "pageId";
        String chapterId = "chapterId";
        String userId = "6f30ca06-bff9-4534-aa13-727efb0a1f22";
        PageDTO page = PageDTO.builder().id(pageId).name("no title")
                .chapterId(chapterId).channelId("channelId").build();

        when(pageRepository.findById(pageId)).thenReturn(Optional.of(page.toEntity()));

        // when
        Page updatePage = pageService.updatePage(userId, page, NON_EDIT, false);

        // then
        assertThat(updatePage.isEditing()).isFalse();
    }

    @Test
    @DisplayName("update EDIT_START")
    public void updatePage_EDIT_START() {
        // given
        String pageId = "pageId";
        String chapterId = "chapterId";
        String userId = "6f30ca06-bff9-4534-aa13-727efb0a1f22";
        PageDTO page = PageDTO.builder().id(pageId).name("no title").userName("user name")
                .chapterId(chapterId).channelId("channelId").build();
        when(pageRepository.findById(pageId)).thenReturn(Optional.of(page.toEntity()));

        // when
        Page updatePage = pageService.updatePage(userId, page, Action.EDIT_START, false);

        // then
        assertThat(updatePage.isEditing()).isTrue();
        assertThat(updatePage.getUpdatedUserId()).isEqualTo(userId);
        assertThat(updatePage.getUserName()).isEqualTo("user name");
    }

    @Test
    @DisplayName("update MOVE")
    public void updatePage_MOVE() {
        // given
        String pageId = "pageId";
        String chapterId = "new chapter id";
        String userId = "6f30ca06-bff9-4534-aa13-727efb0a1f22";
        PageDTO page = PageDTO.builder().id(pageId).name("no title")
                .chapterId("chapterId").channelId("channelId").build();
        Chapter newChapter = Chapter.builder().id(chapterId).channelId("channelId").build();

        when(pageRepository.findById(pageId)).thenReturn(Optional.of(page.toEntity()));
        when(chapterRepository.findById(chapterId)).thenReturn(Optional.of(newChapter));

        page.setChapterId(chapterId);

        // when
        Page output = pageService.updatePage(userId, page, Action.MOVE, false);

        // then
        assertThat(output.getChapter().getId()).isEqualTo(chapterId);
    }

    @Test
    @DisplayName("update RENAME")
    public void updatePage_RENAME() {
        // given
        String pageId = "pageId";
        String userId = "6f30ca06-bff9-4534-aa13-727efb0a1f22";
        PageDTO page = PageDTO.builder().id(pageId).name("no title")
                .chapterId("chapterId").channelId("channelId").build();

        when(pageRepository.findById(pageId)).thenReturn(Optional.of(page.toEntity()));

        page.setName("rename");

        // when
        Page output = pageService.updatePage(userId, page, Action.RENAME, false);

        // then
        assertThat(output.getName()).isEqualTo("rename");
    }

    @Test
    @DisplayName("update EDIT_DONE")
    public void updatePage_EDIT_DONE() {
        // given
        String pageId = "pageId";
        String userId = "6f30ca06-bff9-4534-aa13-727efb0a1f22";
        PageDTO page = PageDTO.builder().id(pageId).name("no title").editingUserId(userId)
                .userName("user name")
                .chapterId("chapterId").channelId("channelId").build();
        when(pageRepository.findById(pageId)).thenReturn(Optional.of(page.toEntity()));

        page.setContent("updated content");

        // when
        Page output = pageService.updatePage(userId, page, Action.EDIT_DONE, false);

        // then
        assertThat(output.isEditing()).isFalse();
        assertThat(output.getUserName()).isEqualTo("user name");
    }

    @Test
    @DisplayName("THROW")
    public void updateRecyclePage_THROW() {
        // given
//        String channelId = "channelId";
//        String pageId = "pageId";
//        String userId = "6f30ca06-bff9-4534-aa13-727efb0a1f22";
//        PageDTO dto = PageDTO.builder().channelId(channelId).type(THROW.name())
//                .id(pageId).build();
//        Chapter chapter = Chapter.builder().id("chapterId").channelId(channelId)
//                .type(RECYCLE_BIN)
//                .build();
//
//        Page page = Page.builder().id(pageId).userId(userId)
//                .editingUserId(userId)
//                .name("no title")
//                .chapter(chapter).build();
//        page.addTag(new Tag("tagId", "name"));
//        chapter.getPageList().add(page);
//
//        when(pageRepository.getById(pageId)).thenReturn(page);
//        when(chapterRepository.findByChannelIdAndType(channelId, RECYCLE_BIN)).thenReturn(
//            Optional.of(chapter));
//
//        // when
//        pageService.updateRecyclePage(userId, dto, THROW, false);
//
//        // then
//        assertThat(page.isShared()).isFalse();
//        assertThat(page.getChapter().getType()).isEqualTo(RECYCLE_BIN);
    }

    @Test
    @DisplayName("RESTORE")
    public void updateRecyclePage_RESTORE() {
        // given
//        String channelId = "channelId";
//        String pageId = "pageId";
//        String userId = "6f30ca06-bff9-4534-aa13-727efb0a1f22";
//        PageDTO dto = PageDTO.builder().channelId(channelId).type(THROW.name())
//                .id(pageId).build();
//        Chapter chapter = Chapter.builder().id("chapterId").channelId(channelId)
//                .type(RECYCLE_BIN)
//                .build();
//
//        Page page = Page.builder().id(pageId).userId(userId)
//                .editingUserId(userId)
//                .name("no title")
//                .chapter(chapter).build();
//        page.addTag(new Tag("tagId", "name"));
//        chapter.getPageList().add(page);
//
//        when(pageRepository.getById(pageId)).thenReturn(page);
//        when(chapterRepository.findByChannelIdAndType(channelId, RECYCLE_BIN)).thenReturn(
//            Optional.of(chapter));
//
//        // when
//        pageService.updateRecyclePage(userId, dto, RESTORE, false);
//
//        // then
//        assertThat(page.isShared()).isFalse();
//        assertThat(page.getChapter().getType()).isNull();
    }
}