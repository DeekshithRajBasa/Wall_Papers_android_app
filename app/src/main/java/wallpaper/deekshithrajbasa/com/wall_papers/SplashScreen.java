package wallpaper.deekshithrajbasa.com.wall_papers;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.karumi.dexter.listener.single.PermissionListener;

import java.util.List;

import wallpaper.deekshithrajbasa.com.wall_papers.Manifest.permission;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static wallpaper.deekshithrajbasa.com.wall_papers.Manifest.permission.*;

public class SplashScreen extends AppCompatActivity {
    private int currentApiVersion;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        hidnav();

        int SPLASH_TIME_OUT = 1000;
        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {

                Intent i = new Intent(SplashScreen.this, MainActivity.class);
                startActivity(i);
                finish();

            }
        }, SPLASH_TIME_OUT);

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




}

