package de.couchduell.krimi;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/** Zeigt den gehosteten Couch-Krimi im Vollbild – die App ist nur eine Hülle um die Webseite. */
public class MainActivity extends Activity {
    static final String URL = "https://nicoboehringer.de/couch-duell/krimi/";
    static final String OFFLINE = "<html><body style='margin:0;height:100vh;display:flex;align-items:center;justify-content:center;"
            + "background:#14161c;color:#eef0f5;font-family:sans-serif;text-align:center'><div><h1>Keine Verbindung</h1>"
            + "<p>Dr&uuml;cke OK auf der Fernbedienung, um es nochmal zu versuchen.</p></div></body></html>";
    WebView web;
    boolean offline;

    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        web = new WebView(this);
        web.setBackgroundColor(Color.parseColor("#14161c"));
        WebSettings s = web.getSettings();
        s.setJavaScriptEnabled(true);
        s.setDomStorageEnabled(true);
        s.setMediaPlaybackRequiresUserGesture(false);
        // Immer die neueste Version der Seite laden (sonst hält die WebView sie bis zu 10 Minuten im Cache)
        s.setCacheMode(WebSettings.LOAD_NO_CACHE);
        web.setWebChromeClient(new WebChromeClient());
        web.setWebViewClient(new WebViewClient() {
            @Override
            public void onReceivedError(WebView v, WebResourceRequest req, WebResourceError err) {
                if (req.isForMainFrame()) {
                    offline = true;
                    v.loadDataWithBaseURL(null, OFFLINE, "text/html", "utf-8", null);
                }
            }
        });
        setContentView(web);
        web.loadUrl(URL);
    }

    @Override
    public void onWindowFocusChanged(boolean has) {
        super.onWindowFocusChanged(has);
        if (has) web.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
    }

    @Override
    public boolean onKeyDown(int code, KeyEvent e) {
        if (offline && (code == KeyEvent.KEYCODE_DPAD_CENTER || code == KeyEvent.KEYCODE_ENTER)) {
            offline = false;
            web.loadUrl(URL);
            return true;
        }
        return super.onKeyDown(code, e);
    }

    @Override protected void onPause() { super.onPause(); web.onPause(); }
    @Override protected void onResume() { super.onResume(); web.onResume(); }
}
