package com.rest;

import com.google.gson.Gson;
import com.rest.model.Article;
import com.rest.utility.CommonUtils;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.json.JSONObject;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.util.Assert;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RunWith(JUnit4.class)
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)

public class MaterialNg3RestApplicationTests {
	@Autowired
	static Environment environment;

	private static String URI = "https://materialng3.herokuapp.com/";//http://localhost:8080
	private static Map<String,String> cookie;
	private Response response;
	private Gson gson = new Gson();
	private static String id,DateNow;
	private static Map<String,String> data = new HashMap<>();

	/*@BeforeClass
	public static void setupEnvironment(){
		System.out.println(Arrays.asList(environment.getActiveProfiles()).size());
	}*/

	@Test
	public void A_loginTest() {
		Map<String,String> credentials = new HashMap<>();
		cookie = new HashMap<>();
		credentials.put("username", "admin");
		credentials.put("password", "rdt_user");

		response = RestAssured.given().baseUri(URI).formParams(credentials).post("/login");
		cookie.putAll(response.getCookies());
		Assert.isTrue(response.getCookies().containsKey("JSESSIONID"),"Valid JSESSIONID Not Found");
	}

	@Test
	public void B_createArticleTest(){
		DateNow = LocalDateTime.now().toString();

		data.put("title", CommonUtils.getRandomString(5));
		data.put("articleBody",CommonUtils.getRandomString(50));
		data.put("createdDate",DateNow);
		data.put("lastModifiedDate",DateNow);
		//data.put("commentList","");
		data.put("auther","admin");

		JSONObject jobject = new JSONObject(data);
		System.out.println(jobject.toString());
		response = RestAssured.given().header("Content-Type","application/json")
				.baseUri(URI).cookies(cookie).body(jobject.toString()).post("/articles");
		Article article = gson.fromJson(response.getBody().asString(),Article.class);
		System.out.println("Id of Article is "+article.getId());
		id = article.getId();
		Assert.notNull(article,"Article Was Not Generated");
	}


	@Test
	public void C_findANDreadArticlesTest() {
		response = RestAssured.given().baseUri(URI).cookies(cookie).get("/articles/"+id);
		Article article = gson.fromJson(response.getBody().asString(),Article.class);

		Assert.isTrue(article.getTitle().equalsIgnoreCase(data.get("title")),"Title Not Equal");
		Assert.isTrue(article.getArticleBody().equalsIgnoreCase(data.get("articleBody")),"Artical Body Not Equal");
		Assert.isTrue(article.getCreatedDate().equalsIgnoreCase(DateNow),"Created Date Not Same");
		Assert.isTrue(article.getLastModifiedDate().equalsIgnoreCase(DateNow),"Modified Date Not Same");
		Assert.isTrue(article.getAuther().equalsIgnoreCase("admin"),"Author Not Same");
		//Assert.assertNull(article.getCommentList());
	}

	@Test
	public void D_editArticleTest(){

		response = RestAssured.given().baseUri(URI).cookies(cookie).get("/articles/"+id);
		Article oldArticle = gson.fromJson(response.getBody().asString(),Article.class);

		DateNow = LocalDateTime.now().toString();

		data.put("title", CommonUtils.getRandomString(5));
		data.put("articleBody",CommonUtils.getRandomString(50));
		//data.put("commentList","");
		data.put("auther","admin_1");

		response = RestAssured.given().baseUri(URI).header("Content-Type","application/json")
				.cookies(cookie).body(data).put("/articles/"+id);
		Article article = gson.fromJson(response.getBody().asString(),Article.class);

		Assert.isTrue(article.getTitle().equalsIgnoreCase(data.get("title")),"Title Not Equal");
		Assert.isTrue(article.getArticleBody().equalsIgnoreCase(data.get("articleBody")),"Artical Body Not Equal");
		Assert.isTrue(!oldArticle.getLastModifiedDate().equalsIgnoreCase(article.getLastModifiedDate()),"Modified Date Same");
		Assert.isTrue(!oldArticle.getAuther().equalsIgnoreCase("admin_1"),"Author Same");
	}

	@Test
	public void E_deleteArticleTest(){
		response = RestAssured.given().baseUri(URI).cookies(cookie).delete("/articles/"+id);
		Assert.isTrue(response.getStatusCode() == 202,"Response Was "+response.getStatusCode());
	}
}
