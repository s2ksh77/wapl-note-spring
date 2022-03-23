package ai.wapl.noteapi.controller;

import java.util.List;
import java.util.Map;
import java.util.Set;

import ai.wapl.noteapi.util.ResponseUtil;
import ai.wapl.noteapi.util.ResponseUtil.ResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import ai.wapl.noteapi.domain.Tag;
import ai.wapl.noteapi.dto.TagDTO;
import ai.wapl.noteapi.service.TagService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

import static ai.wapl.noteapi.util.Constants.DEFAULT_API_URI;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = DEFAULT_API_URI)
public class TagController {
    private final TagService tagService;
    // TODO: 태그 페이지 조회 서비스. tagnoteList

    /**
     * tagSortList
     *
     * @param channelId
     * @return "KOR" / "ENG" / "NUM" / "ETC"</p>
     * <p>태그 이름 첫 번째 문자 (모음/자음) </p>
     * 태그 ID, 태그 이름
     */
    @ApiOperation(value = "태그 전체 조회 정렬 서비스. tagSortList ", notes = "[KOR : [tagList], ENG : [tagList], NUM : [tagList], ETC : [tagList] 데이터 반환")
    @GetMapping(path = "/app/{channelId}/tag")
    public ResponseEntity<ResponseDTO<Map<String, Map<String, List<Tag>>>>> getAllTagList(
            @PathVariable("channelId") String channelId) {
        Map<String, Map<String, List<Tag>>> result = tagService.getAllTagList(channelId);

        return ResponseUtil.success(result);
    }

    @ApiOperation(value = "페이지 하위 태그 리스트 조회 서비스", notes = "페이지 하위 태그 리스트 조회 서비스. tagList")
    @GetMapping(path = "/page/{pageId}/tag")
    public ResponseEntity<ResponseDTO<Set<Tag>>> getTagList(@PathVariable("pageId") String pageId) {
        Set<Tag> result = tagService.getTagList(pageId);

        return ResponseUtil.success(result);
    }

    @ApiOperation(value = "태그 생성 서비스", notes = "태그 생성 서비스. tagCreate")
    @PostMapping(path = "/tag")
    public ResponseEntity<ResponseDTO<Tag>> createTag(@RequestBody List<TagDTO> inputDTO) {
        Tag result = tagService.createTag(inputDTO);

        return ResponseUtil.success(result);
    }

    @ApiOperation(value = "태그 삭제 서비스", notes = "태그 삭제 서비스. tagDelete ")
    @DeleteMapping(path = "/tag")
    public ResponseEntity<ResponseDTO<Tag>> deleteTag(@RequestBody List<TagDTO> inputDTO) {
        tagService.deleteTag(inputDTO);

        return ResponseUtil.noContent();
    }

    @ApiOperation(value = "태그 업데이트 서비스", notes = "태그 업데이트 서비스. tagUpdate ")
    @PutMapping(path = "/tag")
    public ResponseEntity<ResponseDTO<Tag>> updateTag(@RequestBody List<TagDTO> inputDTO) {
        tagService.updateTag(inputDTO);

        return ResponseUtil.noContent();
    }

}
