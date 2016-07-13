package cn.xmrk.jbook.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import net.margaritov.preference.colorpicker.ColorPickerDialog;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.xmrk.jbook.R;
import cn.xmrk.jbook.helper.FontHelper;
import cn.xmrk.jbook.helper.JBookHelper;
import cn.xmrk.jbook.widget.DrawView;
import cn.xmrk.rkandroid.activity.BaseActivity;
import cn.xmrk.rkandroid.activity.ChoicePicActivity;
import cn.xmrk.rkandroid.utils.CommonUtil;
import cn.xmrk.rkandroid.utils.FileUtil;
import cn.xmrk.rkandroid.utils.StringUtil;

public class MainActivity extends BaseActivity implements View.OnClickListener, View.OnLayoutChangeListener {
    private final int REQUEST_CODE_ASK_PERMISSIONS = 100;
    private final int CHOOSE_PIC = 55;

    private DrawView drawView;
    private ImageButton btn_color;
    private ImageButton btn_texture;
    private ImageButton btn_photo;
    private ImageButton btn_font;
    private ImageButton btn_delete;
    private ImageView iv_gift;
    private ImageButton ib_ok;
    private ImageView iv_setting;

    private RelativeLayout layout_color;
    private RelativeLayout layout_texture;
    private RelativeLayout layout_delete;

    private LinearLayout layout_drawtext;
    private EditText et_drawtext;
    private ImageButton btn_left;
    private ImageButton btn_mid;
    private ImageButton btn_right;

    private RelativeLayout layout_containert;
    //屏幕高度
    private int screenHeight = 0;
    //软件盘弹起后所占高度阀值
    private int keyHeight = 0;

    private boolean isKeyboardShow;


    /**
     * 当前选择的图片的地址
     **/
    private String nowDrawBitmapPath;

    private ColorPickerDialog mColorDialog;

    /**
     * 颜色字体使用
     **/
    private LinearLayout layoutControl;
    private TextView tvSizeAdd;
    private TextView tvSizeDecrease;
    private ImageButton ibColor;
    private ImageButton ibMore;


