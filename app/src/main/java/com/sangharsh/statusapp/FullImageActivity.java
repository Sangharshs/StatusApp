package com.sangharsh.statusapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.DownloadManager;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.os.Environment;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Path;
import android.graphics.Path.Direction;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookDialog;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.ads.AdSize;
import com.facebook.ads.AdView;
import com.facebook.ads.AudienceNetworkAds;
import com.facebook.ads.InterstitialAd;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareContent;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.model.ShareMediaContent;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareDialog;
import com.google.android.gms.common.api.Status;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.sangharsh.statusapp.Adapters.ImageAdapter;
import com.sangharsh.statusapp.Model.Image_Model;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static android.bluetooth.BluetoothGattDescriptor.PERMISSION_READ;
import static android.bluetooth.BluetoothGattDescriptor.PERMISSION_WRITE;
import static android.graphics.Path.Direction.*;
import static android.os.Environment.DIRECTORY_DOWNLOADS;
import static android.provider.ContactsContract.CommonDataKinds.Website.URL;
import static com.sangharsh.statusapp.CategorywiseimagesActivity.image_modelList;
import static com.sangharsh.statusapp.Constant.SAVE_FOLDER_NAME;
import static com.sangharsh.statusapp.Utils.RootDirectoryWhatsappShow;
import static com.sangharsh.statusapp.api.GET_IMAGE_FOLDER;

public class FullImageActivity extends AppCompatActivity {
    public static ImageView imageView;
    public static String image;
    RelativeLayout relativeLayout;
    // RecyclerView recyclerView;
    public static Image_Model qte;
    String fileUri;
    public String SaveFilePath = RootDirectoryWhatsappShow+ "/";
    Context context;
    CardView downoad_img, share_fb, share_instagram, share_wh, share_multi;
    OnSwipeTouchListener onSwipeTouchListener;
    RelativeLayout swipelayout, swipelayout1;
    public static int currentimage = 0;
    private InterstitialAd interstitialAd;
    CallbackManager callbackManager;
    ShareDialog shareDialog;
    private AdView adView;
    BitmapDrawable drawable;
    Bitmap bitmap;
    private static final String FACEBOOK_ID = "com.facebook.katana";
    int ci;

