package ai.wapl.noteapi.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Tag {

    @JsonProperty("TAG_ID")
    private String id;

    @JsonProperty("TEXT")
    private String name;
}
