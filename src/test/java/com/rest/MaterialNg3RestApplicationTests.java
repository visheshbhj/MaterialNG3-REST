package com.rest;

import com.rest.model.Article;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;

@RunWith(JUnit4.class)
@SpringBootTest
public class MaterialNg3RestApplicationTests {
	private static String URI;
	Map<String,String> credentials;
	static Map<String,String> cookie;
	Response response;

	@BeforeClass
	public static void setupBeforeClass(){
		URI = "https://materialng3.herokuapp.com/";
	}

	@Test
	public void loginTest() {
		credentials = new HashMap<>();
		cookie = new HashMap<>();
		credentials.put("username", "admin");
		credentials.put("password", "rdt_user");

		response = RestAssured.given().baseUri(URI).formParams(credentials).post("/login");
		cookie.putAll(response.getCookies());
		Assert.assertTrue(response.getCookies().containsKey("JSESSIONID"));
	}

	@Test
	public void loadArticlesTest() {
		response = RestAssured.given().baseUri(URI).cookies(cookie).get("/rest/api/articles");
		System.out.println(response.getBody().asString());
		//Article[] list = (Article)response.getBody().asString();
	}
}
