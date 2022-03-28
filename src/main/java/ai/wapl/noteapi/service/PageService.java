package ai.wapl.noteapi.service;

import ai.wapl.noteapi.dto.SearchDTO;
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

import static ai.wapl.noteapi.domain.Chapter.Type.recycle_bin;
import static ai.wapl.noteapi.dto.PageDTO.*;

@Service
@RequiredArgsConstructor
@Transactional
public class PageService {
    private final ChapterRepository chapterRepository;
    private final PageRepository pageRepository;
    private final FileService fileService;

    /**
     * 페이지 단일 조회 서비스
     * tag mapping 리스트 및 file drive 리스트 조회
     */
    @Transactional(readOnly = true)
    public PageDTO getPageInfo(String userId, String pageId) {
        PageDTO result = pageRepository.findById(userId, pageId);
        result.setFileList(fileService.getFileListByPageId(pageId));
        return result;
    }

    /**
     * 페이지 추가 서비스
     * createdDate, modifiedDate
     */
    public Page createPage(PageDTO inputPage) {
        Chapter chapter = chapterRepository.findById(inputPage.getChapterId()).orElseThrow(ResourceNotFoundException::new);
        Page page = Page.createPage(chapter, inputPage.toEntity());
        return pageRepository.save(page);
    }

    /**
     * 페이지 추가 서비스
     * createdDate, modifiedDate
     */
    public Page createPage(Page inputPage) {
        Page page = Page.createPage(inputPage.getChapter(), inputPage);
        return pageRepository.save(page);
    }

    /**
     * 페이지 삭제 서비스
     */
    public Page deletePage(String pageId) {
        Page page = pageRepository.findById(pageId).orElseThrow(ResourceNotFoundException::new);
        fileService.deleteFileByPageId(pageId);
        pageRepository.delete(page);
        return page;
    }

    public Page updatePage(String userId, PageDTO input, Action action) {
        Page page = pageRepository.findById(input.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Not found Page."));
        if (!action.equals(Action.EDIT_DONE) && (page.isEditing() || !userId.equals(page.getEditingUserId())))
            throw new IllegalStateException("Can not access this page!");

        switch (action) {
            case NON_EDIT:
                page.setEditingUserId(null);
                return page;
            case EDIT_START:
                page.setEditingUserId(userId);
                page.setUserName(getNotNull(input.getUserName(), page.getUserName()));
                return page;
            case MOVE:
                page.setChapter(chapterRepository.findById(input.getChapterId())
                        .orElseThrow(ResourceNotFoundException::new));
                page.setUserName(getNotNull(input.getUserName(), page.getUserName()));
                page.setModifiedDate(NoteUtil.generateDate());
                page.setUpdatedUserId(userId);
                return page;
            case RENAME:
                page.setName(input.getName());
                page.setModifiedDate(NoteUtil.generateDate());
                return page;
            case EDIT_DONE:
                page.setName(getNotNull(input.getName(), page.getName()));
                page.setContent(getNotNull(input.getContent(), page.getContent()));
                page.setTextContent(getNotNull(input.getTextContent(), page.getTextContent()));
                page.setModifiedDate(NoteUtil.generateDate());
                page.setUserName(getNotNull(input.getUserName(), page.getUserName()));
                page.setUpdatedUserId(getNotNull(userId, page.getUpdatedUserId()));
                page.setEditingUserId(null);
                return page;
            default:
                throw new IllegalArgumentException("Wrong Action");
        }
    }

    /**
     * 휴지통 관련 THROW 또는 RESTORE 관련 서비스
     */
    public Page updateRecyclePage(PageDTO page, Action action) {
        String channelId = page.getChannelId();
        Chapter recycleBin = chapterRepository.findByChannelIdAndType(channelId, recycle_bin.toString());
        Page pageInfo = pageRepository.getById(page.getId());
        switch (action) {
            case THROW:
                pageInfo.setChapter(recycleBin);
                pageInfo.setType(null);
                pageInfo.setRestoreChapterId(page.getRestoreChapterId());
                pageInfo.setDeletedDate(NoteUtil.generateDate());
                return pageInfo;
            case RESTORE:
                pageInfo.setChapter(
                        chapterRepository.findById(page.getChapterId())
                                .orElseThrow(ResourceNotFoundException::new)
                );
                pageInfo.setType(null);
                pageInfo.setRestoreChapterId(null);
                pageInfo.setDeletedDate(null);
                pageInfo.setModifiedDate(NoteUtil.generateDate());
                return pageInfo;
            default:
                throw new IllegalArgumentException("Wrong Action");
        }
    }

    public Page sharePage(Chapter chapter, Page input) {
        // create page with same content
        Page page = Page.createPage(chapter, input);
        pageRepository.save(page);

        // deep copy files of page
        fileService.copyFileListByPageId(input.getId(), page.getId());

        return page;
    }

    public SearchDTO search(String channelId, String text) {
        SearchDTO output = new SearchDTO();

        if(text.equals("%")) text = "@%%";
        else if(text.equals("_")) text = "@__";
        output.setChapterList(pageRepository.searchChapter(channelId, text));
        output.setPageList(pageRepository.searchPage(channelId, text));
        output.setTagList(pageRepository.searchTag(channelId, text));

        return output;
    }

    private String getNotNull(String name, String name2) {
        return name != null ? name : name2;
    }

}
