package com.whiner.ocean;

import android.graphics.Color;
import android.util.Log;
import android.view.View;

import com.blankj.utilcode.util.PathUtils;
import com.hjq.permissions.Permission;
import com.whiner.kit.base.BaseActivity;
import com.whiner.kit.utils.UuidUtils;
import com.whiner.ocean.databinding.ActivityMainBinding;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends BaseActivity<ActivityMainBinding> {


    private static final String TAG = "MainActivity";

    private static final String url = "https://down.wireless-tech.cn/resourceDown/HX-AT-MESH-APK/hx-mesh-release-v0.1.0.apk";


    //private static final String url = "http://file.mounriver.com/upgrade/MounRiver_Studio_Setup_V184.zip";

    @Override
    public void preInit() {
        super.preInit();
        writeAppBackgroundSrc("http://tse2-mm.cn.bing.net/th/id/OIP-C.d0uSI7WjUmxhaR_MXssxRQHaE8?w=280&h=187&c=7&r=0&o=5&pid=1.7");
    }

    @Override
    public ActivityMainBinding getViewBinding() {
        return ActivityMainBinding.inflate(getLayoutInflater());
    }

    @Override
    public void initView() {
        viewBinding.tv.setText("ssssssssss - " + UuidUtils.getUUID());
        Log.d(TAG, "initView: " + UuidUtils.getUUID());

        viewBinding.tv.setTextColor(Color.WHITE);
        viewBinding.tv.setFocusable(true);
        viewBinding.tv.setClickable(true);
        viewBinding.tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //AlertDialogActivity.start();

                OkDownloadDialog fragment = new OkDownloadDialog();
                fragment.showDialog(getSupportFragmentManager());
                fragment.setTitle("ssfdasdfasfdasd");
                fragment.setContent("这个是内容");
                fragment.setUrl(url, PathUtils.getAppDataPathExternalFirst());
            }
        });
    }

    @Override
    public List<String> getPermissionList() {
        //return null;
        return Arrays.asList(Permission.MANAGE_EXTERNAL_STORAGE);
    }

    @Override
    public boolean waitPermissionInit() {
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

}