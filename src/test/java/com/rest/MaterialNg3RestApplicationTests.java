package com.rest;

import com.google.gson.Gson;
import com.rest.model.Article;
import com.rest.model.Comment;
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

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

@RunWith(JUnit4.class)
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)

public class MaterialNg3RestApplicationTests {
	@Autowired
	static Environment environment;

	private static String URI = "https://materialng3.herokuapp.com/";
	//private static String URI = "http://localhost:8080/";
	private static Map<String,String> cookie;
	private Response response;
	private Gson gson = new Gson();
	private static String id;
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

		data.put("title", CommonUtils.getRandomString(5));
		data.put("articleBody",CommonUtils.getRandomString(50));
		data.put("author","admin");

		JSONObject jobject = new JSONObject(data);
		System.out.println(jobject.toString());
		response = RestAssured.given().header("Content-Type","application/json")
				.baseUri(URI).cookies(cookie).body(jobject.toString()).post("/articles");
		Article article = gson.fromJson(response.getBody().asString(),Article.class);
		System.out.println("Id of Article is "+article.getId());
		id = article.getId();
		Assert.notNull(article.getId(),"Article Was Not Generated");

	}


	@Test
	public void C_findANDreadArticlesTest() {
		response = RestAssured.given().baseUri(URI).cookies(cookie).get("/articles/"+id);
		Article article = gson.fromJson(response.getBody().asString(),Article.class);

		Assert.isTrue(article.getTitle().equalsIgnoreCase(data.get("title")),"Title Not Equal");
		Assert.isTrue(article.getArticleBody().equalsIgnoreCase(data.get("articleBody")),"Artical Body Not Equal");
		//Assert.isTrue(article.getCreatedDate().equalsIgnoreCase(DateNow),"Created Date Not Same");
		//Assert.isTrue(article.getLastModifiedDate().equalsIgnoreCase(DateNow),"Modified Date Not Same");
		Assert.isTrue(article.getAuthor().equalsIgnoreCase("admin"),"Author Not Same");
		//Assert.assertNull(article.getCommentList());
		data.clear();
	}

	@Test
	public void D_editArticleTest(){

		response = RestAssured.given().baseUri(URI).cookies(cookie).get("/articles/"+id);
		Article oldArticle = gson.fromJson(response.getBody().asString(),Article.class);

		data.put("title", CommonUtils.getRandomString(5));
		data.put("articleBody",CommonUtils.getRandomString(50));
		data.put("author","admin_1");

		response = RestAssured.given().baseUri(URI).header("Content-Type","application/json")
				.cookies(cookie).body(data).put("/articles/"+id);
		Article article = gson.fromJson(response.getBody().asString(),Article.class);

		Assert.isTrue(article.getTitle().equalsIgnoreCase(data.get("title")),"Title Not Equal");
		Assert.isTrue(article.getArticleBody().equalsIgnoreCase(data.get("articleBody")),"Artical Body Not Equal");
		Assert.isTrue(!oldArticle.getLastModifiedDate().equalsIgnoreCase(article.getLastModifiedDate()),"Modified Date Same");
		Assert.isTrue(!oldArticle.getAuthor().equalsIgnoreCase("admin_1"),"Author Same");
	}

	@Test
	public void E_deleteArticleTest(){
		response = RestAssured.given().baseUri(URI).cookies(cookie).delete("/articles/"+id);
		Assert.isTrue(response.getStatusCode() == 202,"Response Was "+response.getStatusCode());
	}

	@Test
	public void F_commentInArticleTest(){
		data.put("title", CommonUtils.getRandomString(5));
		data.put("articleBody",CommonUtils.getRandomString(50));
		data.put("author","admin");

		JSONObject jobject = new JSONObject(data);
		System.out.println(jobject.toString());

		response = RestAssured.given().header("Content-Type","application/json")
				.baseUri(URI).cookies(cookie).body(jobject.toString()).post("/articles");

		Article article = gson.fromJson(response.getBody().asString(),Article.class);

		System.out.println("Id of Article is "+article.getId());
		AtomicBoolean flag = new AtomicBoolean(false);
		HashMap<String,String> commentData = new HashMap<>();

		//Lets Create Couple Of Comments
		for(int count = 0; count<3;count++){
			flag.set(false);
			commentData.put("userName",CommonUtils.getRandomString(15));
			commentData.put("comment",CommonUtils.getRandomString(15));

			JSONObject jObject = new JSONObject(commentData);
			System.out.println(jObject.toString());
			response = RestAssured.given().header("Content-Type","application/json")
					.baseUri(URI).cookies(cookie).body(jObject.toString()).post("/articles/"+article.getId()+"/comment");

			Assert.isTrue(response.getStatusCode()==200,"Issues in Posting Comment, Code Was "+response.getStatusCode());

			article = gson.fromJson(response.getBody().asString(),Article.class);

			for (Comment generated_comment : article.getCommentList()) {
				System.err.println(generated_comment.getId());
				if (generated_comment.getUserName().equals(commentData.get("userName")) && generated_comment.getComment().equals(commentData.get("comment"))) {
					flag.set(true);
					break;
				}
			}

			Assert.isTrue(flag.get(),"Something Wrong At Itr "+count);
		}

		//Now Using Random Generator Select any Comment & Modify it

		response = RestAssured.given().header("Content-Type","application/json")
				.baseUri(URI).cookies(cookie).get("/articles/"+article.getId());

		article = gson.fromJson(response.getBody().asString(),Article.class);

		Random random = new Random();
		int index = random.nextInt(article.getCommentList().size());
		Comment newComment = article.getCommentList().get(index);
		data.clear();
		data.put("comment",CommonUtils.getRandomString(15));

		response = RestAssured.given().header("Content-Type","application/json")
				.baseUri(URI).cookies(cookie).body(data).put("/articles/"+article.getId()+"/comment/"+newComment.getId());

		article = gson.fromJson(response.getBody().asString(),Article.class);
		Assert.isTrue(article.getCommentList().get(index).getComment().equals(data.get("comment")),"Unable To Edit");


		//Now We Delete Random Comment
		index = random.nextInt(article.getCommentList().size());
		newComment = article.getCommentList().get(index);

		response = RestAssured.given().header("Content-Type","application/json")
				.baseUri(URI).cookies(cookie).delete("/articles/"+article.getId()+"/comment/"+newComment.getId());

		Assert.isTrue(response.statusCode()==200,"Comment Delete Not Successful");

		response = RestAssured.given().baseUri(URI).cookies(cookie).delete("/articles/"+article.getId());
		Assert.isTrue(response.getStatusCode() == 202,"Delete Article Response Was "+response.getStatusCode());
	}
}
