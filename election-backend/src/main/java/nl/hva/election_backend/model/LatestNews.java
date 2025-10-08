package nl.hva.election_backend.model;

public class LatestNews {
    private String title;
    private String summary;
    private String date;

    public  LatestNews(String title, String summary, String date){
        this.title = title;
        this.summary = summary;
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
