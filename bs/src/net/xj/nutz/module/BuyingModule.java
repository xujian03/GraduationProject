package net.xj.nutz.module;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpSession;

import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.pager.Pager;
import org.nutz.dao.sql.Criteria;
import org.nutz.dao.util.cri.SqlExpressionGroup;
import org.nutz.img.Images;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.json.Json;
import org.nutz.json.JsonFormat;
import org.nutz.lang.Strings;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.annotation.AdaptBy;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Attr;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.POST;
import org.nutz.mvc.annotation.Param;
import org.nutz.mvc.upload.TempFile;
import org.nutz.mvc.upload.UploadAdaptor;
import org.nutz.mvc.view.JspView;
import org.nutz.trans.Atom;
import org.nutz.trans.Trans;

import com.drew.imaging.jpeg.JpegMetadataReader;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifDirectory;

import net.xj.nutz.bean.Result;
import net.xj.nutz.bean.Tb_buying;
import net.xj.nutz.bean.Tb_buyingComment;
import net.xj.nutz.bean.Tb_buyingImg;
import net.xj.nutz.bean.Tb_goodsComment;
import net.xj.nutz.bean.Tb_user;
import net.xj.nutz.ext.Messages;

@At("/buying")
@IocBean
public class BuyingModule {
	@Inject
	private Dao dao;
	private static final Log log=Logs.get();
	
	@At("/upload")//上传求购图片最多支持6张
	@POST
	@AdaptBy(type=UploadAdaptor.class)
	public Object upload(@Attr("user")Tb_user user,
						 @Param("buyingName")String buyingName,
						 @Param("buyingMsg")String buyingMsg,
						 @Param("imgs")TempFile[] imgs){
		Result result=new Result();
		if(user==null){result.setInfo("未登录！");result.setStatus(0);return result;}
		if(Strings.isBlank(buyingMsg)){result.setInfo("标题不能为空！");result.setStatus(-1);return result;}
		buyingName=toHtml(buyingName);
		buyingMsg=toHtml(buyingMsg);
		if(buyingMsg.length()>=800||buyingName.length()>=50){result.setInfo("标题或内容过长！");result.setStatus(-2);return result;}
		Tb_buying buying=new Tb_buying();
		buying.setBuyingName(buyingName);
		buying.setBuyingMsg(buyingMsg);
		buying.setBuyingStatus(0);
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		buying.setBuyingTime(df.format(new Date()));
		buying.setUserId(user.getUserId());
		buying.setUserName(user.getUserName());
		buying=dao.insert(buying);
		List<Tb_buyingImg> imglist=new ArrayList<>();
		for(int i=0;i<imgs.length;i++){
			if(i>=6)break;
			Tb_buyingImg img=new Tb_buyingImg();
			String path=UploadImg(imgs[i]);
			if(path!=null){
				img.setBuyingImgPath(path);
				img.setBuyingId(buying.getBuyingId());
				imglist.add(img);
			}
		}
		dao.fastInsert(imglist);
		result.setInfo("上传成功！");
		result.setStatus(1);
		result.setObj(buying.getBuyingId());
		return result;
	}
	
	@At("/getbuying")
	@Ok("json")
	public Object getBuying(@Param("page")int page,
							@Param("keyword")String keyWord
							)
	{
		final int PAGESIZE=10;
		Result result=new Result();
		Pager pager = dao.createPager(page, PAGESIZE);
		if(Strings.isBlank(keyWord)){//无关键词状态
			int count=dao.count(Tb_buying.class,Cnd.where("buyingStatus","=","0"));
			int page1=count/PAGESIZE;//评论的总页码
			if(count%PAGESIZE!=0){page1++;}
			result.setTotal(page1);
			result.setList(dao.query(Tb_buying.class,Cnd.where("buyingStatus","=","0").desc("buyingTime"),pager));
		}
		else{//有关键词状态
			String[] keywords=keyWord.split(" ");
			Criteria cri = Cnd.cri();
			Criteria cri1 = Cnd.cri();
			for(int i=0;i<keywords.length;i++)
			{
				if(!Strings.isBlank(keywords[i])){
					cri.where().orLike("buyingName",keywords[i]);
					cri1.where().orLike("buyingMsg",keywords[i]);
				}
				
			}
			cri.where().andIn("buyingStatus","0");
			cri1.where().andIn("buyingStatus","0");
//			cri.getOrderBy().desc("buyingTime");
//			cri1.getOrderBy().desc("buyingTime");
			result.setList(dao.query(Tb_buying.class, Cnd.where(cri.where()).or(cri1.where()).desc("buyingTime"),pager));
			int count=dao.count(Tb_buying.class,Cnd.where(cri.where()).or(cri1.where()).desc("buyingTime"));
			int page1=count/PAGESIZE;//评论的总页码
			if(count%PAGESIZE!=0){page1++;}
			result.setTotal(page1);
		}
		return result;
	}
	
	
	
