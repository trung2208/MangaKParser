/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.trung.mangakparser;

import com.opensymphony.xwork2.ActionSupport;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.zeroturnaround.zip.ZipUtil;

/**
 *
 * @author trung
 */
public class ParserAction extends ActionSupport {

    private static final Logger logger = LogManager.getLogger("ParserAction");
    static final String tempForlder = "F:/abc/";
    public String urlChapterBean;
    private FileInputStream fileInputStream;
    private String filename;

    public String initAction() {

        return SUCCESS;
    }

    public String downloadChapters() throws IOException {

//        String[] parseUrl = link.attr("abs:href").split("-");
        String  chapter = "";
        String parent=urlChapterBean.split("/")[3];
        for (String link : getChapterLinks(urlChapterBean)) {
            String[] parseUrl = link.split("-");
           // parent = parseUrl[0].substring(parseUrl[0].lastIndexOf("/") + 1);
            chapter = parseUrl[parseUrl.length-1];
            if (Files.notExists(Paths.get(tempForlder + parent + "/" + chapter), new LinkOption[]{LinkOption.NOFOLLOW_LINKS})) {
                Files.createDirectories(Paths.get(tempForlder + parent + "/" + chapter));
            }
            for (String imgLink : getImageLinksOfChapter(link)) {
                System.out.println(imgLink);
                
                System.out.println(downloadComic(imgLink, tempForlder + parent + "/" + chapter));
            }
        }
        zipFolder(tempForlder + parent + "/" + chapter, tempForlder + parent + "/" + chapter.split("/")[0] + ".zip");
        filename = new File(tempForlder + parent + "/" + chapter.split("/")[0] + ".zip").getName();
        fileInputStream = new FileInputStream(new File(tempForlder + parent + "/" + chapter.split("/")[0] + ".zip"));

        return SUCCESS;
    }

    private List<String> getChapterLinks(String url) {
        try {
            Document document = Jsoup.connect(url).get();
            Element chapterGroup = document.select(".list_chapter").first();

            Elements links = chapterGroup.select("a[href]");
            return links.eachAttr("abs:href");
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(ParserAction.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public List<ChapterBean> getAllchap(List<String> links) {
        List<ChapterBean> chapterList = new ArrayList<ChapterBean>();
        for (String link : links) {
            String[] parseUrl = link.split("-");
            String parent = parseUrl[0];
            String chapter = parseUrl[2];
            ChapterBean bean = new ChapterBean(parent, chapter, link);
            chapterList.add(bean);
        }
        return chapterList;
    }

    private List<String> getImageLinksOfChapter(String url) {
        try {
            Document document = Jsoup.connect(url).get();
            Element chapterGroup = document.select(".entry-content").first();
            Elements links = chapterGroup.select("img[src]");
            return links.eachAttr("abs:src");
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(ParserAction.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    private boolean downloadComic(String url) throws IOException {
        String[] parseUrl = url.split("-");
        String parent = parseUrl[0];
        String chapter = parseUrl[2];
        if (Files.notExists(Paths.get(tempForlder + parent + "/" + chapter), new LinkOption[]{LinkOption.NOFOLLOW_LINKS})) {
            Files.createDirectory(Paths.get(tempForlder + parent + "/" + chapter));
        }
        try (InputStream in = new URL(url).openStream()) {
            String[] fileName1 = url.split("?")[0].split("/");
            String fileName = fileName1[fileName1.length - 1];
            Files.copy(in, Paths.get(tempForlder + parent + "/" + chapter + "/" + fileName));
            return true;
        } catch (MalformedURLException ex) {
            java.util.logging.Logger.getLogger(ParserAction.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(ParserAction.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    private static boolean downloadComic(String url, String saveFolder) throws IOException {

        try (InputStream in = new URL(url).openStream()) {
            String[] fileName1 = url.split("\\?")[0].split("/");
            String fileName = fileName1[fileName1.length - 1];
            Files.copy(in, Paths.get(saveFolder + "/" + fileName));
            return true;
        } catch (MalformedURLException ex) {
            java.util.logging.Logger.getLogger(ParserAction.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(ParserAction.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    private void zipFolder(String path, String destinyPath) {
        ZipUtil.pack(new File(path), new File(destinyPath));
    }

    public String getUrlBean() {
        return urlChapterBean;
    }

    public void setUrlBean(String urlChapterBean) {
        this.urlChapterBean = urlChapterBean;
    }

    public FileInputStream getFileInputStream() {
        return fileInputStream;
    }

    public void setFileInputStream(FileInputStream fileInputStream) {
        this.fileInputStream = fileInputStream;
    }

}
