package com.example.administrator.webviewtest;

import android.util.Log;

import junit.framework.TestCase;

import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.PrettyXmlSerializer;
import org.htmlcleaner.SimpleHtmlSerializer;
import org.htmlcleaner.TagNode;

import java.io.File;
import java.net.URL;

/**
 * Created by Administrator on 2015/11/2.
 */
public class MainActivityTest extends TestCase {

    public void setUp() throws Exception {
        super.setUp();

    }

    public void tearDown() throws Exception {

    }

    public void testUseCleaner() throws Exception {
        HtmlCleaner htmlCleaner = new HtmlCleaner();
        String siteUrl = "http://github201407.github.io";
        TagNode node = htmlCleaner.clean(new URL(siteUrl));

        TagNode body= node.getElementsByName("body",true)[0];
        body.addAttribute("","");


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