    //字体呈现动画
    private Animation showAnimation;
    private Animation dismissAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showVersion();

    }

    private void canUse() {
        setContentView(R.layout.activity_main);
        //不需要标题
        showCustomTitlebar(false);
        initView();
        initAnimation();
    }

    private void showVersion() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {//当前的sdk版本大于等于23
            insertDummyContactWrapper();
        } else {
            canUse();
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void insertDummyContactWrapper() {
        //需要手动请求的权限，（文件读写，拍照）
        String[] needPermission = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
        //未允许使用的权限
        List<String> needToPer = new ArrayList<>();
        for (int i = 0; i < needPermission.length; i++) {
            int hasWriteContactsPermission = checkSelfPermission(needPermission[i]);
            if (hasWriteContactsPermission != PackageManager.PERMISSION_GRANTED) {
                needToPer.add(needPermission[i]);
            }
        }
        if (needToPer.size() == 0) {
            canUse();
        } else {
            String[] sp = new String[needToPer.size()];
            for (int i = 0; i < sp.length; i++) {
                sp[i] = needToPer.get(i);
            }
            requestPermissions(sp, REQUEST_CODE_ASK_PERMISSIONS);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE_ASK_PERMISSIONS) {//权限结果来了
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                    new AlertDialog.Builder(MainActivity.this)
                            .setMessage("有权限未被允许使用，可在安全中心-权限管理中打开权限")
                            .setCancelable(false)
                            .setPositiveButton("退出简图", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    finish();
                                }
                            })
                            .create()
                            .show();
                    return;
                }
            }
            canUse();
        }
    }




    private void initAnimation() {
        showAnimation = AnimationUtils.loadAnimation(this, R.anim.trans_in);
        dismissAnimation = AnimationUtils.loadAnimation(this, R.anim.alpha_out);
    }

    private void initView() {
        drawView = (DrawView) findViewById(R.id.drawView);
        btn_color = (ImageButton) findViewById(R.id.btn_color);
        btn_texture = (ImageButton) findViewById(R.id.btn_texture);
        btn_photo = (ImageButton) findViewById(R.id.btn_photo);
        btn_font = (ImageButton) findViewById(R.id.btn_font);
        ib_ok = (ImageButton) findViewById(R.id.ib_ok);
        iv_gift = (ImageView) findViewById(R.id.iv_gift);
        iv_setting = (ImageView) findViewById(R.id.iv_setting);
        btn_delete = (ImageButton) findViewById(R.id.btn_delete);
        layout_color = (RelativeLayout) findViewById(R.id.layout_color);
        layout_texture = (RelativeLayout) findViewById(R.id.layout_texture);
        layout_delete = (RelativeLayout) findViewById(R.id.layout_delete);
        layout_containert = (RelativeLayout) findViewById(R.id.layout_containert);

        layout_drawtext = (LinearLayout) findViewById(R.id.layout_drawtext);
        et_drawtext = (EditText) findViewById(R.id.et_drawtext);
        btn_left = (ImageButton) findViewById(R.id.btn_left);
        btn_mid = (ImageButton) findViewById(R.id.btn_mid);
        btn_right = (ImageButton) findViewById(R.id.btn_right);

        layoutControl = (LinearLayout) findViewById(R.id.layout_control);
        tvSizeAdd = (TextView) findViewById(R.id.tv_size_add);
        tvSizeDecrease = (TextView) findViewById(R.id.tv_size_decrease);
        ibColor = (ImageButton) findViewById(R.id.ib_color);
        ibMore = (ImageButton) findViewById(R.id.ib_more);


        ibColor.setOnClickListener(this);
        tvSizeAdd.setOnClickListener(this);
        tvSizeDecrease.setOnClickListener(this);
        ibMore.setOnClickListener(this);
        btn_color.setOnClickListener(this);
        btn_texture.setOnClickListener(this);
        btn_photo.setOnClickListener(this);
        btn_font.setOnClickListener(this);
        ib_ok.setOnClickListener(this);
        iv_gift.setOnClickListener(this);
        iv_setting.setOnClickListener(this);
        btn_delete.setOnClickListener(this);
        btn_left.setOnClickListener(this);
        btn_mid.setOnClickListener(this);
        btn_right.setOnClickListener(this);

        CommonUtil.setLongClick(btn_color, "纯色");
        CommonUtil.setLongClick(btn_texture, "纹理");
        CommonUtil.setLongClick(btn_photo, "图像");
        CommonUtil.setLongClick(btn_font, "字体");
        CommonUtil.setLongClick(btn_delete, "删除");
        CommonUtil.setLongClick(ib_ok, "完成编辑");
        CommonUtil.setLongClick(iv_gift, "样例");
        CommonUtil.setLongClick(iv_setting, "设置");
        CommonUtil.setLongClick(btn_left, "左对齐");
        CommonUtil.setLongClick(btn_mid, "居中对齐");
        CommonUtil.setLongClick(btn_right, "右对齐");
        CommonUtil.setLongClick(ibColor, "字体颜色");
        CommonUtil.setLongClick(tvSizeAdd, "增大字体");
        CommonUtil.setLongClick(tvSizeDecrease, "减小字体");
        CommonUtil.setLongClick(ibMore, "修改字体");

        //设置宽高均为屏幕的宽度
        int size = CommonUtil.getScreenDisplay().getWidth();
        drawView.setLayoutParams(new RelativeLayout.LayoutParams(size, size));

        drawView.setOnDrawViewClickListener(new DrawView.OnDrawViewClickListener() {
            @Override
            public void onClick(String text) {
                if (!isKeyboardShow) {
                    showDrawText(true);
                    et_drawtext.requestFocus();
                    CommonUtil.showKeyboard(MainActivity.this, et_drawtext);
                } else {
                    CommonUtil.hideKeyboard(MainActivity.this);
                }
            }

            @Override
            public void onLightSeek(float percent) {
                //亮度处理
                drawView.setBrightPaint(percent * 255);
                drawView.invalidate();
            }

            @Override
            public void onDimSeek(float percent) {
                //高斯模糊处理
                if (percent > 0 && percent <= 1) {
                    drawView.setBackBitmap(CommonUtil.blurBitmap(BitmapFactory.decodeFile(nowDrawBitmapPath), percent * 25f), JBookHelper.getColorRes());
                } else {
                    drawView.setBackBitmap(BitmapFactory.decodeFile(nowDrawBitmapPath), JBookHelper.getColorRes());
                }

            }
        });
        //获取屏幕高度
        screenHeight = CommonUtil.getScreenDisplay().getHeight();
        //阀值设置为屏幕高度的1/3
        keyHeight = screenHeight / 3;
        layout_containert.addOnLayoutChangeListener(this);
    }

    private void showDrawText(boolean needShow) {
        if (needShow) {
            et_drawtext.setText(StringUtil.isEqualsString(drawView.getDrawText(), getString(R.string.default_text)) ? "" : drawView.getDrawText());
            et_drawtext.setSelection(et_drawtext.getText().toString().length());

            layout_drawtext.setVisibility(View.VISIBLE);
            drawView.setDrawText("");
        } else {
            layout_drawtext.setVisibility(View.GONE);
            drawView.setDrawText(StringUtil.isEmptyString(et_drawtext.getText().toString()) ? getString(R.string.default_text) : et_drawtext.getText().toString());
        }
        drawView.invalidate();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_color://颜色
                drawView.setBackBitmap(null, JBookHelper.getNextColorRes());
                break;
            case R.id.btn_texture://纹理
                drawView.setBackBitmap(BitmapFactory.decodeResource(getResources(), JBookHelper.getNextTextureRes()), JBookHelper.getColorRes());
                break;
            case R.id.btn_photo://图像
                choosePic();
                break;
            case R.id.btn_font://字体
                //先判断数量是否为1
                if (FontHelper.getTypefaces().size() == 1) {
                    startActivity(FontActivity.class);
                } else {//改变字体
                    drawView.setTextStyle(FontHelper.changeTypeface());
                }
                break;
            case R.id.ib_ok://确定
                saveAndShare();
                break;
            case R.id.iv_gift://默认图片

                break;
            case R.id.iv_setting://设置
                startActivity(FontActivity.class);
                break;
            case R.id.btn_delete://删除
                //显示之前的纹理图
                drawView.setBackBitmap(BitmapFactory.decodeResource(getResources(), JBookHelper.getTextureRes()), JBookHelper.getColorRes());
                showDeleteButton(false);
                showOkButton(false);
                break;
            case R.id.btn_left://左对齐
                et_drawtext.setGravity(Gravity.LEFT);
                drawView.setAlignment(Paint.Align.LEFT);
                break;
            case R.id.btn_mid://居中
                et_drawtext.setGravity(Gravity.CENTER_HORIZONTAL);
                drawView.setAlignment(Paint.Align.CENTER);
                break;
            case R.id.btn_right://右对齐
                et_drawtext.setGravity(Gravity.RIGHT);
                drawView.setAlignment(Paint.Align.RIGHT);
                break;
            case R.id.tv_size_add://增大字体
                drawView.addOrDecreaseTextSize(true);
                break;
            case R.id.tv_size_decrease://减小字体
                drawView.addOrDecreaseTextSize(false);
                break;
            case R.id.ib_color://改变颜色
                showChooseColor();
                break;
            case R.id.ib_more://修改字体
                showOrDissmissControl();
                break;
        }
    }

    public void choosePic() {
        ChoicePicActivity.start(this, true, CHOOSE_PIC);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == CHOOSE_PIC) {
                //实例化图片
                nowDrawBitmapPath = data.getData().getPath();
                drawView.setBackBitmap(BitmapFactory.decodeFile(nowDrawBitmapPath), 0);
                showDeleteButton(true);
                showOkButton(true);
            }
        }

    }

    private void showOkButton(boolean needShow) {
        if (!needShow && (StringUtil.isEmptyString(drawView.getDrawText()) || StringUtil.isEqualsString(drawView.getDrawText(), getString(R.string.default_text)))) {
            ib_ok.setVisibility(View.GONE);
        } else {
            ib_ok.setVisibility(View.VISIBLE);
        }
    }

    private void showDeleteButton(boolean needShow) {
        //隐藏颜色和纹理
        layout_texture.setVisibility(needShow ? View.GONE : View.VISIBLE);
        layout_color.setVisibility(needShow ? View.GONE : View.VISIBLE);
        //显示删除
        layout_delete.setVisibility(needShow ? View.VISIBLE : View.GONE);

        //显示deletebutton的时候表示的是图像，这个时候需要调整亮度啥的
        if (needShow) {
            drawView.setIsNeedSeekShow(true);
            drawView.setSeekShowOrDissmiss(false);
        } else {
            drawView.setIsNeedSeekShow(false);
        }
        drawView.setDefault();
    }

    @Override
    public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
        if (oldBottom != 0 && bottom != 0 && (oldBottom - bottom > keyHeight)) {//软键盘弹起
            isKeyboardShow = true;
        } else if (oldBottom != 0 && bottom != 0 && (bottom - oldBottom > keyHeight)) {//软键盘关闭
            isKeyboardShow = false;
            showDrawText(false);
            showOkButton(false);
        }
    }

    private void showOrDissmissControl() {
        if (layoutControl.getVisibility() == View.VISIBLE) {
            layoutControl.startAnimation(dismissAnimation);
            layoutControl.setVisibility(View.GONE);
        } else if (layoutControl.getVisibility() == View.GONE) {
            layoutControl.setVisibility(View.VISIBLE);
            layoutControl.startAnimation(showAnimation);
        }
    }

    /**
     * 选择颜色
     **/
    private void showChooseColor() {
        mColorDialog = new ColorPickerDialog(this, drawView.getPaintColor());
        mColorDialog.setOnColorChangedListener(new ColorPickerDialog.OnColorChangedListener() {
            @Override
            public void onColorChanged(int color) {
                drawView.setPaintColor(color);
                mColorDialog.dismiss();
            }
        });
        mColorDialog.show();
    }


    /**
     * 保存图片并且分享
     **/
    private void saveAndShare() {
        getPDM().showProgress("正在生成jbook");
        new Thread(new Runnable() {
            @Override
            public void run() {
                Bitmap srcBitmap = drawView.getShareBitmap();
                //生成父路径
                File parentFile = new File(CommonUtil.getDir() + File.separator + "jbook");
                if (!parentFile.exists()) {
                    parentFile.mkdirs();
                }
                //保存图片
                File file = new File(parentFile.getAbsolutePath() + File.separator + System.currentTimeMillis() + ".png");
                FileUtil.saveBmpToFilePng(srcBitmap, file);

                Message msg = new Message();
                msg.what = 0;
                msg.obj = file.getAbsolutePath();
                mHandler.sendMessage(msg);
            }
        }).start();

    }

    private Handler mHandler = new Handler() {
        @Override
        public void dispatchMessage(Message msg) {
            if (msg.what == 0) {
                getPDM().dismiss();
                //分享图片
                Uri uri = Uri.parse("file://" + msg.obj);
                Intent it = new Intent(Intent.ACTION_SEND);
                it.putExtra(Intent.EXTRA_STREAM, uri);
                it.setType("image/*");
                startActivityForResult(Intent.createChooser(it,
                        "分享您的JBook"), 10);
            }

        }
    };
}
