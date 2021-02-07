package com.example.newsapp;

// Based on the API we got, we fetch out the fields we desire from the API and create a Separate 'News' custom class.
// From here, there would be 5 variables defined: 'title', 'author', 'date', 'urlToImage' and 'url'
public class News
{

    String title, author, date, urlToImage, url;
    public News() {}

    public News(String title, String author, String date, String urlToImage, String url)
    {
        this.title = title;
        this.author = author;
        this.date = date;
        this.urlToImage = urlToImage;
        this.url = url;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getAuthor()
    {
        return author;
    }
    public void setAuthor(String author) {this.author = author;}
    public String getDate() {return date;}
    public void setDate(String date) {this.date = date;}
    public String getUrlToImage() {return urlToImage;}
    public void setUrlToImage(String urlToImage) {this.urlToImage = urlToImage;}
    public String getUrl() {return url;}
    public void setUrl(String url) {this.url = url;}

}
