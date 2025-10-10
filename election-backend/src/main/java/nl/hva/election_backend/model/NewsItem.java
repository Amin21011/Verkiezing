package nl.hva.election_backend.model;

import java.time.OffsetDateTime;

public class NewsItem {
    private String title;
    private String link;
    private String description;
    private OffsetDateTime publishedAt;

    public NewsItem() {}

    public NewsItem(String title, String link, String description, OffsetDateTime publishedAt) {
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

    public OffsetDateTime getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(OffsetDateTime publishedAt) {
        this.publishedAt = publishedAt;
    }
}