	/**----------------------回复接口开始   -------------------------------**/
	@At("/postcomment")
	@POST
	@Ok("json")
	public Object postComment(@Param("buyingId")long buyingId,//帖子id
					   @Param("buyingMsg")String buyingMsg,//回复内容
					   @Param("isDoubleComment")boolean isDoubleComment,//是否是回复了别人的回复
					   @Param("isHaveReplyUser")boolean isHaveReplyUser,//如果是回复了别人的回复，那么是否有回复了别人回复的回复
					   @Param("flowCommentId")long flowCommentId,//如果是回复了别人的回复 回复的id
					   @Param("flowUserId")long flowUserId,//如果是回复了别人的回复，且是否有回复了别人回复的回复，回复的回复的人的id
					   @Attr("user")Tb_user user
					   )
	{
		Result result=new Result();
		if(user==null){result.setInfo("未登录！");result.setStatus(0);return result;}
		if(Strings.isBlank(buyingMsg)||buyingMsg.length()>=1000){result.setInfo("回复长度不得超过1000！");result.setStatus(0);return result;}
		if(buyingId==0){result.setInfo("帖子id缺失！");result.setStatus(-1);return result;}
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		final Tb_buyingComment comment=new Tb_buyingComment();
		comment.setBuyingId(buyingId);//p评论的帖子的id
		comment.setBuyingCommentTime(df.format(new Date()));//设置评论时间
		comment.setBuyingCommentMsg(buyingMsg);//设置评论内容
		comment.setUserId(user.getUserId());//设置评论用户id
		comment.setUserName(user.getUserName());//设置评论用户姓名
		comment.setDoubleComment(isDoubleComment);
		if(isDoubleComment==true)//是回复了别人的回复
		{
			if(flowCommentId==0){result.setInfo("flowCommentId缺失！");result.setStatus(-1);return result;}
			comment.setFlowCommentId(flowCommentId);
			comment.setHaveReplyUser(isHaveReplyUser);
			if(isHaveReplyUser==true)//回复了别人回复的回复
			{
				comment.setHaveReplyUser(isHaveReplyUser);
				comment.setFlowUserId(flowUserId);
				if(flowUserId==0){result.setInfo("flowUserId缺失！");result.setStatus(-1);return result;}
				comment.setFlowUserName(dao.fetch(Tb_user.class,flowUserId).getUserName());
			}
		}else{
			comment.setHaveReplyUser(false);
		}
		Trans.exec(new Atom(){//锁
		    public void run(){
		    	dao.insert(comment);
		    }
		  });
		//添加消息
		Tb_buying buying=dao.fetch(Tb_buying.class, buyingId);
		Messages.addMessage(dao, buying.getUserId(), "/bs/buying/"+buyingId, user.getUserName()+"回复了你关于‘"+buying.getBuyingName()+"’的求购帖子", 2);
		result.setInfo("回复成功");
		result.setStatus(1);
		return result;
	}
	
	/**
	 * 获取回复接口
	 * @param buyingId
	 * @param page
	 * @return
	 */
	@At("/getcomment")
	@Ok("json")
	public Object getComment(@Param("buyingId")long buyingId,
							 @Param("page")int page){
		final int PAGESIZE=10;
		List<Result> results=new ArrayList<Result>();
		Pager pager = dao.createPager(page, PAGESIZE);
		int count=dao.count(Tb_buyingComment.class, Cnd.where("buyingId","=",buyingId).and("isDelete","=",false).and("isDoubleComment","=",false));
		int page1=count/PAGESIZE;//评论的总页码
		if(count%PAGESIZE!=0){page1++;}
		List<Tb_buyingComment> comments=dao.query(Tb_buyingComment.class, Cnd.where("buyingId","=",buyingId).and("isDelete","=",false).and("isDoubleComment","=",false),pager);
		for(int i=0;i<comments.size();i++){
			Result result=new Result();
			result.setInfo(dao.fetch(Tb_user.class,comments.get(i).getUserId()).getUserAvatar());
			result.setList(dao.query(Tb_buyingComment.class, Cnd.where("buyingId","=",buyingId).and("isDelete","=",false).and("isDoubleComment","=",true).and("flowCommentId","=",comments.get(i).getBuyingCommentId())));
			result.setTotal(page1);
			result.setObj(comments.get(i));
			results.add(result);
		}
		return results;
	}
	
