package com.rahedalat.app;

import android.app.Activity;
import android.os.Bundle;
import android.graphics.Color;
import android.graphics.drawable.*;
import android.view.*;
import android.view.animation.*;
import android.widget.*;
import android.content.*;
import android.text.TextUtils;
import java.util.*;

public class MainActivity extends Activity {
    GameUI ui;
    @Override public void onCreate(Bundle b) {
        super.onCreate(b);
        getWindow().setStatusBarColor(Color.rgb(17,23,26));
        getWindow().setNavigationBarColor(Color.rgb(17,23,26));
        ui = new GameUI(this);
        setContentView(ui.root);
        ui.showSplash();
    }
    @Override public void onBackPressed() {
        if (ui.goBack()) return;
        super.onBackPressed();
    }
}

class GameUI {
    final Activity a;
    final FrameLayout root;
    final int gold = Color.rgb(208,176,100);
    final int navy = Color.rgb(12,28,40);
    final int glass = Color.argb(226,10,20,27);
    final int white = Color.rgb(245,242,232);
    final int muted = Color.rgb(190,194,190);
    final int dark = Color.rgb(17,23,26);
    int screen = 0;
    boolean caseUnlocked = true;
    boolean caseCompleted = false;
    int dialogue = 0;
    int quizScore = 0;
    final ArrayDeque<Integer> history = new ArrayDeque<>();

    GameUI(Activity a) {
        this.a=a;
        root=new FrameLayout(a);
        root.setLayoutParams(new FrameLayout.LayoutParams(-1,-1));
        root.setBackgroundColor(dark);
        root.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
    }

    int dp(float v) { return (int)(v*a.getResources().getDisplayMetrics().density+.5f); }

    TextView tv(String s, float sp, int color) {
        TextView t=new TextView(a);
        t.setText(s);
        t.setTextColor(color);
        t.setTextSize(sp);
        t.setGravity(Gravity.CENTER);
        t.setTextDirection(View.TEXT_DIRECTION_RTL);
        t.setFontFeatureSettings("kern");
        t.setPadding(dp(8),dp(4),dp(8),dp(4));
        return t;
    }

    GradientDrawable bg(int color, float r) {
        GradientDrawable g=new GradientDrawable();
        g.setColor(color); g.setCornerRadius(dp(r)); return g;
    }

    Button btn(String text) {
        Button b=new Button(a);
        b.setText(text);
        b.setTextColor(white);
        b.setTextSize(15);
        b.setAllCaps(false);
        b.setGravity(Gravity.CENTER);
        b.setMinHeight(dp(52));
        b.setMinWidth(dp(52));
        b.setPadding(dp(14),0,dp(14),0);
        b.setBackground(bg(Color.rgb(17,51,73),16));
        b.setStateListAnimator(null);
        return b;
    }

    ImageView image(int res, ImageView.ScaleType st) {
        ImageView iv=new ImageView(a);
        iv.setImageResource(res);
        iv.setScaleType(st);
        return iv;
    }

    void clear() { root.removeAllViews(); }

    void add(View v, int w, int h, int gravity, int l,int t,int r,int b) {
        FrameLayout.LayoutParams lp=new FrameLayout.LayoutParams(w,h,gravity);
        lp.setMargins(l,t,r,b); root.addView(v,lp);
    }

    void addFull(View v) { root.addView(v,new FrameLayout.LayoutParams(-1,-1)); }

    void overlayTitle(String title, boolean back) {
        LinearLayout bar=new LinearLayout(a);
        bar.setGravity(Gravity.CENTER_VERTICAL);
        bar.setPadding(dp(10),0,dp(10),0);
        bar.setBackgroundColor(Color.argb(218,10,18,23));
        TextView left=tv(back ? "‹" : "☰",28,white);
        left.setOnClickListener(v -> { if(back) goBack(); else showOffice(); });
        bar.addView(left,new LinearLayout.LayoutParams(dp(56),-1));
        TextView tt=tv(title,18,white);
        tt.setTypeface(null,android.graphics.Typeface.BOLD);
        bar.addView(tt,new LinearLayout.LayoutParams(0,-1,1));
        TextView right=tv("⚙",22,white);
        right.setOnClickListener(v -> showSettings());
        bar.addView(right,new LinearLayout.LayoutParams(dp(56),-1));
        add(bar,-1,dp(64),Gravity.TOP,0,0,0,0);
    }

    void glassLabel(String s, float size, int y, int h, int alpha) {
        TextView t=tv(s,size,white); t.setBackground(bg(Color.argb(alpha,8,18,25),18));
        add(t,dp(320),dp(h),Gravity.TOP|Gravity.CENTER_HORIZONTAL,0,dp(y),0,0);
    }

