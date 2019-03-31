package net.xj.nutz.ext;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.nutz.dao.Dao;

import net.xj.nutz.bean.Result;
import net.xj.nutz.bean.Tb_message;

//消息的工具类
public class Messages {
	/**
	 * 向数据库添加消息
	 * @param dao
	 * @param userId
	 * @param messageUrl
	 * @param messageString
	 * @param messageLevel
	 * @return
	 */
	public static Object addMessage(Dao dao,
									long userId,
									String messageUrl,
									String messageString,
									int messageLevel) {
		//Result result =new Result();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String messageTime=df.format(new Date());//消息时间
		Tb_message message=new Tb_message();
		message.setUserId(userId);
		message.setMessageUrl(messageUrl);
		message.setIsRead(false);
		message.setMessageString(messageString);
		message.setMessageTime(messageTime);
		message.setMessageLevel(messageLevel);
		message =dao.fastInsert(message);
		
		
		return message;
	}
}
