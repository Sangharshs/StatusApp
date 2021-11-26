package com.sangharsh.statusapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import com.facebook.ads.AdSize;
import com.facebook.ads.AdView;
import com.facebook.ads.AudienceNetworkAds;
import com.facebook.ads.InterstitialAd;
import com.sangharsh.statusapp.Adapters.HindiCategoryAdapter;
import com.sangharsh.statusapp.Adapters.QuoteAdapter;
import com.sangharsh.statusapp.Model.HindiCategoryModel;
import com.sangharsh.statusapp.Model.QuoteModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.sangharsh.statusapp.api.GET_HINDI_CATEGORY;
import static com.sangharsh.statusapp.api.GET_HINDI_QUOTE;

public class FullquoteActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    TextView quotetext;
    public static List<QuoteModel> quoteModelList = new ArrayList<>();
    ImageButton share_insta, share_fb,share_wh,share_direct;
    QuoteAdapter adapter;
    QuoteModel quoteModel;
    String category;
    String header_name;
    private AdView adView;
    InterstitialAd interstitialAd;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullquote);
        progressBar = findViewById(R.id.progressBar);
        AudienceNetworkAds.initialize(this);
        interstitialAd = new InterstitialAd(this, getString(R.string.fb_interstitial_ad));

        adView = new AdView(this, getString(R.string.fb_banner_ads), AdSize.BANNER_HEIGHT_50);

        // Find the Ad Container
        LinearLayout adContainer = (LinearLayout) findViewById(R.id.banner_container);

        // Add the ad view to your activity layout
        adContainer.addView(adView);

        // Request an ad
        adView.loadAd();

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        recyclerView = findViewById(R.id.quote_RecyclerView);
        quotetext = findViewById(R.id.quotetext);

        share_direct = findViewById(R.id.share_share);
        share_fb = findViewById(R.id.share_fb);
        share_wh = findViewById(R.id.share_wh);
        share_insta = findViewById(R.id.share_insta);
        interstitialAd.loadAd();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        Intent intent = getIntent();
        header_name = intent.getStringExtra("cname");
        getSupportActionBar().setTitle(header_name);
        quoteModelList = new ArrayList<>();
        loadHindiQuotes();

    }

    private void loadHindiQuotes() {
        progressBar.setVisibility(View.VISIBLE);
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET,  GET_HINDI_QUOTE, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject object = response.getJSONObject(i);
                        quoteModel = new QuoteModel();
                        quoteModel.setQuote(object.getString("quotetext"));
                        category = quoteModel.setCategory(object.getString("category"));
                        Intent matchCategory = getIntent();
                        if (matchCategory.getStringExtra("cname").equals(category)){
                            quoteModelList.add(quoteModel);
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                if(quoteModelList.isEmpty()){
                    Toast.makeText(FullquoteActivity.this, "No Data", Toast.LENGTH_SHORT).show();
                }
                Collections.reverse(quoteModelList);
                adapter = new QuoteAdapter(quoteModelList);
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
}