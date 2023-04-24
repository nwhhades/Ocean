package com.whiner.ocean;

import com.blankj.utilcode.util.ActivityUtils;
import com.whiner.kit.base.BaseActivity;
import com.whiner.ocean.databinding.ActivityAlertdialogBinding;

public class AlertDialogActivity extends BaseActivity<ActivityAlertdialogBinding> {

    public static void start() {
        ActivityUtils.startActivity(AlertDialogActivity.class);
    }

    @Override
    public ActivityAlertdialogBinding getViewBinding() {
        return ActivityAlertdialogBinding.inflate(getLayoutInflater());
    }

    @Override
    public void initView() {

    }

}
