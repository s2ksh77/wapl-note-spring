package ai.wapl.noteapi.domain;

import org.springframework.data.annotation.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import javax.persistence.*;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Table(name = "TB_NOTEAPP_TAG_MST")
public class PageTag {

    @Id
    @Column(name = "NOTE_ID")
    @OneToMany
    private Page pageId;

    @Column(name = "TAG_ID")
    @OneToMany
    private Tag tagId;

}