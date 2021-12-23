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

import ai.wapl.noteapi.domain.Chapter;
import ai.wapl.noteapi.service.ChapterService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/note-api")
public class ChapterController {
    private final ChapterService chapterService;

    @GetMapping(path = "app/{channelId}")
    public ResponseEntity<List<Chapter>> getChapterList(@PathVariable("channelId") String channelId) {
        List<Chapter> chapterList = chapterService.getChapterList(channelId);

        chapterList.forEach(chapter -> chapter.getPageList().forEach(page -> {
            page.setContent(null);
            page.setTextContent(null);
        }));

        return ResponseEntity.ok().body(chapterList);
    }

    @GetMapping(path = "chapter/{chapterId}")
    public ResponseEntity<Chapter> getChapterInfoList(@PathVariable("chapterId") String chapterId) {
        Chapter chapterInfo = chapterService.getChapterInfoList(chapterId);

        return ResponseEntity.ok().body(chapterInfo);
    }

    @PostMapping(path = "/chapter/{language}")
    public ResponseEntity<Chapter> createChapter(@RequestBody Chapter inputDTO, @PathVariable String language) {

        Chapter result = chapterService.createChapter(inputDTO, language);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

}
