package ai.wapl.noteapi.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import ai.wapl.noteapi.domain.Page;
import ai.wapl.noteapi.dto.PageDTO;
import ai.wapl.noteapi.repository.PageRepository;
import ai.wapl.noteapi.util.NoteUtil;

@Service
public class PageService {
    @Autowired
    private final PageRepository pageRepository;

    public PageService(PageRepository pageRepository) {
        this.pageRepository = pageRepository;
    }

    /**
     * 페이지 단일 조회 서비스
     * tag mapping 리스트 및 file drive 리스트 조회
     * 
     * @param pageId
     * @return
     */
    public Page getPageInfo(String pageId) {
        Page result = pageRepository.findById(pageId)
                .orElseThrow(() -> new ResourceNotFoundException("Not found Page"));
        return result;
    }

    /**
     * 페이지 추가 서비스
     * createdDate, modifiedDate
     * 
     * @param inputPage
     * @return
     */
    public Page createPage(Page inputPage) {

        inputPage.setCreatedDate(NoteUtil.generateDate());
        inputPage.setModifiedDate(NoteUtil.generateDate());

        Page result = pageRepository.save(inputPage);

        return result;
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

    public Page updatePage(Page inputPage) {
        Page pageInfo = pageRepository.findById(inputPage.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Not found Page."));
        Page output = new Page();

        if (inputPage.getType().equals("NONEDIT") && !pageInfo.getEditingUserId().isEmpty()
                && inputPage.getUserId().equals(pageInfo.getEditingUserId())) {
            pageInfo.setEditingUserId("");
            pageRepository.save(pageInfo);
            output.setResultMsg("Success");
            return output;
        }

        if (inputPage.getType().equals("EDIT_START") && !pageInfo.getEditingUserId().isEmpty()
                && !inputPage.getUserId().equals(pageInfo.getEditingUserId())) {
            output.setResultMsg("Fail");
            return output;
        }
        if (inputPage.getType().equals("MOVE")) {
            pageInfo.setChapter(inputPage.getChapter());
            pageInfo.setModifiedDate(NoteUtil.generateDate());
        } else if (inputPage.getType().equals("RENAME") && pageInfo.getEditingUserId().isEmpty()) {
            pageInfo.setName(inputPage.getName());
            pageInfo.setModifiedDate(NoteUtil.generateDate());
        } else if (inputPage.getType().equals("EDIT_DONE")) {
            pageInfo.setName(inputPage.getName() != null ? inputPage.getName() : pageInfo.getName());
            pageInfo.setContent(inputPage.getContent() != null ? inputPage.getContent() : pageInfo.getContent());
            pageInfo.setTextContent(
                    inputPage.getTextContent() != null ? inputPage.getTextContent() : pageInfo.getTextContent());
            pageInfo.setModifiedDate(NoteUtil.generateDate());
            pageInfo.setUserName(inputPage.getUserName() != null ? inputPage.getUserName() : pageInfo.getUserName());
            pageInfo.setUserId(inputPage.getUserId() != null ? inputPage.getUserId() : pageInfo.getUserId());
        } else if (inputPage.getType().equals("THROW")) {
            pageInfo.setChapter(inputPage.getChapter());
            pageInfo.setType("");
            pageInfo.setRestoreChapterId(inputPage.getRestoreChapterId());
            pageInfo.setDeletedDate(NoteUtil.generateDate());
        } else {
            output.setResultMsg("Fail");
            return output;
        }
        output.setResultMsg("Success");
        pageRepository.save(pageInfo);
        return output;
    }
}
