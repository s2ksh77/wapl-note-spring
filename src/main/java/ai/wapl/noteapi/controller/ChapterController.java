package ai.wapl.noteapi.controller;

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
    private String userId = "userId";

    @ApiOperation(value = "채널 생성 noteappCreate ", notes = "채널 생성")
    @PostMapping
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
    public ResponseEntity<ResponseDTO<List<Chapter>>> getChapterList(@PathVariable("channelId") String channelId) {
        List<Chapter> chapterList = chapterService.getChapterList(channelId);

        chapterList.forEach(chapter -> chapter.getPageList().forEach(page -> {
            page.setContent(null);
            page.setTextContent(null);
        }));

        return success(chapterList);
    }

    @ApiOperation(value = "단일 챕터 조회 chatpershareList ", notes = "채널 정보를 조회하는 서비스")
    @GetMapping(path = "/app/{channelId}/chapter/{chapterId}")
    public ResponseEntity<ResponseDTO<Chapter>> getChapterInfoList(@PathVariable("chapterId") String chapterId) {
        Chapter chapterInfo = chapterService.getChapterInfoList(chapterId);
        return success(chapterInfo);
    }

    @ApiOperation(value = "챕터 생성 서비스 notebooksCreate ", notes = "챕터 생성 서비스 ( 국제화 언어에 따라 챕터명 생성) ")
    @PostMapping(path = "/app/{channelId}/chapter/{language}")
    public ResponseEntity<ResponseDTO<Chapter>> createChapter(@PathVariable String channelId,
        @PathVariable String language, @RequestBody Chapter inputDTO, @RequestHeader("user-agent") String userAgent) {
        Chapter result = chapterService.createChapter(userId, inputDTO, language);

        Notifier notifier = new Notifier(userId, channelId, Method.CHAPTERCREATE, NoteUtil.isMobile(userAgent));
        notifier.publishMQTT(result.getId(), null, result.getName());

        return success(result);
    }

    @ApiOperation(value = "챕터 삭제 서비스 notebookDelete ", notes = "챕터 삭제 서비스")
    @DeleteMapping(path = "/app/{channelId}/chapter/{chapterId}")
    public ResponseEntity<?> deleteChapter(@PathVariable String channelId,
        @PathVariable String chapterId, @RequestHeader("user-agent") String userAgent) {
        chapterService.deleteChapter(channelId, chapterId);

        Notifier notifier = new Notifier(userId, channelId, Method.CHAPTERDELETE,
            NoteUtil.isMobile(userAgent));
        notifier.publishMQTT(chapterId, null, null);

        return noContent();
    }

    @ApiOperation(value = "챕터 업데이트 서비스 notebooksUpdate ", notes = "챕터 업데이트 서비스")
    @PutMapping(path = "/app/{channelId}/chapter")
    public ResponseEntity<ResponseDTO<Chapter>> updateChapter(@PathVariable String channelId,
        @RequestBody Chapter inputDTO,
        @RequestHeader("user-agent") String userAgent) {
        Chapter result = chapterService.updateChapter(inputDTO);

        Notifier notifier = new Notifier(userId, channelId, Method.CHAPTERRENAME,
            NoteUtil.isMobile(userAgent));
        notifier.publishMQTT(inputDTO.getId(), null, null);

        return success(result);
    }

    @ApiOperation(value = "챕터 전달 서비스 chaptershareCreate ", notes = "챕터 전달 서비스")
    @PostMapping(path = "/app/{channelId}/chapter/share")
    public ResponseEntity<ResponseDTO<Chapter>> shareChapter(@PathVariable String channelId,
        @RequestBody Chapter inputDTO,
        @RequestHeader("user-agent") String userAgent) {
        Chapter chapter = chapterService.shareChapter(userId, inputDTO.getId());

        ServiceCaller caller = new ServiceCaller();
        caller.createTalkMeta("", userId, chapter.getId(), chapter.getName(),
            chapter.getType().name(),
            NoteUtil.dateToString(chapter.getModifiedDate()
            ));

        Notifier notifier = new Notifier(userId, channelId, Method.SHARECHAPTER,
            NoteUtil.isMobile(userAgent));
        notifier.publishMQTT(inputDTO.getId(), null, chapter.getName());

        return ResponseUtil.success(chapter);
    }

}
