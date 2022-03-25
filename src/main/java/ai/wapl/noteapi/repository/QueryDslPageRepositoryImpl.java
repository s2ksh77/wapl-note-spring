package ai.wapl.noteapi.repository;

import javax.persistence.EntityManager;

import javax.persistence.Query;

import ai.wapl.noteapi.domain.*;
import ai.wapl.noteapi.dto.ChapterDTO;
import ai.wapl.noteapi.dto.PageDTO;
import ai.wapl.noteapi.dto.TagDTO;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;

import java.util.List;

import static ai.wapl.noteapi.domain.Chapter.*;
import static ai.wapl.noteapi.domain.Chapter.Type.allNote;
import static ai.wapl.noteapi.domain.QChapter.chapter;
import static ai.wapl.noteapi.domain.QPage.*;
import static ai.wapl.noteapi.domain.QTag.*;

@Repository
public class QueryDslPageRepositoryImpl implements QueryDslPageRepository {

        private final JPAQueryFactory queryFactory;
        private final EntityManager em;

        public QueryDslPageRepositoryImpl(EntityManager em) {
                this.queryFactory = new JPAQueryFactory(em);
                this.em = em;
        }

        @Override
        public Page findById(String userId, String pageId) {
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

                return (Page) nativeQuery.getSingleResult();
        }

        @Override
        public long moveToRecycleBin(String channelId, String chapterId) {
                QChapter r = new QChapter("r");
                return queryFactory.update(page.chapter).where(page.chapter.id.eq(chapterId))
                        .set(page.chapter,
                                queryFactory.selectFrom(r)
                                        .where(r.channelId.eq(channelId).and(r.type.eq(Type.recycle_bin)))
                        ).execute();
        }

        @Override
        public List<ChapterDTO> searchChapter(String channelId, String text) {
                String lowerText = "%" + text.toLowerCase() + "%";
                return queryFactory.select(Projections.constructor(ChapterDTO.class, chapter)).from(chapter)
                        .where(chapter.channelId.eq(channelId)
                                .and(chapter.name.lower().like(lowerText, '@'))
                                .and(chapter.type.ne(allNote))
                        ).fetch();
        }

        @Override
        public List<PageDTO> searchPage(String channelId, String text) {
                String lowerText = "%" + text.toLowerCase() + "%";
                return queryFactory.select(Projections.constructor(PageDTO.class, page)).from(page)
                        .join(chapter).on(page.chapter.id.eq(chapter.id))
                        .where(chapter.channelId.eq(channelId).and(
                                page.name.lower().like(lowerText, '@')
                                        .or(page.textContent.lower().like(lowerText, '@'))
                                )
                        ).fetch();
        }

        @Override
        public List<TagDTO> searchTag(String channelId, String text) {
                String lowerText = "%" + text.toLowerCase() + "%";
                JPQLQuery<String> subQuery = getPageIdWithTag(channelId);

                return queryFactory.select(
                        Projections.fields(TagDTO.class,
                                tag.id.as("id"),
                                tag.name.as("name"),
                                page.id.as("pageId")
                        )
                )
                        .from(page)
                        .join(tag).on(page.tagSet.any().id.eq(tag.id))
                        .where(page.id.in(subQuery)
                                .and(tag.name.lower().like(lowerText, '@'))
                        )
                        .fetch();
        }

        private JPQLQuery<String> getPageIdWithTag(String channelId) {
                return JPAExpressions.select(page.id).from(page)
                        .join(tag).on(page.tagSet.any().id.eq(tag.id))
                        .join(chapter).on(chapter.id.eq(page.chapter.id))
                        .where(chapter.channelId.eq(channelId));
        }

        @Deprecated
        public List searchTagByNativeQuery(String channelId, String text) {
                String lowerText = text.toLowerCase();
                String query = "SELECT t.*, m.NOTE_ID as pageId\n" +
                        "FROM TB_NOTEAPP_TAG T \n" +
                        "JOIN TB_NOTEAPP_TAG_MST M ON M.TAG_ID = T.TAG_ID\n" +
                        "WHERE M.NOTE_ID IN (\n" +
                        "SELECT M.NOTE_ID\n" +
                        "FROM TB_NOTEAPP_TAG_MST M\n" +
                        "LEFT JOIN TB_NOTEAPP_NOTE_MST N on m.note_id = n.note_id \n" +
                        "LEFT JOIN TB_NOTEAPP_NOTEBOOK_MST B on n.parent_notebook = b.id \n" +
                        "WHERE NOTE_CHANNEL_ID = :channel_id )\n" +
                        "AND (LOWER(T.TEXT) LIKE CONCAT('%',CONCAT(LOWER(:search_text),'%')) ESCAPE '@')";

                Query nativeQuery = em.createNativeQuery(query, Tag.class);
                nativeQuery.setParameter("channel_id", channelId);
                nativeQuery.setParameter("search_text", lowerText);

                return nativeQuery.getResultList();
        }

}
