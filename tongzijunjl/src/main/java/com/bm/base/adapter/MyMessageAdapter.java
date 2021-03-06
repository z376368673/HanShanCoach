package com.bm.base.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bm.app.App;
import com.bm.base.BaseAd;
import com.bm.entity.MessageList;
import com.bm.entity.Model;
import com.richer.tzjjl.R;
import com.lib.widget.RoundImageViewFive;
import com.nostra13.universalimageloader.core.ImageLoader;

import cz.msebera.android.httpclient.util.TextUtils;

/**
 * 我的消息适配器
 * @author wanghy
 *
 */
public class MyMessageAdapter  extends BaseAd<MessageList>{
	public MyMessageAdapter(Context context,List<MessageList> prolist){
		setActivity(context, prolist);
	}
	
	@Override
	protected View setConvertView(View convertView, final int position) {
		ItemView itemView = null;
		if(convertView  ==null){
			itemView = new ItemView();
			convertView = mInflater.inflate(R.layout.item_list_my_message, null);
			itemView.tv_title = (TextView)convertView.findViewById(R.id.tv_title);
			itemView.iv_pic = (ImageView)convertView.findViewById(R.id.iv_pic);
			itemView.tv_date = (TextView)convertView.findViewById(R.id.tv_date);
			itemView.img_read = (ImageView)convertView.findViewById(R.id.img_read);
			convertView.setTag(itemView);
		}else{
			itemView = (ItemView)convertView.getTag();
		}
		
		MessageList entity= mList.get(position);
		ImageLoader.getInstance().displayImage(entity.titleMultiUrl, itemView.iv_pic,App.getInstance().getHeadOptions());
		itemView.tv_title.setText(getNullData(entity.title));//课程名称
		itemView.tv_date.setText(getNullData(entity.ctime).substring(0, 16));//时间
		//是否未读  1 已读 0 未读
		if(!TextUtils.isEmpty(entity.isRead)){
			if(entity.isRead.equals("0")){
				itemView.img_read.setVisibility(View.VISIBLE);
			}else{
				itemView.img_read.setVisibility(View.GONE);
			}
		}
		
		return convertView;
	}
	class ItemView{
		private TextView tv_title,tv_date;
		private ImageView iv_pic,img_read;
	
	}
}
