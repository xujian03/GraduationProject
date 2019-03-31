package net.xj.nutz.module;


import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import java.util.UUID;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.pager.Pager;
import org.nutz.img.Images;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.json.Json;
import org.nutz.lang.Strings;
import org.nutz.lang.Times;
import org.nutz.lang.meta.Email;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.adaptor.JsonAdaptor;
import org.nutz.mvc.annotation.AdaptBy;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Attr;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.POST;
import org.nutz.mvc.annotation.Param;
import org.nutz.mvc.upload.TempFile;
import org.nutz.mvc.upload.UploadAdaptor;
import org.nutz.trans.Atom;
import org.nutz.trans.Trans;

import com.drew.imaging.jpeg.JpegMetadataReader;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifDirectory;
import com.ndktools.javamd5.Mademd5;
import com.sun.org.apache.bcel.internal.generic.DALOAD;

import net.xj.nutz.module.CreateImageCode;
import net.xj.nutz.bean.Result;
import net.xj.nutz.bean.Tb_address;
import net.xj.nutz.bean.Tb_bigClass;
import net.xj.nutz.bean.Tb_classCollege;
import net.xj.nutz.bean.Tb_classGrade;
import net.xj.nutz.bean.Tb_goods;
import net.xj.nutz.bean.Tb_login;
import net.xj.nutz.bean.Tb_order;
import net.xj.nutz.bean.Tb_smallClass;
import net.xj.nutz.bean.Tb_user;

@IocBean
@At("/user")
public class UserModule {
	@Inject
	private Dao dao;
	
