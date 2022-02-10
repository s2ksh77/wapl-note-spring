package ai.wapl.noteapi.repository;

import javax.persistence.EntityManager;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import generated.ai.wapl.noteapi.domain.*;
import ai.wapl.noteapi.domain.Page;
import ai.wapl.noteapi.domain.Tag;

public class PageRepositoryImpl implements PageRepositoryInterface {

        private final JPAQueryFactory queryFactory;

        public PageRepositoryImpl(EntityManager em) {
                this.queryFactory = new JPAQueryFactory(em);
        }

        @Override
        public Page findByInterfaceId(String pageId, String userId) {
                QPage page = QPage.page;
                QTag tag = QTag.tag;
                QFile file = QFile.file;
                QBookmark bookmark = QBookmark.bookmark;

                Long count = queryFactory.select(bookmark.count())
                                .from(bookmark)
                                .where(bookmark.id.eq(pageId).and(bookmark.userId.eq(userId))).fetchFirst();

                Expression<String> cases = new CaseBuilder()
                                .when(JPAExpressions.select(bookmark)
                                                .from(bookmark)
                                                .where(bookmark.id.eq(pageId).and(bookmark.userId.eq(userId))).exists())
                                .then("TRUE").otherwise("FALSE").as(page.favorite);

                // Page result = queryFactory.select(Projections.bean(Page.class,
                // page.id,
                // page.name,
                // page.createdDate,
                // page.modifiedDate,
                // page.userName,
                // page.userId,
                // page.editingUserId,
                // page.content,
                // page.type,
                // page.sharedUserId,
                // page.sharedRoomId,
                // page.deletedDate,
                // page.textContent,
                // page.chapter,
                // page.restoreChapterId,
                // page.tagList,
                // page.fileList,
                // cases))
                // .from(page)
                // .leftJoin(page.tagList, tag)
                // .fetchJoin()
                // .leftJoin(page.fileList, file)
                // .fetchJoin()
                // .where(page.id.eq(pageId))
                // .fetchFirst();
                Page result = queryFactory.select(page)
                                .from(page)
                                .where(page.id.eq(pageId))
                                .fetchFirst();
                result.setFavorite(count > 0 ? "TRUE" : "FALSE");

                return result;
        }

}
