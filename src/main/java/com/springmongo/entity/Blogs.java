package com.springmongo.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "blogs")
public class Blogs {
	@Id
	private String id;
	String title;
	String description;
	String image;
	Boolean isPublished = false;
	Boolean isDeleted = false;
	String publishedBy;
	long createtimestamp = System.currentTimeMillis() / 1000L;
	long updatetimestamp = System.currentTimeMillis() / 1000L;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public Boolean getIsPublished() {
		return isPublished;
	}

	public void setIsPublished(Boolean isPublished) {
		this.isPublished = isPublished;
	}

	public Boolean getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(Boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	public String getPublishedBy() {
		return publishedBy;
	}

	public void setPublishedBy(String publishedBy) {
		this.publishedBy = publishedBy;
	}

	public long getCreatetimestamp() {
		return createtimestamp;
	}

	public void setCreatetimestamp(long createtimestamp) {
		this.createtimestamp = createtimestamp;
	}

	public long getUpdatetimestamp() {
		return updatetimestamp;
	}

	public void setUpdatetimestamp(long updatetimestamp) {
		this.updatetimestamp = updatetimestamp;
	}

}
