package ai.wapl.noteapi.repository;

import javax.persistence.EntityManager;

import javax.persistence.Query;
import com.querydsl.jpa.impl.JPAQueryFactory;

import ai.wapl.noteapi.domain.Page;

public class QueryDslPageRepositoryImpl implements QueryDslPageRepository {

        private final JPAQueryFactory queryFactory;
        private final EntityManager em;

        public QueryDslPageRepositoryImpl(EntityManager em) {
                this.queryFactory = new JPAQueryFactory(em);
                this.em = em;
        }

        @Override
        public Page findById(String pageId, String userId) {
                String query = "select n.note_id, \n"
                                + "n.parent_notebook, \n"
                                + "n.note_title, \n"
                                + "n.note_content, \n"
                                + "n.text_content, \n"
                                + "n.created_date, \n"
                                + "n.modified_date, \n"
                                + "n.note_deleted_at, \n"
                                + "n.USER_ID, \n"
                                + "n.created_user_id, \n"
                                + "n.type, \n"
                                + "n.USER_NAME, \n"
                                + "n.is_edit, \n"
                                + "n.shared_room_name, \n"
                                + "n.shared_user_id, \n"
                                + "n.restoreChapterId, \n"
                                + "CASE WHEN  \n"
                                + "        (SELECT COUNT(*) FROM TB_NOTEAPP_BOOKMARK  \n"
                                + "        WHERE note_id = :note_id AND USER_ID = :USER_ID) > 0  \n"
                                + "THEN 'TRUE' \n"
                                + "ELSE 'FALSE'  \n"
                                + "END AS is_favorite, \n"
                                + "f.*, \n"
                                + "s.* \n"
                                + "FROM TB_NOTEAPP_NOTE_MST as n \n"
                                + "LEFT JOIN TB_NOTEAPP_NOTE_FILE_MAP as f \n"
                                + "on n.note_id = f.note_id \n"
                                + "LEFT JOIN TB_DRIVE_MST as s \n"
                                + "on f.file_id = s.log_file_id \n"
                                + "where n.note_id = :note_id";

                Query nativeQuery = em.createNativeQuery(query, Page.class);
                nativeQuery.setParameter("note_id", pageId);
                nativeQuery.setParameter("USER_ID", userId);

                Page result = (Page) nativeQuery.getSingleResult();

                return result;
        }

}
