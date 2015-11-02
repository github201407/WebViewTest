package com.example.administrator.webviewtest;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;

import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;

import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {

    private WebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        initActivityUI();
    }

    private void initActivityUI() {
        mWebView = (WebView) findViewById(R.id.web);
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        String html = "<html>\n" +
                "<head>\n" +
                "    <link rel=stylesheet href='css/style.css'>\n" +
                "    <title>我的第一个 HTML 页面</title>\n" +
                "</head>\n" +
                "<body>\n" +
                "<p><img style='width: 100%;' src='ic_launcher.png'/></p>\n" +
                "<p>body 元素的内容会显示在浏览器中。</p>\n" +
                "<p>title 元素的内容会显示在浏览器的标题栏中。</p>\n" +
                "</body>\n" +
                "</html>";
        mWebView.loadDataWithBaseURL("file:///android_asset/", html, "text/html", "utf-8", null);

//        mWebView.loadUrl("file:///android_asset/web.html");

//        mWebView.setWebViewClient(new K3WebViewClient());
//        mWebView.loadUrl("http://github201407.github.io/");

//
//        mWebView.addJavascriptInterface(new WebAppInterface(this), "Android");
//        String html = "<html>\n" +
//                "<body>\n" +
//                "\n" +
//                "<h1>My First Heading</h1>\n" +
//                "\n" +
//                "<p>My first paragraph.</p>\n" +
//                "<input type=\"button\" value=\"Say hello\" onClick=\"showAndroidToast()\" />\n" +
//                "\n" +
//                "<script type=\"text/javascript\">\n" +
//                "    function showAndroidToast() {\n" +
//                "        Android.showToast('Hello Android!');\n" +
//                "    }\n" +
//                "</script>\n" +
//                "</body>\n" +
//                "</html>";
//
//        HtmlCleaner htmlCleaner = new HtmlCleaner();
//        TagNode node = htmlCleaner.clean(html);
//        TagNode body = node.getElementsByName("body", true)[0];
//        body.addAttribute("bgcolor", "red");
//        TagNode js = node.getElementsByName("script", true)[0];
//        body.removeChild(js);
//        TagNode js2 = new TagNode("script");
//        String values = "<script type=\"text/javascript\">\n" +
//                "    function showAndroidToast() {\n" +
//                "        Android.showToast('Hello');\n" +
//                "    }\n";
//        js.addAttribute("type", "text/javascript");
//        js.addAttribute("value", values);
//        body.addChild(js2);
//        html = htmlCleaner.getInnerHtml(node);
//        mWebView.loadDataWithBaseURL("null", html, "text/html", "utf-8", null);
    }

    public void useCleaner() {

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // Check if the key event was the Back button and if there's history
        if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebView.canGoBack()) {
            mWebView.goBack();
            return true;
        }
        // If it wasn't the Back key or there's no web page history, bubble up to the default
        // system behavior (probably exit the activity)
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public class WebAppInterface {
        Context mContext;

        /**
         * Instantiate the interface and set the context
         */
        WebAppInterface(Context c) {
            mContext = c;
        }

        /**
         * Show a toast from the web page
         */
        @JavascriptInterface
        public void showToast(String toast) {
            Toast.makeText(mContext, toast, Toast.LENGTH_SHORT).show();
        }
    }
}
