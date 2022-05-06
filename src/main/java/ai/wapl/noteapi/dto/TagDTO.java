package ai.wapl.noteapi.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TagDTO {
    private String id;
    private String name;
    private String pageId;
    private Integer tagCount;

}
