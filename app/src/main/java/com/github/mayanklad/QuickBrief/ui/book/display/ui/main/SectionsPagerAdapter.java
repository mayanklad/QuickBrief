package com.github.mayanklad.QuickBrief.ui.book.display.ui.main;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.github.mayanklad.QuickBrief.Books;
import com.github.mayanklad.QuickBrief.R;
import com.github.mayanklad.QuickBrief.ui.book.display.CommentsFragment;
import com.github.mayanklad.QuickBrief.ui.book.display.DetailsFragment;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

    //private Books book;
    //private String comments_url;
    private Fragment[] fragments;

    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.tab_text_1, R.string.tab_text_2};
    private final Context mContext;

    public SectionsPagerAdapter(Context context, FragmentManager fm, Books book, String comments_url) {
        super(fm);
        mContext = context;
        //this.book = book;
        //this.comments_url = comments_url;
        fragments = new Fragment[] {
                new DetailsFragment(book),
                new CommentsFragment(comments_url)
        };
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        /*Fragment fragment = null;
        switch (position) {
            case 0:
                fragment = new DetailsFragment(book);
                break;
            case 1:
                fragment = new CommentsFragment(comments_url);
        }
        return fragment;*/
        return fragments[position];
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getResources().getString(TAB_TITLES[position]);
    }

    @Override
    public int getCount() {
        // Show 2 total pages.
        //return 2;
        return fragments.length;
    }
}