package com.rest.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rest.model.Article;
import com.rest.repository.ArticleRepository;

@Service
public class ArticleService {
	
	@Autowired
	private ArticleRepository articleRepo;
	
	public Article createArticle(Article article) {
		return articleRepo.save(article);
	}
	
	public Article findOneArticles(String id){
		try {
			return articleRepo.findById(id).orElseThrow(()-> new Exception("Not Found"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public List<Article> findAllArticles(){
		return articleRepo.findAll();
	}
	
	public void deleteOneArticle(String id) {
		articleRepo.deleteById(id);
	}
	
	public Article editOneArticle(Article article) {
		return articleRepo.save(article);
	}
	
}
