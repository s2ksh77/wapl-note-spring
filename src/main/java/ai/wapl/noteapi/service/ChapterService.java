package ai.wapl.noteapi.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import ai.wapl.noteapi.domain.Chapter;
import ai.wapl.noteapi.domain.Page;
import ai.wapl.noteapi.dto.ChapterDTO;
import ai.wapl.noteapi.repository.ChapterRepository;
import ai.wapl.noteapi.util.NoteUtil;

import static ai.wapl.noteapi.util.Constants.RECYCLE_BIN;

@Service
public class ChapterService {

    private final ChapterRepository chapterRepository;
    private final PageService pageService;

    @Autowired
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
        pageDTO.setUpdatedUserId(inputChapter.getUserId());
        pageDTO.setUserName(inputChapter.getUserName());
        pageDTO.setContent("<p></br></p>");
        pageDTO.setName(language.equals("ko") ? "새 페이지" : "New Page");

        List<Page> pageList = new ArrayList<>();
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
            System.out.println("Execption occur with Delete Chapter ::" + e);
            result.setResultMsg("Fail");
        }
        return result;
    }

    /**
     * 챕터 수정 서비스
     * 챕터 이름, 챕터 색상(기획엔 없음)
     */
    public Chapter updateChapter(Chapter inputDTO) {
        Chapter chapterInfo = chapterRepository.findById(inputDTO.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Not found Chapter."));
        Chapter result = new Chapter();

        try {
            if (inputDTO.getName() != null && inputDTO.getColor() == null) {
                chapterInfo.setName(inputDTO.getName());
                chapterInfo.setModifiedDate(NoteUtil.generateDate());
            } else if (inputDTO.getName() == null && inputDTO.getColor() != null) {
                chapterInfo.setColor(inputDTO.getColor());
                chapterInfo.setModifiedDate(NoteUtil.generateDate());
            }
            chapterRepository.save(chapterInfo);
            result = chapterInfo;
            result.setResultMsg("Success");
        } catch (Exception e) {
            System.out.println("Execption occur with Update Chapter ::" + e);
            result.setResultMsg("Fail");
        }
        return result;
    }

    /**
     * @param channelId
     * @return RecycleBinId
     */
    public Chapter getRecycleBin(String channelId) {
        Chapter result = chapterRepository.findByChannelIdAndType(channelId, RECYCLE_BIN);
        return result;
    }
}
