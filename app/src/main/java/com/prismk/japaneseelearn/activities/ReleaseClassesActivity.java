package com.prismk.japaneseelearn.activities;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.prismk.japaneseelearn.R;
import com.prismk.japaneseelearn.bean.UserData;
import com.prismk.japaneseelearn.bean.VideoData;
import com.prismk.japaneseelearn.managers.UserDBManager;
import com.prismk.japaneseelearn.managers.VideoDBManager;
import com.prismk.japaneseelearn.oss.InitOssClient;
import com.prismk.japaneseelearn.oss.OSSConfig;
import com.prismk.japaneseelearn.oss.UpFile;
import com.prismk.japaneseelearn.views.loadview.ZProgressHUD;
import com.prismk.japaneseelearn.widgets.Title;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ReleaseClassesActivity extends BaseActivity implements View.OnClickListener {

    private boolean isVip;
    private TextView tv_classes_title;
    private TextView tv_introduction;
    private TextView tv_context;
    private ImageView imv_video;
    private RelativeLayout rl_classes_title;
    private RelativeLayout rl_introduction;
    private RelativeLayout rl_context;
    private int userId;
    private String videoPath;
    private Bitmap bitmap;
    private VideoDBManager videoDBManager;
    private UpFile upFile;
    private ZProgressHUD progressHUD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        getVipIntentValue();
        setStatusBarDark();
        initTitle();
        initVideoManager();
        initView();
        initUserInfo();
        InitOssClient.initOssClient(this, OSSConfig.stsServer, OSSConfig.endPoint);
        upFile = new UpFile();
    }

    private void initTitle() {
        Title title = findViewById(R.id.title);
        title.setTitleNameStr("发布课程");
        title.setTheme(Title.TitleTheme.THEME_LIGHT);
        title.setShowDivider(true);
        Title.ButtonInfo backbutton = new Title.ButtonInfo(true, Title.BUTTON_LEFT, R.mipmap.navigationbar_back_gray, null);
        Title.ButtonInfo savebutton = new Title.ButtonInfo(true, Title.BUTTON_RIGHT1, 0, "发布");
        title.setButtonInfo(backbutton);
        title.setButtonInfo(savebutton);
        title.setOnTitleButtonClickListener(new Title.OnTitleButtonClickListener() {
            @Override
            public void onClick(int id, Title.ButtonViewHolder viewHolder) {
                switch (id) {
                    case Title.BUTTON_LEFT:
                        finish();
                        goPreAnim();
                        break;
                    case Title.BUTTON_RIGHT1:
                        boolean b = checkInfoFull();
                        if (b) {
                            releaseClass();
                        } else {
                            return;
                        }
                        finish();
                        goPreAnim();
                        break;
                }
            }
        });
    }

    private void showLoadingView() {
        progressHUD = ZProgressHUD.getInstance(this);
        progressHUD.setMessage("正在加载");
        progressHUD.setSpinnerType(ZProgressHUD.SIMPLE_ROUND_SPINNER);
        progressHUD.show();
    }

    private void releaseClass() {
        String photoUpPath = OSSConfig.upRootPath + "zack/" + userId + "/img/" + System.currentTimeMillis() + ".jpg";
        String videoUpPath = OSSConfig.upRootPath + "zack/" + userId + "/" + System.currentTimeMillis()+ ".mp4";

        String currentTime = getCurrentTime();
        String title = tv_classes_title.getText().toString().trim();
        String introduction = tv_introduction.getText().toString().trim();
        String context = tv_context.getText().toString().trim();
        String imgPath = saveImageToGallery(bitmap);
        VideoData videoData = new VideoData(videoPath, photoUpPath, title, introduction, context, isVip, currentTime, userId);

        upFile.upfile(imgPath, photoUpPath);
        upFile.upfile(videoPath, videoUpPath);

        videoDBManager.insertVideoData(videoData);
        Toast.makeText(ReleaseClassesActivity.this, "发布成功！", Toast.LENGTH_SHORT).show();
    }

    private void initVideoManager() {
        if (videoDBManager == null) {
            videoDBManager = new VideoDBManager(this);
            videoDBManager.openDataBase();
        }
    }

    private String getCurrentTime() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");// HH:mm:ss
        Date date = new Date(System.currentTimeMillis());
        return simpleDateFormat.format(date);
    }

    private void initUserInfo() {
        UserDBManager userDBManager = new UserDBManager(ReleaseClassesActivity.this);
        List<UserData> userDataListFromUserDB = userDBManager.getUserDataListFromUserDB();
        userId = userDataListFromUserDB.get(userDBManager.getLoginUesrID() - 1).getUserId();
    }

    private void initView() {
        tv_classes_title = (TextView) findViewById(R.id.tv_classes_title);
        tv_introduction = (TextView) findViewById(R.id.tv_introduction);
        tv_context = (TextView) findViewById(R.id.tv_context);
        imv_video = (ImageView) findViewById(R.id.imv_video);
        rl_classes_title = (RelativeLayout) findViewById(R.id.rl_classes_title);
        rl_introduction = (RelativeLayout) findViewById(R.id.rl_introduction);
        rl_context = (RelativeLayout) findViewById(R.id.rl_context);
        rl_classes_title.setOnClickListener(this);
        rl_introduction.setOnClickListener(this);
        rl_context.setOnClickListener(this);
        imv_video.setOnClickListener(this);
    }

    private void getVipIntentValue() {
        Intent intent = getIntent();
        isVip = intent.getBooleanExtra("isVip", false);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_release_classes;
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(ReleaseClassesActivity.this, EditVideoInfoActivity.class);
        switch (v.getId()) {
            case R.id.rl_classes_title:
                startActivityForResult(intent,2001);
                goNextAnim();
                break;
            case R.id.rl_introduction:
                startActivityForResult(intent,2002);
                goNextAnim();
                break;
            case R.id.rl_context:
                startActivityForResult(intent,2003);
                goNextAnim();
                break;
            case R.id.imv_video:
                choiceVideo();
                break;
        }
    }

    /**
     * 从相册中选择视频
     */
    private void choiceVideo() {
        Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, 1001);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1001 && resultCode == RESULT_OK && null != data) {
            Uri selectedVideo = data.getData();
            String[] filePathColumn = {MediaStore.Video.Media.DATA};
            Cursor cursor = getContentResolver().query(selectedVideo,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            videoPath = cursor.getString(columnIndex);

            MediaMetadataRetriever media = new MediaMetadataRetriever();
            media.setDataSource(ReleaseClassesActivity.this, selectedVideo);
            bitmap = media.getFrameAtTime();
            imv_video.setImageBitmap(bitmap);

            cursor.close();
        }
        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        if (requestCode == 2001) {
            tv_classes_title.setText(data.getStringExtra("edit"));
        } else if (requestCode == 2002) {
            tv_introduction.setText(data.getStringExtra("edit"));
        } else if (requestCode == 2003) {
            tv_context.setText(data.getStringExtra("edit"));
        }

    }

    public String saveImageToGallery(Bitmap bmp) {
        //生成路径
        String root = Environment.getExternalStorageDirectory().getAbsolutePath();
        String dirName = "1ELearnFolder/";
        File appDir = new File(root, dirName);
        if (!appDir.exists()) {
            appDir.mkdirs();
        }
        //文件名为时间
        long timeStamp = System.currentTimeMillis();
        /*SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String sd = sdf.format(new Date(timeStamp));*/
        String fileName = timeStamp + ".jpg";
        //获取文件
        File file = new File(appDir, fileName);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            //通知系统相册刷新
            this.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                    Uri.fromFile(new File(file.getPath()))));
            return root + dirName + fileName;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "";
    }

    private boolean checkInfoFull() {
        String title = tv_classes_title.getText().toString().trim();
        String introduction = tv_introduction.getText().toString().trim();
        String context = tv_context.getText().toString().trim();
        if (title.isEmpty()) {
            Toast.makeText(this, "请填写标题", Toast.LENGTH_SHORT).show();
            return false;
        } else if (introduction.isEmpty()) {
            Toast.makeText(this, "请填写标题", Toast.LENGTH_SHORT).show();
            return false;
        } else if (context.isEmpty()) {
            Toast.makeText(this, "请填写标题", Toast.LENGTH_SHORT).show();
            return false;
        } else if (videoPath.isEmpty()) {
            Toast.makeText(this, "请填写标题", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }
}
