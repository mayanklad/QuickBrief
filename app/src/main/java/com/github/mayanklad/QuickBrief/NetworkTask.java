package com.github.mayanklad.QuickBrief;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

import com.github.mayanklad.QuickBrief.ui.book.display.DisplayBookActivity;
import com.github.mayanklad.QuickBrief.ui.book.select.Model;
import com.github.mayanklad.QuickBrief.ui.book.select.SelectBookActivity;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class NetworkTask extends AsyncTask<Object, Void, Object> {

    private SelectBookActivity selectBookActivity;
    private DisplayBookActivity displayBookActivity;
    private String url;
    private XmlPullParserFactory xmlFactoryObject;
    private ProgressDialog pDialog;
    boolean SelectBookActivity_flag = false;
    boolean DisplayBookActivity_flag = false;
    private List<Model> models;
    private Books book;

    private static final String TAG = "NetworkTask";


    public NetworkTask(SelectBookActivity activity, String url) {
        this.selectBookActivity = activity;
        this.url = url;
        models = new ArrayList<>();
        SelectBookActivity_flag = true;
        Log.i(TAG, "Call from SelectBookActivity");
    }

    public NetworkTask(DisplayBookActivity activity, String url) {
        this.displayBookActivity = activity;
        this.url = url;
        DisplayBookActivity_flag = true;
        book = new Books();
        Log.i(TAG, "Call from DisplayBookActivity");
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (SelectBookActivity_flag) {
            pDialog = new ProgressDialog(selectBookActivity);
        } else if (DisplayBookActivity_flag) {
            pDialog = new ProgressDialog(displayBookActivity);
        }
        pDialog.setTitle("Fetching data");
        pDialog.setMessage("Loading...");
        pDialog.show();
    }

    @Override
    protected Object doInBackground(Object... values) {
        try {
            URL url = new URL(this.url);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setReadTimeout(10000 /* milliseconds */);
            connection.setConnectTimeout(15000 /* milliseconds */);
            connection.setRequestMethod("GET");
            connection.setDoInput(true);
            connection.connect();
            InputStream stream = connection.getInputStream();

            xmlFactoryObject = XmlPullParserFactory.newInstance();
            XmlPullParser myParser = xmlFactoryObject.newPullParser();

            myParser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            myParser.setInput(stream, null);
            parseXML(myParser);
            stream.close();

        } catch (Exception e) {
            e.printStackTrace();
            Log.e("AsyncTask", "exception");
        } finally {
            return null;
        }
    }

    public void parseXML(XmlPullParser parser) {

        int event;

        if (SelectBookActivity_flag) {
            Log.i(TAG, "Parsing for SelectBookActivity");

            boolean best_book_tag = false;
            boolean found_id = false;
            int count = 0;

            //Model model;
            String title = "", id = "", image_url = "";

            try {
                event = parser.getEventType();
                while (event != XmlPullParser.END_DOCUMENT){
                    String name;
                    switch (event){
                        //case XmlPullParser.START_DOCUMENT:
                        //    break;
                        case XmlPullParser.START_TAG:
                            name = parser.getName();
                            if (name.equals("best_book")) {
                                best_book_tag = true;
                                //model = new Model();
                                count++;
                            } else if (name.equals("id") && !found_id && best_book_tag) {
                                id = parser.nextText();
                                found_id = true;
                            } else if (name.equals("title") && best_book_tag) {
                                title = parser.nextText();
                            } else if (name.equals("image_url") && best_book_tag) {
                                image_url = parser.nextText();
                            }
                            break;
                        case XmlPullParser.END_TAG:
                            name = parser.getName();
                            if (name.equals("best_book")) {
                                Log.i(TAG, "book added in models list!");
                                models.add(new Model(id, title, image_url));
                                best_book_tag = false;
                                found_id = false;
                                title = "";
                                id = "";
                                image_url = "";
                            }
                    }
                    if (count == 5) {
                        break;
                    } else {
                        event = parser.next();
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        } else if (DisplayBookActivity_flag) {
            Log.i(TAG, "Parsing for DisplayBookActivity");

            boolean end_parsing = false;
            boolean found_title = false;
            boolean found_isbn = false;
            boolean found_isbn13 = false;
            boolean found_image_url = false;
            boolean found_publication_year = false;
            boolean found_publication_month = false;
            boolean found_publication_day = false;
            boolean found_publisher = false;
            boolean found_description = false;
            boolean found_text_reviews_count = false;
            boolean found_rating_dist = false;
            boolean found_average_rating = false;
            boolean found_num_pages = false;
            boolean found_format = false;
            boolean found_authors = false;

            try {
                event = parser.getEventType();
                while (event != XmlPullParser.END_DOCUMENT){
                    String name;
                    switch (event){
                        //case XmlPullParser.START_DOCUMENT:
                        //    break;
                        case XmlPullParser.START_TAG:
                            name = parser.getName();
                            if (name.equals("title") && !found_title) {
                                book.setTitle(parser.nextText());
                                found_title = true;
                            } else if (name.equals("isbn") && !found_isbn) {
                                book.setIsbn(parser.nextText());
                                found_isbn = true;
                            } else if (name.equals("isbn13") && !found_isbn13) {
                                book.setIsbn13(parser.nextText());
                                found_isbn13 = true;
                            } else if (name.equals("image_url") && !found_image_url) {
                                book.setImage_url(parser.nextText());
                                found_image_url = true;
                            } else if (name.equals("publication_year") && !found_publication_year) {
                                book.setPublication_year(parser.nextText());
                                found_publication_year = true;
                            } else if (name.equals("publication_month") && !found_publication_month) {
                                book.setPublication_month(parser.nextText());
                                found_publication_month = true;
                            } else if (name.equals("publication_day") && !found_publication_day) {
                                book.setPublication_day(parser.nextText());
                                found_publication_day = true;
                            } else if (name.equals("publisher") && !found_publisher) {
                                book.setPublisher(parser.nextText());
                                found_publisher = true;
                            } else if (name.equals("description") && !found_description) {
                                book.setDescription(parser.nextText());
                                found_description = true;
                            } else if (name.equals("text_reviews_count") && !found_text_reviews_count) {
                                book.setText_reviews_count(parser.nextText());
                                found_text_reviews_count = true;
                            } else if (name.equals("rating_dist") && !found_rating_dist) {
                                book.setRating_dist(parser.nextText());
                                found_rating_dist = true;
                            } else if (name.equals("average_rating") && !found_average_rating) {
                                book.setAverage_rating(parser.nextText());
                                found_average_rating = true;
                            } else if (name.equals("num_pages") && !found_num_pages) {
                                book.setNum_pages(parser.nextText());
                                found_num_pages = true;
                            } else if (name.equals("format") && !found_format) {
                                book.setFormat(parser.nextText());
                                found_format = true;
                            } else if (!found_authors) {
                                if (name.equals("name")) {
                                    book.addAuthor(parser.nextText());
                                }
                            }
                            break;
                        case XmlPullParser.END_TAG:
                            name = parser.getName();
                            if (name.equals("authors")) {
                                found_authors = true;
                                end_parsing = true;
                            }
                    }
                    if (end_parsing) {
                        break;
                    } else {
                        event = parser.next();
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    protected void onPostExecute(Object values) {
        //call back data to main thread
        pDialog.dismiss();
        if (SelectBookActivity_flag) {
            Log.i(TAG, "Model content: "+models.get(0).getTitle());
            selectBookActivity.callBackData(models);
        } else if (DisplayBookActivity_flag) {
            displayBookActivity.callBackData(book);
        }
    }
}
