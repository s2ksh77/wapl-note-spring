package ai.wapl.noteapi.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ai.wapl.noteapi.domain.Page;
import ai.wapl.noteapi.service.TagService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/note-api")
public class TagController {
    private final TagService tagService;

    @GetMapping(path = "page-tag/{pageId}")
    public ResponseEntity<Page> getTagList(@PathVariable("pageId") String pageId) {
        Page result = tagService.getTagList(pageId);
        System.out.println(result);
        return ResponseEntity.ok().body(result);
    }
}
