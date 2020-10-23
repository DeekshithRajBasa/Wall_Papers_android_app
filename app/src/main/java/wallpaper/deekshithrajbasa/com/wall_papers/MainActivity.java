package wallpaper.deekshithrajbasa.com.wall_papers;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
//import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;
import wallpaper.deekshithrajbasa.com.wall_papers.adapter.instagramAdapter;
import wallpaper.deekshithrajbasa.com.wall_papers.category.Nature;
import wallpaper.deekshithrajbasa.com.wall_papers.category.building;
import wallpaper.deekshithrajbasa.com.wall_papers.category.flower;
import wallpaper.deekshithrajbasa.com.wall_papers.category.material;
import wallpaper.deekshithrajbasa.com.wall_papers.category.quotes;
import wallpaper.deekshithrajbasa.com.wall_papers.utils.SimpleDividerItemDecoration;
import wallpaper.deekshithrajbasa.com.wall_papers.utils.customview;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static final String EXTRA_URL = "imageurl";
    DatabaseReference dref;
    ListView listview;
    private RecyclerView recyclerView;
    private DatabaseReference myref;
    ArrayList<String> list = new ArrayList<>();
    Button button;
    Button copyText;
    TextView textView;
    public Intent i;
    static String desc;
    android.widget.Toolbar toolbar;
    private EditText mSearchField;
    private ImageButton mSearchBtn;

    //arrays to store image, title, description, int position(0,1,2,3..)
    public static ArrayList<String> imageUrl = new ArrayList<>();

    public static ArrayList<Integer> pos = new ArrayList<>();
    private InterstitialAd mInterstitialAd;
    private static final int PERMISSION_REQUEST_CODE = 200;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Thread.setDefaultUncaughtExceptionHandler(new material.MyUncaughtExceptionHandler());

        MobileAds.initialize(this,
                "ca-app-pub-7250174976428336~3101262175");

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-7250174976428336/9434859947");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());


        AdView adView = new AdView(this);
        adView.setAdSize(AdSize.BANNER);
        adView.setAdUnitId("ca-app-pub-7250174976428336/1382229664");

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //recycleview
        recyclerView = (RecyclerView) findViewById(R.id.Recycleview);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new SimpleDividerItemDecoration(this));
        recyclerView.setItemAnimator(new SlideInUpAnimator());
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(),2);
        gridLayoutManager.setOrientation(LinearLayoutManager.VERTICAL); // set Horizontal Orientation
        recyclerView.setLayoutManager(gridLayoutManager);
        //firebase
        myref = FirebaseDatabase.getInstance().getReference().child("/wallpaper");
        FirebaseRecyclerAdapter<instagramAdapter, BlogViewHolder> recyclerAdapter = new FirebaseRecyclerAdapter<instagramAdapter, BlogViewHolder>(
                instagramAdapter.class,
                R.layout.individual_row,
                BlogViewHolder.class,
                myref
        )
        {
            @Override
            protected void populateViewHolder(BlogViewHolder viewHolder, instagramAdapter model, int position) {

                //Lv-Edited
                viewHolder.imageView.setImageDrawable(getResources().getDrawable(R.drawable.loadingpic));

                viewHolder.setImage(model.getImage());
                viewHolder.setPosition(position);
                imageUrl.add("" + model.getImage());
                // StringTokenizer tokens = new StringTokenizer(model.getDescription(), "#");

                if(!pos.contains(position))
                    pos.add(position);

            }
        };
        recyclerView.setAdapter(recyclerAdapter);
      requestPermission();
    }

    public static class BlogViewHolder extends RecyclerView.ViewHolder {
        View mView;
        int position;
        private ImageView imageView;

        public BlogViewHolder(final View itemView) {
            super(itemView);

            mView = itemView;

            mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent passdata = new Intent(v.getContext(), customview.class);
                    passdata.putExtra("image", imageUrl.get(position));
                    v.getContext().startActivity(passdata);

                }
            });

            imageView = itemView.findViewById(R.id.image);

        }

        public void setPosition(int pos) {
            this.position = pos;
        }

        public void setImage(String image) {
            new getThumbnail().execute(image);
        }

        //Lv-edit
        public class getThumbnail extends AsyncTask<String,Void,Void>{

            Bitmap bitmap;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected Void doInBackground(String... strings) {

                try {
                    URL url = new URL(strings[0]);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setDoInput(true);
                    connection.connect();
                    InputStream inputStream = connection.getInputStream();

                    Bitmap fetchedBitmap=BitmapFactory.decodeStream(inputStream);

                    Float width = (float) fetchedBitmap.getWidth();
                    Float height = (float) fetchedBitmap.getHeight();
                    Float ratio = width/height;

                    bitmap= ThumbnailUtils.extractThumbnail(fetchedBitmap,(int)(250*ratio),250);

                }catch (Exception exception){
                    exception.printStackTrace();
                }

                return null;
            }

            @Override
            protected void onPostExecute(Void voidValue) {
                super.onPostExecute(voidValue);
                imageView.setImageBitmap(bitmap);
                }
         }
       }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
       // getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

