package ai.wapl.noteapi.util;

import ai.wapl.noteapi.dto.PageDTO.Action;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.tmax.common.dto.DOUserNoti;
import lombok.Data;
import net.minidev.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

public class Notifier {
  private static final String baseUrl = "http://dev-2826.teespace.com/CMS"; // for test
  private static final String notifyService = "/EventServer/PublishWwms?action=Event";

  private WebClient webClient;
  private final Logger logger = LoggerFactory.getLogger(Notifier.class);

  private final String userId;
  private final String channelId;
  private final Method method;
  private final boolean isMobile;
  private JsonObject alarmCenterObj;

  public Notifier(String userId, String channelId, Method method, boolean isMobile) {
    this.userId = userId;
    this.channelId = channelId;
    this.method = method;
    this.isMobile = isMobile;
    this.webClient = WebClient.create(baseUrl);
  }

  private String getJsonString(String chapterId, String pageId) {
    StringBuilder builder = new StringBuilder(method.name()).append(",");
    if (pageId != null)
      builder.append(pageId).append(",").append(chapterId);
    else
      builder.append(chapterId).append(",");
    builder.append(",").append(userId).append(",").append(isMobile ? "Mobile" : "PC");
    return builder.toString();
  }

  public void setAlarmCenter(String resourceId, String resourceName) {
    alarmCenterObj = new JsonObject();
    alarmCenterObj.addProperty("key", "CM_NOTI_CENTER_09");
    alarmCenterObj.addProperty("contentId", resourceId);

    JsonArray jsonArray = new JsonArray();
    jsonArray.add(resourceName);
    alarmCenterObj.add("values", jsonArray);
  }

  public void publishMQTT(String chapterId, String pageId, String resourceName) {
    JsonObject noti = new JsonObject();
    noti.addProperty("CH_TYPE", Constants.NOTE_CHANNEL_CODE);
    noti.addProperty("CH_ID", channelId);
    noti.addProperty("NOTI_TARGET", pageId != null ? pageId : chapterId);
    noti.addProperty("NOTI_TYPE", method.type);
    noti.addProperty("NOTI_MSG", method.getMessage(resourceName));
    noti.addProperty("NOTI_ETC", getJsonString(chapterId, pageId));
    if (alarmCenterObj != null) {
      noti.addProperty("TYPE", "history");
      noti.addProperty("NotiMessage", alarmCenterObj.toString());
    }

    _publishToMQTT(noti);
  }

  private void _publishToMQTT(JsonObject userNoti) {
    Mono<ResponseEntity<JsonObject>> jsonObjectMono = webClient.post()
        .uri(notifyService)
        .accept(MediaType.APPLICATION_JSON)
        .contentType(MediaType.APPLICATION_JSON)
        .body(Mono.just(userNoti), JsonObject.class)
        .retrieve().toEntity(JsonObject.class);

    logger.info("Publish Wwms Event: " + userNoti);
    // logger.info("Publish Result: " +
    // jsonObjectMono.block().getBody().toString());
  }

  public enum Method {
    CHAPTERCREATE("tnote_chapter_create") {
      public String getMessage(String name) {
        return "????????? ?????? ???????????????.";
      }
    },
    CHAPTERDELETE("tnote_chapter_delete") {
      public String getMessage(String name) {
        return "????????? ?????? ???????????????.";
      }
    },
    CHAPTERRENAME("tnote_chapter_update") {
      public String getMessage(String name) {
        return "????????? ?????? ???????????????.";
      }
    },
    CREATE("tnote_create") {
      public String getMessage(String name) {
        return "????????? ???????????? ?????????????????????.";
      }
    },
    DELETE("tnote_delete") {
      public String getMessage(String name) {
        return name + " ???????????? ?????? ?????????????????????.";
      }
    },
    EDIT("tnote_editstart") {
      public String getMessage(String name) {
        return name + " ????????? ????????? ?????? ???????????????.";
      }
    },
    EDITDONE("tnote_modify") {
      public String getMessage(String name) {
        return name + " ????????? ?????????????????????.";
      }
    },
    MOVE(null) {
      public String getMessage(String name) {
        return "???????????? ?????????????????????.";
      }
    },
    NONEDIT("tnote_nonedit") {
      public String getMessage(String name) {
        return "???????????? ?????? ???????????????.";
      }
    },
    RESTORE(null) {
      public String getMessage(String name) {
        return "??????????????? ??????/?????? ???????????????.";
      }
    },
    SHARECHAPTER(null) {
      public String getMessage(String name) {
        return "?????? : " + name;
      }
    },
    SHAREPAGE(null) {
      public String getMessage(String name) {
        return "?????? : " + name;
      }
    },
    THROW(null) {
      public String getMessage(String name) {
        return "??????????????? ??????/?????? ???????????????.";
      }
    },
    UPDATE(null) {
      public String getMessage(String name) {
        return name + " ???????????? ?????? ???????????????.";
      }
    },
    ;

    private final String type;

    Method(String type) {
      this.type = type;
    }

    public abstract String getMessage(String name);

    public static Method valueOf(Action action) {
      if (action.equals(Action.EDIT_START))
        return EDIT;
      if (action.equals(Action.EDIT_DONE))
        return EDITDONE;
      if (action.equals(Action.NON_EDIT))
        return NONEDIT;
      return UPDATE;
    }
  }

}
