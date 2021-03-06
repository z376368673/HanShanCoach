package com.bm.im.ac;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.bm.api.IMService;
import com.bm.app.App;
import com.bm.base.BaseActivity;
import com.bm.entity.CoachInfo;
import com.bm.im.adapter.FriendAdapter;
import com.bm.im.api.ImApi;
import com.bm.im.tool.Constant;
import com.richer.tzjjl.R;
import com.lib.http.ServiceCallback;
import com.lib.http.result.CommonListResult;
import com.lib.http.result.StringResult;

/**
 * 查找好友
 * 
 * @author shiyt
 * 
 */
public class AddFriendAc extends BaseActivity implements FriendAdapter.OnSeckillClick, OnClickListener {
	private Context mContext;
	private ListView lv_friend;
	private List<CoachInfo> list = new ArrayList<CoachInfo>();
	private FriendAdapter adapter ;
	private String pageType="";
	private TextView tv_newfriend,tv_seach;
	private EditText et_seach;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		contentView(R.layout.ac_lookfriend);
		mContext = this;
		pageType=getIntent().getStringExtra("pageType");
		init();
		
	}
	
	/**
	 * 初始化控件
	 */
	public void init(){
		et_seach=findEditTextById(R.id.et_seach);
		tv_seach=findTextViewById(R.id.tv_seach);
		tv_seach.setOnClickListener(this);
		tv_newfriend=findTextViewById(R.id.tv_newfriend);
		if("AddFriend".equals(pageType)){
			setTitleName("添加新玩伴儿");
			et_seach.setHint("按手机号/姓名搜索");
			tv_newfriend.setVisibility(View.VISIBLE);
		}else{
			setTitleName("查找玩伴儿");
			et_seach.setHint("按手机号/姓名搜索");
			tv_newfriend.setVisibility(View.GONE);
		}
		lv_friend=findListViewById(R.id.lv_friend);
		adapter = new FriendAdapter(mContext, list,pageType);
		lv_friend.setAdapter(adapter);
		
		adapter.setOnSeckillClick(this);
		
		lv_friend.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				if(!"AddFriend".equals(pageType)){
					ImApi.syncUserInfo(list.get(position));
					Intent intent = new Intent(mContext, ChatActivity.class);
					intent.putExtra("userId", list.get(position).userId);
					intent.putExtra("chatType", Constant.CHATTYPE_SINGLE);
					startActivity(intent);
				}
			}
		});
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		if("AddFriend".equals(pageType)){
			findNewFriends();
		}else{
			getNewFriends("");
		}
	}
	
	/**
	 * 查找玩伴儿
	 */
	 
	public void getNewFriends(String nickname) {
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("userid", App.getInstance().getCoach().coachId);
		if(!TextUtils.isEmpty(nickname)){
			map.put("nickname", nickname);
		}
		showProgressDialog();
		list.clear();
		IMService.getInstance().findNewFriendList(mContext, map, new ServiceCallback<CommonListResult<CoachInfo>>() {
			
			@Override
			public void error(String msg) {
				dialogToast(msg);
				hideProgressDialog();
			}
			
			@Override
			public void done(int what, CommonListResult<CoachInfo> obj) {
				hideProgressDialog();
				if(obj.data.size() > 0){
					list.addAll(obj.data);
				}
				adapter.notifyDataSetChanged();
			}
		});
	}
	/**
	 * 找朋友
	 */
	 
	public void findNewFriends() {
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("userid", App.getInstance().getCoach().coachId);
		showProgressDialog();
		
		IMService.getInstance().findMyFriendList(mContext, map, new ServiceCallback<CommonListResult<CoachInfo>>() {
			
			@Override
			public void error(String msg) {
				dialogToast(msg);
				hideProgressDialog();
			}
			
			@Override
			public void done(int what, CommonListResult<CoachInfo> obj) {
				hideProgressDialog();
				if(obj.data.size() > 0){
					list.clear();
					list.addAll(obj.data);
				}
				adapter.notifyDataSetChanged();
			}
		});
	}

	private void addFriend(final int position) {
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("userid", App.getInstance().getCoach().coachId);
		map.put("friendId", list.get(position).friendUserId);
		showProgressDialog();
		IMService.getInstance().addFriend(mContext, map, new ServiceCallback<StringResult>() {
			
			@Override
			public void error(String msg) {
				dialogToast(msg);
				hideProgressDialog();
			}
			
			@Override
			public void done(int what, StringResult obj) {
				hideProgressDialog();
				ImApi.syncUserInfo(list.get(position));
				Intent intent = new Intent(mContext, ChatActivity.class);
				intent.putExtra("userId", list.get(position).coachId);
				intent.putExtra("chatType", Constant.CHATTYPE_SINGLE);
				startActivity(intent);
			}
		});
	}
	@Override
	public void onSeckillClick(int position) {
		//同意
		if("AddFriend".equals(pageType)){
			//0表示同意 （调用同意接口并跳转聊天窗口）  1已添加（跳转聊天窗口）    
			if(list.get(position).friStatus.equals("0")){
				//同步用户信息
				addFriend(position);
			}else{
				//同步用户信息
				ImApi.syncUserInfo(list.get(position));
				Intent intent = new Intent(mContext, ChatActivity.class);
				intent.putExtra("userId", list.get(position).coachId);
				intent.putExtra("chatType", Constant.CHATTYPE_SINGLE);
				startActivity(intent);
			}
		}else{
			//同步用户信息
			ImApi.syncUserInfo(list.get(position));
			Intent intent = new Intent(mContext, ChatActivity.class);
			intent.putExtra("userId", list.get(position).coachId);
			intent.putExtra("chatType", Constant.CHATTYPE_SINGLE);
			startActivity(intent);
		}
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_seach:
			if("AddFriend".equals(pageType)){
//				if(TextUtils.isEmpty(et_seach.getText())){
//					dialogToast("请输入");
//					return;
//				}
				Intent intent =new Intent(mContext, SearchFriendAc.class);
				intent.putExtra("keyWord", et_seach.getText().toString());
				startActivity(intent);
			}else{
				list.clear();
				getNewFriends(et_seach.getText().toString());
			}
			
			break;

		default:
			break;
		}
		
	}
	
}