    void showSplash() {
        screen=0; clear();
        ImageView bg=image(com.rahedalat.app.R.drawable.office_main,ImageView.ScaleType.CENTER_CROP);
        bg.setColorFilter(Color.argb(105,0,0,0),android.graphics.PorterDuff.Mode.DARKEN);
        addFull(bg);
        LinearLayout box=new LinearLayout(a); box.setOrientation(LinearLayout.VERTICAL); box.setGravity(Gravity.CENTER);
        TextView title=tv("راه عدالت",36,white); title.setTypeface(null,1);
        TextView sub=tv("بازی آموزشی آیین دادرسی مدنی",16,Color.rgb(235,222,189));
        Button start=btn("▶  شروع بازی");
        start.setTextSize(18); start.setBackground(bg(Color.rgb(16,48,70),18));
        start.setOnClickListener(v -> showOffice());
        box.addView(title,new LinearLayout.LayoutParams(-1,dp(60)));
        box.addView(sub,new LinearLayout.LayoutParams(-1,dp(48)));
        LinearLayout.LayoutParams sp=new LinearLayout.LayoutParams(dp(260),dp(60)); sp.topMargin=dp(25);
        box.addView(start,sp);
        TextView maker=tv("ساخته شده توسط پارسا",12,muted); LinearLayout.LayoutParams mp=new LinearLayout.LayoutParams(-1,dp(50)); mp.topMargin=dp(35); box.addView(maker,mp);
        add(box,dp(360),dp(260),Gravity.CENTER,0,0,0,0);
    }

    void showOffice() {
        if(screen!=1) history.push(screen);
        screen=1; clear();
        ImageView bg=image(R.drawable.office_main,ImageView.ScaleType.CENTER_CROP);
        addFull(bg);
        overlayTitle("دفتر وکیل",false);

        // Functional hotspots aligned to the actual generated scene.
        TextView lib=hotspot("📚\nکتابخانه",16);
        lib.setOnClickListener(v->showLibrary());
        add(lib,dp(130),dp(75),Gravity.TOP|Gravity.START,dp(25),dp(240),0,0);

        TextView monitor=hotspot("📊\nپیشرفت",14);
        monitor.setOnClickListener(v->showStats());
        add(monitor,dp(120),dp(70),Gravity.TOP|Gravity.END,0,dp(530),dp(12),0);

        TextView folder=hotspot("📁\nپرونده جاری",14);
        folder.setOnClickListener(v->showCase());
        add(folder,dp(150),dp(72),Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL,0,0,0,dp(225));

        TextView law=hotspot("⚖\nکتاب قانون",14);
        law.setOnClickListener(v->showGlossary());
        add(law,dp(130),dp(75),Gravity.BOTTOM|Gravity.START,dp(20),0,0,dp(100));

        TextView archive=hotspot("🗄\nبایگانی",14);
        archive.setOnClickListener(v->showArchive());
        add(archive,dp(120),dp(75),Gravity.BOTTOM|Gravity.END,0,0,dp(18),dp(120));

        TextView hint=tv("اشیای کاربردی دفتر را لمس کن",12,Color.WHITE);
        hint.setBackground(bg(Color.argb(190,8,17,22),16));
        add(hint,dp(270),dp(42),Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL,0,0,0,dp(18));
    }

    TextView hotspot(String s,float size) {
        TextView t=tv(s,size,white); t.setTypeface(null,1);
        t.setBackground(bg(Color.argb(185,8,28,40),18));
        t.setElevation(dp(8)); return t;
    }

    void showLibrary() {
        history.push(screen); screen=2; clear();
        ImageView bg=image(R.drawable.library_zoom,ImageView.ScaleType.CENTER_CROP);
        addFull(bg);
        overlayTitle("کتابخانه پرونده‌ها",true);

        TextView header=tv("پرونده‌های حقوقی",21,white); header.setTypeface(null,1);
        header.setBackground(bg(Color.argb(190,8,18,25),16));
        add(header,dp(300),dp(52),Gravity.TOP|Gravity.CENTER_HORIZONTAL,0,dp(82),0,0);

        Button case1=btn("✓  پرونده ۰۱\nحقیقت پشت یک امضا");
        case1.setTextSize(15);
        case1.setGravity(Gravity.CENTER);
        case1.setBackground(bg(Color.rgb(20,66,91),16));
        case1.setOnClickListener(v->showCase());
        add(case1,dp(320),dp(92),Gravity.CENTER,0,dp(0),0,dp(230));

        Button locked1=btn("🔒  پرونده ۰۲\nپس از تکمیل پرونده ۰۱");
        locked1.setTextColor(Color.rgb(165,166,160));
        locked1.setBackground(bg(Color.argb(215,40,42,41),16));
        locked1.setOnClickListener(v->toast("این پرونده هنوز در دسترس نیست."));
        add(locked1,dp(320),dp(82),Gravity.CENTER,0,dp(110),0,0);

        Button locked2=btn("🔒  پرونده ۰۳\nپرونده ویژه");
        locked2.setTextColor(Color.rgb(165,166,160));
        locked2.setBackground(bg(Color.argb(215,40,42,41),16));
        locked2.setOnClickListener(v->toast("برای دسترسی، پرونده‌های قبلی را تکمیل کن."));
        add(locked2,dp(320),dp(82),Gravity.CENTER,0,dp(220),0,0);
    }

