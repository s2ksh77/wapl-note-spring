package ai.wapl.noteapi.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class TagDTO {
    private String id;
    private String name;
}
