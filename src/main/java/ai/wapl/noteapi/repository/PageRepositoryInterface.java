package ai.wapl.noteapi.repository;

import com.querydsl.jpa.impl.JPAQuery;

import ai.wapl.noteapi.domain.Page;

public interface PageRepositoryInterface {
    Page findByInterfaceId(String pageId, String userId);
}
