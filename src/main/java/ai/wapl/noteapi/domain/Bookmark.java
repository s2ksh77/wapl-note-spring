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
@Table(name = "TB_NOTEAPP_BOOKMARK")
public class Bookmark {

    @Id
    @JoinColumn(name = "NOTE_ID")
    private String id;

    @Column(name = "USER_ID")
    private String userId;

}