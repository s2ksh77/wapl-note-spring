package ai.wapl.noteapi.service;

import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import ai.wapl.noteapi.domain.Chapter;
import ai.wapl.noteapi.domain.Page;
import ai.wapl.noteapi.dto.PageDTO;
import ai.wapl.noteapi.repository.ChapterRepository;
import ai.wapl.noteapi.repository.PageRepository;
import ai.wapl.noteapi.util.NoteUtil;
import org.springframework.transaction.annotation.Transactional;

import static ai.wapl.noteapi.domain.Page.PageType.*;
import static ai.wapl.noteapi.dto.PageDTO.Type.*;
import static ai.wapl.noteapi.util.Constants.*;

@Service
@RequiredArgsConstructor
@Transactional
public class PageService {
    private final PageRepository pageRepository;
    private final ChapterRepository chapterRepository;

    /**
     * 페이지 단일 조회 서비스
     * tag mapping 리스트 및 file drive 리스트 조회
     * 
     * @param pageId
     * @return
     */
    @Transactional(readOnly = true)
    public Page getPageInfo(String pageId) {
        // session 에서 userId 조회하는 부분 들어오면 userId 넣어야 함.
        Page result = pageRepository.findById(pageId, "6f30ca06-bff9-4534-aa13-727efb0a1f22");
        // result.setFavorite(isBookMark(pageId));
        return result;
    }

    // public Boolean isBookMark(String pageId) {
    // int count = pageRepository.findByIsBookMark(pageId,
    // "6f30ca06-bff9-4534-aa13-727efb0a1f22");
    // Boolean result = count > 0 ? true : false;
    // return result;
    // }

    /**
     * 페이지 추가 서비스
     * createdDate, modifiedDate
     * 
     * @param inputPage
     * @return
     */
    public Page createPage(Page inputPage) {
//        Page page = Page.createPage(inputPage);
//        Page result = pageRepository.save(page);
//
//        return result;
        return null;
    }

    /**
     * 페이지 삭제 서비스
     * 
     * @param inputList
     * @return
     */
    public Page deletePage(List<PageDTO> inputList) {
        Page output = new Page();

        // TODO 페이지 별 파일 조회해서 삭제 후 페이지 삭제하도록
        try {
            for (PageDTO page : inputList) {
                pageRepository.deleteById(page.getId());
            }
            output.setResultMsg("Success");
        } catch (Exception e) {
            System.out.println("Execption occur with Delete Page ::" + e);
            output.setResultMsg("Fail");
        }

        return output;
    }

    public Page updatePage(Page input) {
        Page page = pageRepository.findById(input.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Not found Page."));
        Page output = new Page();

        if (input.getType().equals(NONEDIT) && !page.getEditingUserId().isEmpty()
                && input.getUpdatedUserId().equals(page.getEditingUserId())) {
            page.setEditingUserId("");
            pageRepository.save(page);
            output.setResultMsg(RETURN_MSG_SUCCESS);
            return output;
        }

        if (input.getType().equals(EDIT_START) && !page.getEditingUserId().isEmpty()
                && !input.getUpdatedUserId().equals(page.getEditingUserId())) {
            output.setResultMsg(RETURN_MSG_FAIL);
            return output;
        }
        if (input.getType().equals(MOVE)) {
            page.setChapter(input.getChapter());
            page.setModifiedDate(NoteUtil.generateDate());
        } else if (input.getType().equals(RENAME) && page.getEditingUserId().isEmpty()) {
            page.setName(input.getName());
            page.setModifiedDate(NoteUtil.generateDate());
        } else if (input.getType().equals(EDIT_DONE)) {
            page.setName(getNotNull(input.getName(), page.getName()));
            page.setContent(getNotNull(input.getContent(), page.getContent()));
            page.setTextContent(getNotNull(input.getTextContent(), page.getTextContent()));
            page.setModifiedDate(NoteUtil.generateDate());
            page.setUserName(getNotNull(input.getUserName(), page.getUserName()));
            page.setUpdatedUserId(getNotNull(input.getUpdatedUserId(), page.getUpdatedUserId()));
        } else {
            output.setResultMsg(RETURN_MSG_FAIL);
            return output;
        }
        output.setResultMsg(RETURN_MSG_SUCCESS);
        pageRepository.save(page);
        return output;
    }

    private String getNotNull(String name, String name2) {
        return name != null ? name : name2;
    }

    /**
     * 휴지통 관련 THROW 또는 RESOTRE 관련 서비스
     * 
     * @param inputList
     * @return
     */
    public Page updateRecyclePage(List<PageDTO> inputList) {
        Page output = new Page();
        try {
            String type = inputList.get(0).getType();
            String channelId = inputList.get(0).getChannelId();
            Chapter recycleBin = chapterRepository.findByChannelIdAndType(channelId, "recycle_bin");
            for (PageDTO page : inputList) {
                Page pageInfo = pageRepository.getById(page.getId());
                if (type.equals(THROW)) {
                    pageInfo.setChapter(recycleBin);
                    pageInfo.setType("");
                    pageInfo.setRestoreChapterId(page.getRestoreChapterId());
                    pageInfo.setDeletedDate(NoteUtil.generateDate());
                } else if (type.equals(RESTORE)) {
                    Chapter inputChapter = new Chapter();
                    inputChapter.setId(page.getChapterId());
                    pageInfo.setChapter(inputChapter);
                    pageInfo.setType("");
                    pageInfo.setRestoreChapterId("");
                    pageInfo.setDeletedDate("");
                    pageInfo.setModifiedDate(NoteUtil.generateDate());
                }
//                pageRepository.save(pageInfo);
            }
            output.setResultMsg("Success");
        } catch (Exception e) {
            System.out.println("Execption occur with Delete Page ::" + e);
            output.setResultMsg("Fail");
        }

        return output;
    }

    // 전달받은 챕터, 전달받은 페이지
    // 1. 챕터 id로 페이지 조회
    // 2. 페이지 하위 첨부된 파일 조회
    // 3. 파일 삭제
    public int deleteFileInChapter(String chapterId) {
        return 0;
    }

    public Page sharePage(Chapter chapter, Page input) {
        // create page with same content
        Page page = Page.createPage(chapter, input);
        pageRepository.save(page);

        // deep copy files of page


        return page;
    }
}
