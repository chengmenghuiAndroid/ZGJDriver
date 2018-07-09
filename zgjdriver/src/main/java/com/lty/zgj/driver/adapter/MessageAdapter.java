package com.lty.zgj.driver.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.lty.zgj.driver.R;
import com.lty.zgj.driver.bean.DriverNoticeInfoModel;
import com.lty.zgj.driver.core.tool.TimeUtils;
import com.zhy.autolayout.utils.AutoUtils;

import butterknife.BindView;
import cn.droidlover.xdroid.base.SimpleRecAdapter;
import cn.droidlover.xdroid.kit.KnifeKit;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Administrator on 2018/6/15.
 */


public class MessageAdapter extends SimpleRecAdapter<DriverNoticeInfoModel.RecordsBean, MessageAdapter.ViewHolder> {

    private static final int TAG_VIEW = 0;

    public MessageAdapter(Context context) {
        super(context);
    }

    @Override
    public ViewHolder newViewHolder(View itemView) {
        return new ViewHolder(itemView);
    }

    @Override
    public int getLayoutId() {
        return R.layout.message_adapter;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        final DriverNoticeInfoModel.RecordsBean recordsBean = data.get(position);
        holder.tvMsgTitle.setText(recordsBean.getTitle());
        holder.tvMsgContent.setText(recordsBean.getContent());

        String createTime = recordsBean.getCreateTime();
        String ymdHms = TimeUtils.getYMDHms(createTime);
        holder.tvMsgTime.setText(ymdHms);



        int type = Integer.parseInt(recordsBean.getType());
        switch (type){
            case 1://坐公交信息
            case 3://验票码信息
                holder.profileImageType.setImageDrawable(context.getResources().getDrawable(R.mipmap.pic_zgj));
                break;

            case 2://邯郸公交
                holder.profileImageType.setImageDrawable(context.getResources().getDrawable(R.mipmap.pic_handan));
                break;
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getRecItemClick().onItemClick(position, recordsBean, TAG_VIEW, holder);
            }
        });


    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.profile_image_type)
        CircleImageView profileImageType;
        @BindView(R.id.tv_msg_title)
        TextView tvMsgTitle;
        @BindView(R.id.tv_msg_time)
        TextView tvMsgTime;
        @BindView(R.id.tv_msg_date)
        TextView tvMsgDate;
        @BindView(R.id.tv_msg_content)
        TextView tvMsgContent;


        public ViewHolder(View itemView) {
            super(itemView);
            KnifeKit.bind(this, itemView);
            AutoUtils.auto(itemView);
        }
    }
}
