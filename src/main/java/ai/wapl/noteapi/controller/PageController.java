package ai.wapl.noteapi.controller;

import ai.wapl.noteapi.dto.SearchDTO;
import ai.wapl.noteapi.util.NoteUtil;
import ai.wapl.noteapi.util.Notifier;
import ai.wapl.noteapi.util.Notifier.Method;
import ai.wapl.noteapi.util.ResponseUtil;
import ai.wapl.noteapi.util.ResponseUtil.ResponseDTO;
import ai.wapl.noteapi.util.ServiceCaller;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import ai.wapl.noteapi.domain.Page;
import ai.wapl.noteapi.dto.PageDTO;
import ai.wapl.noteapi.service.PageService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import static ai.wapl.noteapi.dto.PageDTO.*;
import static ai.wapl.noteapi.util.Constants.DEFAULT_API_URI;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = DEFAULT_API_URI + "/app/{channelId}")
public class PageController {

    private final PageService pageService;
    // DEBUG
    private String userId = "caf1a998-c39e-49d4-81c7-719f6cc624d9";

    // TODO: 공동 편집 기능. pending

    @ApiOperation(value = "채널 하위 모든 페이지 조회 서비스. allnoteList (deprecated)", notes = "전체 조회")
    @GetMapping("/page/all")
    public ResponseEntity<ResponseDTO<List<PageDTO>>> getAllPageList(
            @PathVariable("channelId") String channelId) {
        List<PageDTO> pageDTOS = pageService.getAllPageList(userId, channelId);

        return ResponseUtil.success(pageDTOS);
    }

    @ApiOperation(value = "최근 페이지 조회 서비스 noteRecentList", notes = "최근 페이지 조회 서비스")
    @GetMapping("/page")
    public ResponseEntity<ResponseDTO<List<PageDTO>>> getRecentPageList(
            @PathVariable("channelId") String channelId,
            @RequestParam(value = "count", defaultValue = "5") Integer count) {
        List<PageDTO> pageDTOS = pageService.getRecentPageList(userId, channelId, count);

        return ResponseUtil.success(pageDTOS);
    }

    @ApiOperation(value = "단일 페이지 정보 조회 서비스 noteinfoList ", notes = "단일 페이지 정보 조회 서비스")
    @GetMapping(path = "/page/{pageId}")
    public ResponseEntity<ResponseDTO<PageDTO>> getPageInfoList(
            @PathVariable("pageId") String pageId) {
        PageDTO pageInfo = pageService.getPageInfo(userId, pageId);
        return ResponseUtil.success(pageInfo);
    }

    @ApiOperation(value = "페이지 생성 서비스 noteCreate ", notes = "페이지 생성 서비스")
    @PostMapping("/chapter/{chapterId}/page")
    public ResponseEntity<ResponseDTO<Page>> createPage(@PathVariable String channelId,
            @PathVariable String chapterId,
            @RequestBody PageDTO inputDTO, @RequestHeader("user-agent") String userAgent) {
        Page result = pageService.createPage(userId, inputDTO, NoteUtil.isMobile(userAgent));

        Notifier notifier = new Notifier(userId, channelId, Method.CREATE,
                NoteUtil.isMobile(userAgent));
        notifier.publishMQTT(chapterId, result.getId(), result.getName());

        return ResponseUtil.success(result);
    }

    @ApiOperation(value = "페이지 삭제 서비스 noteDelete ", notes = "페이지 삭제 서비스")
    @PostMapping(path = "/page")
    public ResponseEntity<ResponseDTO<List<PageDTO>>> deletePage(@PathVariable String channelId,
            @RequestBody List<PageDTO> inputList, @RequestHeader("user-agent") String userAgent) {
        pageService.deletePage(userId, channelId, inputList, NoteUtil.isMobile(userAgent));

        // TODO:// 삭제 noti
        // Notifier notifier = new Notifier(userId, channelId, Method.DELETE,
        // NoteUtil.isMobile(userAgent));
        // notifier.publishMQTT(chapterId, result.getId(), result.getName());

        return ResponseUtil.noContent();
    }

    @ApiOperation(value = "페이지 업데이트 서비스 noteUpdate ", notes = "페이지 업데이트 서비스")
    @PutMapping("/chapter/{chapterId}/page")
    public ResponseEntity<ResponseDTO<Page>> updatePage(@PathVariable String channelId,
            @PathVariable String chapterId,
            @RequestBody PageDTO inputDTO,
            @RequestParam("action") Action action, @RequestParam(required = false) boolean isNewPage,
            @RequestHeader("user-agent") String userAgent) {
        boolean mobile = NoteUtil.isMobile(userAgent);
        Page result = pageService.updatePage(userId, inputDTO, action, mobile);

        Notifier notifier = new Notifier(userId, channelId, Method.valueOf(action), mobile);

        if (action.equals(Action.EDIT_DONE) && isNewPage)
            notifier.setAlarmCenter(result.getId(), result.getName());
        notifier.publishMQTT(chapterId, result.getId(), result.getName());

        return ResponseUtil.success(result);
    }

    @ApiOperation(value = "휴지통 서비스 noteRecycleBinUpdate", notes = "휴지통 서비스")
    @PutMapping(path = "/page/recycle")
    public ResponseEntity<ResponseDTO<?>> updateRecyclePage(@PathVariable String channelId,
            @RequestBody List<PageDTO> inputDTO,
            @RequestParam("action") Action action, @RequestHeader("user-agent") String userAgent) {
        boolean mobile = NoteUtil.isMobile(userAgent);
        pageService.updateRecyclePage(userId, channelId, inputDTO, action, mobile);

        // TODO: 리스트로 휴지통 보낸 데이터 notifier 연결

        // Notifier notifier = new Notifier(userId, channelId, Method.valueOf(action),
        // mobile);
        // notifier.publishMQTT(result.getChapter().getId(), result.getId(),
        // result.getName());
        return ResponseUtil.success();
    }

    @ApiOperation(value = "노트 통합검색 서비스 noteSearchList", notes = "노트 통합검색 서비스")
    @GetMapping(path = "/search")
    public ResponseEntity<ResponseUtil.ResponseDTO<SearchDTO>> searchAll(
            @PathVariable String channelId, @RequestParam("text") String text) {
        SearchDTO output = pageService.search(channelId, text);
        return ResponseUtil.success(output);
    }

    @ApiOperation(value = "페이지 전달 서비스 noteshareCreate", notes = "페이지 전달 서비스")
    @PostMapping("/page/copy")
    public ResponseEntity<ResponseDTO<Page>> sharePage(@RequestBody PageDTO dto,
            @RequestHeader("user-agent") String userAgent) {
        boolean mobile = NoteUtil.isMobile(userAgent);
        Page page = pageService
                .sharePageToChannel(userId, dto.getChannelId(), dto.getId(), dto.getSharedRoomId(),
                        mobile);

        ServiceCaller caller = new ServiceCaller();
        caller.createTalkMeta("", userId, page.getId(), page.getName(), page.getShared(),
                NoteUtil.dateToString(page.getModifiedDate()));

        Notifier notifier = new Notifier(userId, dto.getChannelId(), Method.SHAREPAGE, mobile);
        notifier.publishMQTT(page.getChapter().getId(), page.getId(), page.getName());
        return ResponseUtil.success(page);
    }
}
