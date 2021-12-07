package ai.wapl.noteapi.domain;

import org.springframework.data.annotation.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "TB_NOTEAPP_NOTE_FILE_MAP")
public class PageFile {

    @Id
    @Column(name = "NOTE_ID")
    @OneToMany
    private Page pageId;

    @Column(name = "FILE_ID")
    @OneToMany
    private String fileId;

}