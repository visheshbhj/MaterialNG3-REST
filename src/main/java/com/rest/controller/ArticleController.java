package com.rest.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.rest.model.Article;
import com.rest.service.ArticleService;

@RestController
public class ArticleController {
	
	@Autowired
	private ArticleService articleService;
	
	@PostMapping(value="/rest/api/articles")
	public Article create(@RequestBody Article body) {
		Article convertedBody = new Article(body);
		Article article = articleService.createArticle(convertedBody);
		return article;
	}
	
	@GetMapping(value="/rest/api/articles")
	public @ResponseBody List<Article> getAllArticles() {
		List<Article> article = articleService.findAllArticles();
		return article;
	}
	
	@GetMapping(value="/rest/api/articles/{id}")
	public @ResponseBody Article getOneArticles(@PathVariable String id) {
		Article articlePOJO = new Article(id);
		Article article = articleService.findOneArticles(articlePOJO.getId());
		return article;
	}
	
	@DeleteMapping(value="/rest/api/articles/{id}")
	public ResponseEntity<Article> deleteArticle(@PathVariable String id) {
		try {
			articleService.deleteOneArticle(id);
			return new ResponseEntity<Article>(HttpStatus.ACCEPTED);
		}catch (Exception e) {
			return new ResponseEntity<Article>(HttpStatus.CONFLICT);
		}
	}
	
	@PutMapping(value="/rest/api/articles/{id}")
	public @ResponseBody ResponseEntity<Article> editArticle(@PathVariable String id,@RequestBody Article modified) {
		Article actual = articleService.findOneArticles(id);
		
		if(modified.getTitle()!=null&&!modified.getTitle().isEmpty()) {
			if (!actual.getTitle().equalsIgnoreCase(modified.getTitle())) {
				actual.setTitle(modified.getTitle());
			}	
		}
		
		if(modified.getArticleBody()!=null&&!modified.getArticleBody().isEmpty()) {
			if (!actual.getArticleBody().equalsIgnoreCase(modified.getArticleBody())) {
				actual.setArticleBody(modified.getArticleBody());
			}
		}
		
		return new ResponseEntity<Article>(articleService.editOneArticle(actual),HttpStatus.OK);
	}
	
}