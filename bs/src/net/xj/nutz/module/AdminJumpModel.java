package net.xj.nutz.module;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.pager.Pager;
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
import org.nutz.trans.Atom;
import org.nutz.trans.Trans;

import com.ndktools.javamd5.Mademd5;

import net.xj.nutz.bean.Result;
import net.xj.nutz.bean.Tb_admin;
import net.xj.nutz.bean.Tb_bigClass;
import net.xj.nutz.bean.Tb_cashApplication;
import net.xj.nutz.bean.Tb_classCollege;
import net.xj.nutz.bean.Tb_classGrade;
import net.xj.nutz.bean.Tb_goods;
import net.xj.nutz.bean.Tb_login;
import net.xj.nutz.bean.Tb_message;
import net.xj.nutz.bean.Tb_smallClass;
import net.xj.nutz.bean.Tb_user;
import net.xj.nutz.ext.Messages;


@IocBean
public class AdminJumpModel {
	
	@Inject
	private Dao dao;
	private static final Log log=Logs.get();

	//访问/admin后接口根据是否登录管理员账号来返回jsp，未登录返回登录页面，已登录返回管理页面
	@At("/admin")
	public JspView admin(@Attr("admin")Tb_admin admin){
		if(admin==null){
			return new JspView("AdminLogin");
		}else{
			return new JspView("AdminControl");
		}
	}
	
	/**
	 * 管理员登录接口
	 * @param userId
	 * @param password
	 * @param req
	 * @param session
	 * @return
	 */
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
			admin.setLastLoginIp(getIpAddress(req));
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
	
	
	/**
	 * 获取所有学院
	 * @param admin
	 * @return
	 */
	@At("/admin/getcollege")
	@Ok("json")
	public Object getCollege(@Attr("admin")Tb_admin admin){
		if(admin==null){return null;}
		List<Tb_classCollege> list=dao.query(Tb_classCollege.class, null);
		return list;
	}
	/**
	 * 获取班级
	 * @param admin
	 * @return
	 */
	@At("/admin/getgrade")
	@Ok("json")
	public Object getCollege(@Attr("admin")Tb_admin admin,@Param("collegeId")long collegeId){
		if(admin==null){return null;}
		List<Tb_classGrade> list;
		if(collegeId==0)
		list=dao.query(Tb_classGrade.class, null);
		else
			list=dao.query(Tb_classGrade.class, Cnd.where("classCollegeId","=",collegeId));
		return list;
	}
	
	
	/**
	 * 获取bigclass
	 * @param admin
	 * @return
	 */
	@At("/admin/getbigclass")
	@Ok("json")
	public Object getBigClass(@Attr("admin")Tb_admin admin){
		if(admin==null){return null;}
		List<Tb_bigClass> list=dao.query(Tb_bigClass.class, null);
		return list;
	}
	
