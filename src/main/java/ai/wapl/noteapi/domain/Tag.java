package ai.wapl.noteapi.domain;

import javax.persistence.Id;

import org.hibernate.annotations.GenericGenerator;

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
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "TAG_ID")
    private String id;

    @Column(name = "TEXT")
    private String name;

    @Transient
    private String resultMsg;

}