    void showCase() {
        history.push(screen); screen=3; clear();
        ImageView bg=image(R.drawable.office_main,ImageView.ScaleType.CENTER_CROP);
        bg.setColorFilter(Color.argb(125,0,0,0),android.graphics.PorterDuff.Mode.DARKEN);
        addFull(bg); overlayTitle("پرونده ۰۱",true);

        LinearLayout card=new LinearLayout(a); card.setOrientation(LinearLayout.VERTICAL); card.setGravity(Gravity.CENTER);
        card.setPadding(dp(18),dp(16),dp(18),dp(16)); card.setBackground(bg(Color.argb(232,91,62,33),22));
        TextView h=tv("حقیقت پشت یک امضا",25,white); h.setTypeface(null,1);
        TextView info=tv("پرونده آموزشی مقدماتی\n\nهدف: شناخت دعوا، خواهان، خوانده، خواسته و دادخواست\n\nساختار: داستان → بررسی → آموزش → تصمیم → بازخورد",14,Color.rgb(244,234,214));
        Button start=btn("📂  باز کردن پرونده");
        start.setOnClickListener(v->showGameplay());
        card.addView(h,new LinearLayout.LayoutParams(-1,dp(58)));
        card.addView(info,new LinearLayout.LayoutParams(-1,0,1));
        card.addView(start,new LinearLayout.LayoutParams(-1,dp(58)));
        add(card,dp(360),dp(450),Gravity.CENTER,0,0,0,0);
    }

    void showGameplay() {
        history.push(screen); screen=4; clear();
        ImageView scene=image(R.drawable.office_main,ImageView.ScaleType.CENTER_CROP);
        scene.setColorFilter(Color.argb(90,0,0,0),android.graphics.PorterDuff.Mode.DARKEN);
        addFull(scene); overlayTitle("حقیقت پشت یک امضا",true);

        // Character crop
        ImageView ch=image(R.drawable.client,ImageView.ScaleType.CENTER_INSIDE);
        add(ch,dp(330),dp(470),Gravity.TOP|Gravity.CENTER_HORIZONTAL,0,dp(120),0,0);

        LinearLayout dialog=new LinearLayout(a); dialog.setOrientation(LinearLayout.VERTICAL); dialog.setPadding(dp(15),dp(10),dp(15),dp(10));
        dialog.setBackground(bg(Color.argb(235,7,18,24),18));
        TextView name=tv("موکل",13,gold); name.setGravity(Gravity.RIGHT);
        TextView body=tv("",15,white); body.setGravity(Gravity.RIGHT|Gravity.CENTER_VERTICAL);
        dialog.addView(name,new LinearLayout.LayoutParams(-1,dp(32)));
        dialog.addView(body,new LinearLayout.LayoutParams(-1,dp(82)));
        Button next=btn("ادامه");
        dialog.addView(next,new LinearLayout.LayoutParams(-1,dp(52)));
        updateDialogue(body,next);
        add(dialog,-1,dp(190),Gravity.BOTTOM,dp(10),0,dp(10),dp(18));
    }

    void updateDialogue(TextView body, Button next) {
        String[] d={
            "من یک قرارداد را امضا نکرده‌ام، اما حالا علیه من به آن استناد شده است.",
            "قبل از اینکه سراغ امضا برویم، باید مسئله حقوقی پرونده را دقیق مشخص کنیم.",
            "در یک دعوای مدنی باید بدانیم چه کسی درخواست رسیدگی دارد و چه چیزی از دادگاه می‌خواهد."
        };
        body.setText(d[Math.min(dialogue,d.length-1)]);
        if(dialogue<2) {
            next.setText("ادامه");
            next.setOnClickListener(v->{dialogue++; showGameplay();});
        } else {
            next.setText("بررسی مدارک");
            next.setOnClickListener(v->showEvidence());
        }
    }

