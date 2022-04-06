package ai.wapl.noteapi.util;

import com.google.gson.JsonObject;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import reactor.core.publisher.Mono;

public class ServiceCaller {

  private WebClient webClient;

  public ServiceCaller() {
    String baseUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
    this.webClient = WebClient.create(baseUrl);
  }

  public void createTalkMeta(String talkChannelId, String userId, String resourceId, String name, String type, String modifiedDate) {
    String uri = "Messenger/internalmessages";

    JsonObject talkInputDTO = getTalkInputDTO(talkChannelId, userId, resourceId, name, type,
        modifiedDate);

    webClient.post()
        .uri(uri)
        .accept(MediaType.APPLICATION_JSON)
        .contentType(MediaType.APPLICATION_JSON)
        .body(Mono.just(talkInputDTO), JsonObject.class)
        .retrieve().toEntity(JsonObject.class);
  }

  private JsonObject getTalkInputDTO(String talkChannelId, String userId, String id, String name, String type, String modifiedDate) {
    JsonObject talkInputDTO = new JsonObject();
    JsonObject attachment = new JsonObject();
    JsonObject msgBody = new JsonObject();
    msgBody.addProperty("id", id);
    msgBody.addProperty("text", name);
    msgBody.addProperty("type", type);
    msgBody.addProperty("date", modifiedDate);

    attachment.addProperty("ATTACHMENT_BODY", "<" + msgBody + ">");
    attachment.addProperty("ATTACHMENT_TYPE", "note");

//    talkInputDTO.addProperty("WS_ID", wsId);
    talkInputDTO.addProperty("CH_ID", talkChannelId);
    talkInputDTO.addProperty("USER_ID", userId);
    talkInputDTO.addProperty("MSG_TYPE", "create");
    talkInputDTO.addProperty("ATTACHMENT_LIST", "[" + attachment + "]");
    return talkInputDTO;
  }

}
