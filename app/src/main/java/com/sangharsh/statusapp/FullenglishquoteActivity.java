package com.sangharsh.statusapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.FacebookSdk;
import com.facebook.ads.AdSize;
import com.facebook.ads.AdView;
import com.facebook.ads.InterstitialAd;
import com.sangharsh.statusapp.Adapters.EnglishQuoteAdapter;
import com.sangharsh.statusapp.Model.EnglishQuoteModel;
import com.sangharsh.statusapp.Model.QuoteModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.sangharsh.statusapp.api.GET_ENGLISH_QUOTE;

public class FullenglishquoteActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    TextView quotetext;
    public static List<EnglishQuoteModel> quoteModelListenglish = new ArrayList<>();
    ImageButton share_insta, share_fb,share_wh,share_direct;
    EnglishQuoteAdapter adapter;
    EnglishQuoteModel quoteModel1;
    String header_name;
    InterstitialAd interstitialAd;
    ProgressBar progressBar;
    AdView adView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullenglishquote);
        FacebookSdk.sdkInitialize(this);
        interstitialAd = new InterstitialAd(this, getString(R.string.fb_interstitial_ad));

        adView = new AdView(this, getString(R.string.fb_banner_ads), AdSize.BANNER_HEIGHT_50);

        // Find the Ad Container
        LinearLayout adContainer = (LinearLayout) findViewById(R.id.banner_container);

        // Add the ad view to your activity layout
        adContainer.addView(adView);

        // Request an ad
        adView.loadAd();
        progressBar = findViewById(R.id.progressBar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        recyclerView = findViewById(R.id.quote_RecyclerView);
        quotetext = findViewById(R.id.quotetext);

        share_direct = findViewById(R.id.share_share);
        share_fb = findViewById(R.id.share_fb);
        share_wh = findViewById(R.id.share_wh);
        share_insta = findViewById(R.id.share_insta);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        Intent intent = getIntent();
        header_name = intent.getStringExtra("cname");
        getSupportActionBar().setTitle(header_name);
        quoteModelListenglish = new ArrayList<EnglishQuoteModel>();
        loadHindiQuotes();

        adapter = new EnglishQuoteAdapter(quoteModelListenglish);
        recyclerView.setAdapter(adapter);
        interstitialAd.loadAd();

    }

    private void loadHindiQuotes() {
        progressBar.setVisibility(View.VISIBLE);
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET,  GET_ENGLISH_QUOTE, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject object = response.getJSONObject(i);
                        quoteModel1 = new EnglishQuoteModel();
                        quoteModel1.setQuote(object.getString("quotetext"));
                        String category = quoteModel1.setCategory(object.getString("category"));
                        Intent matchCategory = getIntent();
                        if (matchCategory.getStringExtra("cname").equals(category)){
                            quoteModelListenglish.add(quoteModel1);
                        }else {
                          //  Toast.makeText(FullenglishquoteActivity.this, "No Data Found", Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                Collections.reverse(quoteModelListenglish);
                adapter = new EnglishQuoteAdapter(quoteModelListenglish);
                recyclerView.setAdapter(adapter);
                progressBar.setVisibility(View.INVISIBLE);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(request);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
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
}