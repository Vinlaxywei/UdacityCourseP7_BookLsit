package com.example.hhoo7.udacitycoursep7;


public class BookClass {
    private String mBookTitle;
    private String mAuthors;
    private String mPublicsher;
    private String mPublicshedDate;

    public BookClass(String mBookTitle, String mAuthors, String mPublicsher, String mPublicshedDate) {
        this.mBookTitle = mBookTitle;
        this.mAuthors = mAuthors;
        this.mPublicsher = mPublicsher;
        this.mPublicshedDate = mPublicshedDate;
    }

    public String getmBookTitle() {
        return mBookTitle;
    }

    public String getmAuthors() {
        return mAuthors;
    }

    public String getmPublicsher() {
        return mPublicsher;
    }

    public String getmPublicshedDate() {
        return mPublicshedDate;
    }



}
