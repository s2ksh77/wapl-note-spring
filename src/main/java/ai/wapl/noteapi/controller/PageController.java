package ai.wapl.noteapi.controller;

import ai.wapl.noteapi.dto.SearchDTO;
import ai.wapl.noteapi.util.ResponseUtil;
import ai.wapl.noteapi.util.ResponseUtil.ResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import ai.wapl.noteapi.domain.Page;
import ai.wapl.noteapi.dto.PageDTO;
import ai.wapl.noteapi.service.PageService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static ai.wapl.noteapi.dto.PageDTO.*;
import static ai.wapl.noteapi.util.Constants.DEFAULT_API_URI;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = DEFAULT_API_URI + "/app/{channelId}")
public class PageController {

    private final PageService pageService;
    // DEBUG
    private String userId = "caf1a998-c39e-49d4-81c7-719f6cc624d9";

    // TODO: new 기능. pending
    // TODO: 공동 편집 기능. pending

    @ApiOperation(value = "최근 페이지 조회 서비스 noteRecentList & 채널 하위 모든 페이지 조회 서비스. allnoteList deprecated", notes = "count > 0면 최근 페이지 조회 서비스, else 전체 조회")
    @GetMapping("/page")
    public ResponseEntity<ResponseDTO<List<PageDTO>>> getRecentPageList(
        @PathVariable("channelId") String channelId,
        @RequestParam(value = "count", required = false) Integer count) {
        List<PageDTO> pageDTOS = count != null
            ? pageService.getRecentPageList(userId, channelId, count)
            : pageService.getAllPageList(userId, channelId);

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
    @PostMapping("/page")
    public ResponseEntity<ResponseDTO<Page>> createPage(@RequestBody PageDTO inputDTO) {
        Page result = pageService.createPage(inputDTO);
        return ResponseUtil.success(result);
    }

    @ApiOperation(value = "페이지 삭제 서비스 noteDelete ", notes = "페이지 삭제 서비스")
    @DeleteMapping(path = "/page/{pageId}")
    public ResponseEntity<ResponseDTO<Page>> deletePage(@PathVariable String channelId,
        @PathVariable String pageId) {
        Page result = pageService.deletePage(channelId, pageId);
        return ResponseUtil.success(result);
    }

    @ApiOperation(value = "페이지 업데이트 서비스 noteUpdate ", notes = "페이지 업데이트 서비스")
    @PutMapping("/page")
    public ResponseEntity<ResponseDTO<Page>> updatePage(@RequestBody PageDTO inputDTO,
        @RequestParam("action") Action action) {
        Page result = pageService.updatePage(userId, inputDTO, action);
        return ResponseUtil.success(result);
    }

    @ApiOperation(value = "휴지통 서비스 noteRecycleBinUpdate", notes = "휴지통 서비스")
    @PutMapping(path = "/page/recycle")
    public ResponseEntity<ResponseDTO<Page>> updateRecyclePage(@RequestBody PageDTO inputDTO,
        @RequestParam("action") Action action) {
        Page result = pageService.updateRecyclePage(inputDTO, action);
        return ResponseUtil.success(result);
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
    public ResponseEntity<ResponseDTO<Page>> sharePage(@RequestBody PageDTO dto) {
        return ResponseUtil.success(
            pageService.sharePageToChannel(userId, dto.getChannelId(), dto.getId(), dto.getSharedRoomId()));
    }
}
