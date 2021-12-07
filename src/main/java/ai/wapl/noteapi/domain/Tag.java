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
@Table(name = "TB_NOTEAPP_TAG")
public class Tag {

    @Id
    @Column(name = "TAG_ID")
    private String id;

    @Column(name = "TEXT")
    private String name;

}