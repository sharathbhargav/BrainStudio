package brainstudio.s4pl.com.brainstudio;

import android.support.v4.view.PagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.github.chrisbanes.photoview.PhotoView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PhotoFullViewerCommon extends AppCompatActivity {
    @BindView(R.id.commonPhotoFullView) HackyViewPager commonPhotoFullView;
    ArrayList<String> urlList;
    String singleUrl;
    int position=0;
    String type;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_full_viewer_common);
        ButterKnife.bind(this);
         type=getIntent().getStringExtra("type");
        if(type.equals("single"))
            singleUrl=getIntent().getStringExtra("url");
        else if(type.equals("multi")){
            urlList = (ArrayList<String>) getIntent().getSerializableExtra("urllist");
            position=getIntent().getIntExtra("position",0);
        }
        commonPhotoFullView.setAdapter(new PhotoFullViewerAdapter());
        commonPhotoFullView.setCurrentItem(position);
    }
    public class PhotoFullViewerAdapter extends PagerAdapter
    {
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            PhotoView photoView = new PhotoView(container.getContext());
            if(type.equals("single"))
                Glide.with(getApplicationContext())
                        .load(singleUrl)
                        .thumbnail(Glide.with(getApplicationContext()).load(R.drawable.thumbnail))
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .fitCenter()
                        .into(photoView);
            else if(type.equals("multi"))
                Glide.with(getApplicationContext())
                        .load(urlList.get(position))
                        .thumbnail(Glide.with(getApplicationContext()).load(R.drawable.thumbnail))
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .fitCenter()
                        .into(photoView);
            container.addView(photoView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            return photoView;
        }

        @Override
        public int getCount()
        {
            if(type.equals("single"))
                return 1;
            else
                return urlList.size();
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }
}
