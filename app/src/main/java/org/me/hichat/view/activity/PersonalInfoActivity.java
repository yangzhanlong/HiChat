package org.me.hichat.view.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.fourmob.datetimepicker.date.DatePickerDialog;
import com.lljjcoder.Interface.OnCityItemClickListener;
import com.lljjcoder.bean.CityBean;
import com.lljjcoder.bean.DistrictBean;
import com.lljjcoder.bean.ProvinceBean;
import com.lljjcoder.citywheel.CityConfig;
import com.lljjcoder.style.citypickerview.CityPickerView;

import org.me.hichat.R;
import org.me.hichat.base.BaseActivity;
import org.me.hichat.model.bean.User;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Calendar;

import butterknife.ButterKnife;
import cn.bmob.v3.datatype.BmobFile;

public class PersonalInfoActivity extends BaseActivity implements View.OnClickListener {

    private static final int REQUEST_CODE_SELECT_PICTURE = 100;
    private static final int PHOTO_REQUEST_CUT = 200;

    private ImageView ivIcon;
    private TextView tvNickname;
    private TextView tvBirthday;
    private TextView tvHome;
    private RadioButton rbMale;
    private RadioButton rbFemale;
    private Button btNext;

    private DatePickerDialog datePickerDialog;
    private Calendar calendar;

    // 申明对象
    private CityPickerView mPicker = new CityPickerView();

    public static final String DATEPICKER_TAG = "datepicker";
    private CityConfig cityConfig;

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.WRITE_EXTERNAL_STORAGE"};
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 6.0 动态申请权限
        try {
            //检测是否有写的权限
            int permission = ActivityCompat.checkSelfPermission(this,
                    "android.permission.WRITE_EXTERNAL_STORAGE");
            if (permission != PackageManager.PERMISSION_GRANTED) {
                // 没有写的权限，去申请写的权限，会弹出对话框
                ActivityCompat.requestPermissions(this, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        /**
         * 预先加载仿iOS滚轮实现的全部数据
         */
        mPicker.init(this);
    }

    @Override
    protected View addLayoutToFrameLayout(LayoutInflater layoutInflater, FrameLayout frameLayout) {
        View view = layoutInflater.inflate(R.layout.activity_personal_info, frameLayout, true);
        return view;
    }

    @Override
    protected void initView(View view) {
        ivIcon = ButterKnife.findById(view, R.id.iv_icon);
        tvNickname = ButterKnife.findById(view, R.id.tv_nickname);
        tvBirthday = ButterKnife.findById(view, R.id.tv_birthday);
        tvHome = ButterKnife.findById(view, R.id.tv_home);
        rbMale = ButterKnife.findById(view, R.id.rb_male);
        rbFemale = ButterKnife.findById(view, R.id.rb_female);
        btNext = ButterKnife.findById(view, R.id.bt_next);

        btNext.setOnClickListener(this);
        ivIcon.setOnClickListener(this);
        tvHome.setOnClickListener(this);
        tvBirthday.setOnClickListener(this);
        rbMale.setOnClickListener(this);
        rbFemale.setOnClickListener(this);

        // 获取 nickName
        user = (User) getIntent().getSerializableExtra("user");
        tvNickname.setText(user.getNickName());
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.iv_icon:
                getPicFromGallery(); // 获取头像
                break;
            case R.id.tv_birthday:
                showSelectBirthdayDialog(); // 选择生日
                break;
            case R.id.tv_home:
                showSelectHomeDialog(); // 选择家乡
                break;
            case R.id.rb_male:
                rbMale.setChecked(true);  // 选择性别
                rbFemale.setChecked(false);
                updateButtonState();
                break;
            case R.id.rb_female:
                rbFemale.setChecked(true);
                rbMale.setChecked(false);
                updateButtonState();
                break;
            case R.id.bt_next:
                showConfirmDialog();
                break;
        }
    }

    // 显示注册提示窗口
    private void showConfirmDialog() {
        new AlertDialog.Builder(this)
                .setTitle("提示")
                .setMessage("注册成功后，性别不可以修改")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        savePersonalInfo();
                        Intent intent = new Intent(getApplicationContext(), UserPasswordActivity.class);
                        intent.putExtra("user", user);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("取消", null)
                .show();
    }

    /**
     * 保存个人信息到 user
     */
    private void savePersonalInfo() {
        user.setDate(tvBirthday.getText().toString());
        user.setAddress(tvHome.getText().toString());
        user.setSex(rbMale.isChecked() ? 0 : 1);
        String file_path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/hichat_pic/head.jpg";
        File file = new File(file_path);
        BmobFile bmobFile = new BmobFile(file);
        user.setIcon(bmobFile);
    }

    /**
     * 城市选择器
     */
    private void showSelectHomeDialog() {
        if (cityConfig == null) {
            // 添加配置
            cityConfig = new CityConfig.Builder()
                    .title("选择城市")//标题
                    .titleTextSize(20)//标题文字大小
                    .titleTextColor("#b9b7b8")//标题文字颜  色
                    .titleBackgroundColor("#b9b7b8")//标题栏背景色
                    .confirTextColor("#FF4081")//确认按钮文字颜色
                    .confirmText("确定")//确认按钮文字
                    .cancelTextColor("#FF4081")//取消按钮文字颜色
                    .cancelText("取消")//取消按钮文字
                    .setCityWheelType(CityConfig.WheelType.PRO_CITY_DIS)//显示类，只显示省份一级，显示省市两级还是显示省市区三级
                    .showBackground(true)//是否显示半透明背景
                    .visibleItemsCount(7)//显示item的数量
                    .province("广东省")//默认显示的省份
                    .city("广州市")//默认显示省份下面的城市
                    .district("天河区")//默认显示省市下面的区县数据
                    .provinceCyclic(true)//省份滚轮是否可以循环滚动
                    .cityCyclic(false)//城市滚轮是否可以循环滚动
                    .districtCyclic(false)//区县滚轮是否循环滚动
                    .drawShadows(false)//滚轮不显示模糊效果
                    //.setLineColor("#03a9f4")//中间横线的颜色
                    //.setLineHeigh(5)//中间横线的高度
                    .setShowGAT(true)//是否显示港澳台数据，默认不显示
                    .build();

        }
        //设置自定义的属性配置
        mPicker.setConfig(cityConfig);
        //监听选择点击事件及返回结果
        mPicker.setOnCityItemClickListener(new OnCityItemClickListener() {
            @Override
            public void onSelected(ProvinceBean province, CityBean city, DistrictBean district) {
                super.onSelected(province, city, district);
                tvHome.setText(province.getName() + city.getName() + district.getName());
                updateButtonState();
            }
        });

        // 显示
        mPicker.showCityPicker();
    }

    /**
     * 显示日期选择器
     */
    private void showSelectBirthdayDialog() {
        if (datePickerDialog == null) {
            calendar = Calendar.getInstance();
            datePickerDialog = DatePickerDialog.newInstance(new MyDateSetListener(),
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH), false);
            datePickerDialog.setYearRange(1985, 2028);
        }
        datePickerDialog.show(getSupportFragmentManager(), DATEPICKER_TAG);
    }