    void showEvidence() {
        history.push(screen); screen=5; clear();
        ImageView bg=image(R.drawable.office_main,ImageView.ScaleType.CENTER_CROP);
        bg.setColorFilter(Color.argb(130,0,0,0),android.graphics.PorterDuff.Mode.DARKEN);
        addFull(bg); overlayTitle("بررسی مدارک",true);

        TextView title=tv("سه مدرک پرونده را بررسی کن",19,white); title.setTypeface(null,1);
        add(title,dp(340),dp(55),Gravity.TOP|Gravity.CENTER_HORIZONTAL,0,dp(82),0,0);

        String[] labels={"📄 قرارداد","✍ تصویر امضا","📱 پیام تلفن"};
        for(int i=0;i<3;i++){
            Button b=btn(labels[i]+"\nبرای مشاهده لمس کن");
            final int n=i;
            b.setOnClickListener(v->{
                if(n==0) clue1=true; if(n==1) clue2=true; if(n==2) clue3=true;
                toast(n==0 ? "مدرک قرارداد ثبت شد." : n==1 ? "نشانه امضا ثبت شد." : "پیام مرتبط پیدا شد.");
            });
            add(b,dp(320),dp(90),Gravity.TOP|Gravity.CENTER_HORIZONTAL,0,dp(170+i*105),0,0);
        }
        Button next=btn("🔎  جمع‌بندی سرنخ‌ها");
        next.setOnClickListener(v->showQuiz());
        add(next,dp(320),dp(58),Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL,0,0,0,dp(25));
    }

    void showQuiz() {
        history.push(screen); screen=6; clear();
        LinearLayout bgbox=new LinearLayout(a); bgbox.setOrientation(LinearLayout.VERTICAL);
        bgbox.setPadding(dp(18),dp(20),dp(18),dp(20)); bgbox.setGravity(Gravity.CENTER);
        bgbox.setBackground(bg(Color.rgb(10,28,40),0));
        addFull(bgbox);
        TextView t=tv("سؤال حقوقی",25,gold); t.setTypeface(null,1);
        TextView q=tv("بر اساس آموزش پرونده، «خواهان» کدام است؟",18,white);
        q.setGravity(Gravity.CENTER);
        bgbox.addView(t,new LinearLayout.LayoutParams(-1,dp(70)));
        bgbox.addView(q,new LinearLayout.LayoutParams(-1,dp(120)));
        String[] opts={"شخصی که رسیدگی به دعوا را درخواست می‌کند","شخصی که همیشه برنده دعواست","شاهد پرونده","قاضی رسیدگی‌کننده"};
        for(int i=0;i<opts.length;i++){
            Button b=btn(opts[i]); final int n=i;
            b.setOnClickListener(v->{
                quizScore=(n==0?100:0);
                showResult();
            });
            LinearLayout.LayoutParams bp=new LinearLayout.LayoutParams(-1,dp(56)); bp.topMargin=dp(8);
            bgbox.addView(b,bp);
        }
        TextView src=tv("مبنای آموزشی: ماده ۲ قانون آیین دادرسی مدنی و ساختار دادخواست در مواد ۴۸ و ۵۱.",11,muted);
        bgbox.addView(src,new LinearLayout.LayoutParams(-1,dp(70)));
    }

    void showResult() {
        history.push(screen); screen=7; clear();
        LinearLayout box=new LinearLayout(a); box.setOrientation(LinearLayout.VERTICAL); box.setGravity(Gravity.CENTER); box.setPadding(dp(20),dp(20),dp(20),dp(20));
        box.setBackground(bg(Color.rgb(11,28,37),0)); addFull(box);
        TextView h=tv(quizScore==100?"پاسخ درست است ✓":"این پاسخ درست نبود",27,quizScore==100?Color.rgb(125,220,150):Color.rgb(240,160,120)); h.setTypeface(null,1);
        TextView info=tv("خواهان، شخص یا اشخاص ذی‌نفع یا نماینده قانونی آنان هستند که رسیدگی به دعوا را برابر قانون درخواست می‌کنند.\n\nدر این پرونده، این مفهوم را بعداً در تعیین نقش طرفین و تنظیم دادخواست به کار می‌بریم.",15,white);
        info.setGravity(Gravity.CENTER);
        Button cont=btn("ادامه پرونده");
        cont.setOnClickListener(v->{caseCompleted=true; showOffice();});
        box.addView(h,new LinearLayout.LayoutParams(-1,dp(80)));
        box.addView(info,new LinearLayout.LayoutParams(-1,0,1));
        box.addView(cont,new LinearLayout.LayoutParams(-1,dp(60)));
    }

