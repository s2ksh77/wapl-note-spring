package ai.wapl.noteapi.domain;

import javax.persistence.Id;

import org.hibernate.annotations.GenericGenerator;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "TB_NOTEAPP_NOTEBOOK_MST")
public class Chapter {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "ID")
    private String id;

    @Column(name = "NOTE_CHANNEL_ID")
    private String channelId;

    @Column(name = "TEXT")
    private String name;

    @Column(name = "MODIFIED_DATE")
    private String modifiedDate;

    @Column(name = "TYPE")
    private String type;

    @Column(name = "COLOR")
    private String color;

    @Column(name = "SHARED_DATE")
    private String sharedDate;

    @Column(name = "SHARED_USER_ID")
    private String sharedUserId;

    @Column(name = "SHARED_ROOM_NAME")
    private String sharedRoomId;

    @OneToMany(mappedBy = "chapter", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Page> pageList = new ArrayList<>();

    @Transient
    private String userId;

    @Transient
    private String userName;

    @Transient
    private String resultMsg;

    @Builder
    public Chapter(String id, String channelId, String name, String type, String color, String userId, String userName) {
        this.id = id;
        this.channelId = channelId;
        this.name = name;
        this.type = type;
        this.color = color;
        this.userId = userId;
        this.userName = userName;
    }

    // TODO: create, share create method
}
