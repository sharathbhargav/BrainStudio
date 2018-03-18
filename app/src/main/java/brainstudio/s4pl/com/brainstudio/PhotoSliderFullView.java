package brainstudio.s4pl.com.brainstudio;

import android.content.Intent;
import android.graphics.Camera;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
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
import com.keiferstone.nonet.Configuration;
import com.keiferstone.nonet.ConnectionStatus;
import com.keiferstone.nonet.Monitor;
import com.keiferstone.nonet.NoNet;
import com.leo.simplearcloader.ArcConfiguration;
import com.leo.simplearcloader.SimpleArcDialog;
import com.leo.simplearcloader.SimpleArcLoader;
import com.neurenor.permissions.PermissionCallback;
import com.neurenor.permissions.PermissionsHelper;
import com.robertsimoes.shareable.Shareable;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.mateware.snacky.Snacky;
import de.mateware.snacky.SnackyUtils;
import proguard.annotation.Keep;
import proguard.annotation.KeepClassMembers;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.Manifest.permission_group.STORAGE;

@Keep
@KeepClassMembers
public class PhotoSliderFullView extends AppCompatActivity {
    @BindView(R.id.photosliderFullView)
    HackyViewPager photoSliderFullView;
    @BindView(R.id.imageListDownloadButtonFullView)
    ArrowDownloadButton downloadProgress;
    @BindView(R.id.fullViewDownload)
    Button downloadButton;
    @BindView(R.id.fullViewWatsupShare)
    Button watsupShareButton;
    @BindView(R.id.fullViewOthersShare)
    Button otherShareButton;
    @BindView(R.id.downloadProgressContainer)
    RelativeLayout progressContainer;
    int position;
    public int progress = 0;
    ArrayList<String> urlList;
    FirebaseStorage storage;
    SimpleArcDialog mDialog;
    ArcConfiguration configuration;
    PermissionsHelper helper;
    boolean result = false;

