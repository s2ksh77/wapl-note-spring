package ai.wapl.noteapi.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import ai.wapl.noteapi.controller.PageController;
import ai.wapl.noteapi.domain.Chapter;
import ai.wapl.noteapi.domain.Page;
import ai.wapl.noteapi.dto.ChapterDTO;
import ai.wapl.noteapi.dto.PageDTO;
import ai.wapl.noteapi.repository.ChapterRepository;
import ai.wapl.noteapi.repository.PageRepository;
import ai.wapl.noteapi.util.NoteUtil;

@Service
public class ChapterService {

    @Autowired
    private final ChapterRepository chapterRepository;

    private final PageService pageService;

    public ChapterService(ChapterRepository chapterRepository, PageService pageService) {
        this.chapterRepository = chapterRepository;
        this.pageService = pageService;
    }

    /**
     * 챕터 전체 조회 서비스
     * 
     * 
     * @param channelId
     * @return
     */

    public List<Chapter> getChapterList(String channelId) {
        List<Chapter> result = chapterRepository.findByChannelId(channelId);
        return result;
    }

    /**
     * 챕터 하위 페이지 조회 서비스
     * 
     * 
     * @param chapterId
     * @return
     */
    public Chapter getChapterInfoList(String chapterId) {
        Chapter result = chapterRepository.findById(chapterId)
                .orElseThrow(() -> new ResourceNotFoundException("Not found Chapter"));

        return result;
    }

    /**
     * 챕터 추가 서비스
     * createdDate, modifiedDate
     * 
     * @param inputChapter
     * @return
     */
    public Chapter createChapter(Chapter inputChapter, String language) {

        inputChapter.setModifiedDate(NoteUtil.generateDate());
        Chapter result = chapterRepository.save(inputChapter);

        Page pageDTO = new Page();
        pageDTO.setChapter(result);
        pageDTO.setUserId(inputChapter.getUserId());
        pageDTO.setUserName(inputChapter.getUserName());
        pageDTO.setContent("<p></br></p>");
        pageDTO.setName(language.equals("ko") ? "새 페이지" : "New Page");

        List<Page> pageList = new ArrayList<Page>();
        pageList.add(pageService.createPage(pageDTO));

        result.setPageList(pageList);

        return result;
    }

    /**
     * 챕터 삭제 서비스
     * 하위 페이지는 휴지통으로 이동 되고 챕터는 삭제 된다.
     */
    public Chapter deleteChapter(List<ChapterDTO> chapterList) {
        Chapter result = new Chapter();
        try {
            for (ChapterDTO chapter : chapterList) {
                int output = chapterRepository.updateRecycleBin(chapter.getId(), chapter.getChannelId(),
                        NoteUtil.generateDate());
                if (output > 0) {
                    chapterRepository.deleteById(chapter.getId());
                }
            }
            result.setResultMsg("Success");
        } catch (Exception e) {
            System.out.println("Execption occur with Delete Page ::" + e);
            result.setResultMsg("Fail");
        }
        return result;
    }

    /**
     * @param channelId
     * @return RecycleBinId
     */

    public Chapter getRecycleBinId(String channelId) {
        Chapter result = chapterRepository.findByChannelIdAndType(channelId, "recycle_bin");
        return result;
    }
}
