package com.ngbj.wallpaper.module.app;



import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.ngbj.wallpaper.R;
import com.ngbj.wallpaper.base.BaseActivity;
import com.ngbj.wallpaper.module.fragment.CategoryFragment;
import com.ngbj.wallpaper.module.fragment.IndexFragment;
import com.ngbj.wallpaper.module.fragment.MyFragment;
import com.ngbj.wallpaper.mvp.contract.app.HomeContract;
import com.ngbj.wallpaper.mvp.presenter.app.HomePresenter;
import com.ngbj.wallpaper.utils.common.ToastHelper;

import butterknife.BindView;
import butterknife.OnClick;


public class HomeActivity extends BaseActivity<HomePresenter>
            implements HomeContract.View {

    @BindView(R.id.index_icon)
    ImageView indexIcon;

    @BindView(R.id.category_icon)
    ImageView categoryIcon;

    @BindView(R.id.my_icon)
    ImageView myIcon;


    IndexFragment indexFragment;
    CategoryFragment categoryFragment;
    MyFragment myFragment;
    Fragment currentFragment;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_home;
    }

    @Override
    protected void initData() {
        initIndexFragment();
    }

    @Override
    protected void initPresenter() {
        mPresenter = new HomePresenter();
    }

    /** 默认的Fragment */
    private void initIndexFragment(){
        FragmentTransaction transaction  = getSupportFragmentManager().beginTransaction();
        if(currentFragment == null)
            indexFragment = IndexFragment.getInstance();
        currentFragment = indexFragment;
        transaction.add(R.id.frameLayout,currentFragment);
        transaction.commit();
    }


    /** 切换Fragment */
    private void switchFragment(Fragment fragment){
        if(currentFragment != fragment){
            FragmentTransaction transaction  = getSupportFragmentManager().beginTransaction();
            transaction.hide(currentFragment);
            currentFragment = fragment;
            if(!fragment.isAdded()){
                transaction.add(R.id.frameLayout,fragment).commit();
            }else{
                transaction.show(fragment).commit();
            }
        }
    }

    /** 隐藏状态 */
    private void hideImageStatus(){
        indexIcon.setImageResource(R.mipmap.index_uncheck);
        categoryIcon.setImageResource(R.mipmap.category_uncheck);
        myIcon.setImageResource(R.mipmap.my_uncheck);
    }

    @OnClick({R.id.index,R.id.category,R.id.my})
    public void bottomClick(FrameLayout layout){

        hideImageStatus();
        switch (layout.getId()){
            case R.id.index:
                if(null == indexFragment) indexFragment = IndexFragment.getInstance();
                indexIcon.setImageResource(R.mipmap.index_check);
                switchFragment(indexFragment);
                break;
            case R.id.category:
                if(null == categoryFragment) categoryFragment = CategoryFragment.getInstance();
                categoryIcon.setImageResource(R.mipmap.category_check);
                switchFragment(categoryFragment);
                break;
            case R.id.my:
                if(null == myFragment) myFragment = MyFragment.getInstance();
                myIcon.setImageResource(R.mipmap.my_check);
                switchFragment(myFragment);
                break;
        }
    }


    @Override
    public void showSearchHistory() {

    }


    /** --------- 退出逻辑  在2秒内快速点击2次，则退出app --------- */
    long currentTime;
    long mExitTime; //退出时的时间
    public void exit() {
        currentTime = System.currentTimeMillis();
        if ((currentTime - mExitTime) > 2000) {
            ToastHelper.customToastView(this,"再按一次退出");
            mExitTime = currentTime;
        } else {
            mPresenter.getSearchHistory();
            finish();
        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0){
            exit();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
