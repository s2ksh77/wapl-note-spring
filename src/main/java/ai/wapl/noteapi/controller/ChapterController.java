package ai.wapl.noteapi.controller;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonView;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ai.wapl.noteapi.domain.Chapter;
import ai.wapl.noteapi.service.ChapterService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/note-api/chapter")
public class ChapterController {
    private final ChapterService chapterService;

    // @Autowired
    // public PageController(PageService pageService) {
    // this.pageService = pageService;
    // }
    @GetMapping(path = "/{channelId}")
    public ResponseEntity<List<Chapter>> getChapterList(@PathVariable("channelId") String channelId) {
        System.out.println("Request Method : GET " + channelId);
        List<Chapter> chapterList = chapterService.getChapterList(channelId);

        return ResponseEntity.ok().body(chapterList);
    }

    @GetMapping(path = "Info/{chapterId}")
    public ResponseEntity<Chapter> getChapterInfoList(@PathVariable("chapterId") String chapterId) {
        Chapter chapterInfo = chapterService.getChapterInfoList(chapterId);

        return ResponseEntity.ok().body(chapterInfo);
    }

}
