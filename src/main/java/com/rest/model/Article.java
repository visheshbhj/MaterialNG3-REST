package com.rest.model;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "Article")
public class Article {
	
	@Id
	private String id;
	private String title;
	private String articleBody;
	private String auther;
	private String createdDate;
	private String lastModifiedDate;
	private List<Comment> commentList;
	
	public List<Comment> getCommentList() {
		return commentList;
	}

	public void setCommentList(List<Comment> commentList) {
		this.commentList = commentList;
	}

	public String getLastModifiedDate() {
		return lastModifiedDate;
	}

	public void setLastModifiedDate(String lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getArticleBody() {
		return articleBody;
	}
	public void setArticleBody(String articleBody) {
		this.articleBody = articleBody;
	}
	public String getAuther() {
		return auther;
	}
	public void setAuther(String auther) {
		this.auther = auther;
	}
	
	public Article() {
		
	}
	
	public Article(String id) {
		this.id = id;
	}
	
	public Article(String title, String articleBody,String auther) {
		this.title = title;
		this.articleBody = articleBody;
		this.auther = auther;
	}
	
	public Article(Article article) {
		this.title = article.title;
		this.articleBody = article.articleBody;
		this.auther = article.auther;
		this.createdDate = LocalDateTime.now().toString();
		this.lastModifiedDate = null;
		commentList = null;
	}
	
	@Override
	public String toString() {
		return this.id+" "+this.title +" "+this.articleBody +" "+this.auther;
	}
}
