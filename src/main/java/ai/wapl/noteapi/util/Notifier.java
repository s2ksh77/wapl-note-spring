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
    com.tmax.common.dto.DOUserNoti noti = new com.tmax.common.dto.DOUserNoti();
    noti.setCH_TYPE(Constants.NOTE_CHANNEL_CODE);
    noti.setCH_ID(channelId);
    noti.setNOTI_TARGET(pageId != null ? pageId : chapterId);
    noti.setNOTI_TYPE(method.type);
    noti.setNOTI_MSG(method.getMessage(resourceName));
    noti.setNOTI_ETC(getJsonString(chapterId, pageId));
    if (alarmCenterObj != null) {
      noti.setTYPE("history");
      noti.setNotiMessage(alarmCenterObj.toString());
    }

    _publishToMQTT(noti);
  }

  private void _publishToMQTT(DOUserNoti userNoti) {
    Mono<ResponseEntity<JSONObject>> jsonObjectMono = webClient.post()
        .uri(notifyService)
        .accept(MediaType.APPLICATION_JSON)
        .contentType(MediaType.APPLICATION_JSON)
        .body(Mono.just(new ProObjectDTO<>(userNoti)), ProObjectDTO.class)
        .retrieve().toEntity(JSONObject.class);

    logger.info("Publish Wwms Event: " + userNoti);
//    logger.info("Publish Result: " + jsonObjectMono.block().getBody().toString());
  }

  public enum Method {
    CHAPTERCREATE("tnote_chapter_create") {
      public String getMessage(String name) {
        return "챕터를 생성 하였습니다.";
      }
    },
    CHAPTERDELETE("tnote_chapter_delete") {
      public String getMessage(String name) {
        return "챕터를 삭제 하였습니다.";
      }
    },
    CHAPTERRENAME("tnote_chapter_update") {
      public String getMessage(String name) {
        return "챕터를 수정 하였습니다.";
      }
    },
    CREATE("tnote_create") {
      public String getMessage(String name) {
        return "새로운 페이지가 생성되었습니다.";
      }
    },
    DELETE("tnote_delete") {
      public String getMessage(String name) {
        return name + " 페이지가 영구 삭제되었습니다.";
      }
    },
    EDIT("tnote_editstart") {
      public String getMessage(String name) {
        return name + " 페이지 수정이 시작 되었습니다.";
      }
    },
    EDITDONE("tnote_modify") {
      public String getMessage(String name) {
        return name + " 페이지 수정되었습니다.";
      }
    },
    MOVE(null) {
      public String getMessage(String name) {
        return "페이지가 이동하였습니다.";
      }
    },
    NONEDIT("tnote_nonedit") {
      public String getMessage(String name) {
        return "수정하지 않고 나갔습니다.";
      }
    },
    RESTORE(null) {
      public String getMessage(String name) {
        return "휴지통으로 이동/복원 되었습니다.";
      }
    }, SHARECHAPTER(null) {
      public String getMessage(String name) {
        return "챕터 : " + name;
      }
    },
    SHAREPAGE(null) {
      public String getMessage(String name) {
        return "노트 : " + name;
      }
    }, THROW(null) {
      public String getMessage(String name) {
        return "휴지통으로 이동/복원 되었습니다.";
      }
    }, UPDATE(null) {
      public String getMessage(String name) {
        return name + " 페이지가 변경 되었습니다.";
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

  @Data
  @JsonNaming(CustomNamingStrategy.class)
  public class ProObjectDTO<T> {
    private T dto;

    public ProObjectDTO(T dto) {
      this.dto = dto;
    }
  }

  public static class CustomNamingStrategy extends PropertyNamingStrategies.NamingBase {
    public static final PropertyNamingStrategy PROOBJECT_DTO_CASE = new CustomNamingStrategy();

    @Override
    public String translate(String input) {
      return input;
    }
  }
}
