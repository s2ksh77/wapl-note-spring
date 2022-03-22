package ai.wapl.noteapi.domain;

import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonFormat;

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
@Table(name = "TB_DRIVE_MST")
public class File {

    @Id
    @Column(name = "LOG_FILE_ID")
    private String id;

    @Column(name = "FILE_NAME")
    private String name;

    @Column(name = "FILE_EXTENSION")
    private String extension;

    @Column(name = "FILE_SIZE")
    private String size;

    @Column(name = "CREATED_AT")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private String createdAt;

    @Column(name = "UPDATED_AT")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private String updatedAt;

    @Column(name = "DELETED_AT")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private String deletedAt;

    @Column(name = "LAST_UPDATE_USER_ID")
    private String createdUser;

    // TODO: create, delete, update create method
}