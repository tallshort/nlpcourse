package weps.test.mock;

public class BlogEntry {
    private String title;
    private String author;
    private String excerpt;
    
    public BlogEntry(String title, String author, String excerpt) {
        this.title = title;
        this.author = author;
        this.excerpt = excerpt;
    }
    
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getAuthor() {
        return author;
    }
    public void setAuthor(String author) {
        this.author = author;
    }
    public String getExcerpt() {
        return excerpt;
    }
    public void setExcerpt(String excerpt) {
        this.excerpt = excerpt;
    }
    
    @Override
    public String toString() {
        return this.title + " " + this.author + " " + this.excerpt;
    }
}
