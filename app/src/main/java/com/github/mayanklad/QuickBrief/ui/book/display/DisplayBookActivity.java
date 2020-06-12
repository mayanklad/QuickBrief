package com.github.mayanklad.QuickBrief.ui.book.display;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.github.mayanklad.QuickBrief.Books;
import com.github.mayanklad.QuickBrief.NetworkTask;
import com.github.mayanklad.QuickBrief.R;
import com.github.mayanklad.QuickBrief.ui.book.select.Model;
import com.github.mayanklad.QuickBrief.ui.book.select.SelectBookActivity;
import com.github.mayanklad.QuickBrief.ui.ocr.OcrActivity;
import com.squareup.picasso.Picasso;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.List;

public class DisplayBookActivity extends AppCompatActivity {

    private static final String TAG = "DisplayBookActivity";
    private String url = "https://www.goodreads.com/book/show.xml?key=PsvPRqj9yNt4VF4mc62iDQ&id=";//8921";

    private Books book;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_book);

        //ActionBar action = getSupportActionBar();

        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();
        String message = intent.getStringExtra(SelectBookActivity.EXTRA_MESSAGE);
        Log.i(TAG, "Message got from SelectBookActivity: \'"+message+"\'");
        url += message.trim();
        Log.i(TAG, "DisplayBookActivity url: \'"+url+"\'");

        new NetworkTask(this, url).execute();

    }

    public void callBackData(Books book) {
        this.book = book;
        try {
            display();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_display_book, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.home) {
            showHomeActivity();
        }
        return super.onOptionsItemSelected(item);
    }

    private void showHomeActivity() {
        Intent intent = new Intent(this, OcrActivity.class);
        startActivity(intent);
        finish();
    }

    private void display() throws ParseException {

        ImageView coverImage = findViewById(R.id.coverImage);
        Picasso.get().load(book.getImage_url()).into(coverImage);

        TextView title = findViewById(R.id.title);
        title.setText(book.getTitle());

        TextView authors = findViewById(R.id.authors);
        String st_authors = "by ";
        Iterator<String> iter = book.getAuthors().iterator();
        while (iter.hasNext()) {
            st_authors += iter.next();
            if(iter.hasNext()) {
                st_authors += ", ";
            }
        }
        authors.setText(st_authors);

        String date_text = book.getPublication_day()+"-"+book.getPublication_month()+"-"+book.getPublication_year();
        date_text = DateFormat.getDateInstance(DateFormat.MEDIUM).format(new SimpleDateFormat("dd-MM-yyyy").parse(date_text));
        TextView txt = findViewById(R.id.publish);
        txt.setText(book.getFormat()+", "+book.getNum_pages()+" pages\n"+"Published on "+date_text+" by "+book.getPublisher());

        TextView average_rating = findViewById(R.id.average_rating);
        average_rating.setText(book.getAverage_rating());
        String circle_color = "#000000";
        if (Float.parseFloat(book.getAverage_rating()) >= 4) {
            circle_color = "#57e32c";
        } else if (Float.parseFloat(book.getAverage_rating()) >= 3) {
            circle_color = "#b7dd29";
        } else if (Float.parseFloat(book.getAverage_rating()) >= 2) {
            circle_color = "#ffe234";
        } else if (Float.parseFloat(book.getAverage_rating()) >= 1) {
            circle_color = "#ffa534";
        } else if (Float.parseFloat(book.getAverage_rating()) >= 0) {
            circle_color = "#ff4545";
        }
        ((GradientDrawable)average_rating.getBackground().getCurrent()).setColor(Color.parseColor(circle_color));

        ProgressBar pb_rating_5 = findViewById(R.id.pb_rating_5);
        pb_rating_5.setMax(Integer.parseInt(book.getRatings().getString("total")));
        pb_rating_5.setProgress(Integer.parseInt(book.getRatings().getString("5")));

        ProgressBar pb_rating_4 = findViewById(R.id.pb_rating_4);
        pb_rating_4.setMax(Integer.parseInt(book.getRatings().getString("total")));
        pb_rating_4.setProgress(Integer.parseInt(book.getRatings().getString("4")));

        ProgressBar pb_rating_3 = findViewById(R.id.pb_rating_3);
        pb_rating_3.setMax(Integer.parseInt(book.getRatings().getString("total")));
        pb_rating_3.setProgress(Integer.parseInt(book.getRatings().getString("3")));

        ProgressBar pb_rating_2 = findViewById(R.id.pb_rating_2);
        pb_rating_2.setMax(Integer.parseInt(book.getRatings().getString("total")));
        pb_rating_2.setProgress(Integer.parseInt(book.getRatings().getString("2")));

        ProgressBar pb_rating_1 = findViewById(R.id.pb_rating_1);
        pb_rating_1.setMax(Integer.parseInt(book.getRatings().getString("total")));
        pb_rating_1.setProgress(Integer.parseInt(book.getRatings().getString("1")));

        TextView txt_rating_5 = findViewById(R.id.txt_rating_5);
        txt_rating_5.setText(String.valueOf((100 * Integer.parseInt(book.getRatings().getString("5"))) / Integer.parseInt(book.getRatings().getString("total"))) +"% ("+book.getRatings().getString("5")+")");

        TextView txt_rating_4 = findViewById(R.id.txt_rating_4);
        txt_rating_4.setText(String.valueOf((100 * Integer.parseInt(book.getRatings().getString("4"))) / Integer.parseInt(book.getRatings().getString("total")))+"% ("+book.getRatings().getString("4")+")");

        TextView txt_rating_3 = findViewById(R.id.txt_rating_3);
        txt_rating_3.setText(String.valueOf((100 * Integer.parseInt(book.getRatings().getString("3"))) / Integer.parseInt(book.getRatings().getString("total")))+"% ("+book.getRatings().getString("3")+")");

        TextView txt_rating_2 = findViewById(R.id.txt_rating_2);
        txt_rating_2.setText(String.valueOf((100 * Integer.parseInt(book.getRatings().getString("2"))) / Integer.parseInt(book.getRatings().getString("total")))+"% ("+book.getRatings().getString("2")+")");

        TextView txt_rating_1 = findViewById(R.id.txt_rating_1);
        txt_rating_1.setText(String.valueOf((100 * Integer.parseInt(book.getRatings().getString("1"))) / Integer.parseInt(book.getRatings().getString("total")))+"% ("+book.getRatings().getString("1")+")");

        TextView description = findViewById(R.id.description);
        description.setText(book.getDescription().replace("<br />", "\n"));
    }

    /*@Override
    protected void onStop () {
        super.onStop();
        if (queue != null) {
            queue.cancelAll(this);
        }
    }*/


    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        Intent intent = new Intent(this, OcrActivity.class);
        startActivity(intent);
        finish();
    }
}

