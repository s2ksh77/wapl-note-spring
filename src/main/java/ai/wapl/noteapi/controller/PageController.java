package ai.wapl.noteapi.controller;

import java.io.Console;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ai.wapl.noteapi.domain.Page;
import ai.wapl.noteapi.dto.PageDTO;
import ai.wapl.noteapi.service.PageService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/note-api/page")
public class PageController {

    private final PageService pageService;

    // @Autowired
    // public PageController(PageService pageService) {
    // this.pageService = pageService;
    // }
    @GetMapping(path = "/{pageId}")
    public ResponseEntity getPageInfoList(@PathVariable("pageId") String pageId) {
        System.out.println("Request Method : GET");
        Page pageInfo = pageService.getPageInfo(pageId);
        return ResponseEntity.ok().body(pageInfo);
    }

    @GetMapping
    public List<Page> getAll() {
        return null;
    }

    @PostMapping
    public ResponseEntity<Page> createPage(@RequestBody Page inputDTO) {
        System.out.println(inputDTO);
        // return ResponseEntity.ok().body(inputDTO);
        Page result = pageService.createPage(inputDTO);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @DeleteMapping(path = "/Delete")
    public ResponseEntity<Page> deletePage(@RequestBody List<PageDTO> pageList) {
        System.out.println(pageList);
        Page result = pageService.deletePage(pageList);
        // return ResponseEntity.ok().body(result);
        System.out.println(result);
        return ResponseEntity.ok().body(result);
    }

}