	/**----------------------回复接口结束   -------------------------------**/	
	
	@At("/over")
	@Ok("json")
	public Object buyingOver(@Param("bid")long buyingId,
			@Attr("user")Tb_user user){
		Result results=new Result();
		if(user!=null){
			Tb_buying buying=dao.fetch(Tb_buying.class,buyingId);
			if(buying!=null&&buying.getUserId()==user.getUserId())
			{
				buying.setBuyingStatus(1);
				dao.update(buying);
				results.setInfo("求购关闭成功！");
				return results;
			}else{
				results.setInfo("用户未登录！");
				return results;
			}
			
			
		}
		
			results.setInfo("用户未登录！");
			return results;
		
		
	}
	
	
	
	
	/**
	 * 帖子的jsp映射
	 * @param Id
	 * @return
	 */
	@At("/*")
	@Ok(">>:/buy.html")
	public Object buyingShow(String Id,@Attr("user")Tb_user user1){
		long goodsId=0;
		if(Id==null){
			return null;
		}
		for(int i=0;i<Id.length();i++)
		{
			char a=Id.charAt(i);
			if(a>='0'&&a<='9'){
				goodsId=goodsId*10+(a-'0');
			}else {
				break;
			}
		}
		log.debug("求购页面id="+goodsId);
		Tb_buying b=dao.fetch(Tb_buying.class,goodsId);
		if(b==null||b.getBuyingStatus()==-1){return null;}
		List<Tb_buyingImg> imgs=dao.query(Tb_buyingImg.class, Cnd.where("buyingId","=",b.getBuyingId()));
		String strings="";
		for(int i=0;i<imgs.size();i++){
			strings+=imgs.get(i).getBuyingImgPath()+",";
		}
		boolean isMyBuying=false;
		if(user1!=null&&user1.getUserId()==b.getUserId()){
			isMyBuying=true;
		}
		Tb_user user=dao.fetch(Tb_user.class,b.getUserId());
		return new JspView("../showBuying?buyingId="+b.getBuyingId()+"&buyingName="+b.getBuyingName()+
				"&buyingMsg="+b.getBuyingMsg()+"&buyingImg="+strings+
				"&userName="+b.getUserName()+"&buyingTime="+b.getBuyingTime()+"&userAvage="+user.getUserAvatar()+
				"&buyingStatus="+b.getBuyingStatus()+"&isMyBuying="+isMyBuying);
	}
	
	
	
	/**
	 * 图片保存方法
	 * @param img
	 * @return
	 */
	public String UploadImg(TempFile img) {
		String uuid = UUID.randomUUID().toString().replaceAll("-", "");
		String end=".";
		if(img.getContentType().equals("image/jpeg")){
			end=".jpeg";
		}else if(img.getContentType().equals("image/png")){
			end=".png";
		}else if(img.getContentType().equals("image/gif")){
			end=".gif";
		}else {
			return null;
		}
		while (true) {//判断该图片名是否存在
			Tb_buyingImg pimg=dao.fetch(Tb_buyingImg.class, Cnd.where("buyingImgPath","=","/bs/uploadimg/buying/"+uuid+end));
			if(pimg==null)
				break;
			uuid = UUID.randomUUID().toString().replaceAll("-", "");
		}
		BufferedImage img1=Images.read(img.getFile());
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
		img1=Images.zoomScale(img1, 2000, 2000,new Color(255,255,255));
		img1=Images.addWatermark(img1, new File("d:/file/buyingwater.png"),  0.99f, Images.WATERMARK_BOTTOM_RIGHT, 10);
		Images.write((RenderedImage) img1, new File("d:/file/buying/"+uuid+end));
		return "/bs/uploadimg/buying/"+uuid+end;
	}
	/**
	 * java转html
	 * @param str
	 * @return
	 */
	public String toHtml(String str){   
	    if (str==null){   
	      return "";   
	    }else{   
	      str=str.replaceAll("<", "&lt;");   
	      str=str.replaceAll(">", "&gt;");   
	      str=str.replaceAll("'", "''");   
	      str=str.replaceAll(" ", "&nbsp;");   
	      str=str.replaceAll("\n", "<br>");
	      str=str.replaceAll("%", "%25");
	      str=str.replaceAll("&", "%26");
	      str=str.replaceAll("#", "%23");
	    }   
	    return str;   
	  } 
}
