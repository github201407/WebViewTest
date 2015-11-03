package com.example.administrator.webviewtest;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;

public class MainActivity extends AppCompatActivity {

    private WebView mWebView;
    String localHtml = "<html>\n" +
            "<body>\n" +
            "\n" +
            "<h1>My First Heading</h1>\n" +
            "\n" +
            "<p>My first paragraph.</p>\n" +
            "<input type=\"button\" value=\"Say hello\" onClick=\"showAndroidToast()\" />\n" +
            "\n" +
            "<script type=\"text/javascript\">\n" +
            "    function showAndroidToast() {\n" +
            "        alert('Hello Android!');\n" +
            "    }\n" +
            "</script>\n" +
            "</body>\n" +
            "</html>";

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
        initUI();
        addAlert();
    }

    private void loadGithub() {
        mWebView.loadUrl("http://github201407.github.io/");
        mWebView.setWebViewClient(K3WebViewClient.newInstance());
    }

    private void initUI() {
        mWebView = (WebView) findViewById(R.id.web);
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
    }

    private void initActivity() {

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
    }


    private void localCssJs() {

        HtmlCleaner htmlCleaner = new HtmlCleaner();
        TagNode node = htmlCleaner.clean(localHtml);
        TagNode body = node.getElementsByName("body", true)[0];
        body.addAttribute("bgcolor", "red");
        TagNode js = node.getElementsByName("script", true)[0];
        body.removeChild(js);
        /* add <script /> TagNode */
        TagNode js2 = new TagNode("script");
        String values = "function showAndroidToast(){\n" +
                "alert('Hello');\n" +
                "}\n";
        htmlCleaner.setInnerHtml(js2, values);
        body.addChild(js2);
        String html = htmlCleaner.getInnerHtml(node);
        mWebView.loadDataWithBaseURL("null", html, "text/html", "utf-8", null);
    }

    private void addAlert() {
        /* WebChromeClient must be set BEFORE calling loadUrl! */
        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onJsAlert(WebView view, String url, String message, final android.webkit.JsResult result) {
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("javaScript dialog")
                        .setMessage(message)
                        .setCancelable(true)
                        .setPositiveButton(android.R.string.ok,
                                new AlertDialog.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        result.confirm();
                                    }
                                })
                        .setCancelable(false)
                        .create()
                        .show();

                return true;
            }
        });
  
        /* load a web page which uses the alert() function */
//        mWebView.loadUrl("http://lexandera.com/files/jsexamples/alert.html");
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
        }if (id == R.id.local_1)
            mWebView.loadDataWithBaseURL("null", localHtml, "text/html", "utf-8", null);
        else if (id == R.id.local_2)
            localCssJs();
        else if (id == R.id.remote_1) {
            mWebView.setWebViewClient(new WebViewClient());
            mWebView.loadUrl("http://github201407.github.io");
        }
        else if (id == R.id.remote_2)
            loadGithub();
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