    public static String RootDirectoryLikee ="/Best dp & Status/";
   // public String SaveFilePath = RootDirectoryLikeeShow+ "/";
    public static File RootDirectoryLikeeShow = new
            File(Environment.getExternalStorageDirectory()
            + "/Download/Best_DP_&_STATUS/");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_image2);
        FacebookSdk.sdkInitialize(this);
        swipelayout = findViewById(R.id.swipelayout);
         relativeLayout = findViewById(R.id.swipelayout);
        AudienceNetworkAds.initialize(this);
        interstitialAd = new InterstitialAd(this, getString(R.string.fb_interstitial_ad));

        callbackManager = CallbackManager.Factory.create();
        shareDialog = new ShareDialog(this);
        adView = new AdView(this, getString(R.string.fb_banner_ads), AdSize.BANNER_HEIGHT_50);

        // Find the Ad Container
        LinearLayout adContainer = (LinearLayout) findViewById(R.id.banner_container);
        // Add the ad view to your activity layout
        adContainer.addView(adView);

        // Request an ad
        adView.loadAd();
        onSwipeTouchListener = new OnSwipeTouchListener(this, findViewById(R.id.swipelayout));
        imageView = findViewById(R.id.fullImage);

        final Intent intent = getIntent();
        image = intent.getStringExtra("image");

        final int[] cp = {intent.getIntExtra("position", 0)};


        Glide.with(imageView.getContext()).load(GET_IMAGE_FOLDER + image).into(imageView);


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);

        currentimage = getIntent().getExtras().getInt("position");
        ci = getIntent().getIntExtra("position",0);

       // Toast.makeText(getApplicationContext(), "Position: "+ci, Toast.LENGTH_SHORT).show();

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        Bundle extras = getIntent().getExtras();
        byte[] byteArray = extras.getByteArray("picture");

        share_wh = findViewById(R.id.share_wh);
        share_fb = findViewById(R.id.share_fb);
        share_multi = findViewById(R.id.share_share);
        share_instagram = findViewById(R.id.share_insta);
        downoad_img = findViewById(R.id.download_btn);


        checkPermission();
        downoad_img.setOnClickListener(new View.OnClickListener() {
          //  @android.support.annotation.RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
            @Override
            public void onClick(View view) {
//                checkFolder();
//                final String path = image_modelList.get(currentimage).getImage();
//                String filename=path.substring(path.lastIndexOf("/")+1);
//                final File file = new File(path);
//                File destFile = new File(SaveFilePath);
//                try {
//                    FileUtils.copyFileToDirectory(file, destFile);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                    Toast.makeText(FullImageActivity.this, "print Strack Trace", Toast.LENGTH_SHORT).show();
//                }
//                String fileNameChange=filename.substring(12);
//                File newFile = new File(SaveFilePath+fileNameChange);
//                String ContentType = "image/*";
//                if (image_modelList.get(currentimage).getImage().toString().endsWith(".mp4")){
//                    ContentType = "video/*";
//                }else {
//                    ContentType = "image/*";
//                }
//                MediaScannerConnection.scanFile(getApplicationContext(), new String[]{newFile.getAbsolutePath()}, new String[]{ContentType},
//                        new MediaScannerConnection.MediaScannerConnectionClient() {
//                            public void onMediaScannerConnected() {
//                            }
//
//                            public void onScanCompleted(String path, Uri uri) {
//                            }
//                        });
//
//                File from = new File(SaveFilePath,filename);
//                File to = new File(SaveFilePath,fileNameChange);
//                from.renameTo(to);
//
//                Toast.makeText(getApplicationContext(), getString(R.string.saved_to) + SaveFilePath + fileNameChange, Toast.LENGTH_LONG).show();

//0//
//               String path =  ((Image_Model)image_modelList.get(currentimage)).getImage();
//               final File file = new File(path);
//               String destpath = Environment.getExternalStorageDirectory().getAbsolutePath()+SAVE_FOLDER_NAME;
//               File destFile = new File(destpath);
//                try {
//                    FileUtils.copyFileToDirectory(file,destFile);
//                    Toast.makeText(getApplicationContext(), "Directory Copied", Toast.LENGTH_SHORT).show();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                    Toast.makeText(getApplicationContext(), "print Strack Trace", Toast.LENGTH_SHORT).show();
//                }
//                MediaScannerConnection.scanFile(
//                        getApplicationContext(),
//                        new String[]{destpath + file.getName()},
//                        new String[]{"*/*"},
//                new MediaScannerConnection.MediaScannerConnectionClient() {
//                    @Override
//                    public void onMediaScannerConnected() {
//
//                    }
//                    @Override
//                    public void onScanCompleted(String path, Uri uri) {
//
//                    }
//                }
//              );
//             Toast.makeText(getApplicationContext(),"Saved to"+destpath+file.getName(), Toast.LENGTH_SHORT).show();
//                Image_Model fileItem = image_modelList.get(currentimage);
//1//
//                Uri uri = Uri.parse( GET_IMAGE_FOLDER+image_modelList.get(currentimage).getImage());
//                DownloadManager.Request request = new DownloadManager.Request(uri);
//                request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE |
//                        DownloadManager.Request.NETWORK_WIFI);  // Tell on which network you want to download file.
//                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);  // This will show notification on top when downloading the file.
//                request.setTitle(getString(R.string.app_name)); // Title for notification.
//                request.setVisibleInDownloadsUi(true);
//                request.setDestinationInExternalPublicDir(DIRECTORY_DOWNLOADS,getString(R.string.app_name));  // Storage directory path
//                ((DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE)).enqueue(request); // This will start downloading
//                Toast.makeText(FullImageActivity.this, "Downloading Start", Toast.LENGTH_SHORT).show();
//                try {
//                    if (Build.VERSION.SDK_INT >= 19) {
//                        MediaScannerConnection.scanFile(getApplicationContext(), new String[]{new File(DIRECTORY_DOWNLOADS + "/"+getString(R.string.app_name)+
//                                    image_modelList.get(currentimage).getImage()).getAbsolutePath()},
//                                   null, new MediaScannerConnection.OnScanCompletedListener() {
//                                    public void onScanCompleted(String path, Uri uri) {
//                                    }
//                                });
//                    } else {
//                        sendBroadcast(new Intent("android.intent.action.MEDIA_MOUNTED", Uri.fromFile(new File(DIRECTORY_DOWNLOADS + "/"))));
//
//                    }
//                }catch (Exception e){
//                    e.printStackTrace();
//                }
// Toast.makeText(getApplicationContext(), link, Toast.LENGTH_SHORT).show();
//1//
                String link = GET_IMAGE_FOLDER + image_modelList.get(currentimage).getImage();
                Uri uri = Uri.parse(link);
                DownloadManager downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
                DownloadManager.Request request = new DownloadManager.Request(uri);
                request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);

                request.setTitle("Best Dp & Status");
                request.setDescription("Download Success");

                request.allowScanningByMediaScanner();
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

                request.setDestinationInExternalPublicDir(DIRECTORY_DOWNLOADS,"/BEST DP & STATUS/" + image);
                request.setMimeType("*/*");
                downloadManager.enqueue(request);

                Toast.makeText(getApplicationContext(), "Download Start...", Toast.LENGTH_SHORT).show();
            }
//                Bitmap bitmap ;
//                drawable = (BitmapDrawable) imageView.getDrawable();
//                bitmap = drawable.getBitmap();
//                String savedImagePath ;
//
//                String imageFileName = image_modelList.get(currentimage).getImage();
//                File storageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
//                        + "/"+getString(R.string.app_name));
//                boolean success = true;
//                storageDir.mkdirs();
//
//
//                    File imageFile = new File(storageDir, imageFileName);
//                    savedImagePath = imageFile.getAbsolutePath();
//                    try {
//                        OutputStream fOut = new FileOutputStream(imageFile);
//                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
//                        fOut.close();
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                try {
//                    OutputStream fOut = new FileOutputStream(imageFile);
//                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
//                    fOut.close();
//                    if (Build.VERSION.SDK_INT >= 19) {
//                        MediaScannerConnection.scanFile(getApplicationContext(), new String[]{new File(DIRECTORY_DOWNLOADS + "/"+getString(R.string.app_name)+
//                                        image_modelList.get(currentimage).getImage()).getAbsolutePath()},
//                                null, new MediaScannerConnection.OnScanCompletedListener() {
//                                    public void onScanCompleted(String path, Uri uri) {
//                                    }
//                                });
//                    } else {
//                        sendBroadcast(new Intent("android.intent.action.MEDIA_MOUNTED", Uri.fromFile(new File(DIRECTORY_DOWNLOADS + "/"+getString(R.string.app_name)+image_modelList.get(currentimage).getImage()))));
//                        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
//                        File f = new File(imageFileName);
//                        Uri contentUri = Uri.fromFile(imageFile);
//                        mediaScanIntent.setData(contentUri);
//                        sendBroadcast(mediaScanIntent);
//                    }
//                }catch (Exception e){
//                    e.printStackTrace();
//                }
//
//                Toast.makeText(getApplicationContext(), "IMAGE SAVED", Toast.LENGTH_LONG).show();
//
//           }
        });

        share_multi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareImage();
            }
        });
        share_wh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareImagewh();
            }
        });
        share_instagram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareImagewithInstagram();
            }
        });
        share_fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareImageonFb();
            }
        });
        interstitialAd.loadAd();
    }

    private void checkFolder() {
       String path = Environment.getExternalStorageDirectory().getAbsolutePath()+ SAVE_FOLDER_NAME;
       File dir = new File(path);

       boolean isderectorycreated = dir.exists();
      if(!isderectorycreated){
          isderectorycreated = dir.mkdir();
      }
      if(isderectorycreated){
          Toast.makeText(getApplicationContext(), "Directory Already Created", Toast.LENGTH_SHORT).show();
      }
    }

    private void shareImageonFb() {

        Bitmap bitmap = getBitmapFromView(imageView);
        try {
            File file = new File(this.getExternalCacheDir(),"download.jpg");
            FileOutputStream fOut = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,fOut);
            fOut.flush();
            fOut.close();
            file.setReadable(true,false);
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra(Intent.EXTRA_STREAM,FileProvider.getUriForFile(FullImageActivity.this, BuildConfig.APPLICATION_ID +
                    ".provider",file));
            intent.setType("image/*");
            intent.setPackage(FACEBOOK_ID);
            startActivity(Intent.createChooser(intent,"Share Image"));
            try {
                startActivity(intent);
            } catch (android.content.ActivityNotFoundException ex) {
                Toast.makeText(FullImageActivity.this, "Facebook have not been installed", Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e){
            e.printStackTrace();
        }


    }

    private void shareImagewithInstagram() {
        Bitmap bitmap = getBitmapFromView(imageView);
        try {
            File file = new File(this.getExternalCacheDir(),"download.jpg");
            FileOutputStream fOut = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,fOut);
            fOut.flush();
            fOut.close();
            file.setReadable(true,false);
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra(Intent.EXTRA_STREAM,FileProvider.getUriForFile(FullImageActivity.this, BuildConfig.APPLICATION_ID +
                    ".provider",file));
            intent.setType("image/*");
            intent.setPackage("com.instagram.android");
            startActivity(Intent.createChooser(intent,"Share Image"));
            try {
                startActivity(intent);
            } catch (android.content.ActivityNotFoundException ex) {
                Toast.makeText(FullImageActivity.this, "Instagram have not been installed", Toast.LENGTH_SHORT).show();
                //ToastHelper.MakeShortText("Whatsapp have not been installed.");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public static void copyFileToDirectory(final File srcFile, final File destDir, boolean b) throws IOException {
        copyFileToDirectory(srcFile, destDir, true);
    }
    private void shareImagewh() {
        Bitmap bitmap = getBitmapFromView(imageView);
        try {
            File file = new File(this.getExternalCacheDir(),"download.jpg");
            FileOutputStream fOut = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,fOut);
            fOut.flush();
            fOut.close();
            file.setReadable(true,false);
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra(Intent.EXTRA_STREAM,FileProvider.getUriForFile(FullImageActivity.this, BuildConfig.APPLICATION_ID +
                    ".provider",file));
            intent.setType("image/*");
            intent.setPackage("com.whatsapp");
            startActivity(Intent.createChooser(intent,"Share Image"));
            try {
                startActivity(intent);
            } catch (android.content.ActivityNotFoundException ex) {
                Toast.makeText(FullImageActivity.this, "Whatsapp have not been installed", Toast.LENGTH_SHORT).show();
                //ToastHelper.MakeShortText("Whatsapp have not been installed.");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void shareImage() {
        Bitmap bitmap = getBitmapFromView(imageView);
        try {
            File file = new File(this.getExternalCacheDir(),"download.jpg");
            FileOutputStream fOut = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,fOut);
            fOut.flush();
            fOut.close();
            file.setReadable(true,false);
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra(Intent.EXTRA_STREAM,FileProvider.getUriForFile(FullImageActivity.this, BuildConfig.APPLICATION_ID +
                    ".provider",file));
            intent.setType("image/*");
            startActivity(Intent.createChooser(intent,"Share Image"));
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private Bitmap getBitmapFromView(View view) {
        Bitmap returnBitmap = Bitmap.createBitmap(view.getWidth(),view.getHeight(),Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnBitmap);
        Drawable bgDrawable = view.getBackground();
        if(bgDrawable != null){
            bgDrawable.draw(canvas);
        }
        else{
            canvas.drawColor(Color.WHITE);
        }
        view.draw(canvas);
        return returnBitmap;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);



    }
    public static class OnSwipeTouchListener implements View.OnTouchListener {
        private final GestureDetector gestureDetector;
        Context context;
        OnSwipeTouchListener(Context ctx, View mainView) {
            gestureDetector = new GestureDetector(ctx, new GestureListener());
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
                    }
                    else if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
                        if (diffY > 0) {
                            onSwipeBottom();
                        } else {
                            onSwipeTop();
                        }
                        result = true;
                    }
                }
                catch (Exception exception) {
                    exception.printStackTrace();
                }
                return result;
            }
        }
        void onSwipeRight() {
            if (currentimage > 0){
                currentimage--;

                qte = image_modelList.get(currentimage);
                Glide.with(context).load(GET_IMAGE_FOLDER+qte.getImage()).into(imageView);


            }

        //    Toast.makeText(context, "Swiped Right", Toast.LENGTH_SHORT).show();
            this.onSwipe.swipeRight();
        }
        void onSwipeLeft() {
            if(currentimage < (image_modelList.size()-1)){
                currentimage++;
                qte = image_modelList.get(currentimage);
                Glide.with(context).load(GET_IMAGE_FOLDER+qte.getImage()).into(imageView);

              //  imageView.setImageResource(image);
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
        onSwipeListener onSwipe;
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
    public void SaveImage(String url) {
        Glide.with(this).asBitmap().load(url).into(new CustomTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap bitmap, Transition<? super Bitmap> transition) {
                try {
                    File mydir = new File(Environment.getExternalStorageDirectory() + "/11zon");
                    if (!mydir.exists()) {
                        mydir.mkdirs();
                    }

                    fileUri = mydir.getAbsolutePath() + File.separator + System.currentTimeMillis() + ".jpg";
                    FileOutputStream outputStream = new FileOutputStream(fileUri);

                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                    outputStream.flush();
                    outputStream.close();
                } catch(IOException e) {
                    e.printStackTrace();
                }
                Toast.makeText(getApplicationContext(), "Image Saved", Toast.LENGTH_LONG).show();
            }
            @Override
            public void onLoadCleared(Drawable placeholder) {
            }
        });
    }

    //runtime storage permission
    public boolean checkPermission() {
        int READ_EXTERNAL_PERMISSION = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE );
        int WRITE_EXTERNAL_PERMISSION = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if((READ_EXTERNAL_PERMISSION != PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_WRITE);
            return false;
        }
        if((WRITE_EXTERNAL_PERMISSION != PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_READ);
            return false;
        }
        return true;
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode==PERMISSION_WRITE && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            //do somethings
        }
    }
    private String saveImage(Bitmap image) {
        String savedImagePath = null;

        String imageFileName = "JPEG_" + "FILE_NAME" + ".jpg";
        File storageDir = new File(            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                + "/YOUR_FOLDER_NAME");
        boolean success = true;
        if (!storageDir.exists()) {
            success = storageDir.mkdirs();
        }
        if (success) {
            File imageFile = new File(storageDir, imageFileName);
            savedImagePath = imageFile.getAbsolutePath();
            try {
                OutputStream fOut = new FileOutputStream(imageFile);
                image.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
                fOut.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

            // Add the image to the system gallery
            galleryAddPic(savedImagePath);
            Toast.makeText(getApplicationContext(), "IMAGE SAVED", Toast.LENGTH_LONG).show();
        }
        return savedImagePath;
    }

    private void galleryAddPic(String imagePath) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(imagePath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        sendBroadcast(mediaScanIntent);
    }
}