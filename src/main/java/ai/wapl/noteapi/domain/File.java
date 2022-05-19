package ai.wapl.noteapi.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "TB_NOTEAPP_NOTE_FILE_MAP")
@Data
@NoArgsConstructor
@IdClass(File.class)
public class File implements Serializable {

    @Id
    @Column(name = "NOTE_ID")
    private String pageId;

    @Id
    @Column(name = "FILE_ID")
    private String fileId;

    @Transient
    private String channelId;

    @Transient
    private String fileName;

    @Transient
    private String fileExtension;

    @Transient
    private String fileSize;

    @Transient
    private String createdAt;

    @Transient
    private String updatedAt;

    @Transient
    private String uploadUserId;

    public File(String pageId, String fileId) {
        this.pageId = pageId;
        this.fileId = fileId;
    }
}