	private static final Log log = Logs.get();
	/**
	 * @At("/regist")
	 * 注册接口   注册成功后再session user中加入用户信息
	 * @param email
	 * @param password
	 * @param name
	 * @param msg
	 * @param studentid
	 * @param img
	 * @param code
	 * @param session
	 * @return  status=-1 验证名错误
	 * 			status=0  必要参数为空，如邮箱、密码、学号和名字
	 * 			status=2 邮箱或学号已经被注册
	 * 			status=1注册成功
	 * 			status=-2 图片后缀不正确
	 */
	@At("/regist")
	@Ok("json")
	@POST
	@AdaptBy(type=UploadAdaptor.class)
	public Object regist(@Param("email")String email,
			@Param("password")String password,
			@Param("name")String name,
			@Param("msg")String msg,
			@Param("studentid")String studentid,
			@Param("img")TempFile img,
			@Param("code")String code,			
			HttpSession session ){
		Result result=new Result();
		code=email+code;
		String sessionCode=(String) session.getAttribute("registCode");
		if(sessionCode==null)
		{
			sessionCode="regetcode";
		}
		session.setAttribute("registCode", "regetcode");
		if(code==null||!code.equals(sessionCode)||sessionCode.equals("regetcode"))//检查输入的验证码是否和session中的验证码一致
		{
			result.setStatus(-1);
			result.setInfo("验证码不正确,请重新获取邮箱验证码");
			return result;
		}	
		List<Tb_user> users=dao.query(Tb_user.class, Cnd.where("userEmail","=",email).or("userStudentId","=",studentid));
		if(users.size()>0)//判断邮箱和学号是否已经注册
		{
			result.setStatus(2);
			result.setInfo("该邮箱或学号已经被注册，请联系管理员");
			return result;
		}
		//重要的数据判空
		if(email==null||email==""||password==null||password==""||name==null||name==""||studentid==null||studentid==""){
			result.setStatus(0);
			result.setInfo("有必需参数为空");
			return result;
		}
		email=email.trim();
		password=password.trim();
		name=name.trim();
		studentid=studentid.trim();
		Mademd5 md = new Mademd5();
		password=md.toMd5(password);//基本信息加工
		
		Tb_user user=new Tb_user();
		user.setUserName(name);
		user.setUserPassWord(password);
		user.setUserEmail(email);
		user.setUserStudentId(studentid);
		if(msg==null||msg=="")msg="";
		user.setUserMsg(msg);
		//判断头像图片是否上传
		String avatar="/bs/img/moren.jpg";
		if(img!=null&&img.getSize()>10)
		{
			String uuid = UUID.randomUUID().toString().replaceAll("-", "");
			String end=".";
			if(img.getContentType().equals("image/jpeg")){
				end=".jpeg";
			}else if(img.getContentType().equals("image/png")){
				end=".png";
			}else if(img.getContentType().equals("image/gif")){
				end=".gif";
			}
			while (true) {//判断该头像名是否存在
				List<Tb_user> lists=dao.query(Tb_user.class, Cnd.where("userAvatar","=","/bs/uploadimg/avatar/"+uuid+end));
				if(lists.size()==0)
					break;
				uuid = UUID.randomUUID().toString().replaceAll("-", "");
			}
			if(!end.equals(".")){
				
			BufferedImage img1=Images.read(img.getFile());//从img中获取image类型的类
			if(end.equals(".jpeg")){
				try {
				Metadata metadata = JpegMetadataReader.readMetadata(img.getFile());
				Directory directory  = metadata.getDirectory(ExifDirectory.class);
				if(directory.containsTag(ExifDirectory.TAG_ORIENTATION)){
					int orientation = directory.getInt(ExifDirectory.TAG_ORIENTATION);
						if(orientation==6){
							img1 = Images.rotate(img1, 90);
						}else if(orientation==3){
							img1 = Images.rotate(img1, 180);
						}else if(orientation==8){
							img1 = Images.rotate(img1, 270);
						}
						
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			img1=Images.zoomScale(img1, 125, 125,new Color(222,222,222,155));
			avatar="/bs/uploadimg/avatar/"+uuid+end;
			Images.write((RenderedImage) img1, new File("d:/file/avatar/"+uuid+end));
			}else{
				result.setStatus(-2);
				result.setInfo("图片类型不符合！");
				return result;
			}
		}
		user.setUserAvatar(avatar);
		user.setUserMoney((float) 0.00);
		dao.fastInsert(user);
		user.setUserPassWord("");
		session.setAttribute("user", user);
		result.setInfo("注册成功！");
		result.setStatus(1);
		return result;
		
		
	}
	/**
	 * @At("/reavatar")
	 * 更换头像接口
	 * @param img
	 * @param email
	 * @param session
	 * @return
	 */
	@At("/reavatar")
	@Ok("json")
	@POST
	@AdaptBy(type=UploadAdaptor.class)
	public Object reAvatar(@Param("img") TempFile img,
						   @Param("email")String email,
						   HttpSession session) {
		Result result=new Result();
		Tb_user user=(Tb_user)session.getAttribute("user");
		if(user==null||email==null||!email.equals(user.getUserEmail())){
			result.setInfo("请先登录！");
			result.setStatus(-1);
			return result;
		}
		final Tb_user user1=dao.fetch(Tb_user.class,Cnd.where("userId","=",user.getUserId()));
		String avatar=new String("");
		if(img!=null&&img.getSize()>10)
		{
			String uuid = UUID.randomUUID().toString().replaceAll("-", "");
			String end=".";
			if(img.getContentType().equals("image/jpeg")){
				end=".jpeg";
			}else if(img.getContentType().equals("image/png")){
				end=".png";
			}else if(img.getContentType().equals("image/gif")){
				end=".gif";
			}
			while (true) {//判断该头像名是否存在
				List<Tb_user> lists=dao.query(Tb_user.class, Cnd.where("userAvatar","=","/bs/uploadimg/avatar/"+uuid+end));
				if(lists.size()==0)
					break;
				uuid = UUID.randomUUID().toString().replaceAll("-", "");
			}
			if(!end.equals(".")){
				
			BufferedImage img1=Images.read(img.getFile());//从img中获取image类型的类
			if(end.equals(".jpeg")){
				try {
				Metadata metadata = JpegMetadataReader.readMetadata(img.getFile());
				Directory directory  = metadata.getDirectory(ExifDirectory.class);
				if(directory.containsTag(ExifDirectory.TAG_ORIENTATION)){
					int orientation = directory.getInt(ExifDirectory.TAG_ORIENTATION);
						if(orientation==6){
							img1 = Images.rotate(img1, 90);
						}else if(orientation==3){
							img1 = Images.rotate(img1, 180);
						}else if(orientation==8){
							img1 = Images.rotate(img1, 270);
						}
						
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			img1=Images.zoomScale(img1, 125, 125,new Color(222,222,222,155));
			avatar="/bs/uploadimg/avatar/"+uuid+end;
			Images.write((RenderedImage) img1, new File("d:/file/avatar/"+uuid+end));
			}else {
				result.setStatus(-2);
				result.setInfo("图片类型不符合！");
				return result;
			}
		}
		user1.setUserAvatar(avatar);
		Trans.exec(new Atom(){//锁
		    public void run(){
		      dao.update(user1);
		    }
		  });
		
		result.setStatus(1);
		result.setInfo("修改成功");
		user.setUserPassWord("");
		session.setAttribute("user", user);
		return result;
	}
	
	/**
	 * 获取收货地址
	 * @param session
	 * @return
	 */
	@At("/getaddress")
	@Ok("json")
	public Object getAddress(HttpSession session){
		Result result=new Result();
		Tb_user user=(Tb_user)session.getAttribute("user");
		if(user==null){
			result.setInfo("获取失败！");
			result.setStatus(0);
			result.setTotal(0);
			return result;
		}
		long id=user.getUserId();
		List<Tb_address> list=dao.query(Tb_address.class,Cnd.where("userId","=",id));
		result.setInfo("获取成功！");
		result.setStatus(1);
		result.setTotal(list.size());
		result.setList(list);
		return result;
	}
	
	
	//修改密码
	@At("/newpasswd")
	@POST
	public Result newPasswd(@Attr("user")Tb_user user,
							@Param("oldpasswd")String oldpasswd,
							@Param("newpasswd")String newpasswd,
							@Param("renewpasswd")String renewpasswd,HttpSession session){
		Result result =new Result();
		if(user==null){
			result.setStatus(0);
			result.setInfo("未登录！");
			return result;
		}
		if(!newpasswd.equals(renewpasswd)){
			result.setStatus(0);
			result.setInfo("两次密码输入不一致！");
			return result;
		}
		if(newpasswd.length()<6||newpasswd.length()>20){
			result.setStatus(0);
			result.setInfo("新密码长度应在6到20之间！");
			return result;
		}
		if(newpasswd.equals(oldpasswd)){
			result.setStatus(0);
			result.setInfo("新密码不得与老密码相同！");
			return result;
		}
		Mademd5 md = new Mademd5();
		oldpasswd=md.toMd5(oldpasswd);
		Tb_user user2=dao.fetch(Tb_user.class,Cnd.where("userId","=",user.getUserId()).and("userPassWord","=",oldpasswd));
		if(user2==null){
			result.setStatus(0);
			result.setInfo("原密码输入错误！");
			return result;
		}
		newpasswd=md.toMd5(newpasswd);
		user2.setUserPassWord(newpasswd);
		dao.update(user2);
		loginout(session);
		result.setStatus(1);
		result.setInfo("改密成功请重新登陆");
		return result;
	}
	
	
	/**
	 * 添加地址
	 * @param phone
	 * @param msg
	 * @param name
	 * @param session
	 * @return
	 */
	@At("/addaddress")
	@Ok("json")
	public Object addAddress(@Param("phone")String phone,
							 @Param("msg")String msg,
							 @Param("name")String name,
							 HttpSession session) {
		Result result=new Result();
		if(Strings.isEmpty(name)||Strings.isEmpty(phone)||Strings.isEmpty(msg)){
			result.setInfo("信息不能为空！");
			result.setStatus(-1);
			return result;
		}
		phone=phone.trim();
		msg=msg.trim();
		name=name.trim();
		Tb_address address=new Tb_address();
		Tb_user user=(Tb_user)session.getAttribute("user");
		if(user==null){
			result.setInfo("请先登录！");
			result.setStatus(0);
			return result;
		}
		int count=dao.count(Tb_address.class,Cnd.where("userId","=",user.getUserId()));
		log.debug("该用户地址数量为："+count+"条");
		if(count>=7){
			result.setInfo("用户地址最多支持7条！");
			result.setStatus(-2);
			return result;
		}
		address.setAddressMsg(msg);
		address.setAddressName(name);
		address.setAddressPhone(phone);
		address.setUserId(user.getUserId());
		dao.fastInsert(address);
		result.setInfo("添加成功！");
		result.setStatus(1);
		return result;
	}
	/**
	 * 删除地址
	 */
	@At("/deleteaddress")
	@Ok("json")
	@POST
	public Object deleteAddress(@Param("addressId")String id,HttpSession session){
		Result result=new Result();
		Tb_user user=(Tb_user)session.getAttribute("user");
		if(user==null){
			result.setInfo("请先登录！");
			result.setStatus(0);
			return result;
		}
		if(Strings.isEmpty(id)){
			result.setInfo("地址信息不存在！");
			result.setStatus(-1);
			return result;
		}
		id=id.trim();
		long toid= Long.parseLong(id);
		Tb_address address=new Tb_address();
		address.setAddressId(toid);
		address.setUserId(user.getUserId());
		int count=dao.delete(address);
		if(count==0){
			result.setInfo("无此记录！");
			result.setStatus(-2);
			return result;
		}
		result.setInfo("删除成功！");
		result.setStatus(1);
		return result;
		
	}
	/**
	 * 修改用户班级的信息
	 * @param studentMsg
	 * @param session
	 * @return
	 */
	@At("/restudentmsg")
	@Ok("json")
	@POST
	public Object restudentmsg(@Param("studentmsg")String studentMsg,
							   HttpSession session) {
		Result result=new Result();
		if(studentMsg==null)
		{
			result.setStatus(0);
			result.setInfo("信息为空，修改失败!");
			return result;
		}
		studentMsg=studentMsg.trim();
		if(studentMsg.equals("")){
			result.setStatus(0);
			result.setInfo("信息为空，修改失败!");
			return result;
		}
		
		Tb_user user=(Tb_user) session.getAttribute("user");
		if(user==null){
			result.setStatus(-1);
			result.setInfo("请先登录！");
			return result;
		}
		final Tb_user user1=dao.fetch(Tb_user.class,Cnd.where("userId","=",user.getUserId()));
		user1.setUserMsg(studentMsg);
		
		Trans.exec(new Atom(){//锁
		    public void run(){
		    	dao.update(user1);
		    }
		  });
		user.setUserPassWord("");
		session.setAttribute("user", user);
		result.setStatus(1);
		result.setInfo("修改成功");
		return result;
	}
	
	/**
	 * @At("/login")
	 * 登陆接口  登录成功在session中的user中加入用户的信息
	 * @param email
	 * @param password
	 * @param session
	 * @param code
	 * @return status=0 账号密码为空 status=-1验证码错误 status=-2 密码错误 status=1 登录成功status=-3账户被锁定
	 */
	@At("/login")
	@Ok("json")
	@POST
	public Object login(@Param("email") String email,
						@Param("password") String password,
						@Param("code") String code,
						HttpSession session){
			Result result=new Result();
			String codeCheck=(String) session.getAttribute("loginCode");
			session.setAttribute("loginCode", "regetcode");
			if(email==null||password==null){//判空
				result.setStatus(0);
				result.setInfo("账号和密码不能为空！");
				return result;
			}
			email=email.trim();
			password=password.trim();
			Mademd5 md = new Mademd5();
			password=md.toMd5(password);
			if(codeCheck==null){codeCheck="regetcode";}
			if(!code.equalsIgnoreCase(codeCheck)&&!codeCheck.equals("loginCode")){//验证码判断
				result.setStatus(-1);
				result.setInfo("验证码错误！请重新获取验证码");
				return result;
			}
			//判断账号输入的是邮箱
			Tb_user user=dao.fetch(Tb_user.class, Cnd.where("userEmail", "=", email).and("userPassWord", "=", password));
			if(user==null){
				user=dao.fetch(Tb_user.class, Cnd.where("userStudentId", "=", email).and("userPassWord", "=", password));
			}
			if(user==null){
				result.setStatus(-2);
				result.setInfo("账号或密码错误！");
				return result;	
			}
			if(user.isLocked()==false){
				result.setStatus(1);
				result.setInfo("登录成功！");
				user.setUserPassWord("");
				session.setAttribute("user", user);
				SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
				String date=df.format(new Date());
				final Tb_login login=dao.fetch(Tb_login.class,Cnd.where("loginDate","=",date));
				
				if(login==null){//用户活跃表添加
					Tb_login login1=new Tb_login();
					login1.setLoginCount(1);
					login1.setLoginDate(date);
					dao.fastInsert(login1);
				}else{
					login.setLoginCount(login.getLoginCount()+1);
					Trans.exec(new Atom(){//锁
					    public void run(){
					      dao.update(login);
					    }
					 });
					
				}
				
				return result;
			}else{
				result.setStatus(-3);
				result.setInfo("账户违规被锁定！");
				return result;
			}

	
	}
	
	/**
	 * 模态框登陆接口  登录成功在session中的user中加入用户的信息
	 * @param email
	 * @param password
	 * @param session
	 * @return status=0 账号密码为空  status=-2 密码错误 status=1 登录成功status=-3账户被锁定
	 */
	@At("/modalogin")
	@Ok("json")
	@POST
	public Object modaLogin(@Param("email") String email,
						@Param("password") String password,
						HttpSession session){
			Result result=new Result();
			String codeCheck=(String) session.getAttribute("loginCode");
			session.setAttribute("loginCode", "regetcode");
			if(email==null||password==null){//判空
				result.setStatus(0);
				result.setInfo("账号和密码不能为空！");
				return result;
			}
			email=email.trim();
			password=password.trim();
			if(email.equals("")||password.equals("")){
				result.setStatus(0);
				result.setInfo("账号和密码不能为空！");
				return result;
			}
			Mademd5 md = new Mademd5();
			password=md.toMd5(password);
			//判断账号输入的是邮箱
			Tb_user user=dao.fetch(Tb_user.class, Cnd.where("userEmail", "=", email).and("userPassWord", "=", password));
			if(user==null){
				user=dao.fetch(Tb_user.class, Cnd.where("userStudentId", "=", email).and("userPassWord", "=", password));
			}
			if(user==null){
				result.setStatus(-2);
				result.setInfo("账号或密码错误！");
				return result;	
			}
			if(user.isLocked()==false){
				result.setStatus(1);
				result.setInfo("登录成功！");
				user.setUserPassWord("");
				session.setAttribute("user", user);
				SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
				String date=df.format(new Date());
				final Tb_login login=dao.fetch(Tb_login.class,Cnd.where("loginDate","=",date));
				
				if(login==null){//用户活跃表添加
					Tb_login login1=new Tb_login();
					login1.setLoginCount(1);
					login1.setLoginDate(date);
					dao.fastInsert(login1);
				}else{
					login.setLoginCount(login.getLoginCount()+1);
					
					Trans.exec(new Atom(){//锁
					    public void run(){
					      dao.update(login);
					    }
					  });
				}
				
				return result;
			}else{
				result.setStatus(-3);
				result.setInfo("账户违规被锁定！");
				return result;
			}
	}
	/**
	 * 登出然后跳转到首页
	 * @param session
	 */
	@At("/loginout")
	@Ok(">>:/")
	public void loginout(HttpSession session) {
		session.invalidate();
	}
	
	
	/**
	 * ajax的方式登出
	 * @param session
	 */
	@At("/ajaxloginout")
	@Ok(">>:/")
	public Object ajaxloginout(HttpSession session) {
		session.invalidate();
		Result result=new Result();
		result.setStatus(1);
		result.setInfo("成功登出");
		return result;
	}
	
	/**
	 * 获取所有二级学院<option></option>
	 * @return
	 */
	@At("/getcollege")
	public String getCollege(){
		List<Tb_classCollege> list=dao.query(Tb_classCollege.class, null);
		String optionString="";
		for(int i=0;i<list.size();i++){
			optionString=optionString+"<option value=\""+list.get(i).getClassCollegeName()+"\">"+list.get(i).getClassCollegeName()+"</option>";
		}
		log.debug(optionString);
		return optionString;
	}
	
	@At("/getgrade")
	public String getGrade(@Param("college")String college){
		List<Tb_classGrade> list=null;
		String optionString="";
		if(college==null||college.equals("")){
			list=dao.query(Tb_classGrade.class, null);
			for(int i=0;i<list.size();i++){
				optionString=optionString+"<option value=\""+list.get(i).getClassGradeName()+"\">"+list.get(i).getClassGradeName()+"</option>";
			}
		}else{
			Tb_classCollege college2=dao.fetch(Tb_classCollege.class, Cnd.where("classCollegeName","=",college));
			list=dao.query(Tb_classGrade.class, Cnd.where("classCollegeId","=",college2.getClassCollegeId()));
			for(int i=0;i<list.size();i++){
				optionString=optionString+"<option value=\""+list.get(i).getClassGradeName()+"\">"+list.get(i).getClassGradeName()+"</option>";
			}
		}
		log.debug(optionString);
		
		return optionString;
	}
	
	@At("/getorder")
	@Ok("json")
	public Object getOrder(@Param("page")int page,@Param("type")int type,HttpSession session){
		Result result;
		final int PAGESIZE=5;
		Tb_user user=(Tb_user)session.getAttribute("user");
		if(user==null){
			return null;
		}
		List<Result> list=new ArrayList<>();
		Pager pager = dao.createPager(page, PAGESIZE);
		if(type==0){//全部订单
			List<Tb_order> listorder=dao.query(Tb_order.class, Cnd.where("userId","=",user.getUserId()).orderBy("orderTime","desc"),pager);//分页查询
			if(listorder==null||listorder.size()==0){return null;}
			int count=dao.count(Tb_order.class, Cnd.where("userId","=",user.getUserId()));
			int page1=count/PAGESIZE;
			if(count%PAGESIZE!=0){
				page1++;
			}
			count=page1;
			log.debug("------一共有："+count+"页");
			for(int i=0;i<listorder.size();i++)
			{
				result=new Result();
				result.setObj(listorder.get(i));
				List<Tb_goods> listgoods=dao.query(Tb_goods.class, Cnd.where("orderOutTradeNo","=",listorder.get(i).getOrderOutTradeNo()));
				result.setList(listgoods);
				result.setTotal(count);
				list.add(result);
			}
		}
		else if(type==-1){//支付失败的订单
			List<Tb_order> listorder=dao.query(Tb_order.class, Cnd.where("userId","=",user.getUserId()).and("orderStatus","=",-1).orderBy("orderTime","desc"),pager);//分页查询
			if(listorder==null||listorder.size()==0){return null;}
			int count=dao.count(Tb_order.class, Cnd.where("userId","=",user.getUserId()).and("orderStatus","=",-1));
			int page1=count/PAGESIZE;
			if(count%PAGESIZE!=0){
				page1++;
			}
			count=page1;
			log.debug("------一共有："+count+"页");
			for(int i=0;i<listorder.size();i++)
			{
				result=new Result();
				result.setObj(listorder.get(i));
				//List<Tb_goods> listgoods=dao.query(Tb_goods.class, Cnd.where("orderOutTradeNo","=",listorder.get(i).getOrderOutTradeNo()));
				//result.setList(listgoods);
				result.setTotal(count);
				list.add(result);
			}
		}
		else if(type==1){//支付成功的订单
			List<Tb_order> listorder=dao.query(Tb_order.class, Cnd.where("userId","=",user.getUserId()).and("orderStatus","=",1).or("orderStatus","=",2).orderBy("orderTime","desc"),pager);//分页查询
			if(listorder==null||listorder.size()==0){return null;}
			int count=dao.count(Tb_order.class, Cnd.where("userId","=",user.getUserId()).and("orderStatus","=",1).or("orderStatus","=",2));
			int page1=count/PAGESIZE;
			if(count%PAGESIZE!=0){
				page1++;
			}
			count=page1;
			log.debug("------一共有："+count+"页");
			for(int i=0;i<listorder.size();i++)
			{
				result=new Result();
				result.setObj(listorder.get(i));
				List<Tb_goods> listgoods=dao.query(Tb_goods.class, Cnd.where("orderOutTradeNo","=",listorder.get(i).getOrderOutTradeNo()));
				result.setList(listgoods);
				result.setTotal(count);
				list.add(result);
			}
		}
		else if(type==2){//售后生成的订单
			List<Tb_order> listorder=dao.query(Tb_order.class, Cnd.where("userId","=",user.getUserId()).and("orderStatus","!=",0).and("orderStatus","!=",1).and("orderStatus","!=",-1).and("orderStatus","!=",2),pager);//分页查询
			if(listorder==null||listorder.size()==0){return null;}
			int count=dao.count(Tb_order.class, Cnd.where("userId","=",user.getUserId()).and("orderStatus","!=",0).and("orderStatus","!=",1).and("orderStatus","!=",-1).and("orderStatus","!=",2));
			int page1=count/PAGESIZE;
			if(count%PAGESIZE!=0){
				page1++;
			}
			count=page1;
			log.debug("------一共有："+count+"页");
			for(int i=0;i<listorder.size();i++)
			{
				result=new Result();
				result.setObj(listorder.get(i));
				List<Tb_goods> listgoods=dao.query(Tb_goods.class, Cnd.where("orderOutTradeNo","=",listorder.get(i).getOrderOutTradeNo()));
				result.setList(listgoods);
				result.setTotal(count);
				list.add(result);
			}
		}
		
		
		return list;
	}
	
	/**
	 * @At("/emailcode")
	 * 获取邮箱验证码,在session 中加入 registCode变量 值为email+code
	 * @param email 邮箱
	 * @param session 
	 * @return
	 */
	@At("/emailcode")
	@POST
 	public String emailCode(@Param("email")String email,HttpSession session){
		 Properties props = new Properties();
		 props.setProperty("mail.transport.protocol", "SMTP");
		 props.setProperty("mail.smtp.host", "smtp.163.com");
		 props.setProperty("mail.smtp.port", "25");
		 // 指定验证为true
		 props.setProperty("mail.smtp.auth", "true");
		 props.setProperty("mail.smtp.timeout","1000");
		 // 验证账号及密码，密码需要是第三方授权码
		 Authenticator auth = new Authenticator() {
		 public PasswordAuthentication getPasswordAuthentication(){
		    return new PasswordAuthentication("wisqq4@163.com", "qwe999");
		            }
		        };
		 Session session1 = Session.getInstance(props, auth);

		// 2.创建一个Message，它相当于是邮件内容
		Message message = new MimeMessage(session1);
		String successMsg="";//用于返回是否发送成功
		try {
			// 设置发送者
			message.setFrom(new InternetAddress("wisqq4@163.com"));
			// 设置发送方式与接收者
			message.setRecipient(MimeMessage.RecipientType.TO, new InternetAddress(email));
			// 设置主题
			message.setSubject("易书网-验证码");
			// 设置内容
			String code=randomText();			
			String msg=new String("<table style='border: 1px solid black;'border='1' cellspacing='0'><tr><th>你的邮箱地址</th><th>验证码</th></tr><tr><td>"+email+"</td><td>"+code+"</td></tr></table>");
			message.setContent(msg, "text/html;charset=utf-8");
			
			// 3.创建 Transport用于将邮件发送
			Transport.send(message);
			session.setAttribute("registCode", email+code);
			successMsg="发送成功";
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			successMsg="发送失败";
		}
		return successMsg;
	}
	
	
	/**
	 * @At("/imglogin")
	 * 登录时的验证码接口,在session中的loginCode中加入对应的字符
	 * @param req
	 * @param response
	 * @param session
	 * @throws IOException
	 */
	@At("/imglogin")
	@Ok("raw:jpeg")
	 public void getCode3(HttpServletRequest req, HttpServletResponse response,HttpSession session) throws IOException{
        // 设置响应的类型格式为图片格式
            response.setContentType("image/jpeg");
            //禁止图像缓存。
            response.setHeader("Pragma", "no-cache");
            response.setHeader("Cache-Control", "no-cache");
            response.setDateHeader("Expires", 0);
            
            
            CreateImageCode vCode = new CreateImageCode(100,33,5,10);
            session.setAttribute("loginCode", vCode.getCode());
            log.debug("验证码："+vCode.getCode());
            vCode.write(response.getOutputStream());
     }
	
	
	
	/**
	 * 获取登录用户的信息  如果没有登录则返回null 
	 * @param session
	 * @return
	 */
	@At("/getuser")
	@Ok("json")
	public Object getuser(HttpSession session) {
		Tb_user user=(Tb_user) session.getAttribute("user");
		if(user==null){
			return null;
		}
		Tb_user user2=dao.fetch(Tb_user.class,Cnd.where("userId","=",user.getUserId()));
		session.setAttribute("user", user2);
		if(user.isLocked()==true){
			session.invalidate();
			return null;
		}
		return user;
	}
	
	//生成验证码  5位数字组成的字符串
	public String randomText() {
		Random random = new Random();
		String result="";
		for (int i=0;i<5;i++)
		{
			result+=random.nextInt(10);
		}
			return result;
	}
}