    void showGlossary() {
        history.push(screen); screen=8; clear(); overlayTitle("واژه‌نامه حقوقی",true);
        LinearLayout list=new LinearLayout(a); list.setOrientation(LinearLayout.VERTICAL); list.setPadding(dp(15),dp(85),dp(15),dp(20));
        String[][] items={{"خواهان","شخصی که رسیدگی به دعوا را درخواست می‌کند."},{"خوانده","طرفی که دعوا علیه او مطرح شده است."},{"دادخواست","درخواست رسمی آغاز رسیدگی در مواردی که قانون تقدیم دادخواست را لازم دانسته است."},{"خواسته","آنچه خواهان از دادگاه درخواست می‌کند."},{"دلیل","وسیله‌ای که برای اثبات ادعا یا دفاع مورد استناد قرار می‌گیرد."}};
        for(String[] it:items){
            Button b=btn(it[0]+"\n"+it[1]); b.setTextSize(13); b.setGravity(Gravity.RIGHT|Gravity.CENTER_VERTICAL);
            LinearLayout.LayoutParams p=new LinearLayout.LayoutParams(-1,dp(75)); p.topMargin=dp(8); list.addView(b,p);
        }
        ScrollView sv=new ScrollView(a); sv.addView(list); add(sv,-1,-1,Gravity.TOP,0,0,0,0);
    }

    void showStats() {
        history.push(screen); screen=9; clear(); overlayTitle("پیشرفت شما",true);
        LinearLayout box=new LinearLayout(a); box.setOrientation(LinearLayout.VERTICAL); box.setGravity(Gravity.CENTER); box.setPadding(dp(20),dp(90),dp(20),dp(20));
        TextView h=tv("پیشرفت آموزشی",25,gold); h.setTypeface(null,1);
        TextView p=tv(caseCompleted?"پرونده ۰۱ تکمیل شده\nپیشرفت فصل: ۱۰۰٪\nامتیاز آموزشی: ۱۰۰":"پرونده ۰۱ در حال انجام\nپیشرفت فصل: ۲۵٪\nمفاهیم کشف‌شده: خواهان، خوانده، خواسته، دادخواست",18,white);
        p.setGravity(Gravity.CENTER);
        box.addView(h,new LinearLayout.LayoutParams(-1,dp(70)));
        box.addView(p,new LinearLayout.LayoutParams(-1,dp(180)));
        Button b=btn("📖  باز کردن واژه‌نامه"); b.setOnClickListener(v->showGlossary());
        box.addView(b,new LinearLayout.LayoutParams(-1,dp(58)));
        addFull(box);
    }

    void showArchive() {
        history.push(screen); screen=10; clear(); overlayTitle("بایگانی",true);
        LinearLayout box=new LinearLayout(a); box.setOrientation(LinearLayout.VERTICAL); box.setGravity(Gravity.CENTER); box.setPadding(dp(25),dp(90),dp(25),dp(20));
        TextView h=tv("پرونده‌های تکمیل‌شده",22,gold); h.setTypeface(null,1);
        TextView p=tv(caseCompleted?"✓ حقیقت پشت یک امضا\nپرونده تکمیل و در بایگانی ثبت شد.":"هنوز پرونده‌ای تکمیل نشده است.\nپرونده جاری از میز دفتر قابل ادامه است.",17,white);
        p.setGravity(Gravity.CENTER);
        box.addView(h,new LinearLayout.LayoutParams(-1,dp(70))); box.addView(p,new LinearLayout.LayoutParams(-1,dp(180)));
        addFull(box);
    }

    void showSettings() {
        Toast.makeText(a,"تنظیمات صدا و رابط کاربری در نسخه بعدی تکمیل می‌شود.",Toast.LENGTH_SHORT).show();
    }

    boolean goBack() {
        if(history.isEmpty()) return false;
        int s=history.pop();
        if(s==0) showSplash(); else if(s==1) showOffice(); else if(s==2) showLibrary();
        else if(s==3) showCase(); else if(s==4) showGameplay(); else if(s==5) showEvidence();
        else if(s==6) showQuiz(); else if(s==7) showResult(); else if(s==8) showGlossary();
        else if(s==9) showStats(); else if(s==10) showArchive();
        return true;
    }

    void toast(String s){ Toast.makeText(a,s,Toast.LENGTH_SHORT).show(); }
}
