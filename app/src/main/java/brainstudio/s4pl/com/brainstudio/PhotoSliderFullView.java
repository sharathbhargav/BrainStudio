package brainstudio.s4pl.com.brainstudio;

import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.support.v4.view.PagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.Target;
import com.fenjuly.library.ArrowDownloadButton;
import com.github.chrisbanes.photoview.PhotoView;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.leo.simplearcloader.ArcConfiguration;
import com.leo.simplearcloader.SimpleArcDialog;
import com.leo.simplearcloader.SimpleArcLoader;
import com.robertsimoes.shareable.Shareable;

import java.io.File;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PhotoSliderFullView extends AppCompatActivity {
    @BindView(R.id.photosliderFullView) HackyViewPager photoSliderFullView;
    @BindView(R.id.imageListDownloadButtonFullView)ArrowDownloadButton downloadProgress;
    @BindView(R.id.fullViewDownload)Button downloadButton;
    @BindView(R.id.fullViewWatsupShare)Button watsupShareButton;
    @BindView(R.id.fullViewOthersShare)Button otherShareButton;
    @BindView(R.id.downloadProgressContainer)RelativeLayout progressContainer;
    int position;
    public int progress=0;
    ArrayList<String> urlList;
    FirebaseStorage storage;
    SimpleArcDialog mDialog;
    ArcConfiguration configuration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_slider_full_view);
        ButterKnife.bind(this);
        position=getIntent().getIntExtra("Position",0);
        urlList=(ArrayList<String >)getIntent().getSerializableExtra("Urlslist");
        storage= FirebaseStorage.getInstance();
        photoSliderFullView.setAdapter(new PhotoSliderAdapter());
        photoSliderFullView.setCurrentItem(position);
        configuration = new ArcConfiguration(getApplicationContext());
        mDialog = new SimpleArcDialog(this);
        configuration.setLoaderStyle(SimpleArcLoader.STYLE.COMPLETE_ARC);
        configuration.setText("Please Wait.....");
        mDialog.setConfiguration(configuration);


    }

    void sharePhoto(String url)
    {
        StorageReference sharePhotoRef = storage.getReferenceFromUrl(url);

        File mediaStorageDir = new File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "Brainstudio");
        if (!mediaStorageDir.exists())
        {
            if (!mediaStorageDir.mkdirs())
                return;

        }
        Log.v("test123",sharePhotoRef.getName()+"\n"+sharePhotoRef.getBucket().toString());
        final File localFile=new File(mediaStorageDir.getPath() + File.separator + sharePhotoRef.getName());
        if(localFile.exists())
        {
            Uri uri=Uri.fromFile(localFile);
            Shareable imageShare = new Shareable.Builder(PhotoSliderFullView.this)
                    .image(uri)
                    .message("")
                    .url("")
                    .build();
            imageShare.share();
        }
        else
        {
            mDialog.show();
            sharePhotoRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {

                    Uri uri = Uri.fromFile(localFile);
                    mDialog.dismiss();
                    Shareable imageShare = new Shareable.Builder(PhotoSliderFullView.this)
                            .image(uri)
                            .message("")
                            .url("")
                            .build();
                    imageShare.share();
                }
            });
        }
    }

    void sharePhotoWatsup(String url)
    {
        StorageReference sharePhotoRef = storage.getReferenceFromUrl(url);

        File mediaStorageDir = new File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "Brainstudio");
        if (!mediaStorageDir.exists())
        {
            if (!mediaStorageDir.mkdirs())
                return;

        }
        Log.v("test123",sharePhotoRef.getName()+"\n"+sharePhotoRef.getBucket().toString());
        final File localFile=new File(mediaStorageDir.getPath() + File.separator + sharePhotoRef.getName());
        if(localFile.exists())
        {
            Uri uri= FileProvider.getUriForFile(getApplicationContext(), getApplicationContext().getPackageName() + ".provider",(localFile));
            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            //Target whatsapp:
            shareIntent.setPackage("com.whatsapp");
            //Add text and then Image URI
            shareIntent.putExtra(Intent.EXTRA_TEXT, "Brain Studio");
            shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
            shareIntent.setType("image/jpeg");
            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

            try
            {
                startActivity(shareIntent);
            }
            catch (android.content.ActivityNotFoundException ex)
            {

            }

        }
        else
        {
            mDialog.show();
            sharePhotoRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    mDialog.dismiss();

                    Uri uri = FileProvider.getUriForFile(getApplicationContext(), getApplicationContext().getPackageName() + ".provider",(localFile));
                    Intent shareIntent = new Intent();
                    shareIntent.setAction(Intent.ACTION_SEND);
                    //Target whatsapp:
                    shareIntent.setPackage("com.whatsapp");
                    //Add text and then Image URI
                    shareIntent.putExtra(Intent.EXTRA_TEXT, "Brain Studio");
                    shareIntent.putExtra(Intent.EXTRA_STREAM,uri);
                    shareIntent.setType("image/jpeg");
                    shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                    try
                    {
                        startActivity(shareIntent);
                    }
                    catch (android.content.ActivityNotFoundException ex)
                    {

                    }

                }
            });
        }

    }

    void downloadPhoto(String url)
    {

        StorageReference sharePhotoRef = storage.getReferenceFromUrl(url);
        File mediaStorageDir = new File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "Brainstudio");
        if (!mediaStorageDir.exists())
        {
            if (!mediaStorageDir.mkdirs())
                return;

        }
        Log.v("test123",sharePhotoRef.getName()+"\n"+sharePhotoRef.getBucket().toString());
        final File localFile=new File(mediaStorageDir.getPath() + File.separator + sharePhotoRef.getName());
        if(localFile.exists())
        {
            Toast.makeText(getApplicationContext(), "The Image is previously downloaded and exists in path " + localFile.getPath(), Toast.LENGTH_LONG).show();

        }
        else
        {
            progressContainer.setVisibility(View.VISIBLE);
            downloadProgress.startAnimating();
            FileDownloadTask task=sharePhotoRef.getFile(localFile);
            task.addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(getApplicationContext(), "File sucessfully downloaded to " + localFile.getPath(), Toast.LENGTH_SHORT).show();
                    progressContainer.setVisibility(View.GONE);
                }

            });
            task.addOnProgressListener(new OnProgressListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onProgress(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100.0 *(float) taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                    if(progress!=100.0)
                        downloadProgress.setProgress((float)progress);

                }
            });

        }

    }
    public class PhotoSliderAdapter extends PagerAdapter
    {

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            PhotoView photoView = new PhotoView(container.getContext());
            Glide.with(getApplicationContext())
                    .load(urlList.get(position))
                    .thumbnail(Glide.with(getApplicationContext()).load(R.drawable.ring))
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .fitCenter()
                    .into(photoView);
            container.addView(photoView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);


            downloadButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    downloadPhoto(urlList.get(position));
                }
            });



            otherShareButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sharePhoto(urlList.get(position));
                }
            });

            watsupShareButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sharePhotoWatsup(urlList.get(position));
                }
            });


            return photoView;
        }

        @Override
        public int getCount() {
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
