package com.ngbj.wallpaper.adapter.app;



import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ngbj.wallpaper.R;
import com.ngbj.wallpaper.bean.entityBean.HistoryBean;
import com.ngbj.wallpaper.bean.entityBean.InterestBean;

import java.util.List;


public class Interest_Adapter extends BaseQuickAdapter<InterestBean,BaseViewHolder> {

    public Interest_Adapter(List<InterestBean> data) {
        super(R.layout.interest_item,data);
    }

    @Override
    protected void convert(BaseViewHolder helper, InterestBean item) {
        helper.setText(R.id.interest_name,  item.getName());
        if(item.isSelect()){
            helper.setImageResource(R.id.icon_love,R.mipmap.interest_select);
        }else
            helper.setImageResource(R.id.icon_love,R.mipmap.interest_unselect);

    }
}