    /**
     * 日期选择回回调
     */
    private class MyDateSetListener implements DatePickerDialog.OnDateSetListener {
        @Override
        public void onDateSet(DatePickerDialog datePickerDialog, int year, int month, int day) {
            tvBirthday.setText(year + "-" + month + "-" + day);
            updateButtonState();
        }
    }

    // 启动系统相册选择图片
    public void getPicFromGallery() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_CODE_SELECT_PICTURE);
    }

    /**
     * 调用系统的剪切图片
     */
    private Uri imageUri;

    private Uri crop(Uri uri) {
        // 裁剪图片意图
        Intent intent = new Intent("com.android.camera.action.CROP");
        //判断是否是AndroidN以及更高的版本
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //添加这一句表示对目标应用临时授权该Uri所代表的文件
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            //访问相册需要被限制，需要通过FileProvider创建一个content类型的Uri
            //imageUri = FileProvider.getUriForFile(this, "org.me.hichat.fileProvider", file);
            // 裁剪整个流程，估计授权一次就好outputUri不需要ContentUri,否则失败
            //outputUri = FileProvider.getUriForFile(activity, "com.bao.cropimage.fileprovider", new File(crop_image));
        }

        // 创建文件夹
        String local_dir = Environment.getExternalStorageDirectory().getAbsolutePath() + "/hichat_pic/";
        File parentPath = new File(local_dir);
        if (!parentPath.exists()) {
            parentPath.mkdirs();
        }

        // 创建文件
        String local_file = parentPath.getAbsolutePath() + "/head.jpg";
        File file = new File(local_file);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        imageUri = Uri.parse("file://" + file.getAbsolutePath());

        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        //设置宽高比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        //设置裁剪图片宽高
        intent.putExtra("outputX", 250);
        intent.putExtra("outputY", 250);
        intent.putExtra("scale", true);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true);  // 取消人脸识别
        intent.putExtra("return-data", false);

        // 开启一个带有返回值的Activity，请求码为 PHOTO_REQUEST_CUT
        startActivityForResult(intent, PHOTO_REQUEST_CUT);
        return imageUri;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE_SELECT_PICTURE:
                if (data != null) {
                    Uri uri = data.getData();
                    if (uri != null) {
                        imageUri = crop(uri);
                    }
                }
                break;
            case PHOTO_REQUEST_CUT:
                if (imageUri != null) {
                    Bitmap bitmap = null;
                    try {
                        bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
                        if (bitmap != null) {
                            ivIcon.setImageBitmap(bitmap);
                            updateButtonState();
                        }
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                break;
        }
    }

    /**
     * 更新button状态
     */
    private void updateButtonState() {
        Drawable drawable = ivIcon.getDrawable();
        String date = tvBirthday.getText().toString();
        String city = tvHome.getText().toString();
        boolean male = rbMale.isChecked();
        boolean female = rbFemale.isChecked();

        if (drawable == null || TextUtils.isEmpty(date)
                || TextUtils.isEmpty(city)
                || !(male || female)) {
            btNext.setEnabled(false);
            return;
        }
        btNext.setEnabled(true);
    }
}
