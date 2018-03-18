package brainstudio.s4pl.com.brainstudio;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;


import cat.ppicas.customtypeface.CustomTypeface;
import cat.ppicas.customtypeface.CustomTypefaceFactory;
import io.fabric.sdk.android.Fabric;
import layout.CommonFragment;
import proguard.annotation.Keep;
import proguard.annotation.KeepClassMembers;

@Keep
@KeepClassMembers
public class Programmes extends AppCompatActivity implements CommonFragment.OnFragmentInteractionListener{

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    TabLayout tabLayout;

    Toolbar toolbar;


   int[] tabIcons = {
            R.drawable.cube,
            R.drawable.juggling,
            R.drawable.hand,
            R.drawable.stack,
            R.drawable.calligraphy,
            R.drawable.analysis
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getLayoutInflater().setFactory(new CustomTypefaceFactory(
                this, CustomTypeface.getInstance()));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_programmes);
        Fabric.with(this, new Crashlytics());
        toolbar = (Toolbar) findViewById(R.id.each_program_toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.



        Log.v("common","in programmes.java on create");



        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.programmes_container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        tabLayout = (TabLayout) findViewById(R.id.programmes_tabs);
        tabLayout.setupWithViewPager(mViewPager);
        for(int i=0;i<=5;i++)
        {
            View view = getLayoutInflater().inflate(R.layout.customimageviewtab, null);
            ImageView tabIconCustom=(ImageView)view.findViewById(R.id.tabIconCustom);
            //ImageView tabIconCustom=new ImageView(getApplicationContext());
            tabIconCustom.setImageResource(tabIcons[i]);
            tabLayout.getTabAt(i).setCustomView(tabIconCustom);
        }


    }



    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            Log.v("common","oncreateview programme.java");
            View rootView = inflater.inflate(R.layout.fragment_programmes, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
            return rootView;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            switch (position) {
                case 0:
                    CommonFragment tab1 = CommonFragment.newInstance("cube");
                    return tab1;
                case 1:
                    CommonFragment tab2 = CommonFragment.newInstance("juggling");
                    return tab2;
                case 2:
                    CommonFragment tab3 =CommonFragment.newInstance("grapho");
                    return tab3;
                case 3:
                    CommonFragment tab4 =CommonFragment.newInstance("stack");
                    return tab4;
                case 4:
                    CommonFragment tab5 = CommonFragment.newInstance("calligraphy");
                    return tab5;
                case 5:
                    CommonFragment tab6=CommonFragment.newInstance("analysis");
                    return tab6;
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 6;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:

                    return "Cube";
                case 1:
                    return "Juggling";
                case 2:
                    return "Scientific Handwriting";
                case 3:
                    return "Speed Stacking";
                case 4:
                    return "Calligraphy";
                case 5:
                    return "Handwriting Analysis";
            }
            return null;
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


}
