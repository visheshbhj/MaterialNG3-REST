package com.rest.controller;

import com.rest.model.Article;
import com.rest.model.Comment;
import com.rest.service.ArticleService;
import com.rest.utility.CommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.atomic.AtomicInteger;

@RestController
public class CommentController {

    @Autowired
    ArticleService articleService;

    @PostMapping(value = "/articles/{id}/comment")
    public Article postSingleComment(@PathVariable String id,@RequestBody Comment comment){
        Article article = articleService.findOneArticles(id);
        comment.setId(CommonUtils.getRandomString(24));
        article.setComment(new Comment(comment.getId(),comment.getUserName(),comment.getComment()));
        return articleService.editOneArticle(article);
    }

    @PutMapping(value = "/articles/{id}/comment/{c_id}")
    public Article editSingleComment(@PathVariable String id,@PathVariable String c_id,@RequestBody Comment comment){
        Article article = articleService.findOneArticles(id);
        article.getCommentList().forEach((child_article) -> {
            if (child_article.getId().equals(c_id)){
                child_article.setComment(comment.getComment());
            }
        });
        return articleService.editOneArticle(article);
    }

    @DeleteMapping(value = "/articles/{id}/comment/{c_id}")
    public @ResponseBody ResponseEntity deleteSingleComment(@PathVariable String id,@PathVariable String c_id){
        AtomicInteger counter = new AtomicInteger();

        Article article = articleService.findOneArticles(id);
        article.getCommentList().forEach((child_article)->{
            if (child_article.getId().equals(c_id)){
                return;
            }else {
                counter.incrementAndGet();
            }
        });
        article.getCommentList().remove(counter.get());
        return new ResponseEntity<>(articleService.editOneArticle(article), HttpStatus.OK);
    }

}
