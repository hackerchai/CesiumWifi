package la.yunduo.cesiumwifi;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;


/**
 * Created by 轶晟 on 2015/1/18.
 */
public class web_view extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.web_view);
        WebView wb=(WebView)findViewById(R.id.webView);
        WebSettings webSettings =wb.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        wb.loadUrl("http://hackerchai.com");


    }
}
