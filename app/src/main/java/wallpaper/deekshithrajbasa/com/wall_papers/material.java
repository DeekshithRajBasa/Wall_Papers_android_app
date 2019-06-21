package wallpaper.deekshithrajbasa.com.wall_papers;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;

public class material extends AppCompatActivity {
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_material);
        Thread.setDefaultUncaughtExceptionHandler(new material.MyUncaughtExceptionHandler());


        recyclerView = (RecyclerView) findViewById(R.id.Recycleview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new SimpleDividerItemDecoration(this));
        recyclerView.setItemAnimator(new SlideInUpAnimator());
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(),2);
        gridLayoutManager.setOrientation(LinearLayoutManager.VERTICAL); // set Horizontal Orientation
        recyclerView.setLayoutManager(gridLayoutManager);
        //firebase
        myref = FirebaseDatabase.getInstance().getReference().child("/material");
        FirebaseRecyclerAdapter<instagramAdapter,material.BlogViewHolder> recyclerAdapter = new FirebaseRecyclerAdapter<instagramAdapter, material.BlogViewHolder>(
                instagramAdapter.class,
                R.layout.individual_row,
                material.BlogViewHolder.class,
                myref

        )
        {
            @Override
            protected void populateViewHolder(material.BlogViewHolder viewHolder, instagramAdapter model, int position) {

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



    }
    public static class MyUncaughtExceptionHandler implements Thread.UncaughtExceptionHandler {
        @Override
        public void uncaughtException(Thread thread, Throwable ex) {
            if(ex.getClass().equals(OutOfMemoryError.class))
            {
                try {
                    android.os.Debug.dumpHprofData("/sdcard/dump.hprof");
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
            ex.printStackTrace();
        }
    }

    public static class BlogViewHolder extends RecyclerView.ViewHolder {
        View mView;

        ImageView imageView;
        int position;

        public BlogViewHolder(final View itemView) {
            super(itemView);

            itemView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {


                    Intent passdata = new Intent(view.getContext(), customview.class);

                    passdata.putExtra("image", imageUrl.get(position));

                    view.getContext().startActivity(passdata);




                }
            });



            mView = itemView;

            imageView = (ImageView) itemView.findViewById(R.id.image);

        }



        public void setPosition(int pos) {
            this.position = pos;
        }

        public void setImage(String image) {
            new getThumbnail().execute(image);
        }

        //Lv-edit
        public class getThumbnail extends AsyncTask<String,Void,Void> {

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

                    Bitmap fetchedBitmap= BitmapFactory.decodeStream(inputStream);

                    Float width = (float) fetchedBitmap.getWidth();
                    Float height = (float) fetchedBitmap.getHeight();
                    Float ratio = width/height;

                    bitmap= ThumbnailUtils.extractThumbnail(fetchedBitmap,(int)(250*ratio),250);

                }catch (Exception e){
                    e.printStackTrace();
                }

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);

                imageView.setImageBitmap(bitmap);

            }

        }

    }
    }

