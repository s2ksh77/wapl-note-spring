package ai.wapl.noteapi.controller;

import ai.wapl.noteapi.dto.SearchDTO;
import ai.wapl.noteapi.service.PageService;
import ai.wapl.noteapi.util.ResponseUtil;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static ai.wapl.noteapi.util.Constants.DEFAULT_API_URI;

@RestController
@RequestMapping(path = DEFAULT_API_URI)
public class SearchController {
    private final PageService pageService;

    public SearchController(PageService pageService) {
        this.pageService = pageService;
    }

    @ApiOperation(value = "노트 통합검색 서비스 noteSearchList", notes = "노트 통합검색 서비스")
    @GetMapping(path = "app/{channelId}/search")
    public ResponseEntity<ResponseUtil.ResponseDTO<SearchDTO>> searchAll(@PathVariable String channelId, @RequestParam("text") String text) {
        SearchDTO output = pageService.search(channelId, text);
        return ResponseUtil.success(output);
    }

}
