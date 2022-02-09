package ai.wapl.noteapi.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ai.wapl.noteapi.domain.Chapter;
import ai.wapl.noteapi.dto.ChapterDTO;
import ai.wapl.noteapi.service.ChapterService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/note-api")
public class ChapterController {
    @Autowired
    private final ChapterService chapterService;

    @ApiOperation(value = "채널 별 챕터 리스트 조회", notes = "채널 별 챕터 및 하위 페이지 리스트 조회 서비스")
    @GetMapping(path = "app/{channelId}")
    public ResponseEntity<List<Chapter>> getChapterList(@PathVariable("channelId") String channelId) {
        List<Chapter> chapterList = chapterService.getChapterList(channelId);

        chapterList.forEach(chapter -> chapter.getPageList().forEach(page -> {
            page.setContent(null);
            page.setTextContent(null);
        }));

        return ResponseEntity.ok().body(chapterList);
    }

    @ApiOperation(value = "단일 챕터 조회", notes = "채널 정보를 조회하는 서비스")
    @GetMapping(path = "chapter/{chapterId}")
    public ResponseEntity<Chapter> getChapterInfoList(@PathVariable("chapterId") String chapterId) {
        Chapter chapterInfo = chapterService.getChapterInfoList(chapterId);

        return ResponseEntity.ok().body(chapterInfo);
    }

    @ApiOperation(value = "챕터 생성 서비스", notes = "챕터 생성 서비스 ( 국제화 언어에 따라 챕터명 생성) ")
    @PostMapping(path = "/chapter/{language}")
    public ResponseEntity<Chapter> createChapter(@RequestBody Chapter inputDTO, @PathVariable String language) {
        Chapter result = chapterService.createChapter(inputDTO, language);

        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @ApiOperation(value = "챕터 삭제 서비스", notes = "챕터 삭제 서비스")
    @PostMapping(path = "chapterDelete")
    public ResponseEntity<Chapter> deleteChapter(@RequestBody List<ChapterDTO> chapterList) {
        Chapter result = chapterService.deleteChapter(chapterList);

        return ResponseEntity.ok().body(result);
    }

    @ApiOperation(value = "챕터 업데이트 서비스", notes = "챕터 업데이트 서비스")
    @PostMapping(path = "chapterUpdate")
    public ResponseEntity<Chapter> updateChapter(@RequestBody Chapter inputDTO) {
        Chapter result = chapterService.updateChapter(inputDTO);

        return ResponseEntity.ok().body(result);
    }

}
