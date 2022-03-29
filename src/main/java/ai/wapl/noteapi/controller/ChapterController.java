package ai.wapl.noteapi.controller;

import java.util.List;

import ai.wapl.noteapi.dto.SearchDTO;
import ai.wapl.noteapi.util.ResponseUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import ai.wapl.noteapi.domain.Chapter;
import ai.wapl.noteapi.service.ChapterService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

import static ai.wapl.noteapi.util.Constants.DEFAULT_API_URI;
import static ai.wapl.noteapi.util.ResponseUtil.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = DEFAULT_API_URI + "/app/{channelId}")
public class ChapterController {
    private final ChapterService chapterService;
    private String userId = "userId";

    // TODO: noteappCreate
    // TODO: noteappDelete

    @ApiOperation(value = "채널 별 챕터 리스트 조회 noteChapterList ", notes = "채널 별 챕터 및 하위 페이지 리스트 조회 서비스")
    @GetMapping
    public ResponseEntity<ResponseDTO<List<Chapter>>> getChapterList(@PathVariable("channelId") String channelId) {
        List<Chapter> chapterList = chapterService.getChapterList(channelId);

        chapterList.forEach(chapter -> chapter.getPageList().forEach(page -> {
            page.setContent(null);
            page.setTextContent(null);
        }));

        return success(chapterList);
    }

    @ApiOperation(value = "단일 챕터 조회 chatpershareList ", notes = "채널 정보를 조회하는 서비스")
    @GetMapping(path = "/chapter/{chapterId}")
    public ResponseEntity<ResponseDTO<Chapter>> getChapterInfoList(@PathVariable("chapterId") String chapterId) {
        Chapter chapterInfo = chapterService.getChapterInfoList(chapterId);
        return success(chapterInfo);
    }

    @ApiOperation(value = "챕터 생성 서비스 notebooksCreate ", notes = "챕터 생성 서비스 ( 국제화 언어에 따라 챕터명 생성) ")
    @PostMapping(path = "/chapter/{language}")
    public ResponseEntity<ResponseDTO<Chapter>> createChapter(@RequestBody Chapter inputDTO, @PathVariable String language) {
        Chapter result = chapterService.createChapter(userId, inputDTO, language);
        return success(result);
    }

    @ApiOperation(value = "챕터 삭제 서비스 notebookDelete ", notes = "챕터 삭제 서비스")
    @DeleteMapping(path = "/chapter/{chapterId}")
    public ResponseEntity<?> deleteChapter(@PathVariable String channelId, @PathVariable String chapterId) {
        chapterService.deleteChapter(channelId, chapterId);
        return noContent();
    }

    @ApiOperation(value = "챕터 업데이트 서비스 notebooksUpdate ", notes = "챕터 업데이트 서비스")
    @PutMapping(path = "/chapter")
    public ResponseEntity<ResponseDTO<Chapter>> updateChapter(@RequestBody Chapter inputDTO) {
        Chapter result = chapterService.updateChapter(inputDTO);

        return success(result);
    }

    @ApiOperation(value = "챕터 전달 서비스 chaptershareCreate ", notes = "챕터 전달 서비스")
    @PostMapping(path = "/chapter/share")
    public ResponseEntity<ResponseDTO<Chapter>> shareChapter(@RequestBody Chapter inputDTO) {
        Chapter chapter = chapterService.shareChapter(userId, inputDTO.getId());
        return ResponseUtil.success(chapter);
    }

}
