package ai.wapl.noteapi.service;

import ai.wapl.noteapi.dto.ChapterDTO;
import ai.wapl.noteapi.dto.PageDTO;
import ai.wapl.noteapi.repository.LogRepository;
import ai.wapl.noteapi.util.Color;
import java.util.ArrayList;
import java.util.List;

import java.util.Set;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import ai.wapl.noteapi.domain.Chapter;
import ai.wapl.noteapi.domain.Page;
import ai.wapl.noteapi.repository.ChapterRepository;
import org.springframework.transaction.annotation.Transactional;

import static ai.wapl.noteapi.domain.Chapter.*;
import static ai.wapl.noteapi.domain.Chapter.Type.DEFAULT;
import static ai.wapl.noteapi.domain.Chapter.Type.RECYCLE_BIN;
import static ai.wapl.noteapi.util.Constants.*;
import static ai.wapl.noteapi.util.NoteUtil.*;

@Service
@Transactional
@RequiredArgsConstructor
public class ChapterService {

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
            language);

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
    public List<ChapterDTO> getChapterList(String userId, String channelId) {
        List<ChapterDTO> chapterDTOS = new ArrayList<>();
        List<Chapter> chapters = chapterRepository.findByChannelId(channelId);
        Set<String> readSet = logRepository.getReadListByChannelId(userId, channelId);
        chapters.forEach(chapter -> {
            ChapterDTO dto = new ChapterDTO(chapter);
            dto.setRead(readSet.contains(dto.getId()));
            chapterDTOS.add(dto);
        });
        return chapterDTOS;
    }

    /**
     * 챕터 하위 페이지 조회 서비스
     */
    @Transactional(readOnly = true)
    public ChapterDTO getChapterInfoList(String userId, String chapterId) {
        Chapter chapter = chapterRepository.findByIdFetchJoin(chapterId)
            .orElseThrow(() -> new ResourceNotFoundException("Not found Chapter"));
        Set<String> readSet = logRepository.getReadListByChapterId(userId, chapterId);

        ChapterDTO dto = new ChapterDTO(chapter);
        dto.setRead(readSet.contains(dto.getId()));
        dto.getPageList().forEach(pageDTO -> pageDTO.setRead(readSet.contains(pageDTO.getId())));

        return dto;
    }

    /**
     * 챕터 추가 서비스
     * createdDate, modifiedDate
     */
    public Chapter createChapter(String userId, Chapter inputChapter, String language) {
        Chapter chapter = chapterRepository.save(Chapter.createChapter(userId, inputChapter));

        Page page = Page.builder().chapter(chapter)
                .userId(inputChapter.getUserId())
                .userName(inputChapter.getUserName())
                .content(EMPTY_CONTENT)
                .name(language.equals(KOREAN) ? NEW_PAGE_KOREAN : NEW_PAGE_ENGLISH)
                .build();

        chapter.addPage(pageService.createPage(page));

        return chapter;
    }

    /**
     * 챕터 삭제 서비스
     * 하위 페이지는 휴지통으로 이동 되고 챕터는 삭제 된다.
     */
    public void deleteChapter(String channelId, String chapterId) {
        Chapter chapter = chapterRepository.findById(chapterId).orElseThrow(ResourceNotFoundException::new);

        if (chapter.getType().equals(DEFAULT) || chapter.getType().equals(Type.NOTEBOOK)) {
            chapterRepository.updateRecycleBin(chapterId, channelId, now());
        } else
            fileService.deleteFileByChapterId(channelId, chapterId);

        chapterRepository.deleteById(chapterId);
    }

    /**
     * 챕터 수정 서비스
     * 챕터 이름, 챕터 색상(기획엔 없음)
     */
    public Chapter updateChapter(Chapter inputDTO) {
        Chapter chapter = chapterRepository.findById(inputDTO.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Not found Chapter."));

        if (inputDTO.getName() != null) {
            chapter.setName(inputDTO.getName());
            chapter.setModifiedDate(now());
        } else if (inputDTO.getColor() != null) {
            chapter.setColor(inputDTO.getColor());
            chapter.setModifiedDate(now());
        }
        return chapter;
    }

    @Transactional(readOnly = true)
    public Chapter getRecycleBin(String channelId) {
        return chapterRepository.findByChannelIdAndType(channelId, RECYCLE_BIN)
            .orElseThrow(ResourceNotFoundException::new);
    }

    public Chapter shareChapter(String userId, String chapterId) {
        // create share type of chapter
        Chapter originChapter = chapterRepository.findByIdFetchJoin(chapterId)
                .orElseThrow(ResourceNotFoundException::new);
        Chapter newChapter = Chapter.createChapterForShare(userId, originChapter);
        chapterRepository.save(newChapter);

        // get page list of chapter
        originChapter.getPageList().forEach(page -> pageService.sharePageToChapter(newChapter, page));

        return newChapter;
    }

}
