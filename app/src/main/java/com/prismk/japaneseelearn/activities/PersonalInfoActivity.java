package com.prismk.japaneseelearn.activities;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.lzy.imagepicker.view.CropImageView;
import com.prismk.japaneseelearn.R;
import com.prismk.japaneseelearn.bean.UserData;
import com.prismk.japaneseelearn.libs.imagepicker.imageloader.GlideImageLoader;
import com.prismk.japaneseelearn.libs.imagepicker.wxdemo.WxDemoActivity;
import com.prismk.japaneseelearn.managers.UserDBManager;
import com.prismk.japaneseelearn.oss.InitOssClient;
import com.prismk.japaneseelearn.oss.OSSConfig;
import com.prismk.japaneseelearn.oss.UpFile;
import com.prismk.japaneseelearn.properties.ELearnAppProperties;
import com.prismk.japaneseelearn.utils.PermissionsUtil;
import com.prismk.japaneseelearn.widgets.Title;
import com.prismk.japaneseelearn.widgets.elearncamera.ElearnCameraManager;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.lzy.imagepicker.ImagePicker.REQUEST_CODE_PREVIEW;
import static com.prismk.japaneseelearn.libs.imagepicker.wxdemo.WxDemoActivity.REQUEST_CODE_SELECT;

public class PersonalInfoActivity extends BaseActivity {


    private CircleImageView userAvator;
    private TextView userName;
    private TextView userSign;
    private TextView userTel;

    public static final int USERNICKNAME = 0;
    public static final int USERSIGN = 1;
    public static final int USERTEL = 2;

    private static final int REQUEST_CODE_TAKE_PHOTO_NEEDS_CAMERA = 119;
    private static final int REQUEST_CODE_SELECT_PHOTO_NEEDS_READ_EXTERNAL_STORAGE = 120;
    public static final String TYPE = "types";

