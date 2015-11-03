package com.example.administrator.webviewtest;

import android.net.Uri;
import android.util.Log;

import junit.framework.TestCase;

import org.htmlcleaner.CleanerProperties;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;
import org.htmlcleaner.XPatherException;

import java.net.URL;
import java.util.List;

/**
 * Created by Administrator on 2015/11/2.
 */
public class MainActivityTest extends TestCase {
    private static final String TAG = "MainActivityTest";

    public void setUp() throws Exception {
        super.setUp();

    }

    public void tearDown() throws Exception {

    }

    public void testUri(){
        String url = "http://www.baidu.com/path/style.css";
        Uri uri = Uri.parse(url);
        String scheme = uri.getScheme();
        System.out.println("scheme:" + scheme);
        String path = uri.getPath();
        System.out.println("path:" + path);
        List<String> paths =  uri.getPathSegments();
        for(String p : paths){
            System.out.println("paths:" + p);
        }
    }

    public void testCommonUsage() {
        // create an instance of HtmlCleaner
        HtmlCleaner cleaner = new HtmlCleaner();

        // take default cleaner properties
        CleanerProperties props = cleaner.getProperties();

        // customize cleaner's behaviour with property setters
        props.setCharset("uft-8");

        // Clean HTML taken from simple string, file, URL, input stream,
        // input source or reader. Result is root node of created
        // tree-like structure. Single cleaner instance may be safely used
        // multiple times.
        TagNode node = cleaner.clean("http://m.baidu.com/");

        // optionally find parts of the DOM or modify some nodes
        TagNode[] myNodes = node.getElementsByName("style", true);
        // and/or
        try {
            Object[] myNode = node.evaluateXPath("//p/last()");
        } catch (XPatherException e) {
            e.printStackTrace();
        }
        // and/or
        node.removeFromTree();
        // and/or
        node.addAttribute("attName", "attValue");
        // and/or
        node.removeAttribute("attName");
        // and/or
        cleaner.setInnerHtml(new TagNode("script"), "htmlContent");
        // and/or do some other tree manipulation/traversal

        // serialize a node to a file, output stream, DOM, JDom...
        //        new XXXSerializer(props).writeXmlXXX(aNode,...);
        //        myJDom = new JDomSerializer(props, true).createJDom(aNode);
        //        myDom = new DomSerializer(props, true).createDOM(aNode);
    }

    public void testCreateHtml() {
        HtmlCleaner htmlCleaner = new HtmlCleaner();
        String errorStr = "<table id=table1 cellspacing=2px\n" +
                "    <h1>CONTENT</h1>\n" +
                "    <td><a href=index.html>1 -> Home Page</a>\n" +
                "    <td><a href=intro.html>2 -> Introduction</a>";
        TagNode tagNode = htmlCleaner.clean(errorStr);
        String html = htmlCleaner.getInnerHtml(tagNode);
        Log.e(TAG, html);
    }


    public void testUseCleaner() throws Exception {
        HtmlCleaner htmlCleaner = new HtmlCleaner();
        String siteUrl = "http://github201407.github.io";
        TagNode node = htmlCleaner.clean(new URL(siteUrl));

        TagNode body = node.getElementsByName("body", true)[0];
        body.addAttribute("", "");


//        String body = node.getAttributeByName("body");
//        TagNode[] tags = node.getChildTags();
//        for (TagNode tag : tags) {
//            String tagName = tag.getName();
//            String tagText = tag.getText().toString();
//            System.out.println(tagName);
//            System.out.println(tagText);
//        }
        String html = htmlCleaner.getInnerHtml(node);
        Log.e("html", html);

//// serialize to xml file
//        new PrettyXmlSerializer(htmlCleaner.getProperties()).writeToFile(
//                node , "cleaned.xml", "utf-8"
//        );

// serialize to html file
//        SimpleHtmlSerializer serializer = new SimpleHtmlSerializer(htmlCleaner.getProperties());
//        serializer.writeToFile(node, "cleaned.html");


    }
}