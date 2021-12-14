package ai.wapl.noteapi.service;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

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

        inputPage.setCreatedDate(NoteUtil.generateDate() + " Asia/Seoul");
        inputPage.setModifiedDate(NoteUtil.generateDate() + " Asia/Seoul");

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
            pageRepository.save(inputPage);
            output.setResultMsg("Success");
            return output;
        }

        if (inputPage.getType().equals("EDIT_START") && !pageInfo.getEditingUserId().isEmpty()
                && !inputPage.getUserId().equals(pageInfo.getEditingUserId())) {
            output.setResultMsg("Fail");
            return output;
        }
        if (inputPage.getType().equals("MOVE")
                || inputPage.getType().equals("RENAME") && pageInfo.getEditingUserId().isEmpty()
                || inputPage.getType().equals("EDIT_DONE")) {
            inputPage.setModifiedDate(NoteUtil.generateDate());
        } else {
            output.setResultMsg("Fail");
            return output;
        }
        output.setResultMsg("Success");
        pageRepository.save(inputPage);
        return output;
    }

    // public Page updatePage(Page inputPage) {
    // long count = 0;
    // String type = inputPage.getType();
    // if (type.equals("EDIT_START")) {
    // count = pageRepository.editStartPage(inputPage.getId(), inputPage.getName(),
    // inputPage.getChapterId(),
    // inputPage.getEditingUserId(), inputPage.getUserId());
    // if (count < 0)
    // throw new RuntimeException("Edit Start Failed");
    // } else if (type.equals("NONEIDT")) {
    // count = pageRepository.nonEditPage(inputPage.getId(),
    // inputPage.getChapterId(),
    // inputPage.getEditingUserId());
    // } else if (type.equals("MOVE") || type.equals("RENAME")) {
    // count = pageRepository.moveRenamePage(inputPage.getId(), inputPage.getName(),
    // inputPage.getChapterId(),
    // inputPage.getEditingUserId(), inputPage.getUserId(),
    // inputPage.getUserName());
    // } else if (type.equals("EDIT_DONE")) {
    // count = pageRepository.editDonePage(inputPage.getId(), inputPage.getName(),
    // inputPage.getContent(),
    // inputPage.getTextContent(), inputPage.getUserName(),
    // inputPage.getFavorite(), inputPage.getChapterId(),
    // inputPage.getEditingUserId(),
    // inputPage.getUserId());
    // }

    // Page result = new Page();
    // return result;
    // }

    // public Object getUpdateQuery(String type) {
    // if (type.equals("EDIT_START")) {
    // return pageRepository.editStartPage(id, name, chapterId, editingUserId,
    // userId);
    // } else if (type.equals("NONEIDT")) {
    // return pageRepository.nonEditPage(id, chapterId, editingUserId);
    // } else if (type.equals("MOVE") || type.equals("RENAME")) {
    // return pageRepository.moveRenamePage(id, name, chapterId, editingUserId,
    // userId, userName);
    // } else if (type.equals("EDIT_DONE")) {
    // return pageRepository.editDonePage(id, name, content, textContent, userName,
    // favorite, chapterId, editingUserId, userId);
    // }

    // }
}
