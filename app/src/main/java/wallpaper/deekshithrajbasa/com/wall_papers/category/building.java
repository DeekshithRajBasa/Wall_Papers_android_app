package wallpaper.deekshithrajbasa.com.wall_papers.category;

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

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;
import wallpaper.deekshithrajbasa.com.wall_papers.R;
import wallpaper.deekshithrajbasa.com.wall_papers.utils.SimpleDividerItemDecoration;
import wallpaper.deekshithrajbasa.com.wall_papers.utils.customview;
import wallpaper.deekshithrajbasa.com.wall_papers.adapter.instagramAdapter;

public class building extends AppCompatActivity {
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
        setContentView(R.layout.activity_building);
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
        myref = FirebaseDatabase.getInstance().getReference().child("/building");
        FirebaseRecyclerAdapter<instagramAdapter, building.BlogViewHolder> recyclerAdapter = new FirebaseRecyclerAdapter<instagramAdapter, building.BlogViewHolder>(
                instagramAdapter.class,
                R.layout.individual_row,
                building.BlogViewHolder.class,
                myref
        )
        {
            @Override
            protected void populateViewHolder(building.BlogViewHolder viewHolder, instagramAdapter model, int position) {
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
                    passdata.putExtra("image",imageUrl.get(position));
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
