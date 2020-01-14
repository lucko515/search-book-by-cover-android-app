package com.laimhe.bookworm.models;


import java.io.Serializable;


/**
 * This is the Book model that holds all related information for a single book taken from the MySQL.
 *
 * It implements Serializable interface so it can be sent from the Intent to the Intent.
 */
public class Book implements Serializable {


    //Define all database columns/filed names
    private String image, name, author, format, isbn, category;


    public Book(String image, String name, String author, String format, String isbn, String category) {
        this.image = image;
        this.name = name;
        this.author = author;
        this.format = format;
        this.isbn = isbn;
        this.category = category;
    }

    public Book(){
        //Default constructor
    }

    //Getters and setters

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return "Book{" +
                "image='" + image + '\'' +
                ", name='" + name + '\'' +
                ", author='" + author + '\'' +
                ", format='" + format + '\'' +
                ", isbn='" + isbn + '\'' +
                ", category='" + category + '\'' +
                '}';
    }
}