    public static final int TAKE_PIC = 10;
    public static final int SELE_PIC = 11;
    private UpFile upFile;
    private int userID;
    private UserDBManager userDBManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        setStatusBarDark();
        initTitle();
        initImagePicker();
        initView();
        initData();
        InitOssClient.initOssClient(this, OSSConfig.stsServer, OSSConfig.endPoint);
        upFile = new UpFile();
    }

    ArrayList<ImageItem> images = null;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == ImagePicker.RESULT_CODE_ITEMS) {
            //添加图片返回
            if (data != null && requestCode == REQUEST_CODE_SELECT) {
                images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                if (images != null) {
                    String path = images.get(0).path;
                    String photoUpPath = OSSConfig.upRootPath + "zack/" + userID + "/" + System.currentTimeMillis();
                    upFile.upfile(path, photoUpPath);
                    userDBManager.updateUserDataById(UserDBManager.USER_HEADIMG, userID, OSSConfig.uploadHalfPath+photoUpPath);
                }
            }
        }

        if (resultCode != RESULT_OK) {
            return;
        }

        int type = data.getIntExtra(TYPE, TAKE_PIC);
        switch (type) {
            case TAKE_PIC:
                String path = data.getStringExtra("path");
                if (path != null) {
                    String photoUpPath = OSSConfig.upRootPath + "zack/" + userID + "/" + System.currentTimeMillis();
                    upFile.upfile(path, photoUpPath);
                    userDBManager.updateUserDataById(UserDBManager.USER_HEADIMG, userID, OSSConfig.uploadHalfPath+photoUpPath);
                }
                break;
        }
    }

    private void initImagePicker() {
        ImagePicker imagePicker = ImagePicker.getInstance();
        imagePicker.setImageLoader(new GlideImageLoader());   //设置图片加载器
        imagePicker.setCrop(true);                           //允许裁剪（单选才有效）
        imagePicker.setSaveRectangle(true);                   //是否按矩形区域保存
        imagePicker.setSelectLimit(1);                      //选中数量限制
        imagePicker.setStyle(CropImageView.Style.RECTANGLE);  //裁剪框的形状
        imagePicker.setFocusWidth(800);                       //裁剪框的宽度。单位像素（圆形自动取宽高最小值）
        imagePicker.setFocusHeight(800);                      //裁剪框的高度。单位像素（圆形自动取宽高最小值）
        imagePicker.setOutPutX(1000);                         //保存文件的宽度。单位像素
        imagePicker.setOutPutY(1000);                         //保存文件的高度。单位像素
    }

    private void initData() {
        userDBManager = new UserDBManager(PersonalInfoActivity.this);
        List<UserData> userDataListFromUserDB = userDBManager.getUserDataListFromUserDB();
        Glide.with(PersonalInfoActivity.this).load(userDataListFromUserDB.get(userDBManager.getLoginUesrID() - 1).getHeadImgUrlString()).into(userAvator);
        userName.setText(userDataListFromUserDB.get(userDBManager.getLoginUesrID() - 1).getNickName());
        userSign.setText(userDataListFromUserDB.get(userDBManager.getLoginUesrID() - 1).getSign());
        userTel.setText(userDataListFromUserDB.get(userDBManager.getLoginUesrID() - 1).getUserName());
        userID = userDBManager.getLoginUesrID();
    }

    private void initView() {
        RelativeLayout rl_userAvator = findViewById(R.id.rl_useravator);
        RelativeLayout rl_userName = findViewById(R.id.rl_usernickname);
        RelativeLayout rl_userSign = findViewById(R.id.rl_usersign);
        RelativeLayout rl_userTel = findViewById(R.id.rl_usertel);
        userAvator = findViewById(R.id.civ_useravator);
        userName = findViewById(R.id.tv_usernickname);
        userSign = findViewById(R.id.tv_usersign);
        userTel = findViewById(R.id.tv_usertel);
        rl_userAvator.setOnClickListener(new onPersonalInfoClickListener());
        rl_userName.setOnClickListener(new onPersonalInfoClickListener());
        rl_userSign.setOnClickListener(new onPersonalInfoClickListener());
        rl_userTel.setOnClickListener(new onPersonalInfoClickListener());
    }

    private void initTitle() {
        Title title = findViewById(R.id.title);
        title.setTitleNameStr("个人信息");
        title.setTheme(Title.TitleTheme.THEME_LIGHT);
        title.setShowDivider(true);
        Title.ButtonInfo backbutton = new Title.ButtonInfo(true, Title.BUTTON_LEFT, R.mipmap.camera_back, null);
        title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == Title.BUTTON_LEFT) {
                    finish();
                    goPreAnim();
                }
            }
        });
        title.setButtonInfo(backbutton);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_personal_info;
    }

    private class onPersonalInfoClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(PersonalInfoActivity.this, ModifyPersonalInfoActivity.class);
            switch (v.getId()) {
                case R.id.rl_useravator:
                    showListDialog();
                    break;
                case R.id.rl_usernickname:
                    intent.putExtra(ELearnAppProperties.USER_INFO, USERNICKNAME);
                    startActivity(intent);
                    goNextAnim();
                    break;
                case R.id.rl_usersign:
                    intent.putExtra(ELearnAppProperties.USER_INFO, USERSIGN);
                    startActivity(intent);
                    goNextAnim();
                    break;
                case R.id.rl_usertel:
                    intent.putExtra(ELearnAppProperties.USER_INFO, USERTEL);
                    startActivity(intent);
                    goNextAnim();
                    break;
            }
        }
    }

    private void showListDialog() {
        final String[] items = {"打开相机拍摄", "进入相册选取"};
        AlertDialog.Builder listDialog = new AlertDialog.Builder(PersonalInfoActivity.this);
        listDialog.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    takePhoto();
                } else if (which == 1) {
                    selectPhoto();
                }
            }
        });
        listDialog.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE_TAKE_PHOTO_NEEDS_CAMERA
                && PermissionsUtil.hasPermission(PersonalInfoActivity.this, PermissionsUtil.Permission.WRITE_EXTERNAL_STORAGE)
                && PermissionsUtil.hasPermission(PersonalInfoActivity.this, PermissionsUtil.Permission.CAMERA)) {
            takePhoto();
        } else if (requestCode == REQUEST_CODE_SELECT_PHOTO_NEEDS_READ_EXTERNAL_STORAGE
                && PermissionsUtil.hasPermission(PersonalInfoActivity.this, PermissionsUtil.Permission.READ_EXTERNAL_STORAGE)) {
            selectPhoto();
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void selectPhoto() {
        if (!PermissionsUtil.checkAndRequestIfNoPermissionForActivity(this, PermissionsUtil.Permission.READ_EXTERNAL_STORAGE, REQUEST_CODE_SELECT_PHOTO_NEEDS_READ_EXTERNAL_STORAGE)) {
            return;
        }
        ImagePicker.getInstance().setSelectLimit(1);
        Intent intent1 = new Intent(PersonalInfoActivity.this, ImageGridActivity.class);
        startActivityForResult(intent1, REQUEST_CODE_SELECT);
    }

    private void takePhoto() {
        checkAllCameraPermissions();
        ElearnCameraManager routeManCameraManager = ElearnCameraManager.getInstance();
        routeManCameraManager.startCamera(PersonalInfoActivity.this, 1);
        routeManCameraManager.setTakePhotoListener(new ElearnCameraManager.TakePictureListener() {
            @Override
            public void onPictureTaken(int requestCode, String path) {
                if (requestCode == 1) {
                    Intent intent = new Intent();
                    intent.putExtra("path", path);
                    intent.putExtra(TYPE, TAKE_PIC);
                    setResult(RESULT_OK, intent);
                }
            }
        });
    }

    private void checkAllCameraPermissions() {
        String[] permissions = new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA,
        };
        List<String> mPermissionList = new ArrayList<>();
        mPermissionList.clear();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                mPermissionList.add(permission);
            }
        }
        if (mPermissionList.size() > 0) {//有权限没有通过，需要申请
            ActivityCompat.requestPermissions(this, permissions, REQUEST_CODE_TAKE_PHOTO_NEEDS_CAMERA);
        }
    }

    @Override
    protected void onResume() {
        initData();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        ElearnCameraManager.getInstance().removeTakePhotoListener();
        super.onDestroy();
    }
}
