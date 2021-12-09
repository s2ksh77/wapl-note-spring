package ai.wapl.noteapi.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

import javax.persistence.*;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Table(name = "TB_NOTEAPP_TAG_MST")
public class PageTag {

    // @IdClass(Page.class)
    @ManyToOne
    @JoinColumn(name = "NOTE_ID")
    private List<Page> pageTagList;

    @OneToMany
    @JoinColumn(name = "TAG_ID")
    private List<Tag> tagPageList;

}