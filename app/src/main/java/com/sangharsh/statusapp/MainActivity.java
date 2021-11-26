package com.sangharsh.statusapp;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.facebook.ads.AdSize;
import com.facebook.ads.AdView;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.sangharsh.statusapp.Adapters.PageAdapter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    NavigationView navigationView;
    DrawerLayout drawer;
    TabLayout tabLayout;
    TabItem tabItem1, tabItem2,tabItem3;
    ViewPager viewPager;
    private AdView adView;
    View noInternet;
    ImageButton button;
    PageAdapter pageAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        noInternet = findViewById(R.id.nil);
        button = findViewById(R.id.refreshbtn);
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

        if (null != activeNetworkInfo) {
            if (activeNetworkInfo.getType() == ConnectivityManager.TYPE_WIFI) {

            }
            if (activeNetworkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                //  Toast.makeText(getActivity(), "Mobile", Toast.LENGTH_SHORT).show();
            }

        } else {
            // bannersliderViewPager.setVisibility(View.GONE);
//            wrong.setVisibility(View.GONE);
            // noInte.setVisibility(View.VISIBLE);
            noInternet.setVisibility(View.VISIBLE);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    recreate();
                }
            });
            Toast.makeText(this, "Check your internet connection", Toast.LENGTH_SHORT).show();

        }

   //     ActivityCompat.requestPermissions(MainActivity.this,new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
        adView = new AdView(this, getString(R.string.fb_banner_ads), AdSize.BANNER_HEIGHT_50);

        // Find the Ad Container
        LinearLayout adContainer = (LinearLayout) findViewById(R.id.banner_container);

        // Add the ad view to your activity layout
        adContainer.addView(adView);

        // Request an ad
        adView.loadAd();

        Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
       toolbar.setNavigationIcon(R.drawable.com_facebook_button_icon);

      //  printkeyHash();

        tabLayout=(TabLayout)findViewById(R.id.tablayout1);
        tabItem1=(TabItem)findViewById(R.id.tab1);
        tabItem2=(TabItem)findViewById(R.id.tab2);
        tabItem3=(TabItem)findViewById(R.id.tab3);
        viewPager=(ViewPager)findViewById(R.id.vpager);
        pageAdapter=new PageAdapter(getSupportFragmentManager(),tabLayout.getTabCount());
        viewPager.setAdapter(pageAdapter);

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());

                if(tab.getPosition()==0 || tab.getPosition()==1 || tab.getPosition()==2)
                    pageAdapter.notifyDataSetChanged();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        //listen for scroll or page change



        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view1);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.hamburgerBlack));
        toggle.setDrawerIndicatorEnabled(true);
        toggle.syncState();
      //  TabLayout.Tab tab = tabLayout.getTabAt(0);


              navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();


              if (id == R.id.rate_us) {
                    Uri uri= Uri.parse("https://play.google.com/store/apps/details?id="+getPackageName());
                    Intent intent = new Intent(Intent.ACTION_VIEW,uri);
                    startActivity(intent);
                }

                else if (id == R.id.privacy) {
                    Intent intent = new Intent(MainActivity.this,PrivacyActivity.class);
                    startActivity(intent);
                }
                else if (id == R.id.share) {
                    Intent share = new Intent(Intent.ACTION_SEND);
                    String linkshare = "https://play.google.com/store/apps/details?id="+getPackageName();
                    String sybject = "Download App"+R.string.app_name;
                    share.setType("text/plain");
                    share.putExtra(Intent.EXTRA_SUBJECT,sybject);
                    share.putExtra(Intent.EXTRA_TEXT,linkshare);
                    startActivity(Intent.createChooser(share,"Share using"));
                }

                else if (id == R.id.about) {
                    LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    int width = LinearLayout.LayoutParams.WRAP_CONTENT;
                    int height = LinearLayout.LayoutParams.WRAP_CONTENT;

                    PopupWindow pw = new PopupWindow(inflater.inflate(R.layout.about_popup, null, false),width,height, true);
                    pw.showAtLocation(findViewById(R.id.cm), Gravity.CENTER, 0, 0);
                }



//                else if (id == R.id.contact) {
//                    LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//                    int width = LinearLayout.LayoutParams.WRAP_CONTENT;
//                    int height = LinearLayout.LayoutParams.WRAP_CONTENT;
//                    PopupWindow pw = new PopupWindow(inflater.inflate(R.layout.about_popup, null, false),width,height, true);
//                    pw.showAtLocation(findViewById(R.id.cm), Gravity.CENTER, 0, 0);
//
//                }
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });
    }

    private void printkeyHash() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo("com.sangharsh.statusapp",
                    PackageManager.GET_SIGNATURES);

            for(Signature signature : info.signatures)
            {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash", Base64.encodeToString(md.digest(),Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }


}