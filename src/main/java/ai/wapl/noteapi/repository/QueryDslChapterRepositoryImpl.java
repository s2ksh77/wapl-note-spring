package ai.wapl.noteapi.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import static ai.wapl.noteapi.domain.QBookmark.bookmark;
import static ai.wapl.noteapi.domain.QChapter.chapter;
import static ai.wapl.noteapi.domain.QPage.page;

import java.util.List;

import ai.wapl.noteapi.dto.ChapterDTO;
import ai.wapl.noteapi.dto.PageDTO;

import javax.persistence.EntityManager;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;

import org.springframework.stereotype.Repository;

@Repository
public class QueryDslChapterRepositoryImpl implements QueryDslChapterRepository {
    private final JPAQueryFactory queryFactory;
    private final EntityManager em;

    public QueryDslChapterRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
        this.em = em;
    }

    @Override
    public ChapterDTO findByIdFetchJoin(String id, String userId) {
        ChapterDTO result = queryFactory.select(Projections.constructor(ChapterDTO.class, chapter))
                .from(chapter)
                .where(chapter.id.eq(id))
                .fetchOne();

        result.setPageList(findByChapterIdWithBookmark(id, userId));
        return result;
    }

    @Override
    public List<PageDTO> findByChapterIdWithBookmark(String chapterId, String userId) {
        JPQLQuery<String> bookmarkJPQLQuery = JPAExpressions.select(bookmark.pageId).from(bookmark)
                .where(bookmark.pageId.eq(page.id).and(bookmark.userId.eq(userId)));

        return queryFactory.select(Projections.constructor(PageDTO.class, page, bookmarkJPQLQuery))
                .from(page)
                .join(chapter).on(chapter.id.eq(page.chapter.id))
                .where(chapter.id.eq(chapterId))
                .fetch();
    }

}
