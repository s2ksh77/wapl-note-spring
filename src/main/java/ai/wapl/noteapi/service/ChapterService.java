package ai.wapl.noteapi.service;

import static ai.wapl.noteapi.domain.Chapter.Type;
import static ai.wapl.noteapi.domain.Chapter.Type.DEFAULT;
import static ai.wapl.noteapi.domain.Chapter.Type.RECYCLE_BIN;
import static ai.wapl.noteapi.util.Constants.EMPTY_CONTENT;
import static ai.wapl.noteapi.util.Constants.KOREAN;
import static ai.wapl.noteapi.util.Constants.NEW_CHAPTER_ENGLISH;
import static ai.wapl.noteapi.util.Constants.NEW_CHAPTER_KOREAN;
import static ai.wapl.noteapi.util.Constants.NEW_PAGE_ENGLISH;
import static ai.wapl.noteapi.util.Constants.NEW_PAGE_KOREAN;
import static ai.wapl.noteapi.util.NoteUtil.now;

import ai.wapl.noteapi.domain.Chapter;
import ai.wapl.noteapi.domain.NoteLog;
import ai.wapl.noteapi.domain.NoteLog.LogAction;
import ai.wapl.noteapi.domain.Page;
import ai.wapl.noteapi.dto.ChapterDTO;
import ai.wapl.noteapi.repository.ChapterRepository;
import ai.wapl.noteapi.repository.LogRepository;
import ai.wapl.noteapi.util.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ai.wapl.noteapi.util.exception.GlobalExceptionHandler;

@Service
@Transactional
@RequiredArgsConstructor
public class ChapterService {

    public static final String RESOURCE_TYPE = "chapter";

    private final ChapterRepository chapterRepository;
    private final LogRepository logRepository;
    private final PageService pageService;
    private final FileService fileService;
    private final Logger logger = LoggerFactory.getLogger(ChapterService.class);

    public String createApp(String language) {
        String channelId = UUID.randomUUID().toString();

        Chapter chapter = createChapter(null,
                Chapter.builder().channelId(channelId)
                        .name(language.equals(KOREAN) ? NEW_CHAPTER_KOREAN : NEW_CHAPTER_ENGLISH)
                        .color(Color.getRandomColor()).build(),
                language, false);

        chapterRepository.save(Chapter.createRecycleBin(channelId));

        return channelId;
    }

    public void deleteApp(String channelId) {
        pageService.deleteAllByChannel(channelId);
        chapterRepository.deleteAllByChannelId(channelId);
    }

    /**
     * 챕터 전체 조회 서비스
     */
    @Transactional(readOnly = true)
    public List<ChapterDTO> getChapterList(String userId, String channelId, boolean mobile) {
        List<ChapterDTO> chapterDTOS = new ArrayList<>();
        List<Chapter> chapters = chapterRepository.findByChannelId(channelId);
        if (chapters.size() == 0)
            return (List<ChapterDTO>) new GlobalExceptionHandler().handleNotFound(null);
        Set<String> readSet = logRepository.getReadListByChannelId(userId, channelId);

        chapters.forEach(chapter -> {
            ChapterDTO dto = new ChapterDTO(chapter);
            // 웹에서만 즐찾 필요
            if (mobile)
                dto.setPageList(chapterRepository.findByChapterIdWithBookmark(dto.getId(), userId));
            dto.setRead(readSet.contains(chapter.getId()));
            chapterDTOS.add(dto);
        });
        return chapterDTOS;
    }

    /**
     * 챕터 하위 페이지 조회 서비스
     */
    @Transactional(readOnly = true)
    public ChapterDTO getChapterInfoList(String userId, String chapterId, String channelId) {
        ChapterDTO chapter = chapterRepository.findByIdFetchJoin(chapterId, userId, channelId);
        // .orElseThrow(() -> new ResourceNotFoundException("Not found Chapter"));
        Set<String> readSet = logRepository.getReadListByChapterId(userId, chapterId);

        // ChapterDTO dto = new ChapterDTO(chapter);
        chapter.setRead(readSet.contains(chapter.getId()));
        chapter.getPageList().forEach(pageDTO -> {
            pageDTO.setFavorite(pageService.isBookMark(pageDTO.getId(), userId));
            pageDTO.setRead(readSet.contains(pageDTO.getId()));
        });

        return chapter;
    }

    /**
     * 챕터 추가 서비스 createdDate, modifiedDate
     */
    public Chapter createChapter(String userId, Chapter inputChapter, String language,
            boolean mobile) {
        Chapter chapter = chapterRepository.save(Chapter.createChapter(userId, inputChapter));

        Page page = Page.builder().chapter(chapter)
                .userId(userId)
                .userName(inputChapter.getUserName())
                .content(EMPTY_CONTENT)
                .name(language.equals(KOREAN) ? NEW_PAGE_KOREAN : NEW_PAGE_ENGLISH)
                .build();

        chapter.addPage(pageService.createPage(page, mobile));
        return chapter;
    }

    /**
     * 챕터 삭제 서비스 하위 페이지는 휴지통으로 이동 되고 챕터는 삭제 된다.
     */
    public void deleteChapter(String userId, String channelId, String chapterId, boolean mobile) {
        Chapter chapter = chapterRepository.findById(chapterId)
                .orElseThrow(ResourceNotFoundException::new);

        if (chapter.getType().equals(DEFAULT) || chapter.getType().equals(Type.NOTEBOOK)) {
            chapterRepository.updateRecycleBin(chapterId, channelId, now());
        } else
            fileService.deleteFileByChapterId(channelId, chapterId);

        chapterRepository.deleteById(chapterId);
        createChapterLog(userId, chapter.getId(), LogAction.delete, mobile);
    }

    /**
     * 챕터 수정 서비스 챕터 이름, 챕터 색상(기획엔 없음)
     */
    public Chapter updateChapter(String userId, Chapter inputDTO, boolean mobile) {
        Chapter chapter = chapterRepository.findById(inputDTO.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Not found Chapter."));

        if (inputDTO.getName() != null) {
            chapter.setName(inputDTO.getName());
            chapter.setModifiedDate(now());
        } else if (inputDTO.getColor() != null) {
            chapter.setColor(inputDTO.getColor());
            chapter.setModifiedDate(now());
        }
        createChapterLog(userId, chapter.getId(), LogAction.rename, mobile);
        return chapter;
    }

    @Transactional(readOnly = true)
    public Chapter getRecycleBin(String channelId) {
        return chapterRepository.findByChannelIdAndType(channelId, RECYCLE_BIN)
                .orElseThrow(ResourceNotFoundException::new);
    }

    public Chapter shareChapter(String userId, String chapterId, boolean mobile) {
        // create share type of chapter
        Chapter originChapter = chapterRepository.findByIdJoin(chapterId).orElseThrow(ResourceNotFoundException::new);
        Chapter newChapter = Chapter.createChapterForShare(userId, originChapter);
        chapterRepository.save(newChapter);

        // // get page list of chapter
        originChapter.getPageList()
                .forEach(page -> pageService.sharePageToChapter(userId, newChapter, page, mobile));

        return newChapter;
    }

    private void createChapterLog(String userId, String chapterId, LogAction action,
            boolean mobile) {
        NoteLog log = new NoteLog("", userId, chapterId, RESOURCE_TYPE, action, mobile);
        logRepository.save(log);
    }

}
