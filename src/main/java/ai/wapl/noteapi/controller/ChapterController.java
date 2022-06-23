package ai.wapl.noteapi.controller;

import ai.wapl.noteapi.dto.ChapterDTO;
import ai.wapl.noteapi.util.NoteUtil;
import ai.wapl.noteapi.util.Notifier;
import ai.wapl.noteapi.util.Notifier.Method;
import ai.wapl.noteapi.util.ServiceCaller;
import java.util.List;

import ai.wapl.noteapi.util.ResponseUtil;
import net.minidev.json.JSONObject;
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
@RequestMapping(path = DEFAULT_API_URI)
public class ChapterController {
    private final ChapterService chapterService;
    private String userId = "caf1a998-c39e-49d4-81c7-719f6cc624d9";

    @ApiOperation(value = "채널 생성 noteappCreate ", notes = "채널 생성")
    @PostMapping("/app")
    public ResponseEntity<ResponseDTO<JSONObject>> createChannelApp(@RequestBody JSONObject dto) {
        JSONObject object = new JSONObject();
        String channelId = chapterService.createApp(dto.getAsString("language"));
        object.put("channelId", channelId);
        return ResponseUtil.success(object);
    }

    @ApiOperation(value = "채널 삭제 noteappDelete", notes = "채널 삭제")
    @DeleteMapping("/app/{channelId}")
    public ResponseEntity<ResponseDTO<Object>> deleteChannelApp(@PathVariable String channelId) {
        chapterService.deleteApp(channelId);
        return ResponseUtil.noContent();
    }

    @ApiOperation(value = "채널 별 챕터 리스트 조회 noteChapterList ", notes = "채널 별 챕터 및 하위 페이지 리스트 조회 서비스")
    @GetMapping("/app/{channelId}")
    public ResponseEntity<ResponseDTO<List<ChapterDTO>>> getChapterList(@PathVariable("channelId") String channelId,
            @RequestHeader("user-agent") String userAgent) {

        // 웹에서만 즐겨찾기 조회 필요함
        // boolean mobile = NoteUtil.isMobile(userAgent);
        boolean mobile = true;
        List<ChapterDTO> chapterList = chapterService.getChapterList(userId, channelId, mobile);

        if (!mobile) { // mobile일 경우 pageList 없게끔
            chapterList.forEach(chapter -> chapter.getPageList().forEach(page -> {
                page.setContent(null);
                page.setTextContent(null);
            }));
        }

        return success(chapterList);
    }

    @ApiOperation(value = "단일 챕터 조회 chatpershareList ", notes = "채널 정보 조회 서비스")
    @GetMapping(path = "/app/{channelId}/chapter/{chapterId}")
    public ResponseEntity<ResponseDTO<ChapterDTO>> getChapterInfoList(@PathVariable("chapterId") String chapterId,
            @PathVariable("channelId") String channelId) {
        ChapterDTO chapterInfo = chapterService.getChapterInfoList(userId, chapterId, channelId);
        return success(chapterInfo);
    }

    @ApiOperation(value = "챕터 생성 서비스 notebooksCreate ", notes = "챕터 생성 서비스 ( 국제화 언어에 따라 챕터명 생성) ")
    @PostMapping(path = "/app/{channelId}/chapter")
    public ResponseEntity<ResponseDTO<Chapter>> createChapter(@PathVariable String channelId,
            @RequestBody Chapter inputDTO, @RequestParam String language,
            @RequestHeader("user-agent") String userAgent) {

        inputDTO.setChannelId(channelId);
        boolean mobile = NoteUtil.isMobile(userAgent);
        Chapter result = chapterService.createChapter(userId, inputDTO, language, mobile);

        Notifier notifier = new Notifier(userId, channelId, Method.CHAPTERCREATE, mobile);
        notifier.publishMQTT(result.getId(), null, result.getName());

        return success(result);
    }

    @ApiOperation(value = "챕터 삭제 서비스 notebookDelete ", notes = "챕터 삭제 서비스")
    @PostMapping(path = "/app/{channelId}/chapter/delete")
    public ResponseEntity<ResponseDTO<List<ChapterDTO>>> deleteChapter(@PathVariable String channelId,
            @RequestBody List<ChapterDTO> inputList, @RequestHeader("user-agent") String userAgent) {
        boolean mobile = NoteUtil.isMobile(userAgent);
        chapterService.deleteChapter(userId, channelId, inputList, mobile);

        // TODO: // 삭제 noti
        // Notifier notifier = new Notifier(userId, channelId, Method.CHAPTERDELETE,
        // mobile);
        // notifier.publishMQTT(chapterId, null, null);

        return noContent();
    }

    @ApiOperation(value = "챕터 업데이트 서비스 notebooksUpdate ", notes = "챕터 업데이트 서비스")
    @PutMapping(path = "/app/{channelId}/chapter")
    public ResponseEntity<ResponseDTO<Chapter>> updateChapter(@PathVariable String channelId,
            @RequestBody Chapter inputDTO,
            @RequestHeader("user-agent") String userAgent) {
        boolean mobile = NoteUtil.isMobile(userAgent);
        Chapter result = chapterService.updateChapter(userId, inputDTO, mobile);

        Notifier notifier = new Notifier(userId, channelId, Method.CHAPTERRENAME, mobile);
        notifier.publishMQTT(inputDTO.getId(), null, null);

        return success(result);
    }

    @ApiOperation(value = "챕터 전달 서비스 chaptershareCreate ", notes = "챕터 전달 서비스")
    @PostMapping(path = "/app/{channelId}/chapter/share")
    public ResponseEntity<ResponseDTO<Chapter>> shareChapter(@PathVariable String channelId,
            @RequestBody Chapter inputDTO,
            @RequestHeader("user-agent") String userAgent) {
        boolean mobile = NoteUtil.isMobile(userAgent);
        Chapter chapter = chapterService.shareChapter(userId, inputDTO.getId(), mobile);

        ServiceCaller caller = new ServiceCaller();
        caller.createTalkMeta("", userId, chapter.getId(), chapter.getName(),
                chapter.getType().name(), NoteUtil.dateToString(chapter.getModifiedDate()));

        Notifier notifier = new Notifier(userId, channelId, Method.SHARECHAPTER, mobile);
        notifier.publishMQTT(inputDTO.getId(), null, chapter.getName());

        return ResponseUtil.success(chapter);
    }

}
