package ai.wapl.noteapi.controller;

import java.util.List;
import java.util.Map;
import java.util.Set;

import ai.wapl.noteapi.util.ResponseUtil;
import ai.wapl.noteapi.util.ResponseUtil.ResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import ai.wapl.noteapi.domain.Page;
import ai.wapl.noteapi.domain.Tag;
import ai.wapl.noteapi.dto.PageDTO;
import ai.wapl.noteapi.dto.PageDTOinterface;
import ai.wapl.noteapi.dto.TagDTO;
import ai.wapl.noteapi.dto.TagDTOInterface;
import ai.wapl.noteapi.service.TagService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

import static ai.wapl.noteapi.util.Constants.DEFAULT_API_URI;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = DEFAULT_API_URI)
public class TagController {
    private final TagService tagService;
    private String userId = "caf1a998-c39e-49d4-81c7-719f6cc624d9"; // for test daeun

    /**
     * tagSortList
     *
     * @param channelId 태그 조회할 채널 id
     * @return "KOR" / "ENG" / "NUM" / "ETC"
     *         </p>
     *         <p>
     *         태그 이름 첫 번째 문자 (모음/자음)
     *         </p>
     *         태그 ID, 태그 이름
     */
    @ApiOperation(value = "태그 전체 조회 정렬 서비스. tagSortList ", notes = "[KOR : [tagList], ENG : [tagList], NUM : [tagList], ETC : [tagList] 데이터 반환")
    @GetMapping(path = "/app/{channelId}/tag")
    public ResponseEntity<ResponseDTO<Map<String, Map<String, List<TagDTOInterface>>>>> getAllTagList(
            @PathVariable("channelId") String channelId) {
        Map<String, Map<String, List<TagDTOInterface>>> result = tagService.getAllTagList(channelId);

        return ResponseUtil.success(result);
    }

    @ApiOperation(value = "페이지 하위 태그 리스트 조회 서비스", notes = "페이지 하위 태그 리스트 조회 서비스. tagList")
    @GetMapping(path = "/page/{pageId}/tag")
    public ResponseEntity<ResponseDTO<Set<Tag>>> getTagList(@PathVariable("pageId") String pageId) {
        Set<Tag> result = tagService.getTagList(pageId);

        return ResponseUtil.success(result);
    }

    @ApiOperation(value = "태그 생성 서비스", notes = "태그 생성 서비스. tagCreate")
    @PostMapping(path = "/page/{pageId}/tag")
    public ResponseEntity<ResponseDTO<List<Tag>>> createTag(@RequestBody List<TagDTO> inputDTO) {
        List<Tag> tags = tagService.createTag(inputDTO);

        return ResponseUtil.success(tags);
    }

    @ApiOperation(value = "태그 삭제 서비스", notes = "태그 삭제 서비스. tagDelete ")
    @DeleteMapping(path = "/page/{pageId}/tag")
    public ResponseEntity<ResponseDTO<Tag>> deleteTag(@RequestBody List<TagDTO> inputDTO) {
        tagService.deleteTag(inputDTO);

        return ResponseUtil.noContent();
    }

    @ApiOperation(value = "태그 업데이트 서비스", notes = "태그 업데이트 서비스. tagUpdate ")
    @PutMapping(path = "/page/{pageId}/tag")
    public ResponseEntity<ResponseDTO<Tag>> updateTag(@RequestBody List<TagDTO> inputDTO) {
        tagService.updateTag(inputDTO);

        return ResponseUtil.noContent();
    }

    @ApiOperation(value = "태그 하위 페이지 리스트 조회 서비스", notes = "태그 하위 페이지 리스트 조회 서비스")
    @GetMapping(path = "/app/{channelId}/tag/{tagId}/page")
    public ResponseEntity<ResponseDTO<List<PageDTO>>> getTagPageList(
            @PathVariable("channelId") String channelId,
            @PathVariable("tagId") String tagId) {
        List<PageDTO> result = tagService.getTagPageList(channelId, tagId, userId);

        return ResponseUtil.success(result);
    }

}
