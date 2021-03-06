package ai.wapl.noteapi.domain;

import ai.wapl.noteapi.util.NoteUtil;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import lombok.Builder;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "TB_NOTEAPP_LOG")
@Data
@SequenceGenerator(name = "NOTE_LOG_ID_GEN", sequenceName = "SEQ_NOTE_LOG_ID", allocationSize = 1)
public class NoteLog {

  @Id
  @Column(name = "LOG_ID")
  @GeneratedValue(generator = "NOTE_LOG_ID_GEN", strategy = GenerationType.SEQUENCE)
  private Long id;

  @Column(name = "WS_ID")
  private String roomId;

  @Column(name = "USER_ID")
  private String userId;

  @Column(name = "RESOURCE_ID")
  private String resourceId;

  @Column(name = "RESOURCE_TYPE")
  private String resourceType;

  @Column(name = "LOG_TIMESTAMP")
  private LocalDateTime timestamp;

  @Column(name = "ACTION")
  @Enumerated(EnumType.STRING)
  private LogAction action;

  @Column(name = "DEVICE_TYPE")
  private String deviceType;

  @Builder
  public NoteLog(String roomId, String userId, String resourceId, String resourceType,
      LogAction action, boolean mobile) {
    this.roomId = roomId;
    this.userId = userId;
    this.resourceId = resourceId;
    this.resourceType = resourceType;
    this.action = action;
    this.deviceType = mobile ? "mobile" : "pc";
    this.timestamp = NoteUtil.now();
  }

  public enum LogAction {
    create, delete, move, rename, edit_start, edit_done, restore, throw_to_recycle_bin
  }

}