    Monitor monitor;
    boolean netLost=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_slider_full_view);
        ButterKnife.bind(this);
        helper = new PermissionsHelper(PhotoSliderFullView.this);
        position = getIntent().getIntExtra("Position", 0);
        urlList = (ArrayList<String>) getIntent().getSerializableExtra("Urlslist");
        storage = FirebaseStorage.getInstance();
        photoSliderFullView.setAdapter(new PhotoSliderAdapter());
        photoSliderFullView.setCurrentItem(position);
        configuration = new ArcConfiguration(getApplicationContext());
        mDialog = new SimpleArcDialog(this);
        configuration.setLoaderStyle(SimpleArcLoader.STYLE.COMPLETE_ARC);
        configuration.setText("Please Wait.....");
        mDialog.setConfiguration(configuration);
        final Configuration config= NoNet.configure()
                .endpoint("https://google.com")
                .timeout(5)
                .connectedPollFrequency(10)
                .disconnectedPollFrequency(3)
                .build();
        NoNet.monitor(this)
                .configure(config)
                .poll()
                .callback(new Monitor.Callback() {
                    @Override
                    public void onConnectionEvent(int connectionStatus) {

                        if(connectionStatus== ConnectionStatus.DISCONNECTED && !netLost)
                        {
                            netLost=true;

                            Intent offLine=new Intent(getApplicationContext(),OfflineActivity.class);
                            startActivity(offLine);

                        }
                        if(connectionStatus==ConnectionStatus.CONNECTED)
                            netLost=false;


                    }
                });
        monitor= NoNet.check(this).start();





    }

    @Override
    public void onRequestPermissionsResult(final int requestCode, @NonNull final String[] permissions, @NonNull final int[] grantResults) {
        /**
         * helper's onRequestPermissionsResult must be called from activity.
         */
        helper.onRequestPermissionsResult(permissions, grantResults);
    }

    void sharePhoto(String url) {
        StorageReference sharePhotoRef = storage.getReferenceFromUrl(url);

        File mediaStorageDir = new File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "Brainstudio");
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs())
                return;

        }
        Log.v("test123", sharePhotoRef.getName() + "\n" + sharePhotoRef.getBucket().toString());
        final File localFile = new File(mediaStorageDir.getPath() + File.separator + sharePhotoRef.getName().toLowerCase());
        if (localFile.exists()) {
            Uri uri = Uri.fromFile(localFile);
            Shareable imageShare = new Shareable.Builder(PhotoSliderFullView.this)
                    .image(uri)
                    .message("")
                    .url("")
                    .build();
            imageShare.share();
        } else {
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

    void sharePhotoWatsup(String url) {
        StorageReference sharePhotoRef = storage.getReferenceFromUrl(url);

        File mediaStorageDir = new File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "Brainstudio");
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs())
                return;

        }
        Log.v("test123", sharePhotoRef.getName() + "\n" + sharePhotoRef.getBucket());
        final File localFile = new File(mediaStorageDir.getPath() + File.separator + sharePhotoRef.getName().toLowerCase());
        if (localFile.exists()) {
            Uri uri = FileProvider.getUriForFile(getApplicationContext(), getApplicationContext().getPackageName() + ".provider", (localFile));
            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            //Target whatsapp:
            shareIntent.setPackage("com.whatsapp");
            //Add text and then Image URI
            shareIntent.putExtra(Intent.EXTRA_TEXT, "Brain Studio");
            shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
            shareIntent.setType("image/jpeg");
            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

            try {
                startActivity(shareIntent);
            } catch (android.content.ActivityNotFoundException ex) {

            }

        } else {
            mDialog.show();
            sharePhotoRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    mDialog.dismiss();

                    Uri uri = FileProvider.getUriForFile(getApplicationContext(), getApplicationContext().getPackageName() + ".provider", (localFile));
                    Intent shareIntent = new Intent();
                    shareIntent.setAction(Intent.ACTION_SEND);
                    //Target whatsapp:
                    shareIntent.setPackage("com.whatsapp");
                    //Add text and then Image URI
                    shareIntent.putExtra(Intent.EXTRA_TEXT, "Brain Studio");
                    shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
                    shareIntent.setType("image/jpeg");
                    shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                    try {
                        startActivity(shareIntent);
                    } catch (android.content.ActivityNotFoundException ex) {

                    }

                }
            });
        }

    }

    void downloadPhoto(String url) {

        StorageReference sharePhotoRef = storage.getReferenceFromUrl(url);
        File mediaStorageDir = new File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "Brainstudio");
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs())
                return;

        }
        Log.v("test123", sharePhotoRef.getName() + "\n" + sharePhotoRef.getBucket().toString());
        final File localFile = new File(mediaStorageDir.getPath() + File.separator + sharePhotoRef.getName().toLowerCase());
        if (localFile.exists()) {
            Snacky.builder()
                    .setActivty(PhotoSliderFullView.this)
                    .setText("The Image is previously downloaded and exists in path " + localFile.getPath())
                    .setDuration(Snacky.LENGTH_SHORT)
                    .info()
                    .show();

        } else {
            progressContainer.setVisibility(View.VISIBLE);

            downloadProgress.startAnimating();
            FileDownloadTask task = sharePhotoRef.getFile(localFile);
            task.addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {

                    Snacky.builder()
                            .setActivty(PhotoSliderFullView.this)
                            .setText("File sucessfully downloaded to " + localFile.getPath())
                            .setDuration(Snacky.LENGTH_SHORT)
                            .success()
                            .show();
                    progressContainer.setVisibility(View.GONE);
                }

            });
            task.addOnProgressListener(new OnProgressListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onProgress(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100.0 * (float) taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                    if (progress != 100.0)
                        downloadProgress.setProgress((float) progress);

                }
            });

        }

    }
    void commonSnackyBar()
    {
        Snacky.builder()
                .setActivty(PhotoSliderFullView.this)
                .setText("Permission required to continue" )
                .setDuration(Snacky.LENGTH_LONG)
                .error()
                .show();
    }



    public class PhotoSliderAdapter extends PagerAdapter
    {

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            PhotoView photoView = new PhotoView(container.getContext());
            Glide.with(getApplicationContext())
                    .load(urlList.get(position))
                    .thumbnail(Glide.with(getApplicationContext()).load(R.drawable.thumbnail))
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .fitCenter()
                    .into(photoView);
            container.addView(photoView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);


            downloadButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (helper.isPermissionGranted(WRITE_EXTERNAL_STORAGE))
                        downloadPhoto(urlList.get(position));
                    else {
                        helper.requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE}, new PermissionCallback() {
                            @Override
                            public void onResponseReceived(HashMap<String, PermissionsHelper.PermissionGrant> mapPermissionGrants) {
                                PermissionsHelper.PermissionGrant permissionGrant = mapPermissionGrants
                                        .get(WRITE_EXTERNAL_STORAGE);
                                switch (permissionGrant) {
                                    case GRANTED:
                                        downloadPhoto(urlList.get(position));
                                        break;
                                    case DENIED:
                                        commonSnackyBar();
                                        break;
                                    case NEVERSHOW:
                                        commonSnackyBar();
                                        break;


                                }
                            }
                        });
                    }
                }
            });



            otherShareButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    if (helper.isPermissionGranted(WRITE_EXTERNAL_STORAGE))
                        sharePhoto(urlList.get(position));
                    else {
                        helper.requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE}, new PermissionCallback() {
                            @Override
                            public void onResponseReceived(HashMap<String, PermissionsHelper.PermissionGrant> mapPermissionGrants) {
                                PermissionsHelper.PermissionGrant permissionGrant = mapPermissionGrants
                                        .get(WRITE_EXTERNAL_STORAGE);
                                switch (permissionGrant) {
                                    case GRANTED:
                                        sharePhoto(urlList.get(position));
                                        break;
                                    case DENIED:
                                        commonSnackyBar();
                                        break;
                                    case NEVERSHOW:
                                        commonSnackyBar();
                                        break;


                                }
                            }
                        });
                    }
                }
            });



            watsupShareButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (helper.isPermissionGranted(WRITE_EXTERNAL_STORAGE))
                        sharePhotoWatsup(urlList.get(position));
                    else {
                        helper.requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE}, new PermissionCallback() {
                            @Override
                            public void onResponseReceived(HashMap<String, PermissionsHelper.PermissionGrant> mapPermissionGrants) {
                                PermissionsHelper.PermissionGrant permissionGrant = mapPermissionGrants
                                        .get(WRITE_EXTERNAL_STORAGE);
                                switch (permissionGrant) {
                                    case GRANTED:
                                        sharePhotoWatsup(urlList.get(position));
                                        break;
                                    case DENIED:
                                        commonSnackyBar();
                                        break;
                                    case NEVERSHOW:
                                        commonSnackyBar();
                                        break;


                                }
                            }
                        });
                    }
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
