package net.xj.nutz.module;

import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Attr;

import net.xj.nutz.bean.Result;
import net.xj.nutz.bean.Tb_message;
import net.xj.nutz.bean.Tb_user;

@At("/message")
@IocBean
public class MessageModel {
	@Inject
	private Dao dao;
	private static final Log log=Logs.get();
	
	@At("/getmessage")
	public Object getMessage(@Attr("user")Tb_user user){
		return null;
	}
	
	@At("/countmessage")
	public int countMessage(@Attr("user")Tb_user user){
		if(user==null){
			return 0;
		}
		return dao.count(Tb_message.class,Cnd.where("userId","=",user.getUserId()).and("isRead","=",false));
		
	}
}
