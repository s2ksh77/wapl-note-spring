package ai.wapl.noteapi.service;

import static ai.wapl.noteapi.util.NoteUtil.now;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import ai.wapl.noteapi.domain.Chapter;
import ai.wapl.noteapi.domain.Chapter.Type;
import ai.wapl.noteapi.repository.ChapterRepository;
import ai.wapl.noteapi.repository.LogRepository;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ChapterMockTest {
  @Mock
  ChapterRepository chapterRepository;
  @Mock
  LogRepository logRepository;
  @Mock
  PageService pageService;
  @Mock
  FileService fileService;

  ChapterService chapterService;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this);
    chapterService = new ChapterService(chapterRepository, logRepository, pageService, fileService);
  }

  @Test
  @DisplayName("delete Chapter type default")
  public void deleteChapterTypeDefault() {
    // given
//    String channelId = "channelId";
//    String chapterId = "chapterId";
//    String userId = "6f30ca06-bff9-4534-aa13-727efb0a1f22";
//    Chapter chapter = Chapter.builder().id(chapterId).channelId(channelId)
//        .type(Type.DEFAULT).build();
//
//    when(chapterRepository.findById(chapterId)).thenReturn(Optional.ofNullable(chapter));
//
//    // when
//    chapterService.deleteChapter(userId, channelId, chapterId, false);
//
//    // then
//    verify(chapterRepository).updateRecycleBin(chapterId, channelId, now());
  }


}
