package com.example.springbootarticles.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Document("articles")
public class Article {
    @Id
    private String id;
    private String title;
    private String content;
    private Date created_at;
    private Date updated_at;
    private String user_id;
    private int favoriteCount;
    private List<Tag> tagList;

    public class Tag {
        private String name;
        public String getName() {
            return name;
        }
    }
    public String getTags() {
        StringBuilder builder = new StringBuilder();
        List<Tag> tags = tagList;
        if (tags != null && !tags.isEmpty()) {
            for (Tag tag : tags) {
                builder.append(tag.getName()).append(", ");
            }
            // Remove the trailing comma and space
            builder.setLength(builder.length() - 2);
            builder.append("\n");
        } else {
            builder.append("No tags\n");
        }
        return builder.toString();
    }

    public Article(String title, String content, Date created_at, Date updated_at, String user_id, List<Tag> tagList, int favoriteCount) {
        super();
        this.title = title;
        this.content = content;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.user_id = user_id;
        this.tagList = tagList;
        this.favoriteCount = favoriteCount;
    }

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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }

    public Date getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(Date updated_at) {
        this.updated_at = updated_at;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public int getFavoriteCount() {
        return favoriteCount;
    }

    public void setFavoriteCount(int favoriteCount) {
        this.favoriteCount = favoriteCount;
    }

    public List<Tag> getTagList() {
        return tagList;
    }

    public void setTagList(List<Tag> tagList) {
        this.tagList = tagList;
    }
}
