package com.rest.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.rest.utility.CommonUtils;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "Article")
public class Article {
	
	@Id
	private String id;
	private String title;
	private String articleBody;
	private String author;
	private String createdDate;
	private String lastModifiedDate;
	private List<Comment> commentList;
	
	public List<Comment> getCommentList() {
		return commentList;
	}

	public void setComment(Comment comment) {
		this.commentList.add(comment);
	}

	public void setComment() {
		this.commentList = new ArrayList<Comment>();
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
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}

	public Article() {

	}
	
	public Article(String id) {
		this.id = id;
	}

	public Article(String title, String articleBody,String author) {
		this.title = title;
		this.articleBody = articleBody;
		this.author = author;
	}

	public Article(Article article) {
		this.title = article.title;
		this.articleBody = article.articleBody;
		this.author = article.author;
		this.createdDate = article.getCreatedDate();
		this.lastModifiedDate = article.getLastModifiedDate();
		commentList = article.getCommentList();
	}
	
	@Override
	public String toString() {
		return this.id+" "+this.title +" "+this.articleBody +" "+this.author;
	}
}
