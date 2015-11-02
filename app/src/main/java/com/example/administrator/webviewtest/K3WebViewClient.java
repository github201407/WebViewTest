package com.example.administrator.webviewtest;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
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

/**
 * Created by Administrator on 2015/11/2.
 */
public class K3WebViewClient extends WebViewClient {

    public K3WebViewClient() {
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
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
//        return super.shouldOverrideUrlLoading(view, url);

        if (Uri.parse(url).getHost().equals("github201407.github.io")) {
            // This is my web site, so do not override; let my WebView load the page
            return false;
        }
        // Otherwise, the link is not for a page on my site, so launch another Activity that handles URLs
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        view.getContext().startActivity(intent);
        return true;
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
     * Notify the host application of a resource request and allow the
     * application to return the data.  If the return value is null, the WebView
     * will continue to load the resource as usual.  Otherwise, the return
     * response and data will be used.  NOTE: This method is called on a thread
     * other than the UI thread so clients should exercise caution
     * when accessing private data or the view system.
     *
     * @param view    The {@link WebView} that is requesting the
     *                resource.
     * @param request Object containing the details of the request.
     * @return A {@link WebResourceResponse} containing the
     * response information or null if the WebView should load the
     * resource itself.
     */
    @Override
    public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
        return super.shouldInterceptRequest(view, request);

       /* if (url.startsWith(scheme))
            try
            {
                return new WebResourceResponse(url.endsWith("js") ? "text/javascript" : "text/css", "utf-8",
                        Foo.this.getAssets().open(url.substring(scheme.length())));
            }
            catch (IOException e)
            {
                Log.e(getClass().getSimpleName(), e.getMessage(), e);
            }

        return null;
        }*/
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
}
