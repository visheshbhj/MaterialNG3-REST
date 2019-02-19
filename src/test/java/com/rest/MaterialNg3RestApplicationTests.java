package com.rest;

import com.google.gson.Gson;
import com.rest.model.Article;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.junit.runners.MethodSorters;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@RunWith(JUnit4.class)
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)

public class MaterialNg3RestApplicationTests {
	private String URI = "https://materialng3.herokuapp.com/";
	private Map<String,String> credentials;
	private static Map<String,String> cookie;
	private Response response;
	private Gson gson = new Gson();
	private String id,DateNow;

	@Test
	public void A_loginTest() {
		credentials = new HashMap<>();
		cookie = new HashMap<>();
		credentials.put("username", "admin");
		credentials.put("password", "rdt_user");

		response = RestAssured.given().baseUri(URI).formParams(credentials).post("/login");
		cookie.putAll(response.getCookies());
		Assert.assertTrue(response.getCookies().containsKey("JSESSIONID"));
	}

	@Test
	public void B_createArticleTest(){
		DateNow = LocalDate.now().toString();
		HashMap<String,String> data = new HashMap<>();
		data.put("title","Test_Title");
		data.put("articleBody","Test_Body");
		data.put("createdDate",DateNow);
		data.put("lastModifiedDate",DateNow);
		data.put("commentList","");
		data.put("auther","admin");

		response = RestAssured.given().baseUri(URI).cookies(cookie).post("/articles",data);
		Article article = gson.fromJson(response.getBody().asString(),Article.class);
		System.out.println("Id of Article is "+article.getId());
		id = article.getId();
		Assert.assertNotNull(article);
	}


	@Test
	public void C_findANDreadArticlesTest() {
		response = RestAssured.given().baseUri(URI).cookies(cookie).get("/articles/"+id);
		Article article = gson.fromJson(response.getBody().asString(),Article.class);

		Assert.assertSame(article.getTitle(),"Test_Title");
		Assert.assertSame(article.getArticleBody(),"Test_Body");
		Assert.assertSame(article.getCreatedDate(),DateNow);
		Assert.assertSame(article.getLastModifiedDate(),DateNow);
		Assert.assertSame(article.getAuther(),"admin");
		Assert.assertNull(article.getCommentList());
	}

	@Test
	public void D_editArticleTest(){

		DateNow = LocalDate.now().toString();
		HashMap<String,String> data = new HashMap<>();
		data.put("title","Test_Title_1");
		data.put("articleBody","Test_Body_1");
		data.put("createdDate",DateNow);
		data.put("lastModifiedDate",DateNow);
		data.put("commentList","");
		data.put("auther","admin_1");

		response = RestAssured.given().baseUri(URI).cookies(cookie).put("/articles/"+id,data);
		Article article = gson.fromJson(response.getBody().asString(),Article.class);

		Assert.assertSame(article.getTitle(),"Test_Title_1");
		Assert.assertSame(article.getArticleBody(),"Test_Body_1");
		Assert.assertSame(article.getCreatedDate(),DateNow);
		Assert.assertSame(article.getLastModifiedDate(),DateNow);
		Assert.assertSame(article.getAuther(),"admin_1");
	}

	@Test
	public void E_deleteArticleTest(){
		response = RestAssured.given().baseUri(URI).cookies(cookie).delete("/articles/"+id);
		if (response.getStatusCode() != 202) {
			Assert.fail("Response Was "+response.getStatusCode());
		}
	}
}
