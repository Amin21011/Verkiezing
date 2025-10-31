package nl.hva.election_backend.model;

import jakarta.persistence.*;

import java.time.OffsetDateTime;

@Entity
public class NewsItem {
    @Id
    @GeneratedValue
    private String title;
    private String link;
    private String description;
    private String publishedAt;


    public NewsItem() {}

    public NewsItem(String title, String link, String description, String publishedAt) {
        this.title = title;
        this.link = link;
        this.description = description;
        this.publishedAt = publishedAt;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }
    public void setLink(String link) {
        this.link = link;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(String publishedAt) {
        this.publishedAt = publishedAt;
    }
}
