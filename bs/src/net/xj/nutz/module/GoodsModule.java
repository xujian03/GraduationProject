package net.xj.nutz.module;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.Console;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringEscapeUtils;
import org.nutz.dao.Cnd;
import org.nutz.dao.Condition;
import org.nutz.dao.Dao;
import org.nutz.dao.pager.Pager;
import org.nutz.dao.util.cri.SqlExpression;
import org.nutz.dao.util.cri.SqlExpressionGroup;
import org.nutz.img.Images;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.json.Json;
import org.nutz.json.JsonFormat;
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
import net.xj.nutz.bean.Tb_bigClass;
import net.xj.nutz.bean.Tb_goods;
import net.xj.nutz.bean.Tb_goodsComment;
import net.xj.nutz.bean.Tb_gphoto;
import net.xj.nutz.bean.Tb_order;
import net.xj.nutz.bean.Tb_smallClass;
import net.xj.nutz.bean.Tb_user;
import net.xj.nutz.ext.Messages;

@At("/goods")
@IocBean
public class GoodsModule {
	@Inject
	private Dao dao;
	
	private static final Log log=Logs.get();
	
	/**
	 * 上传商品信息
	 * @param goodsName  商品名
	 * @param goodsDescribe  商品描述
	 * @param bigClass   所属大类型
	 * @param smallClass  所属小类型
	 * @param goodsPrice  价格
	 * @param imgs   图片
	 * @param session   
	 * @return status=-1 传入参数有空或者有null  
	 *         status=0  未登录
	 *         status=1 上传成功
	 */
	@At("/uploadgoods")
	@AdaptBy(type=UploadAdaptor.class)
	public Object uploadGoods(@Param("goodsName")String goodsName,
							  @Param("goodsDescribe")String goodsDescribe,
							  @Param("bigClass")String bigClass,
							  @Param("smallClass")String smallClass,
							  @Param("goodsPrice")float goodsPrice,
							  @Param("gphotos")TempFile[] imgs,
							  HttpSession session){
		Result result=new Result();
		if(isEmpty(goodsName)||isEmpty(goodsDescribe)||goodsPrice<0||goodsPrice>8000||imgs==null){
			result.setStatus(-1);
			result.setInfo("有参数不符合规则！");
			return result;
		}
		Tb_user user=(Tb_user)session.getAttribute("user");
		if(user==null)
		{
			result.setStatus(0);
			result.setInfo("请先登录！");
			return result;
		}
		Condition c = Cnd.where("goodsId",">",-1).desc("goodsId");
		List<Tb_goods>goodsList=dao.query(Tb_goods.class,c);
		long id=1;
		if(goodsList.size()!=0){
			id=goodsList.get(0).getGoodsId()+1;//获取id
		}
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Tb_goods goods=new Tb_goods();
		goods.setGoodsId(id);
		goods.setGoodsName(toHtml(goodsName));
		goods.setGoodsDescribe(toHtml(goodsDescribe));
		goods.setSmallCalss(toHtml(smallClass));
		goods.setBigClass(toHtml(bigClass));
		goods.setGoodsPrice(goodsPrice);
		goods.setGoodsTime(df.format(new Date()));
		goods.setUserId(user.getUserId());
		goods.setGoodsStatus(1);
		goods.setGoodsHeat(0);
		dao.fastInsert(goods);
		goodsImgUpload(id,imgs);//上传图片
		result.setStatus(1);
		result.setInfo("发布成功！");
		result.setObj(id);
		return result;
	}
	
