package net.xj.nutz.module;

import java.text.SimpleDateFormat;
import java.util.Date;

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
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.POST;
import org.nutz.mvc.annotation.Param;
import org.nutz.mvc.view.JspView;

import com.ndktools.javamd5.Mademd5;

import net.xj.nutz.bean.Tb_admin;
import net.xj.nutz.bean.Tb_user;


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
			return new JspView("AdminLogin");
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
}
