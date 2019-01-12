package com.din.mzitu.ui.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.MenuItem;

import com.din.mzitu.R;
import com.din.mzitu.api.LiGui;
import com.din.mzitu.ui.fragments.main.FragmentLiGui;
import com.din.mzitu.ui.fragments.main.FragmentMzitu;
import com.din.mzitu.utill.FragmentUtil;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private FragmentMzitu fragmentMzitu;
    private FragmentLiGui fragmentLiGui;
    private List<Fragment> list;
    private FragmentUtil fragmentUtil;
    private boolean isInit = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 获取数据时添加观察者模式
        getLifecycle().addObserver(LiGui.getInstance());
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!isInit) {
            initView();
            isInit = true;
        }
    }

    private void initView() {
        drawerLayout = findViewById(R.id.drawerlayout);
        navigationView = findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);

        fragmentMzitu = new FragmentMzitu();
        fragmentLiGui = new FragmentLiGui();
        list = new ArrayList<>();           // Fragment的add与切换
        fragmentUtil = new FragmentUtil(this, R.id.framelayout, list);
        fragmentUtil.showFragment(fragmentMzitu);
        navigationView.getMenu().getItem(0).setChecked(true);   // 选中的侧滑item着色
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(Gravity.LEFT)) {
            drawerLayout.closeDrawers();    // 侧滑打开时，点击返回关闭侧滑
        } else {    // 侧滑关闭时，点击两次返回退出
            if (!moveTaskToBack(false)) {
                super.onBackPressed();
            }
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull final MenuItem item) {
        if (drawerLayout.isDrawerOpen(Gravity.LEFT)) {
            drawerLayout.closeDrawers();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (item.getItemId() == R.id.mzitu) {
                        fragmentUtil.hideFragment().showFragment(fragmentMzitu);
                        navigationView.getMenu().getItem(0).setChecked(true);
                    } else if (item.getItemId() == R.id.ligui) {
                        fragmentUtil.hideFragment().showFragment(fragmentLiGui);
                        navigationView.getMenu().getItem(1).setChecked(true);
                    } else if (item.getItemId() == R.id.setting) {

                    } else if (item.getItemId() == R.id.issues) {
                        // 跳转懂啊网页，提Issues
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse("https://github.com/freedomeden/mzitu-app/issues"));
                        startActivity(intent);
                    } else if (item.getItemId() == R.id.about) {
                        // 联系我
                        Intent intent = new Intent(Intent.ACTION_ATTACH_DATA);
                        intent.setData(Uri.parse("mailto:dinzhenyan1997@126.com"));
                        intent.putExtra(Intent.EXTRA_SUBJECT, "意见反馈");
                        startActivity(intent);
                    }
                }
            }, 300);
        }
        return false;
    }
}