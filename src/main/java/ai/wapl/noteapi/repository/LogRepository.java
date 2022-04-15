package ai.wapl.noteapi.repository;

import ai.wapl.noteapi.domain.NoteLog;
import java.time.LocalDateTime;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface LogRepository extends JpaRepository<NoteLog, Long> {

  @Query(value = "select a.resource_id from tb_noteapp_log a\n"
      + "join (select resource_id, log_id from (\n"
      + "\tselect resource_id, log_id\n"
      + "\t, row_number() over(partition by resource_id order by log_id desc) row_num\n"
      + "\tfrom tb_noteapp_log a\n"
      + "\twhere action in ('create', 'edit_done', 'rename') and \n"
      + "\tlog_timestamp >= current_timestamp - interval '15' day and \n"
      + "\tresource_id in (select note_id from tb_noteapp_note_mst where parent_notebook = :chapterId)\n"
      + ") where row_num = 1\n"
      + ") b on b.resource_id = a.resource_id and a.log_id >= b.log_id\n"
      + "where a.user_id = :userId ", nativeQuery = true)
  Set<String> getReadListByChapterId(@Param("userId") String userId,
      @Param("chapterId") String chapterId);

  @Query(value = "select a.resource_id from tb_noteapp_log a\n"
      + "join (select resource_id, log_id from (\n"
      + "\tselect resource_id, log_id\n"
      + "\t, row_number() over(partition by resource_id order by log_id desc) row_num\n"
      + "\tfrom tb_noteapp_log a\n"
      + "\twhere action in ('create', 'edit_done', 'rename') and \n"
      + "\tlog_timestamp >= current_timestamp - interval '15' day and \n"
      + "\tresource_id in (select id from tb_noteapp_notebook_mst where note_channel_id = :channelId)\n"
      + ") where row_num = 1\n"
      + ") b on b.resource_id = a.resource_id and a.log_id >= b.log_id\n"
      + "where a.user_id = :userId", nativeQuery = true)
  Set<String> getReadListByChannelId(@Param("userId") String userId,
      @Param("channelId") String channelId);

  @Query(value = "insert into tb_noteapp_log (log_id, ws_id, user_id, resource_id, resource_type, log_timestamp, action, device_type)\n"
      + "select seq_note_log_id.nextval, c.ws_id, a.is_edit, a.note_id, 'page', systimestamp, 'edit_done', 'pc'\n"
      + "from tb_noteapp_note_mst a\n"
      + "join tb_noteapp_notebook_mst b on b.id = a.parent_notebook\n"
      + "join tb_map_ws_ch c on c.ch_id = b.note_channel_id\n"
      + "where a.is_edit != null and a.modified_date < :targetDateTime", nativeQuery = true)
  void insertUnLockPageLog(@Param("targetDateTime") LocalDateTime targetDateTime);

}