	/**
	 * 获取小类别
	 * @param admin
	 * @return
	 */
	@At("/admin/getsmallclass")
	@Ok("json")
	public Object getSmallClass(@Attr("admin")Tb_admin admin,@Param("bigClassId")long bigClassId){
		if(admin==null){return null;}
		List<Tb_smallClass> list;
		if(bigClassId==0)
		list=dao.query(Tb_smallClass.class, null);
		else
			list=dao.query(Tb_smallClass.class, Cnd.where("bigClassId","=",bigClassId));
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
	
	
		//删除信息
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
		
		//用户登陆次数
		@At("/admin/getlogincount")
		@Ok("json")
		public Object getLogincount(@Attr("admin")Tb_admin admin){
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			String date=df.format(new Date());
			List<Tb_login> list=dao.query(Tb_login.class, Cnd.where("loginDate","<=",date));
			return list;
		}
		
		//添加学院
		@At("/admin/addschool")
		@POST
		@Ok("json")
		public Object addSchool(@Attr("admin")Tb_admin admin,@Param("college")String college){
			Result result=new Result();
			if(admin==null){result.setInfo("未登录！");result.setStatus(-1);return result;}
			if(college==null||college.length()==0){result.setInfo("学院不能为空！");result.setStatus(-1);return result;}
			List<Tb_classCollege> list=dao.query(Tb_classCollege.class,Cnd.where("classCollegeName","=",college));
			if(list.size()!=0){result.setInfo("该学院已存在！");result.setStatus(-1);return result;}
			Tb_classCollege classCollege=new Tb_classCollege();
			classCollege.setClassCollegeName(college);
			dao.fastInsert(classCollege);
			result.setInfo("添加成功！");
			result.setStatus(1);
			return result;
		}
		
		
		//添加班级
		@At("/admin/addgrade")
		@POST
		@Ok("json")
		public Object addSchool(@Attr("admin")Tb_admin admin,@Param("college")String college,@Param("grade")String grade){
			Result result=new Result();
			if(admin==null){result.setInfo("未登录！");result.setStatus(-1);return result;}
			if(college==null||college.length()==0){result.setInfo("学院不能为空！");result.setStatus(-1);return result;}
			if(grade==null||grade.length()==0){result.setInfo("班级不能为空！");result.setStatus(-1);return result;}
			List<Tb_classCollege> list=dao.query(Tb_classCollege.class,Cnd.where("classCollegeName","=",college));
			if(list.size()==0){result.setInfo("学院不存在！");result.setStatus(-1);return result;}
			Tb_classCollege classCollege=list.get(0);
			Tb_classGrade classGrade=new Tb_classGrade();
			classGrade.setClassGradeName(grade);
			classGrade.setClassCollegeId(classCollege.getClassCollegeId());
			dao.fastInsert(classGrade);
			result.setInfo("添加成功！");
			result.setStatus(1);
			return result;
		}
		
		//删除学院
		@At("/admin/deletecollage")
		@POST
		@Ok("json")
		public Object deleteCollage(@Attr("admin")Tb_admin admin,@Param("classCollegeId")long classCollegeId){
			Result result=new Result();
			if(admin==null){result.setInfo("未登录！");result.setStatus(-1);return result;}
			if(classCollegeId==0){result.setInfo("学院不存在！");result.setStatus(-1);return result;}
			dao.delete(Tb_classCollege.class,classCollegeId);
			dao.clear(Tb_classGrade.class,Cnd.where("classCollegeId","=",Long.valueOf(classCollegeId)));
			result.setInfo("删除成功！");
			result.setStatus(1);
			return result;
		}
		
		
		//删除班级
		@At("/admin/deletegrade")
		@POST
		@Ok("json")
		public Object deleteGrade(@Attr("admin")Tb_admin admin,@Param("classGradeId")long classGradeId){
			Result result=new Result();
			if(admin==null){result.setInfo("未登录！");result.setStatus(-1);return result;}
			if(classGradeId==0){result.setInfo("班级不存在！");result.setStatus(-1);return result;}
			dao.delete(Tb_classGrade.class,classGradeId);
			result.setInfo("删除成功！");
			result.setStatus(1);
			return result;
		}
		
		
		
		
		//添加大类别addbigclass
		@At("/admin/addbigclass")
		@POST
		@Ok("json")
		public Object addBigclass(@Attr("admin")Tb_admin admin,@Param("bigclass")String bigclass){
			Result result=new Result();
			if(admin==null){result.setInfo("未登录！");result.setStatus(-1);return result;}
			if(bigclass==null||bigclass.length()==0){result.setInfo("类别名称为空！");result.setStatus(-1);return result;}
			List<Tb_bigClass> list=dao.query(Tb_bigClass.class,Cnd.where("bigClass","=",bigclass));
			if(list.size()!=0){result.setInfo("该类别已存在！");result.setStatus(-1);return result;}
			Tb_bigClass bigClass=new Tb_bigClass();
			bigClass.setBigClass(bigclass);
			dao.fastInsert(bigClass);
			result.setInfo("添加成功！");
			result.setStatus(1);
			return result;
		}
		
		
		//添加小类别addsmallclass
		@At("/admin/addsmallclass")
		@POST
		@Ok("json")
		public Object addSmallClass(@Attr("admin")Tb_admin admin,@Param("bigClass")String bigClass,@Param("smallclass")String smallclass){
			Result result=new Result();
			if(admin==null){result.setInfo("未登录！");result.setStatus(-1);return result;}
			if(bigClass==null||bigClass.length()==0){result.setInfo("大类别不能为空！");result.setStatus(-1);return result;}
			if(smallclass==null||smallclass.length()==0){result.setInfo("小类别不能为空！");result.setStatus(-1);return result;}
			List<Tb_bigClass> list=dao.query(Tb_bigClass.class,Cnd.where("bigClass","=",bigClass));
			if(list.size()==0){result.setInfo("大类别不存在！");result.setStatus(-1);return result;}
			Tb_bigClass bigClass2=list.get(0);
			Tb_smallClass smallClass2=new Tb_smallClass();
			smallClass2.setBigClassId(bigClass2.getBigClassId());
			smallClass2.setSmallClass(smallclass);
			dao.fastInsert(smallClass2);
			result.setInfo("添加成功！");
			result.setStatus(1);
			return result;
		}
		
		//删除大类别
		@At("/admin/deletebigclass")
		@POST
		@Ok("json")
		public Object deletebigclass(@Attr("admin")Tb_admin admin,@Param("bigClassId")long bigClassId){
			Result result=new Result();
			if(admin==null){result.setInfo("未登录！");result.setStatus(-1);return result;}
			if(bigClassId==0){result.setInfo("大类别不存在！");result.setStatus(-1);return result;}
			dao.delete(Tb_bigClass.class,bigClassId);
			dao.clear(Tb_smallClass.class,Cnd.where("bigClassId","=",Long.valueOf(bigClassId)));
			result.setInfo("删除成功！");
			result.setStatus(1);
			return result;
		}
		
		
		//删除小类别
		@At("/admin/deletesmallcalss")
		@POST
		@Ok("json")
		public Object deleteSmallCalss(@Attr("admin")Tb_admin admin,@Param("smallClassId")long smallClassId){
			Result result=new Result();
			if(admin==null){result.setInfo("未登录！");result.setStatus(-1);return result;}
			if(smallClassId==0){result.setInfo("小类别不存在！");result.setStatus(-1);return result;}
			dao.delete(Tb_smallClass.class,smallClassId);
			result.setInfo("删除成功！");
			result.setStatus(1);
			return result;
		}
		
		//获取商品
		@At("/admin/getgoods")
		@Ok("json")
		public Object getgoods(@Attr("admin")Tb_admin admin,@Param("page")int page){
			Result result=new Result();
			if(admin==null){result.setInfo("未登录！");result.setStatus(-1);return result;}
			if(page==0)page=1;
			final int PAGESIZE=10;
			Pager pager = dao.createPager(page, PAGESIZE);
			int count=dao.count(Tb_goods.class, Cnd.where("userId","!=",-1));
			int yu=count%PAGESIZE;
			count=count/PAGESIZE;
			if(yu!=0){
				count++;//总页数
			}
			List<Tb_goods> list=dao.query(Tb_goods.class, Cnd.where("userId","!=",-1),pager);
			result.setList(list);
			result.setTotal(count);
			return result;
		}
		
		//封禁goods
		@At("/admin/bannedgoods")
		@Ok("json")
		@POST
		public Object userBannedgoods(@Attr("admin")Tb_admin admin,
								 @Param("goodsId")String uid){
			Result result=new Result();
			if(admin==null){
				result.setInfo("未登录！");
				result.setStatus(-1);
				return result;
			}
			Tb_goods goods=dao.fetch(Tb_goods.class,Integer.valueOf(uid));
			if(goods.getGoodsStatus()==1){
				goods.setGoodsStatus(-1);
				dao.update(goods);
				result.setInfo("封禁成功！");
				result.setStatus(1);
				return result;
			}else{
				result.setInfo("商品状态异常成功！");
				result.setStatus(-1);
				return result;
			}
			
		}
		
		//解封goods
		@At("/admin/unbannedgoods")
		@Ok("json")
		@POST
		public Object userUnBannedgoods(@Attr("admin")Tb_admin admin,
								 @Param("goodsId")String uid){
			Result result=new Result();
			if(admin==null){
				result.setInfo("未登录！");
				result.setStatus(-1);
				return result;
			}
			Tb_goods goods=dao.fetch(Tb_goods.class,Integer.valueOf(uid));
			goods.setGoodsStatus(1);
			dao.update(goods);
			result.setInfo("解封成功！");
			result.setStatus(1);
			return result;
		}
		
		
		//申请提现
		@At("/admin/getapplication")
		@Ok("json")
		public Object getapplication(@Attr("admin")Tb_admin admin){
			if(admin==null)return null;
			List<Tb_cashApplication> list=dao.query(Tb_cashApplication.class, Cnd.where("cashApplicationStatus","=",0));
			return list;
		}
		
		@At("/admin/dealapplication")
		@POST
		@Ok("json")
		public Object dealapplication(@Attr("admin")Tb_admin admin,
									  @Param("aid")long aid,
									  @Param("passorcancel")String poc)
		{
			Result result=new Result();
			if(admin==null){
				result.setInfo("未登录！");
				result.setStatus(-1);
				return result;
			}
			if(poc.equals("pass")){
				Tb_cashApplication application=dao.fetch(Tb_cashApplication.class,aid);
				if(application!=null){
					application.setCashApplicationStatus(1);
					dao.update(application);
				}
				
			}else if(poc.equals("cancel")){
				final Tb_cashApplication application=dao.fetch(Tb_cashApplication.class,aid);
				if(application!=null&&application.getCashApplicationStatus()==0){
					final Tb_user user=dao.fetch(Tb_user.class,application.getUserId());
					user.setUserMoney(user.getUserMoney()+application.getCashNumber());
					application.setCashApplicationStatus(-1);
					Trans.exec(new Atom(){//事务
					    public void run() {
					        dao.update(application);
					        dao.update(user);
					    }
					});
				}
			}
			result.setInfo("处理成功！");
			result.setStatus(1);
			return result;
		}
		
			//根据request获取ip地址
		    public  String getIpAddress(HttpServletRequest request) {  
			         String ip = request.getHeader("x-forwarded-for");  
			         if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
			             ip = request.getHeader("Proxy-Client-IP");  
			         }  
			         if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
			             ip = request.getHeader("WL-Proxy-Client-IP");  
			         }  
			         if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
			             ip = request.getHeader("HTTP_CLIENT_IP");  
			         }  
			         if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
			             ip = request.getHeader("HTTP_X_FORWARDED_FOR");  
			         }  
			         if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
			             ip = request.getRemoteAddr();  
			         }  
			         return ip;  
			     }
		
		
		
}
