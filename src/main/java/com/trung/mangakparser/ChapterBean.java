package com.trung.mangakparser;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author trung
 */
public class ChapterBean {
    String comicName;
    String ep;
    String link;
    
    public ChapterBean(String ep, String link) {
        this.ep = ep;
        this.link = link;
    }

    public ChapterBean() {
    }

    public String getEp() {
        return ep;
    }

    public void setEp(String ep) {
        this.ep = ep;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public ChapterBean(String comicName, String ep, String link) {
        this.comicName = comicName;
        this.ep = ep;
        this.link = link;
    }

    public String getComicName() {
        return comicName;
    }

    public void setComicName(String comicName) {
        this.comicName = comicName;
    }

}
