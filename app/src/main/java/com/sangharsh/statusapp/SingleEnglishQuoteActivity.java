package com.sangharsh.statusapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.FileProvider;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.view.GestureDetector;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.ads.AdSize;
import com.facebook.ads.AdView;
import com.facebook.ads.AudienceNetworkAds;
import com.facebook.ads.InterstitialAd;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.sangharsh.statusapp.Model.EnglishQuoteModel;
import com.sangharsh.statusapp.Model.QuoteModel;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import static com.sangharsh.statusapp.FullenglishquoteActivity.quoteModelListenglish;
import static com.sangharsh.statusapp.FullquoteActivity.quoteModelList;


public class SingleEnglishQuoteActivity extends AppCompatActivity {
    private static final String FACEBOOK_ID="com.facebook.katana";
    public static TextView textView;
    public static EnglishQuoteModel qte1;
    ShareDialog shareDialog;
    RelativeLayout quotebglayout;
    CallbackManager callbackManager;
    CardView share_fb,share_instagram, share_wh, share_multi,copy_button;
    OnSwipeTouchListener onSwipeTouchListener;
    RelativeLayout swipelayout;
    public static int currentQuote = 0;
    InterstitialAd interstitialAd;

    AdView adView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_english_quote);
        share_multi = findViewById(R.id.share_share);
        share_fb = findViewById(R.id.share_fb);
        copy_button = findViewById(R.id.copy_quote);
        share_wh = findViewById(R.id.share_wh);
        share_instagram = findViewById(R.id.share_insta);
        swipelayout = findViewById(R.id.swipelayout);
        onSwipeTouchListener = new OnSwipeTouchListener(this, findViewById(R.id.swipelayout));
        textView = findViewById(R.id.single_quote);
        quotebglayout = findViewById(R.id.shareLayout_with_text);
        AudienceNetworkAds.initialize(this);
        interstitialAd = new InterstitialAd(this, getString(R.string.fb_interstitial_ad));

        adView = new AdView(this, getString(R.string.fb_banner_ads), AdSize.BANNER_HEIGHT_50);

        // Find the Ad Container
        LinearLayout adContainer = (LinearLayout) findViewById(R.id.banner_container);

        // Add the ad view to your activity layout
        adContainer.addView(adView);
        callbackManager = CallbackManager.Factory.create();
        shareDialog = new ShareDialog(this);
        // Request an ad
        adView.loadAd();
        interstitialAd.loadAd();
        final Intent intent = getIntent();
        String quote =  intent.getStringExtra("quote");

        textView.setText(quote);
        textView.setTextSize(20);
        Typeface custom_font = Typeface.createFromAsset(getApplicationContext().getAssets(), "Roboto-Regular.ttf");
        textView.setTypeface(custom_font);
        currentQuote = getIntent().getExtras().getInt("position");

        textView.setText(quote);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        copy_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String body = textView.getText().toString();
                ClipboardManager clipboard = (ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText(null,body);
                if (clipboard == null) return;
                clipboard.setPrimaryClip(clip);
                Toast.makeText(SingleEnglishQuoteActivity.this, "Copied", Toast.LENGTH_SHORT).show();

            }
        });

        share_multi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(Intent.ACTION_SEND);
                myIntent.setType("text/plain");
                String body = textView.getText().toString();
                String sub = "Download Status App";
                myIntent.putExtra(Intent.EXTRA_SUBJECT,sub);
                myIntent.putExtra(Intent.EXTRA_TEXT,body);
                view.getContext().startActivity(Intent.createChooser(myIntent, "Share Using"));


            }
        });
        share_wh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setPackage("com.whatsapp");
                shareIntent.setType("text/plain");

                String contentShare = textView.getText().toString();
                shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Download Now:" +
                        view.getContext(). getString(R.string.play_store_link) + view.getContext().getPackageName() + "\n\n" + contentShare + "\n\n" );
                view.getContext().startActivity(Intent.createChooser(shareIntent, "Share via"));

                try {
                    view.getContext().startActivity(shareIntent);
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(view.getContext(), "Whatsapp have not been installed", Toast.LENGTH_SHORT).show();
                    //ToastHelper.MakeShortText("Whatsapp have not been installed.");
                }
            }
        });
        share_instagram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setPackage("com.instagram.android");
                shareIntent.setType("text/plain");

                String contentShare = textView.getText().toString();
                shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Download Now:" +
                        view.getContext().getString(R.string.play_store_link) + view.getContext().getPackageName() + "\n\n" + contentShare + "\n\n" );
                view.getContext().startActivity(Intent.createChooser(shareIntent, "Share via"));

                try {
                    view.getContext().startActivity(shareIntent);
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(view.getContext(), "Instagram have not been installed", Toast.LENGTH_SHORT).show();
                    //ToastHelper.MakeShortText("Whatsapp have not been installed.");
                }

            }
        });
        share_fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              share();
            }
        });

    }

    private void share(){
        Bitmap bitmap = getBitmapFromView(quotebglayout);
        try {
            File file = new File(this.getExternalCacheDir(),"www.jpg");
            FileOutputStream fout = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG,100,fout);
            fout.flush();
            file.setReadable(true,false);

            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra(Intent.EXTRA_STREAM,FileProvider.getUriForFile(SingleEnglishQuoteActivity.this, BuildConfig.APPLICATION_ID +
                    ".provider",file));
            intent.setType("image/*");
            intent.setPackage(FACEBOOK_ID);
            startActivity(Intent.createChooser(intent,"Share"));
            try {
                startActivity(intent);
            } catch (android.content.ActivityNotFoundException ex) {
                Toast.makeText(SingleEnglishQuoteActivity.this, "Facebook have not been installed", Toast.LENGTH_SHORT).show();
                //ToastHelper.MakeShortText("Whatsapp have not been installed.");
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("ResourceAsColor")
    private Bitmap getBitmapFromView(View view){
        Bitmap returnedBitmap = Bitmap.createBitmap(view.getWidth(),view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        Drawable bgDrawable = view.getBackground();
        if(bgDrawable != null){
            bgDrawable.draw(canvas);
        }else{
            canvas.drawColor(android.R.color.white);
        }
        view.draw(canvas);

        return returnedBitmap;
    }


    @Override
    protected void onDestroy() {
        if (adView != null) {
            adView.destroy();
        }
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(interstitialAd.isAdLoaded()){
            interstitialAd.show();
        }else{
            super.onBackPressed();
        }

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }

    public static class OnSwipeTouchListener implements View.OnTouchListener {
        private final GestureDetector gestureDetector;
        Context context;

        OnSwipeTouchListener(Context ctx, View mainView) {
            gestureDetector = new GestureDetector(ctx, new OnSwipeTouchListener.GestureListener());
            mainView.setOnTouchListener(this);
            context = ctx;
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            return gestureDetector.onTouchEvent(event);
        }

        public class GestureListener extends
                GestureDetector.SimpleOnGestureListener {
            private static final int SWIPE_THRESHOLD = 100;
            private static final int SWIPE_VELOCITY_THRESHOLD = 100;

            @Override
            public boolean onDown(MotionEvent e) {
                return true;
            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                boolean result = false;
                try {
                    float diffY = e2.getY() - e1.getY();
                    float diffX = e2.getX() - e1.getX();
                    if (Math.abs(diffX) > Math.abs(diffY)) {
                        if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                            if (diffX > 0) {
                                onSwipeRight();
                            } else {
                                onSwipeLeft();
                            }
                            result = true;
                        }
                    } else if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
                        if (diffY > 0) {
                            onSwipeBottom();
                        } else {
                            onSwipeTop();
                        }
                        result = true;
                    }
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
                return result;
            }
        }

        void onSwipeRight() {
            if (currentQuote > 0) {
                currentQuote--;
                qte1 = quoteModelListenglish.get(currentQuote);
                textView.setText(Html.fromHtml(qte1.getQuote()));
            }

            this.onSwipe.swipeRight();
        }

        void onSwipeLeft() {
            if (currentQuote < (quoteModelListenglish.size() - 1)) {
                currentQuote++;
                qte1= quoteModelListenglish.get(currentQuote);
                textView.setText(Html.fromHtml(qte1.getQuote()));

            }
            //Toast.makeText(context, "Swiped Left", Toast.LENGTH_SHORT).show();

            this.onSwipe.swipeLeft();
        }

        void onSwipeTop() {
            //Toast.makeText(context, "Swiped Up", Toast.LENGTH_SHORT).show();
            this.onSwipe.swipeTop();
        }

        void onSwipeBottom() {
            //  Toast.makeText(context, "Swiped Down", Toast.LENGTH_SHORT).show();
            this.onSwipe.swipeBottom();
        }

        interface onSwipeListener {
            void swipeRight();

            void swipeTop();

            void swipeBottom();

            void swipeLeft();
        }

       OnSwipeTouchListener.onSwipeListener onSwipe;
    }
}