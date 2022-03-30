package ai.wapl.noteapi.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import ai.wapl.noteapi.domain.Chapter;
import ai.wapl.noteapi.domain.Page;
import ai.wapl.noteapi.repository.ChapterRepository;
import ai.wapl.noteapi.util.NoteUtil;
import org.springframework.transaction.annotation.Transactional;

import static ai.wapl.noteapi.domain.Chapter.*;
import static ai.wapl.noteapi.domain.Chapter.Type.recycle_bin;
import static ai.wapl.noteapi.util.Constants.*;
import static ai.wapl.noteapi.util.NoteUtil.*;

@Service
@Transactional
public class ChapterService {

    private final ChapterRepository chapterRepository;
    private final PageService pageService;
    private final FileService fileService;
    private final Logger logger = LoggerFactory.getLogger(ChapterService.class);

    @Autowired
    public ChapterService(ChapterRepository chapterRepository, PageService pageService, FileService fileService) {
        this.chapterRepository = chapterRepository;
        this.pageService = pageService;
        this.fileService = fileService;
    }

    /**
     * 챕터 전체 조회 서비스
     */
    @Transactional(readOnly = true)
    public List<Chapter> getChapterList(String channelId) {
        return chapterRepository.findByChannelId(channelId);
    }

    /**
     * 챕터 하위 페이지 조회 서비스
     */
    @Transactional(readOnly = true)
    public Chapter getChapterInfoList(String chapterId) {
        return chapterRepository.findByIdFetchJoin(chapterId)
                .orElseThrow(() -> new ResourceNotFoundException("Not found Chapter"));
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
                .name(language.equals(KOREAN) ? NEWPAGE_KOREAN : NEWPAGE_ENGLISH)
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

        if (chapter.getType().equals("default") || chapter.getType().equals(Type.notebook)) {
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
        return chapterRepository.findByChannelIdAndType(channelId, recycle_bin.toString());
    }

    public Chapter shareChapter(String userId, String chapterId) {
        // create share type of chapter
        Chapter originChapter = chapterRepository.findByIdFetchJoin(chapterId)
                .orElseThrow(ResourceNotFoundException::new);
        Chapter newChapter = Chapter.createShareChapter(userId, originChapter);
        chapterRepository.save(newChapter);

        // get page list of chapter
        originChapter.getPageList().forEach(page -> pageService.sharePage(newChapter, page));

        return newChapter;
    }

}
