package ai.wapl.noteapi.controller;

import ai.wapl.noteapi.service.PageService;
import ai.wapl.noteapi.util.Constants;
import ai.wapl.noteapi.util.ResponseUtil;
import ai.wapl.noteapi.util.ResponseUtil.ResponseDTO;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import net.minidev.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(Constants.DEFAULT_API_URI)
public class ScheduleController {
    private final PageService pageService;
    private Logger logger = LoggerFactory.getLogger(ScheduleController.class);

    @ApiOperation(value = "휴지통 비우기 서비스. noteRecycleBinDelete", notes = "휴지통 비우기 서비스")
    @DeleteMapping("/recycle-bin")
    public ResponseEntity<ResponseDTO<JSONObject>> clearRecycleBin() {
        long l = pageService.clearRecycleBin();

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("count", l);

        logger.info("clearRecycleBin count: " + l);

        return ResponseUtil.success(jsonObject);
    }

    @ApiOperation(value = "unlock 서비스. noteUnlockUpdate", notes = "unlock 서비스")
    @DeleteMapping("/page/unlock")
    public ResponseEntity<ResponseDTO<JSONObject>> unLock() {
        long l = pageService.changeStateToUnLock();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("count", l);

        logger.info("unLock count: " + l);

        return ResponseUtil.success(jsonObject);
    }

}
