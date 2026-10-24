package com.laifu.kids;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.InputStream;
import java.util.Calendar;

/**
 * 少儿电脑「桌面」——纯原生控件(不用 WebView)：Windows 风格(壁纸+左列图标+任务栏+开始菜单)。
 * 点击 = 原生 OnClickListener，鼠标 / 遥控器(方向键+OK) / 触摸都可靠。
 */
public class MainActivity extends Activity {
    private final Handler h = new Handler();
    private LinearLayout menu;
    private TextView clock;
    private View firstFocus;
    private float d;

    @Override protected void onCreate(Bundle b) {
        super.onCreate(b);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        d = getResources().getDisplayMetrics().density;
        setContentView(buildUi());
        if (firstFocus != null) firstFocus.requestFocus();
        tick();
        h.postDelayed(ticker, 20000);
    }

    private final Runnable ticker = new Runnable() { public void run() { tick(); h.postDelayed(ticker, 20000); } };

    private int dp(float v) { return Math.round(v * d); }

    private GradientDrawable grad(int a, int b) {
        return new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, new int[]{a, b});
    }
    private GradientDrawable grad4(int a, int b, int c, int e) {
        return new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, new int[]{a, b, c, e});
    }

    private View buildUi() {
        FrameLayout root = new FrameLayout(this);
        root.setBackgroundDrawable(grad4(0xFF0A1E3F, 0xFF12539B, 0xFF3F8FD4, 0xFFA7D3F0));

        // === 桌面图标(左列) ===
        LinearLayout col = new LinearLayout(this);
        col.setOrientation(LinearLayout.VERTICAL);
        FrameLayout.LayoutParams clp = new FrameLayout.LayoutParams(dp(104), FrameLayout.LayoutParams.WRAP_CONTENT);
        clp.leftMargin = dp(6);
        clp.topMargin = dp(6);
        col.setLayoutParams(clp);
        col.addView(icon("浏览器", "browser"));
        col.addView(icon("文件", "files"));
        col.addView(icon("设置", "settings"));
        root.addView(col);

        // === 开始菜单 ===
        menu = new LinearLayout(this);
        menu.setOrientation(LinearLayout.VERTICAL);
        menu.setBackgroundColor(0xF00C1A30);
        FrameLayout.LayoutParams mlp = new FrameLayout.LayoutParams(dp(300), FrameLayout.LayoutParams.WRAP_CONTENT, Gravity.BOTTOM | Gravity.LEFT);
        mlp.bottomMargin = dp(46);
        mlp.leftMargin = dp(2);
        menu.setLayoutParams(mlp);
        menu.setVisibility(View.GONE);
        menu.addView(mi("浏览器", "browser"));
        menu.addView(mi("文件", "files"));
        menu.addView(mi("设置", "settings"));
        menu.addView(mi("关机", "power"));
        root.addView(menu);

        // === 任务栏 ===
        RelativeLayout bar = new RelativeLayout(this);
        bar.setBackgroundDrawable(grad(0xFF20406E, 0xFF12294A));
        FrameLayout.LayoutParams blp = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, dp(44), Gravity.BOTTOM);
        bar.setLayoutParams(blp);

        TextView st = new TextView(this);
        st.setText("开始");
        st.setTextSize(15);
        st.setTextColor(Color.WHITE);
        st.setGravity(Gravity.CENTER);
        st.setBackgroundDrawable(grad(0xFF2E5E9E, 0xFF173A63));
        st.setClickable(true);
        st.setFocusable(true);
        RelativeLayout.LayoutParams slp = new RelativeLayout.LayoutParams(dp(72), dp(44));
        st.setLayoutParams(slp);
        st.setOnClickListener(new View.OnClickListener() { public void onClick(View v) { toggleMenu(); } });
        st.setOnFocusChangeListener(focusHi());
        bar.addView(st);

        clock = new TextView(this);
        clock.setTextColor(0xFFEAF3FF);
        clock.setTextSize(13);
        clock.setGravity(Gravity.CENTER_VERTICAL);
        RelativeLayout.LayoutParams clp2 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, dp(44));
        clp2.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        clp2.rightMargin = dp(12);
        clock.setLayoutParams(clp2);
        bar.addView(clock);

        root.addView(bar);

        // 右下角品牌水印
        LinearLayout wm = new LinearLayout(this);
        wm.setOrientation(LinearLayout.VERTICAL);
        wm.setGravity(Gravity.RIGHT);
        FrameLayout.LayoutParams wlp = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT,
                Gravity.BOTTOM | Gravity.RIGHT);
        wlp.bottomMargin = dp(56);
        wlp.rightMargin = dp(20);
        wm.setLayoutParams(wlp);
        TextView w1 = new TextView(this);
        w1.setText("OPEN HW LAB");
        w1.setTextColor(0xCCFFFFFF);
        w1.setTextSize(52);
        w1.setTypeface(Typeface.DEFAULT_BOLD);
        w1.setGravity(Gravity.RIGHT);
        TextView w2 = new TextView(this);
        w2.setText("激发探索的勇气");
        w2.setTextColor(0x99FFFFFF);
        w2.setTextSize(26);
        w2.setGravity(Gravity.RIGHT);
        wm.addView(w2);
        wm.addView(w1);
        root.addView(wm);

        return root;
    }

    private View icon(final String label, final String key) {
        LinearLayout v = new LinearLayout(this);
        v.setOrientation(LinearLayout.VERTICAL);
        v.setGravity(Gravity.CENTER_HORIZONTAL);
        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(dp(100), dp(82));
        p.bottomMargin = dp(2);
        v.setLayoutParams(p);
        v.setFocusable(true);
        v.setClickable(true);
        ImageView im = new ImageView(this);
        im.setImageDrawable(loadIcon(key));
        v.addView(im, new LinearLayout.LayoutParams(dp(44), dp(44)));
        TextView t = new TextView(this);
        t.setText(label);
        t.setTextColor(Color.WHITE);
        t.setTextSize(12);
        t.setGravity(Gravity.CENTER_HORIZONTAL);
        v.addView(t);
        v.setOnClickListener(new View.OnClickListener() { public void onClick(View x) { dispatch(key); } });
        v.setOnFocusChangeListener(focusHi());
        if (firstFocus == null) firstFocus = v;
        return v;
    }

    private View mi(final String label, final String key) {
        LinearLayout v = new LinearLayout(this);
        v.setOrientation(LinearLayout.HORIZONTAL);
        v.setGravity(Gravity.CENTER_VERTICAL);
        v.setPadding(dp(12), dp(8), dp(12), dp(8));
        v.setFocusable(true);
        v.setClickable(true);
        ImageView im = new ImageView(this);
        im.setImageDrawable(loadIcon(key));
        v.addView(im, new LinearLayout.LayoutParams(dp(26), dp(26)));
        TextView t = new TextView(this);
        t.setText("  " + label);
        t.setTextColor(0xFFEAF3FF);
        t.setTextSize(16);
        v.addView(t);
        v.setOnClickListener(new View.OnClickListener() { public void onClick(View x) { toggleMenu(); dispatch(key); } });
        v.setOnFocusChangeListener(focusHi());
        return v;
    }

    private View.OnFocusChangeListener focusHi() {
        return new View.OnFocusChangeListener() {
            public void onFocusChange(View x, boolean has) {
                x.setBackgroundColor(has ? 0x66FFFFFF : 0x00000000);
            }
        };
    }

    private Drawable loadIcon(String key) {
        String f = "icons/" + key + ".png";
        if (key.equals("video")) f = "icons/tv.png";
        else if (key.equals("settings")) f = "icons/gear.png";
        try {
            AssetManager am = getAssets();
            InputStream is = am.open(f);
            Drawable dr = Drawable.createFromStream(is, null);
            is.close();
            return dr;
        } catch (Exception e) {
            return null;
        }
    }

    private void toggleMenu() {
        if (menu != null) menu.setVisibility(menu.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
    }

    private void tick() {
        Calendar c = Calendar.getInstance();
        int hh = c.get(Calendar.HOUR_OF_DAY), mm = c.get(Calendar.MINUTE);
        String s = (hh < 10 ? "0" : "") + hh + ":" + (mm < 10 ? "0" : "") + mm
                 + "    " + (c.get(Calendar.MONTH) + 1) + "/" + c.get(Calendar.DAY_OF_MONTH);
        if (clock != null) clock.setText(s);
    }

    @Override public void onBackPressed() { /* kiosk：屏蔽返回 */ }

    private boolean tryPkg(String pkg) {
        try {
            Intent i = getPackageManager().getLaunchIntentForPackage(pkg);
            if (i != null) { i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); startActivity(i); return true; }
        } catch (Exception e) { }
        return false;
    }

    private void openUrl(String u) {
        try {
            Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(u));
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
        } catch (Exception e) { toast("打不开网页"); }
    }

    private void toast(String m) { Toast.makeText(this, m, Toast.LENGTH_SHORT).show(); }

    private void dispatch(String key) {
        if (key.equals("browser")) {
            if (!tryPkg("org.mozilla.firefox") && !tryPkg("com.zte.browser") && !tryPkg("com.android.browser"))
                openUrl("http://www.baidu.com");
        }
        else if (key.equals("game")) go("com.laifu.gamehall");
        else if (key.equals("video")) go("com.zte.videoplayer");
        else if (key.equals("music")) go("com.zte.musicplayer");
        else if (key.equals("files")) go("com.fb.FileBrower");
        else if (key.equals("settings")) {
            try {
                Intent i = new Intent();
                i.setClassName("com.android.settings", "com.android.settings.Settings");
                i.putExtra("needpassword", "noneed");   // 免密：跳过设置密码门
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
            } catch (Exception e) { go("com.android.settings"); }
        }
        else if (key.equals("power")) {
            try { Runtime.getRuntime().exec(new String[]{"sh", "-c", "su -c 'reboot -p'"}); } catch (Exception e) { }
        }
        else toast("未知: " + key);
    }

    private void go(String pkg) { if (!tryPkg(pkg)) toast("还没安装：" + pkg); }
}
