package net.xj.nutz.module;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Attr;
import org.nutz.mvc.annotation.DELETE;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.POST;
import org.nutz.mvc.annotation.Param;
import org.nutz.mvc.view.JspView;

import com.ndktools.javamd5.Mademd5;

import net.xj.nutz.bean.Result;
import net.xj.nutz.bean.Tb_admin;
import net.xj.nutz.bean.Tb_message;
import net.xj.nutz.bean.Tb_user;
import net.xj.nutz.ext.Messages;


@IocBean
public class AdminJumpModel {
	
	@Inject
	private Dao dao;
	private static final Log log=Logs.get();

	
	@At("/admin")
	public JspView admin(@Attr("admin")Tb_admin admin){
		if(admin==null){
			return new JspView("AdminLogin");
		}else{
			return new JspView("AdminControl");
		}
	}
	
	@At("/admin/login")
	@POST
	@Ok(">>:/admin")
	public Object login(@Param("userId")String userId,
						@Param("password")String password,
						HttpServletRequest req,HttpSession session
			){//req.getRemoteAddr()
		Mademd5 md = new Mademd5();
		password=md.toMd5(password);
		Tb_admin admin=dao.fetch(Tb_admin.class,Cnd.where("userId","=",userId).and("password","=",password));
		if(admin!=null){
			session.setAttribute("admin", admin);
			admin.setLastLoginIp(req.getRemoteAddr());
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			admin.setLastLoginTime(df.format(new Date()));
			dao.update(admin);
		}
		return null;
	}
	
	/**
	 * 获取所有用户
	 * @param admin
	 * @return
	 */
	@At("/admin/getuser")
	@Ok("json")
	public Object getUser(@Attr("admin")Tb_admin admin){
		if(admin==null){return null;}
		List<Tb_user> list=dao.query(Tb_user.class, null);
		return list;
	}
	
	//向所有人发送信息
	@At("/admin/sendmessage")
	@Ok("json")
	@POST
	public Object sendMessage(@Attr("admin")Tb_admin admin,
							  @Param("message")String message){
		Result result=new Result();
		if(message==null||message.length()==0||admin==null){
			result.setInfo("未登录或发送的信息为空！");
			result.setStatus(0);
			return result;
		}
		List<Tb_user> users=dao.query(Tb_user.class, null);
		for(Tb_user user:users){
			Messages.addMessage(dao, user.getUserId(), "#", message, 1);
		}
		result.setInfo("发送成功！");
		result.setStatus(1);
		return result;
	}
	
	//获取所有发送信息
	@At("/admin/getmessage")
	@Ok("json")
	public Object getMessage(@Attr("admin")Tb_admin admin,
							 @Param("keyword")String keyword){
		Result result=new Result();
		if(keyword==null)keyword="";
		if(admin==null){
			return null;
		}
		List<Tb_message> messages=dao.query(Tb_message.class, Cnd.where("messageLevel","=",1).and("messageString","LIKE","%"+keyword+"%").orderBy("messageTime", "desc"));
		return messages;
	}
	
	//封禁某人
	@At("/admin/banned")
	@Ok("json")
	@POST
	public Object userBanned(@Attr("admin")Tb_admin admin,
							 @Param("userId")String uid){
		Result result=new Result();
		if(admin==null){
			result.setInfo("未登录！");
			result.setStatus(-1);
			return result;
		}
		Tb_user user=dao.fetch(Tb_user.class,Integer.valueOf(uid));
		user.setLocked(true);
		dao.update(user);
		result.setInfo("封禁成功！");
		result.setStatus(1);
		return result;
	}
	
	//解封某人
	@At("/admin/unbanned")
	@Ok("json")
	@POST
	public Object userUnBanned(@Attr("admin")Tb_admin admin,
							 @Param("userId")String uid){
		Result result=new Result();
		if(admin==null){
			result.setInfo("未登录！");
			result.setStatus(-1);
			return result;
		}
		Tb_user user=dao.fetch(Tb_user.class,Integer.valueOf(uid));
		user.setLocked(false);
		dao.update(user);
		result.setInfo("解封成功！");
		result.setStatus(1);
		return result;
	}
	
	
		//解封某人
		@At("/admin/delete")
		@Ok("json")
		@POST
		public Object deleteMessage(@Attr("admin")Tb_admin admin,
								 @Param("messageId")String messageid){
			Result result=new Result();
			if(admin==null){
				result.setInfo("未登录！");
				result.setStatus(-1);
				return result;
			}
			dao.delete(Tb_message.class, Integer.valueOf(messageid));
			result.setInfo("删除成功！");
			result.setStatus(1);
			return result;
		}
}
