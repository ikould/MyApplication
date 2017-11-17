package com.aliu.myapplication;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ikould.frame.activity.BaseActivity;
import com.ikould.frame.utils.HttpUtil;
import com.ikould.frame.utils.PLog;
import com.ikould.frame.utils.PhoneUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;
import java.util.Set;

public class MainActivity extends BaseActivity {

    FragmentTest1 fragmentTest1;
    FragmentTest2 fragmentTest2;
    JSONObject json = new JSONObject();

    @Override
    protected void onBaseCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_main);

        if (fragmentTest1 == null)
            fragmentTest1 = new FragmentTest1();
        replaceFragment(R.id.fl_main, fragmentTest1, true);
        try {
            json.put("url", "http://www.baidu.com");
        } catch (JSONException e) {
            e.printStackTrace();
        }


        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fragmentTest2 == null)
                    fragmentTest2 = new FragmentTest2();
                replaceFragment(R.id.fl_main, fragmentTest2, true);
                try {
                    Log.d("MainActivity", "onClick: json = " + json);
                    go2Advertisement(json.getString("url"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        // Test
        tvText = (MyTextView) findViewById(R.id.tv_text);

      /*  CoreApplication.getInstance().handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.d("MainActivity", "run: getCompoundPaddingTop = " + tvText.getCompoundPaddingTop());
                Log.d("MainActivity", "run: getCompoundPaddingLeft = " + tvText.getCompoundPaddingLeft());
                Log.d("MainActivity", "run: getCompoundPaddingBottom = " + tvText.getCompoundPaddingBottom());
                Log.d("MainActivity", "run: getCompoundPaddingRight = " + tvText.getCompoundPaddingRight());
                Map<String, Object> map = ReflexObjectUtil.getKeyAndValue(tvText);
                for (String key : map.keySet()) {
                    Log.d("MainActivity", "run: key = " + key + " value = " + map.get(key));
                }
                Log.d("MainActivity", "run: mRight = " + ReflexObjectUtil.getValueByKey(tvText, "mRight"));
                Log.d("MainActivity", "run: mBottom = " + ReflexObjectUtil.getValueByKey(tvText, "mBottom"));
                Log.d("MainActivity", "run: mLeft = " + ReflexObjectUtil.getValueByKey(tvText, "mLeft"));
                Log.d("MainActivity", "run: mTop = " + ReflexObjectUtil.getValueByKey(tvText, "mTop"));
                Log.d("MainActivity", "run: mScrollY = " + ReflexObjectUtil.getValueByKey(tvText, "mScrollY"));
                Log.d("MainActivity", "run: mScrollX = " + ReflexObjectUtil.getValueByKey(tvText, "mScrollX"));
                // Log.d("MainActivity", "run: getCompoundPaddingTop = " + tvText.getCompoundPaddingTop());
            }
        }, 2000);*/
    }

    MyTextView tvText;

    /**
     * 广告点击，跳转浏览器
     *
     * @param jumpUrl 跳转地址
     */
    private void go2Advertisement(String jumpUrl) {
        PLog.d("AdvertActivity", "go2Advertisement: jumpUrl = " + jumpUrl);
        // 匹配
        if (HttpUtil.isUrlValid(jumpUrl)) {
            // 默认浏览器
            if (PhoneUtil.hasBrowser(this)) {
                Uri uri = Uri.parse(jumpUrl);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            } else {
                // AppToast.show(this, "手机还没有安装浏览器");
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            popFragment(R.id.fl_main, true);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
