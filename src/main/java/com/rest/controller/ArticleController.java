package com.rest.controller;

import java.time.LocalDateTime;
import java.util.List;

import com.rest.repository.ArticleRepository;
import com.rest.utility.CommonUtils;
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
	@Autowired
	private ArticleRepository articleRepo;

	@PostMapping(value="/articles")
	public Article create(@RequestBody Article body) {
		body.setCreatedDate(CommonUtils.standardFormattedDateTime(LocalDateTime.now()));
		body.setLastModifiedDate(CommonUtils.standardFormattedDateTime(LocalDateTime.now()));
		return articleService.createArticle(body);
	}
	
	@GetMapping(value="/articles")
	public @ResponseBody List<Article> getAllArticles() {
		return articleService.findAllArticles();
	}
	
	@GetMapping(value="/articles/{id}")
	public @ResponseBody ResponseEntity getOneArticles(@PathVariable String id) {
		Article articlePOJO = new Article(id);
		if (articleRepo.existsById(articlePOJO.getId())) {
			return new ResponseEntity<Article>(articleService.findOneArticles(articlePOJO.getId()), HttpStatus.OK);
		}else{
			return new ResponseEntity<Article>(HttpStatus.NOT_FOUND);
		}
	}
	
	@DeleteMapping(value="/articles/{id}")
	public ResponseEntity deleteArticle(@PathVariable String id) {
		try {
			articleService.deleteOneArticle(id);
			return new ResponseEntity(HttpStatus.ACCEPTED);
		}catch (Exception e) {
			return new ResponseEntity(HttpStatus.CONFLICT);
		}
	}
	
	@PutMapping(value="/articles/{id}")
	public @ResponseBody ResponseEntity editArticle(@PathVariable String id,@RequestBody Article modified) {
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

		if(modified.getAuthor()!=null&&!modified.getAuthor().isEmpty()) {
			if (!actual.getAuthor().equalsIgnoreCase(modified.getAuthor())) {
				actual.setAuthor(modified.getAuthor());
			}
		}

		actual.setLastModifiedDate(CommonUtils.standardFormattedDateTime(LocalDateTime.now()));
		return new ResponseEntity<>(articleService.editOneArticle(actual), HttpStatus.OK);
	}
	
}