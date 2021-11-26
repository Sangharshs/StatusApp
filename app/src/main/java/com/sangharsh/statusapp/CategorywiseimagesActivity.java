package com.sangharsh.statusapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdSize;
import com.facebook.ads.AdView;
import com.facebook.ads.AudienceNetworkAds;
import com.facebook.ads.InterstitialAd;
import com.facebook.ads.InterstitialAdListener;
import com.sangharsh.statusapp.Adapters.EnglishCategoryAdapter;
import com.sangharsh.statusapp.Adapters.ImageAdapter;
import com.sangharsh.statusapp.Model.EnglishCategoryModel;
import com.sangharsh.statusapp.Model.Image_Model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.sangharsh.statusapp.api.GET_ENGLISH_CATEGORY;
import static com.sangharsh.statusapp.api.GET_IMAGES;

public class CategorywiseimagesActivity extends AppCompatActivity {

    public  RecyclerView recyclerView;
    ProgressBar progressBar;
    Image_Model image_model;
    ImageAdapter adapter;
    String title;
    private AdView adView;
    private InterstitialAd interstitialAd;
    public static List<Image_Model> image_modelList ;
    //RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categorywiseimages);

        AudienceNetworkAds.initialize(this);
        interstitialAd = new InterstitialAd(this, getString(R.string.fb_interstitial_ad));
        adView = new AdView(this, getString(R.string.fb_banner_ads), AdSize.BANNER_HEIGHT_50);

        // Find the Ad Container
        LinearLayout adContainer = (LinearLayout) findViewById(R.id.banner_container);

        // Add the ad view to your activity layout
        adContainer.addView(adView);

        // Request an ad
        adView.loadAd();
       // recyclerView = findViewById(R.id.recyclerView_images);
        recyclerView = findViewById(R.id.recyclerView_images);
        progressBar = findViewById(R.id.progressBar);

        final Intent intent= getIntent();
        title = intent.getStringExtra("cname");

        getSupportActionBar().setTitle(title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,3);
        recyclerView.setLayoutManager(gridLayoutManager);
        image_modelList = new ArrayList<>();
        getCategorywiseImages();
        image_modelList = new ArrayList<>();

//        image_modelList.add(new Image_Model("love",R.drawable.downloadone));
//        image_modelList.add(new Image_Model("love",R.drawable.download));
//        image_modelList.add(new Image_Model("love",R.drawable.download));

        adapter = new ImageAdapter(image_modelList);
        recyclerView.setAdapter(adapter);
       // recyclerView.setAdapter(adapter);

//        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView parent, View view, int position, long id) {
//              // Intent intent1 = new Intent(CategorywiseimagesActivity.this,FullImageActivity.class);
//              // intent1.putExtra("position",parent.getPositionForView(view));
//                Toast.makeText(CategorywiseimagesActivity.this, ""+image_modelList.get(position), Toast.LENGTH_SHORT).show();
//              // startActivity(intent1);
//            }
//        });
//        gridView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent1 = new Intent(CategorywiseimagesActivity.this,FullImageActivity.class);
//                //intent.putExtra("position",image_modelList.get());
//                startActivity(intent1);
//            }
//        });
        interstitialAd.setAdListener(new InterstitialAdListener() {
            @Override
            public void onInterstitialDisplayed(Ad ad) {

            }

            @Override
            public void onInterstitialDismissed(Ad ad) {

            }

            @Override
            public void onError(Ad ad, AdError adError) {
                // Ad error callback
        }

            @Override
            public void onAdLoaded(Ad ad) {

//                interstitialAd.show();
            }

            @Override
            public void onAdClicked(Ad ad) {

            }

            @Override
            public void onLoggingImpression(Ad ad) {

            }
        });

        // For auto play video ads, it's recommended to load the ad
        // at least 30 seconds before it is shown
        interstitialAd.loadAd();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

        private void getCategorywiseImages() {
        progressBar.setVisibility(View.VISIBLE);
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET,  GET_IMAGES, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject object = response.getJSONObject(i);
                        image_model = new Image_Model();

                        image_model.setImage(object.getString("image"));

                        String cat = image_model.setCategory(object.getString("category"));
                      //  Toast.makeText(CategorywiseimagesActivity.this, cat+"", Toast.LENGTH_SHORT).show();

                        Intent matchCategory = getIntent();
                           if (matchCategory.getStringExtra("cname").equals(cat)){
                                    image_modelList.add(image_model);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                Collections.reverse(image_modelList);
                if (image_modelList.isEmpty()){
                    Toast.makeText(CategorywiseimagesActivity.this, "No Data Found", Toast.LENGTH_SHORT).show();
                }
                adapter = new ImageAdapter(image_modelList);
                recyclerView.setAdapter(adapter);
              //  recyclerView.setAdapter(adapter);
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
    public void onBackPressed() {
        super.onBackPressed();
       if(interstitialAd.isAdLoaded()){
           interstitialAd.show();
       }else{
           super.onBackPressed();
       }

    }
}
