package ai.wapl.noteapi.repository;

import ai.wapl.noteapi.domain.NoteLog;
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
      + "\tresource_id in (select note_id from tb_noteapp_note_mst where parent_notebook = :chapterId)\n"
      + ") where row_num = 1\n"
      + ") b on b.resource_id = a.resource_id and a.log_id >= b.log_id\n"
      + "where a.user_id = :userId", nativeQuery = true)
  Set<String> getReadListByChapterId(@Param("userId") String userId,
      @Param("chapterId") String chapterId);

  @Query(value = "select a.resource_id from tb_noteapp_log a\n"
      + "join (select resource_id, log_id from (\n"
      + "\tselect resource_id, log_id\n"
      + "\t, row_number() over(partition by resource_id order by log_id desc) row_num\n"
      + "\tfrom tb_noteapp_log a\n"
      + "\twhere action in ('create', 'edit_done', 'rename') and \n"
      + "\tresource_id in (select id from tb_noteapp_notebook_mst where note_channel_id = :channelId)\n"
      + ") where row_num = 1\n"
      + ") b on b.resource_id = a.resource_id and a.log_id >= b.log_id\n"
      + "where a.user_id = :userId", nativeQuery = true)
  Set<String> getReadListByChannelId(@Param("userId") String userId,
      @Param("channelId") String channelId);

}
