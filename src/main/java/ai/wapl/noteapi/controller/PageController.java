package ai.wapl.noteapi.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ai.wapl.noteapi.domain.Page;
import ai.wapl.noteapi.dto.PageDTO;
import ai.wapl.noteapi.service.PageService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

import static ai.wapl.noteapi.util.Constants.DEFAULT_API_URI;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = DEFAULT_API_URI + "/page")
public class PageController {

    private final PageService pageService;

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
    public ResponseEntity<Page> getPageInfoList(@PathVariable("pageId") String pageId) {
        System.out.println("Request Method : GET");
        Page pageInfo = pageService.getPageInfo(pageId);
        return ResponseEntity.ok().body(pageInfo);
    }

    @ApiOperation(value = "페이지 생성 서비스 noteCreate ", notes = "페이지 생성 서비스")
    @PostMapping
    public ResponseEntity<Page> createPage(@RequestBody Page inputDTO) {
        System.out.println(inputDTO);
        // return ResponseEntity.ok().body(inputDTO);
        Page result = pageService.createPage(inputDTO);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @ApiOperation(value = "페이지 삭제 서비스 noteDelete ", notes = "페이지 삭제 서비스")
    @DeleteMapping(path = "/Delete")
    public ResponseEntity<Page> deletePage(@RequestBody List<PageDTO> pageList) {
        System.out.println(pageList);
        Page result = pageService.deletePage(pageList);
        System.out.println(result);
        return ResponseEntity.ok().body(result);
    }

    @ApiOperation(value = "페이지 업데이트 서비스 noteUpdate ", notes = "페이지 업데이트 서비스")
    @PostMapping(path = "/Update")
    public ResponseEntity<Page> updatePage(@RequestBody Page inputDTO) {
        System.out.println(inputDTO);
        Page result = pageService.updatePage(inputDTO);
        return ResponseEntity.ok().body(result);
        // return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @ApiOperation(value = "휴지통 서비스 noteRecycleBinUpdate", notes = "휴지통 서비스")
    @PostMapping(path = "/RecycleUpdate")
    public ResponseEntity<Page> updateRecyclePage(@RequestBody List<PageDTO> inputDTO) {
        System.out.println(inputDTO);
        Page result = pageService.updateRecyclePage(inputDTO);
        return ResponseEntity.ok().body(result);
        // return new ResponseEntity<>(result, HttpStatus.CREATED);
    }
}
