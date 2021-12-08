package ai.wapl.noteapi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import ai.wapl.noteapi.domain.Page;
import ai.wapl.noteapi.repository.PageRepository;

@Service
public class PageService {
    @Autowired
    private final PageRepository pageRepository;

    public PageService(PageRepository pageRepository) {
        this.pageRepository = pageRepository;
    }

    public Page getPageInfo(String pageId) {
        Page result = pageRepository.findById(pageId)
                .orElseThrow(() -> new ResourceNotFoundException("Not found Page"));
        return result;
    }

    // public Page createPage(Page inputPage){
    // Page result = PageRepository.save(inputPage);
    // }
}
