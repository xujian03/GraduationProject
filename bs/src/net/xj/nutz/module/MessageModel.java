package net.xj.nutz.module;

import java.util.List;

import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Attr;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.POST;
import org.nutz.mvc.annotation.Param;

import net.xj.nutz.bean.Result;
import net.xj.nutz.bean.Tb_message;
import net.xj.nutz.bean.Tb_user;

@At("/message")
@IocBean
public class MessageModel {
	@Inject
	private Dao dao;
	private static final Log log=Logs.get();
	
	/**
	 * 用户获取消息的接口
	 * @param user
	 * @param ishaveRead
	 * @return
	 */
	@At("/getmessage")
	@Ok("json")
	public Object getMessage(@Attr("user")Tb_user user,
							 @Param("ishaveRead")Boolean ishaveRead){
		if(user==null)
		return null;
		List<Tb_message> list=null;
		if(ishaveRead==null||ishaveRead==true){//包含以读消息
			 list=dao.query(Tb_message.class,Cnd.where("userId","=",user.getUserId()).orderBy("messageTime","desc"));
		}
		else{
			list=dao.query(Tb_message.class,Cnd.where("userId","=",user.getUserId()).and("isRead","=",false).orderBy("messageTime","desc"));
		}
		return list;
	}
	/**
	 * 用户有几条未读信息的接口
	 * @param user
	 * @return
	 */
	@At("/countmessage")
	public int countMessage(@Attr("user")Tb_user user){
		if(user==null){
			return 0;
		}
		return dao.count(Tb_message.class,Cnd.where("userId","=",user.getUserId()).and("isRead","=",false));
		
	}
	
	@At("/read")//标记消息为已读
	@POST
	public Object readMessage(@Attr("user") Tb_user user,@Param("messageId")long messageId){
		Result result=new Result();
		if(user==null){
			result.setInfo("未登录！");
		}
		Tb_message message=dao.fetch(Tb_message.class,Cnd.where("messageId","=",messageId).and("userId","=",user.getUserId()));
		if(message!=null){
			message.setIsRead(true);
			dao.update(message);
		}
		return result;
	}
	
}
