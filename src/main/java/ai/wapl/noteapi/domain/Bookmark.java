package ai.wapl.noteapi.domain;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.Id;
import javax.persistence.*;
import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "TB_NOTEAPP_BOOKMARK")
@IdClass(Bookmark.class)
public class Bookmark implements Serializable {

    @Id
    @Column(name = "NOTE_ID")
    private String pageId;

    @Id
    @Column(name = "USER_ID")
    private String userId;

}