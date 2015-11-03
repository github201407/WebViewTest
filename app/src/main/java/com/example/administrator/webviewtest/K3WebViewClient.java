package com.example.administrator.webviewtest;

import android.annotation.TargetApi;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.Browser;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.administrator.webviewtest.Helper.HtmlConverter;
import com.example.administrator.webviewtest.Helper.HtmlSanitizer;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Administrator on 2015/11/2.
 */
public abstract class K3WebViewClient extends WebViewClient {

    private static final WebResourceResponse RESULT_DO_NOT_INTERCEPT = null;
    private static final WebResourceResponse RESULT_DUMMY_RESPONSE = new WebResourceResponse(null, null, null);
    private static final String TAG = "K3WebViewClient";

    public static WebViewClient newInstance() {
        if (Build.VERSION.SDK_INT < 21) {
            return new PreLollipopWebViewClient();
        }

        return new LollipopWebViewClient();
    }

    /**
     * Give the host application a chance to take over the control when a new
     * url is about to be loaded in the current WebView. If WebViewClient is not
     * provided, by default WebView will ask Activity Manager to choose the
     * proper handler for the url. If WebViewClient is provided, return true
     * means the host application handles the url, while return false means the
     * current WebView handles the url.
     * This method is not called for requests using the POST "method".
     *
     * @param view The WebView that is initiating the callback.
     * @param url  The url to be loaded.
     * @return True if the host application wants to leave the current WebView
     * and handle the url itself, otherwise return false.
     */
    @Override
    public boolean shouldOverrideUrlLoading(WebView webView, String url) {
        Uri uri = Uri.parse(url);
        if ("http".equals(uri.getScheme()) || "https".equals(uri.getScheme())) {
            return false;
        }

        Context context = webView.getContext();
        Intent intent = createBrowserViewIntent(uri, context);
        addActivityFlags(intent);

        boolean overridingUrlLoading = false;
        try {
            context.startActivity(intent);
            overridingUrlLoading = true;
        } catch (ActivityNotFoundException ex) {
            // If no application can handle the URL, assume that the WebView can handle it.
        }

        return overridingUrlLoading;
    }

    private Intent createBrowserViewIntent(Uri uri, Context context) {
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        intent.addCategory(Intent.CATEGORY_BROWSABLE);
        intent.putExtra(Browser.EXTRA_APPLICATION_ID, context.getPackageName());
        intent.putExtra(Browser.EXTRA_CREATE_NEW_TAB, true);
        return intent;
    }

    /**
     * Notify the host application that a page has started loading. This method
     * is called once for each main frame load so a page with iframes or
     * framesets will call onPageStarted one time for the main frame. This also
     * means that onPageStarted will not be called when the contents of an
     * embedded frame changes, i.e. clicking a link whose target is an iframe,
     * it will also not be called for fragment navigations (navigations to
     * #fragment_id).
     *
     * @param view    The WebView that is initiating the callback.
     * @param url     The url to be loaded.
     * @param favicon The favicon for this page if it already exists in the
     */
    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        super.onPageStarted(view, url, favicon);
    }

    /**
     * Notify the host application that a page has finished loading. This method
     * is called only for main frame. When onPageFinished() is called, the
     * rendering picture may not be updated yet. To get the notification for the
     * new Picture, use {@link WebView.PictureListener#onNewPicture}.
     *
     * @param view The WebView that is initiating the callback.
     * @param url  The url of the page.
     */
    @Override
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
    }

    /**
     * Notify the host application that the WebView will load the resource
     * specified by the given url.
     *
     * @param view The WebView that is initiating the callback.
     * @param url  The url of the resource the WebView will load.
     */
    @Override
    public void onLoadResource(WebView view, String url) {
        super.onLoadResource(view, url);
    }

    /**
     * Report web resource loading error to the host application. These errors usually indicate
     * inability to connect to the server. Note that unlike the deprecated version of the callback,
     * the new version will be called for any resource (iframe, image, etc), not just for the main
     * page. Thus, it is recommended to perform minimum required work in this callback.
     *
     * @param view    The WebView that is initiating the callback.
     * @param request The originating request.
     * @param error   Information about the error occured.
     */
    @Override
    public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
        super.onReceivedError(view, request, error);
        String content = "<html><head><meta name=\"viewport\" content=\"width=device-width\"/>";
            content += "<style type=\"text/css\">" +
                    "* { background: black ! important; color: #F3F3F3 !important }" +
                    ":link, :link * { color: #CCFF33 !important }" +
                    ":visited, :visited * { color: #551A8B !important }</style> ";
        content += HtmlConverter.cssStylePre();
        String text = "404 出错！！！";
        content += "</head><body>" + text + "</body></html>";

        String sanitizedContent = HtmlSanitizer.sanitize(content);
        view.loadDataWithBaseURL("null", sanitizedContent, "text/html", "utf-8", null);
    }

    protected abstract void addActivityFlags(Intent intent);

    protected WebResourceResponse shouldInterceptRequest(WebView webView, Uri uri) {
        String path = uri.getPath();
        if (path == null || !path.endsWith(".css")) {
            return RESULT_DO_NOT_INTERCEPT;
        }

        Context context = webView.getContext();
//        ContentResolver contentResolver = context.getContentResolver();
        try {
//            new WebResourceResponse(url.endsWith("js") ? "text/javascript" : "text/css", "utf-8",
//                    Foo.this.getAssets().open(url.substring(scheme.length())));
            String mimeType = "text/html";
            InputStream inputStream = context.getAssets().open("style.css");
//
            return new WebResourceResponse(mimeType, null, inputStream);
        } catch (Exception e) {
            Log.e(TAG, "Error while intercepting URI: " + uri, e);
            return RESULT_DUMMY_RESPONSE;
        }
    }

    @SuppressWarnings("deprecation")
    private static class PreLollipopWebViewClient extends K3WebViewClient {
        protected PreLollipopWebViewClient() {
            super();
        }

        @Override
        public WebResourceResponse shouldInterceptRequest(WebView webView, String url) {
            return shouldInterceptRequest(webView, Uri.parse(url));
        }

        @Override
        protected void addActivityFlags(Intent intent) {
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private static class LollipopWebViewClient extends K3WebViewClient {
        protected LollipopWebViewClient() {
            super();
        }

        @Override
        public WebResourceResponse shouldInterceptRequest(WebView webView, WebResourceRequest request) {
            return shouldInterceptRequest(webView, request.getUrl());
        }

        @Override
        protected void addActivityFlags(Intent intent) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT);
        }
    }
}