//         if (id == R.id.nav_camera) {
//             //getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container ,new BuildingFragment()).commit();
//             // Handle the camera action
//         } else if (id == R.id.nav_gallery) {
//         startActivity(new Intent(getApplicationContext(),building.class));
//         } else if (id == R.id.nav_slideshow) {
//             startActivity(new Intent(getApplicationContext(),Nature.class));
//         } else if (id == R.id.nav_manage) {
//             startActivity(new Intent(getApplicationContext(),flower.class));
//         } else if (id == R.id.nav_share) {
//             startActivity(new Intent(getApplicationContext(),quotes.class));
//         } else if (id == R.id.nav_send) {
//             startActivity(new Intent(getApplicationContext(),material.class));
//         }

        switch(id){
        case R.id.nav_gallery : startActivity(new Intent(getApplicationContext(), building.class));break;
        case R.id.nav_slideshow : startActivity(new Intent(getApplicationContext(), Nature.class)); break;
        case R.id.nav_manage : startActivity(new Intent(getApplicationContext(), flower.class));break;
        case R.id.nav_share :  startActivity(new Intent(getApplicationContext(), quotes.class));break;
        case R.id.nav_send : startActivity(new Intent(getApplicationContext(),material.class)); break;
        
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

   /* private void check(){

        if (checkPermission()) {

            Snackbar.make(view, "Permission already granted.", Snackbar.LENGTH_LONG).show();

        } else {

            Snackbar.make(view, "Please request permission.", Snackbar.LENGTH_LONG).show();
        }


        if (!checkPermission()) {

            requestPermission();

        } else {

            Snackbar.make(view, "Permission already granted.", Snackbar.LENGTH_LONG).show();

        }
    }*/

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);
        int result1 = ContextCompat.checkSelfPermission(getApplicationContext(), READ_EXTERNAL_STORAGE);

        return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {

        ActivityCompat.requestPermissions(this, new String[]{WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0) {

                    boolean storageAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean readstorage = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                    if (storageAccepted&&readstorage)
                        Toast.makeText(getApplicationContext(), "Permission Granted, Now you can access to download wallpapers.", Toast.LENGTH_LONG).show();
                    else {

                        Toast.makeText(getApplicationContext(), "Permission Denied, You cannot access location data and camera.", Toast.LENGTH_LONG).show();

                        if (Build.VERSION.SDK_INT >= 23) {
                            if (shouldShowRequestPermissionRationale(WRITE_EXTERNAL_STORAGE)) {
                                showMessageOKCancel("You need to allow access to both the permissions",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                    requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE},
                                                            PERMISSION_REQUEST_CODE);
                                                }
                                            }
                                        });
                                return;
                            }
                        }

                    }
                }


                break;
        }
    }


    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(MainActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }


}


