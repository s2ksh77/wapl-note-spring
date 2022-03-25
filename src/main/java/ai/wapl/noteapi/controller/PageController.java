package ai.wapl.noteapi.controller;

import java.util.List;

import ai.wapl.noteapi.util.ResponseUtil;
import ai.wapl.noteapi.util.ResponseUtil.ResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import ai.wapl.noteapi.domain.Page;
import ai.wapl.noteapi.dto.PageDTO;
import ai.wapl.noteapi.service.PageService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

import static ai.wapl.noteapi.dto.PageDTO.*;
import static ai.wapl.noteapi.util.Constants.DEFAULT_API_URI;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = DEFAULT_API_URI + "/page")
public class PageController {

    private final PageService pageService;
    // DEBUG
    private String userId = "userId";

    // TODO: 최근 페이지 조회 서비스 noteRecentList
    // TODO: 채널 하위 모든 페이지 조회 서비스. allnoteList deprecated
    // TODO: 휴지통 비우기 서비스. 스케쥴링 noteRecycleBinDelete
    // TODO: unlock 서비스. 스케쥴링 UnlockUpdate
    // TODO: 페이지 전달 서비스 noteshareCreate
    // TODO: 즐겨찾기 등록 서비스. bookmarkCreate
    // TODO: 즐겨찾기 해제 서비스. bookmarkDelete
    // TODO: 즐겨찾기 조회 서비스. bookmarkList

    // @Autowired
    // public PageController(PageService pageService) {
    // this.pageService = pageService;
    // }
    @ApiOperation(value = "단일 페이지 정보 조회 서비스 noteinfoList ", notes = "단일 페이지 정보 조회 서비스")
    @GetMapping(path = "/{pageId}")
    public ResponseEntity<ResponseDTO<Page>> getPageInfoList(@PathVariable("pageId") String pageId) {
        System.out.println("Request Method : GET");
        Page pageInfo = pageService.getPageInfo(userId, pageId);
        return ResponseUtil.success(pageInfo);
    }

    @ApiOperation(value = "페이지 생성 서비스 noteCreate ", notes = "페이지 생성 서비스")
    @PostMapping
    public ResponseEntity<ResponseDTO<Page>> createPage(@RequestBody PageDTO inputDTO) {
        Page result = pageService.createPage(inputDTO);
        return ResponseUtil.success(result);
    }

    @ApiOperation(value = "페이지 삭제 서비스 noteDelete ", notes = "페이지 삭제 서비스")
    @DeleteMapping(path = "/{pageId}")
    public ResponseEntity<ResponseDTO<Page>> deletePage(@PathVariable String pageId) {
        Page result = pageService.deletePage(pageId);
        return ResponseUtil.success(result);
    }

    @ApiOperation(value = "페이지 업데이트 서비스 noteUpdate ", notes = "페이지 업데이트 서비스")
    @PutMapping
    public ResponseEntity<ResponseDTO<Page>> updatePage(@RequestBody PageDTO inputDTO, @RequestParam("action") Action action) {
        Page result = pageService.updatePage(userId, inputDTO, action);
        return ResponseUtil.success(result);
    }

    @ApiOperation(value = "휴지통 서비스 noteRecycleBinUpdate", notes = "휴지통 서비스")
    @PutMapping(path = "/recycle")
    public ResponseEntity<ResponseDTO<Page>> updateRecyclePage(@RequestBody PageDTO inputDTO, @RequestParam("action") Action action) {
        Page result = pageService.updateRecyclePage(inputDTO, action);
        return ResponseUtil.success(result);
    }
}
