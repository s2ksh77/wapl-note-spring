package ai.wapl.noteapi.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Id;
import javax.persistence.*;
import java.io.Serializable;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "TB_NOTEAPP_BOOKMARK")
@IdClass(Bookmark.class)
public class Bookmark implements Serializable {

    @Id
    @Column(name = "USER_ID")
    private String userId;

    @Id
    @Column(name = "NOTE_ID")
    private String pageId;

}