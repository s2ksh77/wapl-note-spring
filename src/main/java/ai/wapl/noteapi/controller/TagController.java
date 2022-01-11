package ai.wapl.noteapi.controller;

import java.util.List;
import java.util.Map;

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
import ai.wapl.noteapi.domain.Tag;
import ai.wapl.noteapi.dto.TagDTO;
import ai.wapl.noteapi.service.TagService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/note-api")
public class TagController {
    private final TagService tagService;

    @GetMapping(path = "tag/{channelId}")
    public ResponseEntity<Map<String, Map<String, List<Tag>>>> getAllTagList(
            @PathVariable("channelId") String channelId) {
        Map<String, Map<String, List<Tag>>> result = tagService.getAllTagList(channelId);

        return ResponseEntity.ok().body(result);
    }

    @GetMapping(path = "page-tag/{pageId}")
    public ResponseEntity<Page> getTagList(@PathVariable("pageId") String pageId) {
        Page result = tagService.getTagList(pageId);

        return ResponseEntity.ok().body(result);
    }

    @PostMapping(path = "tag")
    public ResponseEntity<Tag> createTag(@RequestBody List<TagDTO> inputDTO) {
        Tag result = tagService.createTag(inputDTO);

        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @PostMapping(path = "tag/Delete")
    public ResponseEntity<Tag> deleteTag(@RequestBody List<TagDTO> inputDTO) {
        Tag result = tagService.deleteTag(inputDTO);

        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }
}
