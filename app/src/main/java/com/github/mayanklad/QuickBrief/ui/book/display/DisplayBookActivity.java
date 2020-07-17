package com.github.mayanklad.QuickBrief.ui.book.display;

import android.content.Intent;
import android.os.Bundle;

import com.github.mayanklad.QuickBrief.Books;
import com.github.mayanklad.QuickBrief.NetworkTask;
import com.github.mayanklad.QuickBrief.R;
import com.github.mayanklad.QuickBrief.ui.book.select.SelectBookActivity;
import com.github.mayanklad.QuickBrief.ui.ocr.OcrActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.github.mayanklad.QuickBrief.ui.book.display.ui.main.SectionsPagerAdapter;
import com.github.mayanklad.QuickBrief.NetworkTask;

import java.text.ParseException;

public class DisplayBookActivity extends AppCompatActivity {

    private ViewPager viewPager = null;
    private SectionsPagerAdapter sectionsPagerAdapter = null;
    private TabLayout tabs = null;

    private static final String TAG = "DisplayBookActivity";
    private String url = "https://www.goodreads.com/book/show.xml?key=PsvPRqj9yNt4VF4mc62iDQ&id=";//8921";

    private String comments_url = "https://www.goodreads.com/api/reviews_widget_iframe?did=DEVELOPER_ID&amp;format=html&amp;isbn=0316015849&amp;links=660&amp;min_rating=&amp;review_back=fff&amp;stars=000&amp;text=000";
    //private Books book;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_book);

        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();
        String message = intent.getStringExtra(SelectBookActivity.EXTRA_MESSAGE);
        Log.i(TAG, "Message got from SelectBookActivity: \'"+message+"\'");
        url += message.trim();
        Log.i(TAG, "DisplayBookActivity url: \'"+url+"\'");

        /*FloatingActionButton fab = findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        new NetworkTask(this, url).execute();

    }

    public void callBackData(Books book) {
        //this.book = book;

        sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager(), book, book.getComments_url());
        viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);

    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        Fragment comments = getSupportFragmentManager().getFragments().get(viewPager.getCurrentItem());
        if (comments instanceof CommentsFragment && ((CommentsFragment) comments).canGoBack()) {

            ((CommentsFragment) comments).goBack();

        } else {
            Intent intent = new Intent(this, OcrActivity.class);
            startActivity(intent);
            finish();
        }
    }
}