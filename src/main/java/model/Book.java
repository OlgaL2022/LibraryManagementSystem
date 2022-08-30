package model;

public class Book {
    private int id;
    private String title;
    private String author;
    private String genre;
    public int publishYear;
    public int quantity;
    private Status status;

    public Book(int id, String title, String author, String genre, int publishYear, int quantity, Status status) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.genre = genre;
        this.publishYear = publishYear;
        this.quantity = quantity;
        this.status = status;
    }

    public Book() {

    }

    public int getId() {
        return id;
    }

    public void setId() {
        this.id = id;
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

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public int getPublishYear() {
        return publishYear;
    }

    public void setPublishYear(int publishYear) {
        this.publishYear = publishYear;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override public String toString() {
        return id +
                "\t" + "Title: " +title +
                "\t" + "author:  " + author +
                "\t" + "genre: " + genre +
                "\t" + "publishing year: " + publishYear +
                "\t" + "quantity:" + quantity +
                "\t" + "status: " + status;

    }
}

