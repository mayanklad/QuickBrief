package com.github.mayanklad.QuickBrief.ui.book.select;

import android.animation.ArgbEvaluator;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.github.mayanklad.QuickBrief.NetworkTask;
import com.github.mayanklad.QuickBrief.R;
import com.github.mayanklad.QuickBrief.ui.book.display.DisplayBookActivity;
import com.github.mayanklad.QuickBrief.ui.ocr.OcrActivity;

import java.util.List;

public class SelectBookActivity extends AppCompatActivity {

    private static final String TAG = "SelectBookActivity";

    private String url = "https://www.goodreads.com/search/index.xml?key=PsvPRqj9yNt4VF4mc62iDQ&q=";
    private String book_id = "";
    public static String EXTRA_MESSAGE;
    ViewPager viewPager;
    Adapter adapter;
    List<Model> models;
    Integer[] colors = null;
    ArgbEvaluator argbEvaluator = new ArgbEvaluator();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_book);

        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();
        String message = intent.getStringExtra(OcrActivity.EXTRA_MESSAGE);
        Log.i(TAG, "Message got from OcrActivity: \'"+message+"\'");
        message = message.replace('\n', ' ');
        message = message.trim();
        message = message.replace(' ', '+');
        Log.i(TAG, "Message edited: \'"+message+"\'");
        url += message;
        Log.i(TAG, "SelectBookActivity url: \'"+url+"\'");

        new NetworkTask(this, url).execute();

    }

    public void callBackData(List<Model> models) {
        this.models = models;
        display();
    }

    private void display() {
        adapter = new Adapter(models, this);

        viewPager = findViewById(R.id.viewPager);
        viewPager.setAdapter(adapter);
        viewPager.setPadding(130, 0, 130, 0);

        Integer[] colors_temp = {
                getResources().getColor(R.color.color1),
                getResources().getColor(R.color.color2),
                getResources().getColor(R.color.color3),
                getResources().getColor(R.color.color4)
        };

        colors = colors_temp;

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (position < (adapter.getCount() - 1) && position < (colors.length - 1)) {
                    viewPager.setBackgroundColor(

                            (Integer) argbEvaluator.evaluate(
                                    positionOffset,
                                    colors[position],
                                    colors[position + 1]
                            )
                    );
                } else {
                    viewPager.setBackgroundColor(colors[colors.length - 1]);
                }
                book_id = models.get(position).getId();
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    public void onClick(View view) {
        Intent intent = new Intent(this, DisplayBookActivity.class);
        intent.putExtra(EXTRA_MESSAGE, book_id);
        Log.i(TAG, "Message sent from SelectBookActivity (book_id): \'"+book_id+"\'");
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        Intent intent = new Intent(this, OcrActivity.class);
        startActivity(intent);
        finish();
    }
}
