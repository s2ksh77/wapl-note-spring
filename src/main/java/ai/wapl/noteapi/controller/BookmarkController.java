package ai.wapl.noteapi.controller;

import static ai.wapl.noteapi.util.Constants.DEFAULT_API_URI;

import ai.wapl.noteapi.domain.Bookmark;
import ai.wapl.noteapi.domain.Page;
import ai.wapl.noteapi.service.PageService;
import ai.wapl.noteapi.util.ResponseUtil;
import ai.wapl.noteapi.util.ResponseUtil.ResponseDTO;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = DEFAULT_API_URI)
public class BookmarkController {
  private final PageService pageService;
  // DEBUG
  private String userId = "caf1a998-c39e-49d4-81c7-719f6cc624d9";

  @ApiOperation(value = "즐겨찾기 등록 서비스. bookmarkCreate", notes = "즐겨찾기 등록 서비스")
  @PostMapping(path = "/page/{pageId}/bookmark")
  public ResponseEntity<ResponseDTO<Bookmark>> createBookmark(@PathVariable String pageId) {
    return ResponseUtil.success(pageService.createBookmark(userId, pageId));
  }

  @ApiOperation(value = "룸 별 즐겨찾기 조회 서비스 bookmarkList", notes = "룸 별 즐겨찾기 조회 서비스")
  @GetMapping(path = "/app/{channelId}/bookmark")
  public ResponseEntity<ResponseDTO<List<Page>>> getBookmarkInChannel(@PathVariable String channelId) {
    return ResponseUtil.success(pageService.getBookmark(userId, channelId));
  }

  @ApiOperation(value = "유저 별 즐겨찾기 조회 서비스 bookmarkList", notes = "유저 별 즐겨찾기 조회 서비스")
  @GetMapping(path = "/bookmark")
  public ResponseEntity<ResponseDTO<List<Page>>> getBookmark() {
    return ResponseUtil.success(pageService.getBookmark(userId, null));
  }

  @ApiOperation(value = "즐겨찾기 해제 서비스 bookmarkDelete", notes = "즐겨찾기 해제 서비스")
  @DeleteMapping(path = "/page/{pageId}/bookmark")
  public ResponseEntity<ResponseDTO<Bookmark>> deleteBookmark(@PathVariable String pageId) {
    pageService.deleteBookmark(userId, pageId);
    return ResponseUtil.noContent();
  }
}
