/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.trung.mangakparser;

import com.sun.nio.zipfs.ZipPath;
import static com.trung.mangakparser.ParserAction.tempForlder;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Paths;
import java.util.List;
import java.util.logging.Level;
import java.util.zip.ZipOutputStream;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import org.zeroturnaround.zip.*;

/**
 *
 * @author trung
 */
public class test {
    
    public static void main(String[] args) {
        Document document;
        try {
            document = Jsoup.connect("http://mangak.info/gantz/").get();
            String abc="http://mangak.info/gantz/".split("/")[2];
            Element chapterGroup = document.select(".list_chapter").first();
            Elements links = chapterGroup.select("a[href]");
            for (Element link : links) {
                System.out.println(link.attr("abs:href"));
                String[] parseUrl = link.attr("abs:href").split("-");
                String parent = parseUrl[0].substring(parseUrl[0].lastIndexOf("/") + 1);
                String chapter = parseUrl[2];
                if (Files.notExists(Paths.get(tempForlder + parent + "/" + chapter), new LinkOption[]{LinkOption.NOFOLLOW_LINKS})) {
                    Files.createDirectories(Paths.get(tempForlder + parent + "/" + chapter));
                }
                for (String imgLink : getImageLinksOfChapter(link.attr("abs:href"))) {
                    System.out.println(imgLink);
                    
                    System.out.println(downloadComic(imgLink, tempForlder + parent + "/" + chapter));
                }
            }
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(test.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private static List<String> getImageLinksOfChapter(String url) {
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

    private void zipFolder(final Path sourceFolderPath, Path zipPath) throws Exception {
        final ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(zipPath.toFile()));
        Files.walkFileTree(sourceFolderPath, new SimpleFileVisitor<Path>() {
            
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                zos.putNextEntry(new ZipEntry(sourceFolderPath.relativize(file).toString()));
                Files.copy(file, zos);
                zos.closeEntry();
                return FileVisitResult.CONTINUE;
            }
        });
        
        zos.close();
        
    }
    
}
