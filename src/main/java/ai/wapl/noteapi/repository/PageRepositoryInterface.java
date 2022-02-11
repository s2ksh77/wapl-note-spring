package ai.wapl.noteapi.repository;

import ai.wapl.noteapi.domain.Page;

public interface PageRepositoryInterface {
    Page findById(String pageId, String userId);
}
