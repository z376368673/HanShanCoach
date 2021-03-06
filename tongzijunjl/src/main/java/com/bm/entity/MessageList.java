package com.bm.entity;

import java.io.Serializable;

/**
* 消息列表
 * @author jiangsh
 *
 */
public class MessageList implements Serializable {
	/**
	 * 标题
	 */
	public String title;
	/**
	 * 图片路径
	 */
	public String titleMultiUrl;
	/**
	 *日期
	 */
	public String ctime;
	
	/**
	 * 消息id
	 */
	public String messageId;
	
	public String content;
	
	public String thinContent;
	/**
	 *  是否未读  1 已读 0 未读
	 */
	public String isRead;
}
