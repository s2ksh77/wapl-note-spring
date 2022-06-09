package ai.wapl.noteapi.service;

import ai.wapl.noteapi.domain.Bookmark;
import ai.wapl.noteapi.domain.File;
import ai.wapl.noteapi.domain.NoteLog;
import ai.wapl.noteapi.domain.NoteLog.LogAction;
import ai.wapl.noteapi.dto.SearchDTO;
import ai.wapl.noteapi.dto.SearchDTOinterface;
import ai.wapl.noteapi.dto.TagDTO;
import ai.wapl.noteapi.repository.BookmarkRepository;
import ai.wapl.noteapi.repository.LogRepository;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import ai.wapl.noteapi.domain.Chapter;
import ai.wapl.noteapi.domain.Page;
import ai.wapl.noteapi.domain.Tag;
import ai.wapl.noteapi.dto.PageDTO;
import ai.wapl.noteapi.repository.ChapterRepository;
import ai.wapl.noteapi.repository.PageRepository;
import ai.wapl.noteapi.util.NoteUtil;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static ai.wapl.noteapi.domain.Chapter.Type.RECYCLE_BIN;
import static ai.wapl.noteapi.domain.Chapter.Type.SHARED_PAGE;
import static ai.wapl.noteapi.dto.PageDTO.*;

@Service
@RequiredArgsConstructor
@Transactional
public class PageService {
    public static final String RESOURCE_TYPE = "page";

    private final ChapterRepository chapterRepository;
    private final PageRepository pageRepository;
    private final FileService fileService;
    private final TagService tagService;
    private final BookmarkRepository bookmarkRepository;
    private final LogRepository logRepository;

    /**
     * 페이지 단일 조회 서비스
     * tag mapping 리스트 및 file drive 리스트 조회
     */
    @Transactional(readOnly = true)
    public PageDTO getPageInfo(String userId, String pageId) {
        PageDTO result = pageRepository.findById(userId, pageId);
        result.setTagList(new ArrayList<>(tagService.getTagList(pageId)));
        result.setFileList(fileService.getFileListByPageId(pageId));

        return result;
    }

    /**
     * 페이지 추가 서비스 createdDate, modifiedDate
     */
    public PageDTO createPage(String userId, PageDTO inputPage, boolean mobile) {
        Chapter chapter = chapterRepository.findById(inputPage.getChapterId())
                .orElseThrow(ResourceNotFoundException::new);
        Page page = pageRepository.save(Page.createPage(chapter, inputPage.toEntity()));

        createPageLog(userId, page.getId(), LogAction.create, mobile);
        return Page.convertDTO(page);
    }

    Page createPage(Page inputPage, boolean mobile) {
        Page page = pageRepository.save(Page.createPage(inputPage.getChapter(), inputPage));
        createPageLog(inputPage.getCreatedUserId(), page.getId(), LogAction.create, mobile);
        return page;
    }

    public void deletePage(String userId, String channelId, List<PageDTO> inputList, boolean mobile) {
        inputList.forEach(page -> deletePage(userId, channelId, page, mobile));
    }

    public void deletePage(String userId, String channelId, PageDTO inputDTO, boolean mobile) {
        Page page = pageRepository.findById(inputDTO.getId()).orElseThrow(ResourceNotFoundException::new);
        fileService.deleteFileByPageId(channelId, inputDTO.getId());
        pageRepository.delete(page);

        createPageLog(userId, page.getId(), LogAction.delete, mobile);
    }

