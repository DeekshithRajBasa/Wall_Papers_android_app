package wallpaper.deekshithrajbasa.com.wall_papers.utils;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.app.WallpaperManager;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import wallpaper.deekshithrajbasa.com.wall_papers.R;

public class customview extends AppCompatActivity {

    ImageView img;
    String ImageURL;
    FloatingActionButton setwall , download;
    public ProgressDialog progressDialog;
    public static ArrayList<String> imageUrl = new ArrayList<>();
    ProgressBar progressBar;
    private int currentApiVersion;


    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customview);

        download = (FloatingActionButton)findViewById(R.id.dwall);
        setwall = (FloatingActionButton) findViewById(R.id.setwall);
        img = (ImageView) findViewById(R.id.ImageView);
        hidnav();

        //  if (getIntent().getExtras().getString("image") != null) {
        ImageURL = getIntent().getExtras().getString("image");
        Picasso.with(getApplicationContext())
                .load(ImageURL)
                .into(img);
        imageUrl.add("" + ImageURL);

        // }

        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                @SuppressLint("WrongConstant") DownloadManager manager = (DownloadManager) getApplicationContext().getSystemService("download");
                DownloadManager.Request request = new DownloadManager.Request(Uri.parse(ImageURL));
                request.setTitle("Wallpapers(" +toString().trim() + ".png)");
                request.setDescription("Downloading Image..");
                request.setDestinationInExternalPublicDir("/Wallpapers", toString().trim() + ".png");
                request.setNotificationVisibility(0);
                request.setVisibleInDownloadsUi( true );
                Long reference = Long.valueOf(manager.enqueue(request));
                Toast.makeText(  getApplicationContext(),"Your file is getting download. To view your file, please check this folder internal storage/Wallpapers. ",Toast.LENGTH_LONG).show();
            }
        });


        setwall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageURL = getIntent().getExtras().getString("image");
                Log.v("title:",""+ImageURL);

//yey got it
                try {
                    WallpaperManager.getInstance(getApplicationContext()).setStream(new URL(ImageURL).openStream());
                    Toast.makeText(getApplicationContext(),"Wallpaper applied Sucessfully!",Toast.LENGTH_SHORT).show();
                    return;
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "error applying wallpaper", Toast.LENGTH_SHORT).show();
                   return;
                }catch (IOException e2){
                    e2.printStackTrace();
                    return;
                }
        }
        });


    }

    public void hidnav() {
        this.currentApiVersion = Build.VERSION.SDK_INT;
        if (this.currentApiVersion >= 19) {
            getWindow().getDecorView().setSystemUiVisibility(5894);
            final View decorView = getWindow().getDecorView();
            decorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
                public void onSystemUiVisibilityChange(int visibility) {
                    if ((visibility & 4) == 0) {
                        decorView.setSystemUiVisibility(5894);
                    }
                }
            });
        }
    }
    @SuppressLint({"NewApi"})
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (this.currentApiVersion >= 19 && hasFocus) {
            getWindow().getDecorView().setSystemUiVisibility(5894);
        }
    }

    public void showCustomDialogw() {

        final String down = getIntent().getStringExtra("image");
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.custom_dialog, null);
        dialogBuilder.setView(dialogView);
        final EditText edt = (EditText) dialogView.findViewById(R.id.edit1);
        dialogBuilder.setTitle("Save as");
        dialogBuilder.setCancelable(false);
        dialogBuilder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                if (edt.getText().toString() == null) {
                  Toast.makeText(  getApplicationContext(),"Filename can't be empty",Toast.LENGTH_SHORT).show();
                    return;
                }
                File directory = new File(Environment.getExternalStorageDirectory(), "Wallpapers");
                if (!directory.exists()) {

                    directory.mkdirs();
                }
            @SuppressLint("WrongConstant") DownloadManager manager = (DownloadManager) getApplicationContext().getSystemService("download");
                DownloadManager.Request request = new DownloadManager.Request(Uri.parse(down));
                request.setTitle("Wallpapers(" + edt.getText().toString().trim() + ".png)");
                request.setDescription("Downloading Image..");
                request.setDestinationInExternalPublicDir("/Wallpapers", edt.getText().toString().trim() + ".png");
                request.setNotificationVisibility(0);
                Long reference = Long.valueOf(manager.enqueue(request));
                Toast.makeText(  getApplicationContext(),"Your file is getting download, please check your notification drawer",Toast.LENGTH_LONG).show();
                customview.this.progressBar.setVisibility(View.GONE);
            }
        });
        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.dismiss();
              // getApplicationContext().progressBar.setVisibility(View.GONE);
            }
        });
        dialogBuilder.create().show();
    }

 /*   public class SetWallpaperTask extends AsyncTask<String, Void, Bitmap> {


        @Override
        protected Bitmap doInBackground(String... params) {
            ImageURL = getIntent().getExtras().getString("image");
            Log.v("title1:",""+imageUrl);

            Bitmap result = null;
            try {

                result = Picasso.with(getApplicationContext())
                        .load(ImageURL)
                        .get();

            } catch (IOException e) {
                e.printStackTrace();
            }

            WallpaperManager wallpaperManager = WallpaperManager.getInstance(getBaseContext());
            try {
                wallpaperManager.setBitmap(result);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            super.onPostExecute(result);

            WallpaperManager wallpaperManager = WallpaperManager.getInstance(getBaseContext());
            try {
                wallpaperManager.setBitmap(result);
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), "Set wallpaper successfully", Toast.LENGTH_SHORT).show();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = new ProgressDialog(getApplicationContext());
            progressDialog.setMessage("Please wait...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }
        */

        public boolean isNetworkavailable() {
            @SuppressLint("WrongConstant") NetworkInfo networkInfo = ((ConnectivityManager) getSystemService("connectivity")).getActiveNetworkInfo();
            return networkInfo != null && networkInfo.isConnectedOrConnecting();
        }
    }




