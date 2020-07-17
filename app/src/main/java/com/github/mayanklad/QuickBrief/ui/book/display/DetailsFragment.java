package com.github.mayanklad.QuickBrief.ui.book.display;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.github.mayanklad.QuickBrief.Books;
import com.github.mayanklad.QuickBrief.R;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Iterator;

public class DetailsFragment extends Fragment {

    private Books book;
    private String[][] similarBooks;
    Dialog similarBookPreview = null;
    private boolean longPressed = false;

    public DetailsFragment(Books book) {
        this.book = book;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_details, container, false);

        ImageView coverImage = view.findViewById(R.id.coverImage);
        Picasso.get().load(book.getImage_url()).into(coverImage);

        TextView title = view.findViewById(R.id.title);
        title.setText(book.getTitle());

        TextView authors = view.findViewById(R.id.authors);
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
        try {
            date_text = DateFormat.getDateInstance(DateFormat.MEDIUM).format(new SimpleDateFormat("dd-MM-yyyy").parse(date_text));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        TextView txt = view.findViewById(R.id.publish);
        txt.setText(book.getFormat()+", "+book.getNum_pages()+" pages\n"+"Published on "+date_text+" by "+book.getPublisher());

        TextView average_rating = view.findViewById(R.id.average_rating);
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

        ProgressBar pb_rating_5 = view.findViewById(R.id.pb_rating_5);
        pb_rating_5.setMax(Integer.parseInt(book.getRatings().getString("total")));
        pb_rating_5.setProgress(Integer.parseInt(book.getRatings().getString("5")));

        ProgressBar pb_rating_4 = view.findViewById(R.id.pb_rating_4);
        pb_rating_4.setMax(Integer.parseInt(book.getRatings().getString("total")));
        pb_rating_4.setProgress(Integer.parseInt(book.getRatings().getString("4")));

        ProgressBar pb_rating_3 = view.findViewById(R.id.pb_rating_3);
        pb_rating_3.setMax(Integer.parseInt(book.getRatings().getString("total")));
        pb_rating_3.setProgress(Integer.parseInt(book.getRatings().getString("3")));

        ProgressBar pb_rating_2 = view.findViewById(R.id.pb_rating_2);
        pb_rating_2.setMax(Integer.parseInt(book.getRatings().getString("total")));
        pb_rating_2.setProgress(Integer.parseInt(book.getRatings().getString("2")));

        ProgressBar pb_rating_1 = view.findViewById(R.id.pb_rating_1);
        pb_rating_1.setMax(Integer.parseInt(book.getRatings().getString("total")));
        pb_rating_1.setProgress(Integer.parseInt(book.getRatings().getString("1")));

        TextView txt_rating_5 = view.findViewById(R.id.txt_rating_5);
        txt_rating_5.setText(String.valueOf((100 * Integer.parseInt(book.getRatings().getString("5"))) / Integer.parseInt(book.getRatings().getString("total"))) +"% ("+book.getRatings().getString("5")+")");

        TextView txt_rating_4 = view.findViewById(R.id.txt_rating_4);
        txt_rating_4.setText(String.valueOf((100 * Integer.parseInt(book.getRatings().getString("4"))) / Integer.parseInt(book.getRatings().getString("total")))+"% ("+book.getRatings().getString("4")+")");

        TextView txt_rating_3 = view.findViewById(R.id.txt_rating_3);
        txt_rating_3.setText(String.valueOf((100 * Integer.parseInt(book.getRatings().getString("3"))) / Integer.parseInt(book.getRatings().getString("total")))+"% ("+book.getRatings().getString("3")+")");

        TextView txt_rating_2 = view.findViewById(R.id.txt_rating_2);
        txt_rating_2.setText(String.valueOf((100 * Integer.parseInt(book.getRatings().getString("2"))) / Integer.parseInt(book.getRatings().getString("total")))+"% ("+book.getRatings().getString("2")+")");

        TextView txt_rating_1 = view.findViewById(R.id.txt_rating_1);
        txt_rating_1.setText(String.valueOf((100 * Integer.parseInt(book.getRatings().getString("1"))) / Integer.parseInt(book.getRatings().getString("total")))+"% ("+book.getRatings().getString("1")+")");

        TextView description = view.findViewById(R.id.description);
        description.setText(book.getDescription().replace("<br />", "\n"));

        similarBooks = book.getSimilarBooks();

        ImageView similarBook1 = view.findViewById(R.id.similarBookImage1);
        Picasso.get().load(similarBooks[0][2]).into(similarBook1);

        ImageView similarBook2 = view.findViewById(R.id.similarBookImage2);
        Picasso.get().load(similarBooks[1][2]).into(similarBook2);

        ImageView similarBook3 = view.findViewById(R.id.similarBookImage3);
        Picasso.get().load(similarBooks[2][2]).into(similarBook3);

        ImageView similarBook4 = view.findViewById(R.id.similarBookImage4);
        Picasso.get().load(similarBooks[3][2]).into(similarBook4);

        //Similar Book Preview Dialog
        //View preview_book = inflater.inflate(R.layout.preview_book, container, false);
        //ImageView preview_image = preview_book.findViewById(R.id.similarBook);
        //Picasso.get().load("https://i.gr-assets.com/images/S/compressed.photo.goodreads.com/books/1586722975l/2767052._SX98_.jpg").into(preview_image);


        similarBook1.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                longPressed = true;
                longClick(similarBooks[0][1], similarBooks[0][2]);
                return true;
            }
        });
        similarBook1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shortClick(similarBooks[0][0]);
            }
        });

        similarBook1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int actionMasked = event.getActionMasked();
                switch (actionMasked) {
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        // hide preview
                        if(longPressed && similarBookPreview != null) {
                            similarBookPreview.dismiss();
                        }
                        break;
                }
                return false;
            }
        });

        similarBook2.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                longPressed = true;
                longClick(similarBooks[1][1], similarBooks[1][2]);
                return true;
            }
        });
        similarBook2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shortClick(similarBooks[1][0]);
            }
        });

        similarBook2.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int actionMasked = event.getActionMasked();
                switch (actionMasked) {
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        // hide preview
                        if(longPressed && similarBookPreview != null) {
                            similarBookPreview.dismiss();
                        }
                        break;
                }
                return false;
            }
        });

        similarBook3.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                longPressed = true;
                longClick(similarBooks[2][1], similarBooks[2][2]);
                return true;
            }
        });
        similarBook3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shortClick(similarBooks[2][0]);
            }
        });

        similarBook3.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int actionMasked = event.getActionMasked();
                switch (actionMasked) {
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        // hide preview
                        if(longPressed && similarBookPreview != null) {
                            similarBookPreview.dismiss();
                        }
                        break;
                }
                return false;
            }
        });

        similarBook4.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                longPressed = true;
                longClick(similarBooks[3][1], similarBooks[3][2]);
                return true;
            }
        });
        similarBook4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shortClick(similarBooks[3][0]);
            }
        });

        similarBook4.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int actionMasked = event.getActionMasked();
                switch (actionMasked) {
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        // hide preview
                        if(longPressed && similarBookPreview != null) {
                            similarBookPreview.dismiss();
                        }
                        break;
                }
                return false;
            }
        });

        return view;
    }

    public void shortClick(String book_id)
    {
        Toast.makeText(getActivity(), "Short Click!!!: "+book_id, Toast.LENGTH_LONG).show();

    }

    public void longClick(String title, String image_url)
    {
        Toast.makeText(getActivity(), "Press & hold!!!", Toast.LENGTH_LONG).show();
        similarBookPreview = new Dialog(getActivity());
        similarBookPreview.requestWindowFeature(Window.FEATURE_NO_TITLE);
        similarBookPreview.getWindow().setBackgroundDrawable(
                new ColorDrawable(Color.TRANSPARENT));
        similarBookPreview.setContentView(getLayoutInflater().inflate( R.layout.preview_book, null));
        ImageView omg = (ImageView) similarBookPreview.findViewById(R.id.similarBookImage);
        Picasso.get().load(image_url).into(omg);

        similarBookPreview.show();

    }
}