    public Page updatePage(String userId, PageDTO input, Action action, boolean mobile) {
        Page page = pageRepository.findById(input.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Not found Page."));

        if (!action.equals(Action.EDIT_DONE) && (page.isEditing() && !userId
                .equals(page.getEditingUserId()))) {
            throw new IllegalStateException("Can not access this page!");
        }

        switch (action) {
            case NON_EDIT:
                page.setEditingUserId(null);
                return page;
            case EDIT_START:
                page.setEditingUserId(userId);
                page.setUserName(getNotNull(input.getUserName(), page.getUserName()));
                createPageLog(userId, page.getId(), LogAction.edit_start, mobile);
                return page;
            case MOVE:
                page.setChapter(chapterRepository.findById(input.getChapterId())
                        .orElseThrow(ResourceNotFoundException::new));
                // page.setUserName(getNotNull(input.getUserName(), page.getUserName()));
                page.setModifiedDate(NoteUtil.now());
                page.setUpdatedUserId(userId);
                createPageLog(userId, page.getId(), LogAction.move, mobile);
                return page;
            case RENAME:
                page.setName(input.getName());
                page.setModifiedDate(NoteUtil.now());
                createPageLog(userId, page.getId(), LogAction.rename, mobile);
                return page;
            case EDITING:
                page.setName(getNotNull(input.getName(), page.getName()));
                page.setContent(getNotNull(input.getContent(), page.getContent()));
                page.setTextContent(getNotNull(input.getTextContent(), page.getTextContent()));
                page.setModifiedDate(NoteUtil.now());
                return page;
            case EDIT_DONE:
                page.setName(getNotNull(input.getName(), page.getName()));
                page.setContent(getNotNull(input.getContent(), page.getContent()));
                page.setTextContent(getNotNull(input.getTextContent(), page.getTextContent()));
                page.setModifiedDate(NoteUtil.now());
                page.setUserName(getNotNull(input.getUserName(), page.getUserName()));
                page.setUpdatedUserId(getNotNull(userId, page.getUpdatedUserId()));
                page.setEditingUserId(null);
                createPageLog(userId, page.getId(), LogAction.edit_done, mobile);
                return page;
            default:
                throw new IllegalArgumentException("Wrong Action");
        }
    }

    /**
     * 휴지통 관련 THROW 또는 RESTORE 관련 서비스
     */
    public void updateRecyclePage(String userId, String channelId, List<PageDTO> pageList, Action action,
            boolean mobile) {
        Chapter recycleBin = chapterRepository.findByChannelIdAndType(channelId, RECYCLE_BIN)
                .orElseThrow(ResourceNotFoundException::new);
        pageList.forEach(page -> {
            Page pageInfo = pageRepository.getById(page.getId());
            switch (action) {
                case THROW:
                    pageInfo.setChapter(recycleBin);
                    pageInfo.setShared(false);
                    pageInfo.setRestoreChapterId(page.getChapterId());
                    pageInfo.setDeletedDate(NoteUtil.now());
                    createPageLog(userId, page.getId(), LogAction.throw_to_recycle_bin, mobile);
                    return;
                case RESTORE:
                    pageInfo.setChapter(
                            chapterRepository.findById(page.getRestoreChapterId())
                                    .orElseThrow(ResourceNotFoundException::new));
                    pageInfo.setShared(false);
                    pageInfo.setRestoreChapterId(null);
                    pageInfo.setDeletedDate(null);
                    pageInfo.setModifiedDate(NoteUtil.now());
                    createPageLog(userId, page.getId(), LogAction.restore, mobile);
                    return;
                default:
                    throw new IllegalArgumentException("Wrong Action");
            }
        });
    }

    public Page sharePageToChapter(String userId, Chapter chapter, Page input, boolean mobile) {
        // create page with same content
        Page page = Page.createPage(chapter, input);
        pageRepository.save(page);

        // deep copy files of page
        fileService.copyFileListByPageId(chapter.getChannelId(), input.getId(), page.getId());
        createPageLog(userId, page.getId(), LogAction.create, mobile);
        return page;
    }

    public SearchDTO search(String channelId, String text) {
        SearchDTO output = new SearchDTO();

        if (text.equals("%"))
            text = "@%%";
        else if (text.equals("_"))
            text = "@__";

        output.setChapterList(pageRepository.searchChapter(channelId, text));
        output.setPageList(pageRepository.searchPage(channelId, text));
        output.setTagList(searchTagList(channelId, text));

        return output;
    }

    public List<PageDTO> searchTagList(String channelId, String text) {
        String lowerText = "%" + text.toLowerCase() + "%";

        List<SearchDTOinterface> tagList = pageRepository.searchTagInPage(channelId, lowerText);
        List<PageDTO> pageDTOs = new ArrayList<>();
        tagList.forEach(item -> {
            PageDTO dto = new PageDTO();

            dto.setId(item.getId());
            dto.setName(item.getChapterName()); // pageList에서 name 부분에 chapter name
            dto.setChapterId(item.getChapterId());
            dto.setTextContent(item.getName()); // pageList에서 textContent 부분에 page name
            dto.setChapterColor(item.getColor());
            dto.setChapterType(item.getType());

            List<TagDTO> tags = pageRepository.searchTag(channelId, lowerText, item.getId());
            List<Tag> result = new ArrayList<>();

            tags.forEach(tag -> {
                Tag convertTag = new Tag();
                convertTag.setId(tag.getId());
                convertTag.setName(tag.getName());
                result.add(convertTag);
            });
            dto.setTagList(result);
            pageDTOs.add(dto);
        });

        return pageDTOs;
    }

    public List<PageDTO> getRecentPageList(String userId, String channelId, int count) {
        return pageRepository.findByChannelIdOrderByModifiedDate(userId, channelId, count);
    }

    public List<PageDTO> getAllPageList(String userId, String channelId) {
        List<PageDTO> dtoList = pageRepository.findAllPageByChannelId(userId, channelId);
        Set<String> readSet = logRepository.getReadListByChannelId(userId, channelId);
        dtoList.forEach(pageDTO -> {
            pageDTO.setFileList(fileService.getFileListByPageId(pageDTO.getId()));
            pageDTO.setRead(readSet.contains(pageDTO.getId()));
        });
        return dtoList;
    }

    public long clearRecycleBin() {
        LocalDateTime targetDateTime = NoteUtil.now().minusDays(30);
        List<File> files = pageRepository.getFileInRecycleBin(targetDateTime);
        files.forEach(file -> fileService.deleteFileByPageId(file.getChannelId(), file.getFileId()));
        return pageRepository.deleteInRecycleBin(targetDateTime);
    }

    public long changeStateToUnLock() {
        LocalDateTime targetDateTime = NoteUtil.now().minusMinutes(30);

        logRepository.insertUnLockPageLog(targetDateTime);
        return pageRepository.updatePageToNonEdit(targetDateTime);
    }

    public Bookmark createBookmark(String userId, String pageId) {
        return bookmarkRepository.save(new Bookmark(userId, pageId));
    }

    public List<PageDTO> getBookmark(String userId, String channelId) {
        return channelId == null ? pageRepository.findBookmarkedPageByUser(userId)
                : pageRepository.findBookmarkedPageByChannel(userId, channelId);
    }

    public boolean isBookMark(String pageId, String userId) {
        return pageRepository.isBookMark(pageId, userId);
    }

    public Bookmark deleteBookmark(String userId, String pageId) {
        Bookmark bookmark = new Bookmark(userId, pageId);
        bookmarkRepository.delete(bookmark);
        return bookmark;
    }

    public Page sharePageToChannel(String userId, String channelId, String pageId, String sharedRoomId,
            boolean mobile) {
        Chapter sharedChapter = chapterRepository.findByChannelIdAndType(channelId, SHARED_PAGE)
                .orElseGet(() -> chapterRepository.save(Chapter.createShareChapter(userId, channelId)));

        Page sharedPage = pageRepository.save(
                Page.createSharedPage(userId, sharedChapter,
                        pageRepository.findById(pageId).orElseThrow(ResourceNotFoundException::new), sharedRoomId));

        fileService.copyFileListByPageId(channelId, pageId, sharedPage.getId());

        createPageLog(userId, sharedPage.getId(), LogAction.create, mobile);
        return sharedPage;
    }

    public long deleteAllByChannel(String channelId) {
        return pageRepository.deleteAllByChannelId(channelId);
    }

    private void createPageLog(String userId, String pageId, LogAction action, boolean mobile) {
        NoteLog log = new NoteLog("", userId, pageId, RESOURCE_TYPE, action, mobile);
        // logRepository.save(log);
    }

    private String getNotNull(String name, String name2) {
        return name != null ? name : name2;
    }

}
