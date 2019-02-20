package com.rest.model;

import com.rest.utility.CommonUtils;
import org.springframework.data.annotation.Id;

public class Comment {
	@Id
	private String id;
	private String userName;
	private String comment;
	private String createdDate;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	public Comment() {
	}

	public Comment(String Id, String userName, String comment) {
		this.id = Id;
		this.userName = userName;
		this.comment = comment;
		this.createdDate = CommonUtils.getCurrentStandardFormattedDateTime();
	}

	@Override
	public String toString() {
		return this.id +" "+this.userName+" "+this.comment;
	}
}