	/**
	 * 修改商品页面  只能修改状态为0 和1 的商品
	 * @param Id
	 * @param user
	 * @return
	 */
	@At("/edit/*")
	@Ok(">>:/")
	public Object editGoods(String Id,@Attr("user")Tb_user user){
		long goodsId=0;
		if(Id==null||user==null){
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
		log.debug("编辑商品id="+goodsId);
		Tb_goods goods=dao.fetch(Tb_goods.class, Cnd.where("goodsId","=",goodsId).and("userId","=",user.getUserId()));
		if(goods==null){return null;}
		if(goods.getGoodsStatus()!=0&&goods.getGoodsStatus()!=1){return null;}
		return new JspView("../../editGoods?goods="+Json.toJson(goods, JsonFormat.compact()));
		
	}
	/**
	 * 修改商品信息
	 * @param id
	 * @param goodsName
	 * @param goodsDescribe
	 * @param bigClass
	 * @param smallClass
	 * @param goodsPrice
	 * @param user
	 * @return
	 */
	@At("/editgoods")
	@POST
	@AdaptBy(type=UploadAdaptor.class)
	public Result editGoods(@Param("id")String id,
							@Param("goodsName")String goodsName,
							@Param("goodsDescribe")String goodsDescribe,
							@Param("bigClass")String bigClass,
							@Param("smallClass")String smallClass,
							@Param("goodsPrice")float goodsPrice,
							@Attr("user")Tb_user user
							){
		Result result=new Result();
		if(user==null){result.setInfo("未登录！");result.setStatus(0);return result;}
		log.debug(id+goodsName+goodsDescribe);
		if(isEmpty(id)||isEmpty(goodsName)||isEmpty(goodsDescribe)){result.setInfo("有重要参数为空！");result.setStatus(-1);return result;}
		final Tb_goods goods=dao.fetch(Tb_goods.class,Cnd.where("goodsId","=",id).and("userId","=",user.getUserId()));
		if(goods==null){result.setInfo("商品不存在！");result.setStatus(0);return result;}
		if(goods.getGoodsStatus()!=0&&goods.getGoodsStatus()!=1){result.setInfo("商品不存在！");result.setStatus(0);return result;}
		goods.setGoodsName(toHtml(goodsName));
		goods.setGoodsDescribe(toHtml(goodsDescribe));
		goods.setSmallCalss(toHtml(smallClass));
		goods.setBigClass(toHtml(bigClass));
		goods.setGoodsPrice(goodsPrice);
		Trans.exec(new Atom(){//锁
		    public void run(){
		    	dao.update(goods);
		    }
		  });
		result.setInfo("修改成功！");
		result.setStatus(1);
		result.setObj(id);
		return result;
	}
	
	/**
	 * 删除自己的商品，只有状态为-1，0,1的商品可以删除
	 * @param goodsId
	 * @param user
	 * @return
	 */
	@At("/deletegoods")
	@Ok("json")
	@POST
	public Object deleteGoods(@Param("goodsId")long goodsId,
							  @Attr("user")Tb_user user) {
		Result result=new Result();
		if(user==null){result.setInfo("未登录！");result.setStatus(0);return result;}
		final Tb_goods goods=dao.fetch(Tb_goods.class,goodsId);
		if(goods==null){result.setInfo("商品不存在！");result.setStatus(-1);return result;}
		if(goods.getUserId()==user.getUserId()){//商品属于该用户
			if(goods.getGoodsStatus()==-1||goods.getGoodsStatus()==1||goods.getGoodsStatus()==0){
				goods.setUserId(-1);
				Trans.exec(new Atom(){//锁
				    public void run(){
				    	dao.update(goods);
				    }
				  });
				result.setInfo("商品删除成功！");result.setStatus(1);return result;
			}else {result.setInfo("商品已出售，无法删除！");result.setStatus(-3);return result;}
		}else {result.setInfo("该商品不属于你！");result.setStatus(-2);return result;}
	}
	
	/**
	 * 上架商品
	 * @param id
	 * @param user
	 * @return
	 */
	@At("/shelf")//上架
	@POST
	public Object shelfGoods(@Param("id")String id,@Attr("user")Tb_user user){
		Result result=new Result();
		if(user==null){result.setInfo("未登录！");result.setStatus(0);return result;}
		if(id==null||id.equals("")){result.setInfo("参数为空！");result.setStatus(-1);return result;}
		final Tb_goods goods=dao.fetch(Tb_goods.class,Cnd.where("goodsId","=",id).and("userId","=",user.getUserId()));
		if(goods==null){result.setInfo("商品不存在！");result.setStatus(-1);return result;}
		if(goods.getGoodsStatus()!=0){result.setInfo("商品状态不正常！");result.setStatus(-2);return result;}
		goods.setGoodsStatus(1);
		Trans.exec(new Atom(){//锁
		    public void run(){
		    	dao.update(goods);
		    }
		  });
		result.setInfo("上架成功！");result.setStatus(1);
		return result;
	}
	
	@At("/obtained")//下架
	@POST
	public Object obtainedGoods(@Param("id")String id,@Attr("user")Tb_user user){
		Result result=new Result();
		if(user==null){result.setInfo("未登录！");result.setStatus(0);return result;}
		if(id==null||id.equals("")){result.setInfo("参数为空！");result.setStatus(-1);return result;}
		final Tb_goods goods=dao.fetch(Tb_goods.class,Cnd.where("goodsId","=",id).and("userId","=",user.getUserId()));
		if(goods==null){result.setInfo("商品不存在！");result.setStatus(-1);return result;}
		if(goods.getGoodsStatus()!=1){result.setInfo("商品状态不正常！");result.setStatus(-2);return result;}
		goods.setGoodsStatus(0);
		Trans.exec(new Atom(){//锁
		    public void run(){
		    	dao.update(goods);
		    }
		  });
		result.setInfo("下架成功！");result.setStatus(1);
		return result;
	}
	
	/**
	 * 处理bs/goods/*的url如果*在数据库中存在返回jsp页面，不存在返回goods404.jsp
	 * @param Id 获取的goods id
	 * @return
	 */
	@At("/*")
	@Ok(">>:/")
	public Object showGoods(String Id,HttpSession session){
		Tb_user user=(Tb_user)session.getAttribute("user");
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
		log.debug("访问页面id="+goodsId);
		final Tb_goods goods=dao.fetch(Tb_goods.class, Cnd.where("goodsId","=",goodsId));
		List<Tb_gphoto> list=dao.query(Tb_gphoto.class,Cnd.where("goodsId","=",goodsId));
		log.debug(Json.toJson(goods, JsonFormat.compact()));
		if(goods==null||goods.getGoodsStatus()==-1||goods.getUserId()==-1){
			return new JspView("../goods404");
		}else if(goods.getGoodsStatus()==1||goods.getGoodsStatus()==2){//商品status = 1 2
			goods.setGoodsHeat(goods.getGoodsHeat()+1);
			Trans.exec(new Atom(){//锁
			    public void run(){
			    	dao.update(goods);
			    }
			  });
			return new JspView("../showGoods?goods="+Json.toJson(goods, JsonFormat.compact())+"&gphoto="+Json.toJson(list, JsonFormat.compact()));
		}else if(user!=null&&goods.getUserId()==user.getUserId()){//商品被下架但是访问商品的是商品持有人
			return new JspView("../showGoods?goods="+Json.toJson(goods, JsonFormat.compact())+"&gphoto="+Json.toJson(list, JsonFormat.compact()));
		}else {
			String outTradeNo=goods.getOrderOutTradeNo();
			
			Tb_order order=dao.fetch(Tb_order.class,Cnd.where("orderOutTradeNo","=",outTradeNo));
			if(order==null||user==null){
				return new JspView("../goods404");
			}
			if(order.getUserId()==user.getUserId())
			{
				return new JspView("../showGoods?goods="+Json.toJson(goods, JsonFormat.compact())+"&gphoto="+Json.toJson(list, JsonFormat.compact()));
			}
			return new JspView("../goods404");
		}
	}
	
	/**
	 * 获取推荐商品（一般为3件商品）
	 */
	@At("/suggest")
	public Object suggest(@Attr("user") Tb_user user){
		List<Tb_goods> goodsList = new ArrayList<>();
		SqlExpressionGroup e1 = Cnd.exps("goodsStatus", "=", "1").and("userId","!=","-1");
		if(user!=null){
			e1= Cnd.exps("goodsStatus", "=", "1").and("userId","!=","-1").and("userId","!=",user.getUserId());
		}
		Condition c=Cnd.where(e1); 
		int all=dao.count(Tb_goods.class,c);
		if(all<=6){
			goodsList=dao.query(Tb_goods.class,c);
		}else{
			List<Tb_goods> goods=dao.query(Tb_goods.class,c);
			while (goodsList.size()<6) {
				Random random=new Random();
				int rr=Math.abs(random.nextInt())%all;
				Boolean isuse=true;
				Tb_goods goods1=goods.get(rr);
				for(int i=0;i<goodsList.size();i++){
					if(goodsList.get(i).getGoodsId()==goods1.getGoodsId()){
						isuse=false;
						break;
					}	
				}
				if(isuse){
						goodsList.add(goods1);
						goods1=null;
					}
			}
		}
		List<String> strings=new ArrayList<>();
		for(int i=0;i<goodsList.size();i++){
			String string=dao.fetch(Tb_gphoto.class, Cnd.where("goodsId","=",goodsList.get(i).getGoodsId()).and("gphotoRank","=",1)).getGphotoPath();
			strings.add(string);
		}
		Result result=new Result();
		result.setList(goodsList);
		result.setObj(strings);
		result.setTotal(goodsList.size());
		return result;
	}
	/**
	 * 获取我正在贩卖的商品
	 * @return
	 */
	@At("/getmygoods")
	@Ok("json")
	public Object getMyGoods(@Param("page")int page,@Attr("user")Tb_user user) {
		if(user==null){
			return null;
		}
		Result result=new Result();
		final int PAGESIZE=5;//每一页的数量
		Pager pager = dao.createPager(page, PAGESIZE);
		SqlExpressionGroup e1=Cnd.exps("userId","=",user.getUserId()).and("goodsStatus","=",1);
		SqlExpressionGroup e2=Cnd.exps("userId","=",user.getUserId()).and("goodsStatus","=",-1);
		SqlExpressionGroup e3=Cnd.exps("userId","=",user.getUserId()).and("goodsStatus","=",0);
		Condition c = Cnd.where(e1).or(e2).or(e3);
		List<Tb_goods> listGoods=dao.query(Tb_goods.class, c,pager);       
		
		int count=dao.count(Tb_goods.class, c);
		int yu=count%PAGESIZE;
		count=count/PAGESIZE;
		if(yu!=0){
			count++;
		}
		log.debug("一共"+count+"页");
		result.setList(listGoods);
		result.setTotal(count);
		return result;
	}
	
	/**
	 * 获取待发货商品
	 * @param user
	 * @return
	 */
	@At("/daifahuo")//待发货商品 商品状态  3 4
	@Ok("json")
	public Object daiFaHuo(@Attr("user")Tb_user user){
		Result result=new Result();
		if(user==null){result.setInfo("用户未登录！");result.setStatus(0);return result;}//用户登录检查
		SqlExpressionGroup e1=Cnd.exps("userId","=",user.getUserId()).and("goodsStatus","=",3);
		SqlExpressionGroup e2=Cnd.exps("userId","=",user.getUserId()).and("goodsStatus","=",4);
		Condition c = Cnd.where(e1).or(e2).orderBy("goodsStatus", "asc");
		List<Tb_goods> goodsList=dao.query(Tb_goods.class, c);
		class msgaboutgoods{
			String path;
			String addressPhone;
			String addressName;
			String addressMsg;
		}
		List<msgaboutgoods> gphoto=new ArrayList<>();
		if(goodsList==null||goodsList.size()==0){result.setTotal(0);result.setInfo("无商品！");result.setStatus(-1);return result;}
		for(int i=0;i<goodsList.size();i++){
			String gp=dao.fetch(Tb_gphoto.class,Cnd.where("goodsId","=",goodsList.get(i).getGoodsId()).and("gphotoRank","=",1)).getGphotoPath();
			msgaboutgoods mab=new msgaboutgoods();
			mab.path=gp;
			Tb_order order=dao.fetch(Tb_order.class,Cnd.where("orderOutTradeNo","=",goodsList.get(i).getOrderOutTradeNo()));
			mab.addressPhone=order.getAddressPhone();
			mab.addressName=order.getAddressName();
			mab.addressMsg=order.getAddressMsg();
			gphoto.add(mab);
		}
		result.setTotal(goodsList.size());
		result.setList(goodsList);
		result.setObj(gphoto);
		result.setInfo("获取成功！");
		result.setStatus(1);
		return result;
	}
	
	@At("/fahuo")//发货接口
	@Ok("json")
	@POST
	public Object faHuo(@Param("goodsId")long goodsId,
						@Param("goodsDeliveryName")String goodsDeliveryName,
						@Param("goodsDeliveryNo")String goodsDeliveryNo,
						@Attr("user")Tb_user user){
		Result result =new Result();
		if(user==null){result.setInfo("用户未登录！");result.setStatus(0);return result;}//用户登录检查
		if(isEmpty(goodsDeliveryName)||isEmpty(goodsDeliveryNo)){result.setInfo("快递信息为空！");result.setStatus(0);return result;}
		final Tb_goods goods=dao.fetch(Tb_goods.class,Cnd.where("userId","=",user.getUserId()).and("goodsStatus","=",3).and("goodsId","=",goodsId));
		if(goods==null){result.setInfo("无此商品！");result.setStatus(-1);return result;}
		goods.setGoodsDeliveryName(goodsDeliveryName);
		goods.setGoodsDeliveryNo(goodsDeliveryNo);
		goods.setGoodsStatus(4);
		Trans.exec(new Atom(){//锁
		    public void run(){
		    	dao.update(goods);
		    }
		  });
		result.setInfo("发货成功！");
		result.setStatus(1);
		return result;
	}
	
	
	@At("/ftffahuo")//当面交易添加发货接口
	@Ok("json")
	@POST
	public Object faHuo(@Param("goodsId")long goodsId,
						@Attr("user")Tb_user user){
		Result result =new Result();
		if(user==null){result.setInfo("用户未登录！");result.setStatus(0);return result;}//用户登录检查
		final Tb_goods goods=dao.fetch(Tb_goods.class,Cnd.where("userId","=",user.getUserId()).and("goodsStatus","=",3).and("goodsId","=",goodsId));
		if(goods==null){result.setInfo("无此商品！");result.setStatus(-1);return result;}
		goods.setGoodsStatus(4);
		Trans.exec(new Atom(){//锁
		    public void run(){
		    	dao.update(goods);
		    }
		  });
		result.setInfo("发货成功！");
		result.setStatus(1);
		return result;
	}
	
	@At("/daishouhuo")//获取待收货列表
	@Ok("json")
	public Object daiShouHuo(@Attr("user")Tb_user user){
		Result result =new Result();
		if(user==null){result.setInfo("用户未登录！");result.setStatus(0);return result;}//用户登录检查
		List<Tb_goods> goodsList=new ArrayList<>();
		List<Tb_order> orderList=dao.query(Tb_order.class, Cnd.where("orderStatus","=",1).and("userId","=",user.getUserId()));
		if(orderList==null||orderList.size()==0){result.setInfo("暂无商品！");result.setStatus(0);result.setTotal(0);return result;}
		for(int i=0;i<orderList.size();i++){
			List<Tb_goods> goodsOrder=dao.query(Tb_goods.class, Cnd.where("orderOutTradeNo","=",orderList.get(i).getOrderOutTradeNo()).and("goodsStatus","=",4));
			if(goodsOrder!=null&&goodsOrder.size()!=0){
				goodsList.addAll(goodsOrder);
			}
		}
		result.setList(goodsList);
		result.setTotal(goodsList.size());
		result.setInfo("获取成功！");
		result.setStatus(1);
		return result;
	}
	
	@At("/shouhuo")//确认收货接口
	@Ok("json")
	@POST
	public Result shouHuo(@Attr("user")Tb_user user,
						  @Param("goodsId")long goodsId){
		Result result =new Result();
		if(user==null){result.setInfo("用户未登录！");result.setStatus(0);return result;}//用户登录检查
		final Tb_goods goods=dao.fetch(Tb_goods.class,Cnd.where("goodsId","=",goodsId).and("goodsStatus","=",4));
		if(goods==null){result.setInfo("商品不存在！");result.setStatus(-1);return result;}//用户登录检查
		Tb_order order=dao.fetch(Tb_order.class,Cnd.where("orderOutTradeNo","=",goods.getOrderOutTradeNo()).and("userId","=",user.getUserId()));
		if(order==null){result.setInfo("您未购买该商品！");result.setStatus(-2);return result;}
		goods.setGoodsStatus(5);
		final Tb_user user2=dao.fetch(Tb_user.class,goods.getUserId());
		user2.setUserMoney(user2.getUserMoney()+goods.getGoodsPrice());
		Trans.exec(new Atom(){//锁
		    public void run(){
		    	dao.update(goods);
		    	dao.update(user2);
		    }
		  });
		result.setStatus(1);
		result.setInfo("收货成功！");
		return result;
	}
	
	/**
	 * 
	 * @param user
	 * @param goodsId 
	 * @param tkphone 用于订单联系人
	 * @param tkname用于订单联系人姓名
	 * @param tkaddress用于订单联系人地址
	 * @param tkkuaidi 用于订单快递
	 * @param tkkuaididanhao用于快递单号
	 * @return
	 */
	@At("/tuikuan")//退款接口
	@Ok("json")
	@POST
	public Result tuiKuan(@Attr("user")Tb_user user,
						  @Param("goodsId")long goodsId,
						  @Param("tkphone")String tkphone,
						  @Param("tkname")String tkname,
						  @Param("tkaddress")String tkaddress,
						  @Param("tkkuaidi")String tkkuaidi,
						  @Param("tkkuaididanhao")String tkkuaididanhao,
						  @Param("orderNo")String orderNo
						  
	){
		Result result =new Result();
		if(user==null){result.setInfo("用户未登录！");result.setStatus(0);return result;}//用户登录检查
		if(isEmpty(tkphone)||isEmpty(tkname)||isEmpty(tkkuaidi)||isEmpty(tkaddress)||isEmpty(tkkuaididanhao)){result.setInfo("参数不得为空！");result.setStatus(0);return result;}
		final Tb_goods goods=dao.fetch(Tb_goods.class,goodsId);
		if(goods==null||goods.getGoodsStatus()!=4){result.setInfo("商品不存在！");result.setStatus(0);return result;}
		final Tb_order order=dao.fetch(Tb_order.class,Cnd.where("orderOutTradeNo","=",goods.getOrderOutTradeNo()));
		if(order==null||order.getUserId()!=user.getUserId()){result.setInfo("订单不属于您！");result.setStatus(0);return result;}
		order.setOrderCountMoney(order.getOrderCountMoney()-goods.getGoodsPrice());
		int count=dao.count(Tb_goods.class,Cnd.where("orderOutTradeNo","=",order.getOrderOutTradeNo()));
		if(count==1){//说明该订单没有商品了，就将订单状态改为-1
			order.setOrderStatus(-1);
		}
		final Tb_order order2=new Tb_order();
		order2.setAddressMsg(tkaddress);
		order2.setAddressPhone(tkphone);
		order2.setAddressName(tkname);
		goods.setGoodsDeliveryName(tkkuaidi);
		goods.setGoodsDeliveryNo(tkkuaididanhao);
		goods.setGoodsStatus(6);
		goods.setOrderStatus(10);
		goods.setOrderOutTradeNo(orderNo);
		SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		order2.setOrderTime(df.format(new Date()));
		order2.setOrderStatus(10);
		order2.setOrderCountMoney(goods.getGoodsPrice());
		order2.setUserId(user.getUserId());
		order2.setOrderOutTradeNo(orderNo);
		Trans.exec(new Atom(){//锁
		    public void run(){
		    	dao.fastInsert(order2);
		    	dao.update(order);
		    	dao.update(goods);
		    }
		  });
		result.setStatus(1);
		result.setInfo("退货申请成功！");
		return result;
	}
	
	@At("/tuikuangoods")//退款中的货物（卖家）
	@Ok("json")
	public Object tuikuangoods(@Attr("user")Tb_user user){
		if(user==null)return null;
		List<Tb_goods> goods=dao.query(Tb_goods.class, Cnd.where("goodsStatus","=",6).and("userId","=",user.getUserId()));
		return goods;
	}
	
	@At("/tuihuocheck")//确认收货（卖家）
	@Ok("json")
	public Object tuihuocheck(@Attr("user")Tb_user user,
							  @Param("goodsId")long goodsId){
		Result result =new Result();
		if(user==null){result.setInfo("用户未登录！");result.setStatus(0);return result;}//用户登录检查
		if(goodsId==0){result.setInfo("商品不存在！");result.setStatus(0);return result;}
		final Tb_goods goods=dao.fetch(Tb_goods.class,goodsId);
		if(goods==null||goods.getUserId()!=user.getUserId()||goods.getGoodsStatus()!=6){result.setInfo("商品不存在1！");result.setStatus(0);return result;}
		goods.setGoodsStatus(8);
		goods.setOrderStatus(20);
		final Tb_order order=dao.fetch(Tb_order.class,Cnd.where("orderOutTradeNo","=",goods.getOrderOutTradeNo()));
		final Tb_user user2=dao.fetch(Tb_user.class,order.getUserId());
		user2.setUserMoney(user2.getUserMoney()+order.getOrderCountMoney());
		order.setOrderStatus(20);
		Trans.exec(new Atom(){//锁
		    public void run(){
		    	dao.update(goods);
		    	dao.update(user2);
		    	dao.update(order);
		    }
		  });
		result.setInfo("已将钱款退给买家");
		result.setStatus(1);
		return result;
	}
	/**
	 * 获取商品类别中的大类别
	 * @return
	 */
	@At("/getbigclass")
	public String getBigClass(){
		List<Tb_bigClass> list=dao.query(Tb_bigClass.class, null);
		String optionString="";
		for(int i=0;i<list.size();i++){
			optionString=optionString+"<option value=\""+list.get(i).getBigClass()+"\">"+list.get(i).getBigClass()+"</option>";
		}
		log.debug(optionString);
		return optionString;
	}
	/**
	 * 获取商品类别中的小类别
	 * 
	 * @param bigClass 如果为空则返回所有小类别
	 * @return
	 */
	@At("/getsmallclass")
	public String getSmallClass(@Param("bigclass")String bigClass){
		List<Tb_smallClass> list=null;
		String optionString="";
		if(bigClass==null||bigClass.equals("")){
			list=dao.query(Tb_smallClass.class, null);
			for(int i=0;i<list.size();i++){
				optionString=optionString+"<option value=\""+list.get(i).getSmallClass()+"\">"+list.get(i).getSmallClass()+"</option>";
			}
		}else{
			Tb_bigClass bigClass2=dao.fetch(Tb_bigClass.class, Cnd.where("bigClass","=",bigClass));
			list=dao.query(Tb_smallClass.class, Cnd.where("bigClassId","=",bigClass2.getBigClassId()));
			for(int i=0;i<list.size();i++){
				optionString=optionString+"<option value=\""+list.get(i).getSmallClass()+"\">"+list.get(i).getSmallClass()+"</option>";
			}
		}
		log.debug(optionString);
		return optionString;
	}
	/**
	 * 获取商品所有类别（大类别+小类别）
	 * @return
	 */
	@At("/getallclass")
	@Ok("json")
	public Object getAllClass(){
		List<Result>results=new ArrayList<>();
		List<Tb_bigClass> bigClassList=dao.query(Tb_bigClass.class, null);
		for(int i=0;i<bigClassList.size();i++){
			Result r=new Result();
			r.setObj(bigClassList.get(i));
			r.setList(dao.query(Tb_smallClass.class, Cnd.where("bigClassId","=",bigClassList.get(i).getBigClassId())));
			r.setTotal(bigClassList.size());
			results.add(r);
		}
		return results;
	}
	/** --------------------------------------购物车接口开始---------------------------- **/
	/**
	 * 向购物车添加商品   Post方式
	 * @param session
	 * @param id
	 * @return
	 */
	@At("/shoppingcartadd")
	@Ok("json")
	@POST
	public Object shoppingCartAdd(HttpSession session,@Param("goodsId")long id){
		Result result=new Result();
		Tb_user user=(Tb_user)session.getAttribute("user");
		if(user==null){//检查用户是否登录
			result.setStatus(0);
			result.setInfo("请先登录！");
			return result;
		}
		@SuppressWarnings("unchecked")
		List<Tb_goods> list=(List<Tb_goods>) session.getAttribute("shoppingcart");
		if(list==null){
			list=new ArrayList<Tb_goods>();
		}
		Tb_goods goods=dao.fetch(Tb_goods.class,Cnd.where("goodsId","=",id));
		if(goods==null||goods.getGoodsStatus()!=1){//判读该goodsid下是否存在商品
			result.setStatus(-1);
			result.setInfo("添加失败:物品不存在或物品已经被购买！");
			return result;
		}
		if(goods.getUserId()==user.getUserId()){
			result.setStatus(-1);
			result.setInfo("添加失败：不能将自己发布的商品加入购物车！");
			return result;
		}
		if(goods.getUserId()==-1){
			result.setStatus(-1);
			result.setInfo("添加失败：商品已被删除！");
			return result;
		}
		
			for(int i=0;i<list.size();i++){
				if(list.get(i).getGoodsId()==goods.getGoodsId()||goods.getGoodsStatus()!=1){
					result.setStatus(-1);
					result.setInfo("添加失败！");
					return result;
				}
			}
			list.add(goods);
			session.setAttribute("shoppingcart", list);
			result.setStatus(1);
			result.setInfo("添加成功！");
			return result;
		
		
	}
	
	/**
	 * 移除购物车中的商品
	 * @param session
	 * @param id
	 * @param isRemoveAll
	 * @return
	 */
	@At("/shoppingcartremove")
	@Ok("json")
	@POST
	public Object shopingCartRemove(HttpSession session,
									@Param("goodsId")long id,
									@Param("isRemoveAll")boolean isRemoveAll){
		Result result=new Result();
		if(isRemoveAll==true){
			session.setAttribute("shoppingcart",null);
			result.setStatus(1);
			result.setInfo("购物车已清空！");
			return result;
		}
		List<Tb_goods> list=(List<Tb_goods>) session.getAttribute("shoppingcart");
		boolean isUserfulId=false;
		if(list!=null){
			for(int i=0;i<list.size();i++){
				if(list.get(i).getGoodsId()==id){
					list.remove(i);
					isUserfulId=true;
					break;
				}
			}
		}
		if(isUserfulId){
			result.setStatus(1);
			result.setInfo("删除成功！");
			return result;
		}else{
			result.setStatus(-1);
			result.setInfo("购物车中无此商品！");
			return result;
		}
		
	}
	
	/**
	 * 获取购物车中的商品列表 不带图片
	 * @param session
	 * @return  未登录是返回null   列表为空时返回null 
	 */
	@At("/shoppingcartget")
	@Ok("json")
	public Object shopingCartGet(HttpSession session){
		Tb_user user=(Tb_user)session.getAttribute("user");
		if(user==null){//检查用户是否登录
			return null;
		}
		List<Tb_goods> list=(List<Tb_goods>) session.getAttribute("shoppingcart");
		return list;
	}
	
	/**
	 * 获取购物车中的商品列表 带图片
	 * @param session
	 * @return  未登录是返回null   列表为空时返回null
	 * 返回值类似于：[{"info":"/bs/uploadimg/goods/a31190a948ec46458cee56cd64ec1771.jpeg","status":1,"obj":{"goodsId":3,"goodsStatus":1,"userId":2,"bigClass":"计算机","smallClass":"计算机理论基础","goodsName":"c语音综合实践","goodsDescribe":"这本书很好！","orderId":0,"orderStatus":0,"goodsPrice":15.0,"goodsHeat":26,"goodsTime":"2018-12-29 12:05:27"},"total":0}] 
	 */
	@At("/imgshoppingcartget")
	@Ok("json")
	public Object imgshopingCartGet(HttpSession session){
		Tb_user user=(Tb_user)session.getAttribute("user");
		if(user==null){//检查用户是否登录
			return null;
		}
		List<Tb_goods> list=(List<Tb_goods>) session.getAttribute("shoppingcart");
	    List<Result> results=new ArrayList<Result>();
	    if(list==null||list.size()==0){
	    	return null;
	    }
	    for(int i=0;i<list.size();i++)
	    {
	    	Result result=new Result();
	    	String path=dao.fetch(Tb_gphoto.class,Cnd.where("goodsId","=",list.get(i).getGoodsId()).and("gphotoRank","=",1)).getGphotoPath();//获取每个商品的第一张图片
	    	int status=dao.fetch(Tb_goods.class,Cnd.where("goodsId","=",list.get(i).getGoodsId())).getGoodsStatus();
	    	if(status!=1){
	    		status=-1;
	    	}
	    	result.setStatus(status);
	    	result.setInfo(path);
	    	result.setObj(list.get(i));
	    	results.add(result);
	    }
		return results;
	}
	
	
	/** --------------------------------------购物车接口结束---------------------------- **/
	/**
	 * 返回商品快递的路线
	 * @param goodsId
	 * @param user
	 * @return
	 * @throws Exception
	 */
	@At("/querykd")
	@Ok("raw")
	public String queryKuaiDi(@Param("goodsId")long goodsId,
							  @Attr("user")Tb_user user) throws Exception {
		
		if(user==null){//检查用户是否登录
			return null;
		}
		Tb_goods goods=dao.fetch(Tb_goods.class,goodsId);
		if(goods==null){return null;}
		if(goods.getUserId()!=user.getUserId()){//是否属于自己的商品检查
			Tb_order order=dao.fetch(Tb_order.class,Cnd.where("orderOutTradeNo","=",goods.getOrderOutTradeNo()));
			if(order.getUserId()!=user.getUserId()){
				return null;
			}
		}
		KdniaoTrackQueryAPI kdn=new KdniaoTrackQueryAPI();
		String expCode=getKuaiDi(goods.getGoodsDeliveryName());
		if(expCode.equals("none")){return null;}
		
		String result= kdn.getOrderTracesByJson(expCode,goods.getGoodsDeliveryNo());
		result.replaceAll("\\\\", "");
		return result;
	}
	
	//获取快递名
	public String getKuaiDi(String name) {
		if(name.equals("顺丰快递")){
			return "SF";
		}else if(name.equals("圆通快递")){
			return "YTO";
		}else if(name.equals("京东快递")){
			return "JD";
		}else if(name.equals("申通快递")){
			return "STO";
		}else if(name.equals("韵达快递")){
			return "YD";
		}else if(name.equals("中通快递")){
			return "ZTO";
		}else if(name.equals("天天快递")){
			return "HHTT";
		}else if(name.equals("邮政快递")){
			return "YZPY";
		}
		return "none";
	}
	
	
	/** --------------------------------------商品评论接口开始---------------------------- **/
	@At("/postcomment")//发表评论  如果没有目标评论user  则goodsCommentAimUser=0 goodsCommentAimUserId =null
	@POST
	@Ok("json")
	public Object postComment(@Param("goodsCommentMsg")String goodsCommentMsg,
							  @Param("goodsId")long goodsId,
							  @Param("goodsCommentAimUser") String goodsCommentAimUser,
							  @Param("goodsCommentAimUserId")long goodsCommentAimUserId,
							  @Attr("user")Tb_user user){
		Result result=new Result();
		if(user==null){result.setStatus(0);result.setInfo("请先登录！");return result;}
		if(isEmpty(goodsCommentMsg)){result.setStatus(-1);result.setInfo("评论内容不能为空！");return result;}
		if(goodsCommentMsg.length()>=800){result.setStatus(-2);result.setInfo("评论内容不能超过800！");return result;}
		Tb_goodsComment goodsComment=new Tb_goodsComment();
		goodsComment.setGoodsCommentMsg(goodsCommentMsg);
		goodsComment.setGoodsCommentGoodsId(goodsId);
		goodsComment.setGoodsCommentUserId(user.getUserId());
		goodsComment.setGoodsCommentUserName(user.getUserName());
		goodsComment.setGoodsCommentAimUser(goodsCommentAimUser);
		goodsComment.setGoodsCommentAimUserId(goodsCommentAimUserId);
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		goodsComment.setGoodsCommentTime(df.format(new Date()));
		dao.insert(goodsComment);
		result.setInfo("评论成功！");
		Tb_goods goods=dao.fetch(Tb_goods.class,goodsId);
		Messages.addMessage(dao, goods.getUserId(), "/bs/goods/"+goodsId,user.getUserName()+ "评论了你的"+goods.getGoodsName()+"商品", 2);
		result.setStatus(1);
		return result;
	}
	
	@At("/getcomment")//获取商品评论
	@Ok("json")
	public Object getComment(@Param("goodsId")long goodsId,
							 @Param("page")int page){
		Result result =new Result();
		final int PAGESIZE=10;
		List<Result> list=new ArrayList<>();
		Pager pager = dao.createPager(page, PAGESIZE);
		int count=dao.count(Tb_goodsComment.class, Cnd.where("goodsCommentGoodsId","=",goodsId).and("goodsCommentIsdelete","=",false));
		int page1=count/PAGESIZE;//评论的总页码
		if(count%PAGESIZE!=0){page1++;}
		List<Tb_goodsComment> comments=dao.query(Tb_goodsComment.class, Cnd.where("goodsCommentGoodsId","=",goodsId).and("goodsCommentIsdelete","=",false).orderBy("goodsCommentTime","desc"),pager); 
		if(comments==null||comments.size()==0){return null;}
		List<String> paths=new ArrayList<>();
		for(int i=0;i<comments.size();i++){
			String path=dao.fetch(Tb_user.class,comments.get(i).getGoodsCommentUserId()).getUserAvatar();
			paths.add(path);
		}
		result.setTotal(page1);
		result.setList(comments);//评论
		result.setObj(paths);
		result.setStatus(comments.size());
		return result;
	}
	
	/** --------------------------------------商品评论接口结束---------------------------- **/
	@At("/uploading")//上传商品的jsp界面跳转
	public Object upload(){
		return new JspView("../goodsUpload");
	}

	/**
	 * 上传图片
	 * @param id  上传商品的id
	 * @param imgList  图片数组
	 */
	public void goodsImgUpload(long id,TempFile[] imgList) {
		int count=0;
		String[] path=new String[12];
		for(int i=0;i<imgList.length;i++){
			String uuid = UUID.randomUUID().toString().replaceAll("-", "");
			String end=".";
			if(imgList[i].getContentType().equals("image/jpeg")){
				end=".jpeg";
			}else if(imgList[i].getContentType().equals("image/png")){
				end=".png";
			}else if(imgList[i].getContentType().equals("image/gif")){
				end=".gif";
			}
			while (true) {//判断该图片名是否存在
				List<Tb_gphoto> lists=dao.query(Tb_gphoto.class, Cnd.where("gphotoPath","=","/bs/uploadimg/goods/"+uuid+end));
				if(lists.size()==0)
					break;
				uuid = UUID.randomUUID().toString().replaceAll("-", "");
			}
			if(!end.equals(".")&&imgList[i].getSize()<=10000000){
			BufferedImage img1=Images.read(imgList[i].getFile());
			if(end.equals(".jpeg")){
				try {
				Metadata metadata = JpegMetadataReader.readMetadata(imgList[i].getFile());
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
			img1=Images.zoomScale(img1, 2000, 2000,new Color(0,0,0));
			img1=Images.addWatermark(img1, new File("d:/file/water2.png"),  0.99f, Images.WATERMARK_BOTTOM_RIGHT, 0);
			path[count]="/bs/uploadimg/goods/"+uuid+end;
			count++;
			Images.write((RenderedImage) img1, new File("d:/file/goods/"+uuid+end));
			}else {
				log.debug("图片为空或大小超出限制"+imgList[i].getSize());
			}
		}
		for(int i=0;i<count;i++)
		{
			Tb_gphoto gphoto=new Tb_gphoto();
			gphoto.setGoodsId(id);
			gphoto.setGphotoCount(count);
			gphoto.setGphotoPath(path[i]);
			gphoto.setGphotoRank(i+1);
			dao.fastInsert(gphoto);
		}
		
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
	/**
	 * 是否为空
	 * @param a
	 * @return
	 */
	public Boolean isEmpty(String a) {
		if(a==null){
			return true;
		}
		a=a.trim();
		if(a.equals("")){
			return true;
		}
		return false;
	}
}
