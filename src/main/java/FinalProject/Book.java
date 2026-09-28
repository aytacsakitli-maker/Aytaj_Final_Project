package FinalProject;

import java.util.Objects;

public class Book {
    private String id;
    private String title;
    private String author;
    private String category;
    private String accessType;
    private String contentSummary;
    private String imagePath;

    public Book(String id, String title, String author, String category, String accessType, String contentSummary, String imagePath) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.category = category;
        this.accessType = accessType;
        this.contentSummary = contentSummary;
        this.imagePath = imagePath;
    }

    public String getId() { return id; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public String getCategory() { return category; }
    public String getAccessType() { return accessType; }
    public String getContentSummary() { return contentSummary; }
    public String getImagePath() { return imagePath; }

    public void setTitle(String title) { this.title = title; }
    public void setAuthor(String author) { this.author = author; }
    public void setCategory(String category) { this.category = category; }
    public void setAccessType(String accessType) { this.accessType = accessType; }
    public void setContentSummary(String contentSummary) { this.contentSummary = contentSummary; }
    public void setImagePath(String imagePath) { this.imagePath = imagePath; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return Objects.equals(id, book.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}