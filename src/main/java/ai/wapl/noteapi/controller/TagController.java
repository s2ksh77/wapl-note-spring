package ai.wapl.noteapi.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/note-api")
public class TagController {
    private final TagService tagService;

    @ApiOperation(value = "태그 전체 조회 서비스", notes = "[KOR : [tagList], ENG : [tagList], NUM : [tagList], ETC : [tagList] 데이터 반환")
    @GetMapping(path = "tag/{channelId}")
    public ResponseEntity<Map<String, Map<String, List<Tag>>>> getAllTagList(
            @PathVariable("channelId") String channelId) {
        Map<String, Map<String, List<Tag>>> result = tagService.getAllTagList(channelId);

        return ResponseEntity.ok().body(result);
    }

    @ApiOperation(value = "페이지 하위 태그 리스트 조회 서비스", notes = "페이지 하위 태그 리스트 조회 서비스")
    @GetMapping(path = "page-tag/{pageId}")
    public ResponseEntity<Page> getTagList(@PathVariable("pageId") String pageId) {
        Page result = tagService.getTagList(pageId);

        return ResponseEntity.ok().body(result);
    }

    @ApiOperation(value = "태그 생성 서비스", notes = "태그 생성 서비스")
    @PostMapping(path = "tag")
    public ResponseEntity<Tag> createTag(@RequestBody List<TagDTO> inputDTO) {
        Tag result = tagService.createTag(inputDTO);

        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @ApiOperation(value = "태그 삭제 서비스", notes = "태그 삭제 서비스")
    @PostMapping(path = "tag/Delete")
    public ResponseEntity<Tag> deleteTag(@RequestBody List<TagDTO> inputDTO) {
        Tag result = tagService.deleteTag(inputDTO);

        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @ApiOperation(value = "태그 업데이트 서비스", notes = "태그 업데이트 서비스")
    @PostMapping(path = "tag/Update")
    public ResponseEntity<Tag> updateTag(@RequestBody List<TagDTO> inputDTO) {
        Tag result = tagService.updateTag(inputDTO);

        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }
}
