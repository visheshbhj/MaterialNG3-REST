package com.rest.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
//import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.rest.model.Article;

//@RepositoryRestResource(collectionResourceRel="article",path="article")
public interface ArticleRepository extends MongoRepository<Article, String> {

}
