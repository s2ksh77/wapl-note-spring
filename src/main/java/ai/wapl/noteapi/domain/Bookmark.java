package ai.wapl.noteapi.domain;

import javax.persistence.Id;
import javax.persistence.*;

@Entity
@Table(name = "TB_NOTEAPP_BOOKMARK")
public class Bookmark {

    @Id
    @Column(name = "NOTE_ID")
    private String id;

    @Column(name = "USER_ID")
    private String userId;

}