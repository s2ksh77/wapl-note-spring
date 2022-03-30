package ai.wapl.noteapi.repository;

import javax.persistence.EntityManager;

import ai.wapl.noteapi.domain.*;
import ai.wapl.noteapi.dto.ChapterDTO;
import ai.wapl.noteapi.dto.PageDTO;
import ai.wapl.noteapi.dto.TagDTO;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

import static ai.wapl.noteapi.domain.Chapter.*;
import static ai.wapl.noteapi.domain.Chapter.Type.allNote;
import static ai.wapl.noteapi.domain.QBookmark.bookmark;
import static ai.wapl.noteapi.domain.QChapter.chapter;
import static ai.wapl.noteapi.domain.QPage.*;
import static ai.wapl.noteapi.domain.QTag.*;
import static ai.wapl.noteapi.domain.QFile.*;

@Repository
public class QueryDslPageRepositoryImpl implements QueryDslPageRepository {

        private final JPAQueryFactory queryFactory;
        private final EntityManager em;

        public QueryDslPageRepositoryImpl(EntityManager em) {
                this.queryFactory = new JPAQueryFactory(em);
                this.em = em;
        }

        @Override
        public PageDTO findById(String userId, String pageId) {
                JPQLQuery<String> bookmarkJPQLQuery = JPAExpressions.select(bookmark.pageId).from(bookmark)
                        .where(bookmark.pageId.eq(pageId).and(bookmark.userId.eq(userId)));

                return queryFactory.select(Projections.constructor(PageDTO.class, page, bookmarkJPQLQuery))
                        .from(page).where(page.id.eq(pageId)).fetchOne();
        }

        @Override
        public List<PageDTO> findByChannelIdOrderByModifiedDate(String userId, String channelId, int count) {
                return queryFactory.select(Projections.constructor(PageDTO.class, page
                        , JPAExpressions.select(bookmark.pageId).from(bookmark)
                                .where(bookmark.pageId.eq(page.id).and(bookmark.userId.eq(userId)))
                ))
                        .from(page).join(chapter).on(chapter.eq(page.chapter))
                        .where(chapter.channelId.eq(channelId))
                        .orderBy(page.modifiedDate.desc())
                        .limit(count)
                        .fetch();
        }

        @Override
        public List<PageDTO> findAllPageByChannelId(String userId, String channelId) {
                // FIXME Tag 조회 안 됨
                return queryFactory.select(Projections.constructor(PageDTO.class, page))
                        .from(page).join(chapter).on(chapter.eq(page.chapter))
                        .leftJoin(tag).on(page.tagSet.any().id.eq(tag.id))
                        .where(chapter.channelId.eq(channelId))
                        .fetch();
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

        @Override
        public List<File> getFileInRecycleBin(LocalDateTime targetDate) {
                return queryFactory.select(Projections.fields(File.class,
                        page.id.as("pageId"), file.fileId.as("fileId"), chapter.channelId.as("channelId")
                )).from(page).join(chapter).on(chapter.id.eq(page.chapter.id))
                        .join(file).on(file.pageId.eq(page.id))
                        .where(page.deletedDate.before(targetDate))
                        .fetch();
        }

        @Override
        public long deleteInRecycleBin(LocalDateTime targetDate) {
                return queryFactory.delete(page)
                        .where(page.deletedDate.before(targetDate)).execute();
        }

        @Override
        public long updatePageToNonEdit(LocalDateTime targetDateTime) {
                return queryFactory.update(page).set(page.editingUserId, (String) null)
                        .where(page.editingUserId.isNotNull().and(page.modifiedDate.before(targetDateTime)))
                .execute();
        }

        private JPQLQuery<String> getPageIdWithTag(String channelId) {
                return JPAExpressions.select(page.id).from(page)
                        .join(tag).on(page.tagSet.any().id.eq(tag.id))
                        .join(chapter).on(chapter.id.eq(page.chapter.id))
                        .where(chapter.channelId.eq(channelId));
        }

}
