package net.xj.nutz.module;

import java.util.ArrayList;
import java.util.List;

import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.pager.Pager;
import org.nutz.dao.sql.Criteria;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.json.Json;
import org.nutz.lang.Strings;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Attr;
import org.nutz.mvc.annotation.Param;
import org.nutz.mvc.view.JspView;

import net.xj.nutz.bean.Tb_goods;
import net.xj.nutz.bean.Tb_gphoto;
import net.xj.nutz.bean.Tb_user;

@IocBean
public class SearchModule {
	@Inject
	private Dao dao;
	
	private static final Log log = Logs.get();
	
	/**
	 * 搜索返回搜索页面
	 * @param keyword
	 * @param bigClass
	 * @param smallClass
	 * @param page
	 * @param isinclude
	 * @param sortby
	 * @param user
	 * @return
	 */
	@At("/search")
	public Object searchJsp(@Param("keyword")String keyword,
							@Param("bigClass")String bigClass,
							@Param("smallClass")String smallClass,
							@Param("page")int page,
							@Param("isincludemygoods")boolean isinclude,
							@Param("sortby")int sortby,
							@Attr("user")Tb_user user){
		final int PAGESIZE=12;
		String cc="";
		if(page==0)page=1;
		if(keyword==null)keyword="";//如果没有关键字，就吧keyword置空
		if(sortby==0||(sortby!=1&&sortby!=2&&sortby!=3&&sortby!=4))sortby=1;//sortby如果不存在或者用户将url中的sortby改成了不正确的数据后，将sortby置1
		String[] keywords=keyword.split(" ");//使用string方法分割关键字
		List<Tb_goods> goods=new ArrayList<>();
		Pager pager = dao.createPager(page, PAGESIZE);//分页
		Criteria cri = Cnd.cri();
		Criteria cri1 = Cnd.cri();
		Criteria crik = Cnd.cri();
		Criteria crik1 = Cnd.cri();
		boolean haveKeyWord=false;//当没有关键字的自动给crik和crik1送一个为空的关键字，防止sql语句错误
		for(int i=0;i<keywords.length;i++)
		{
			if(!Strings.isBlank(keywords[i])){
				crik.where().orLike("goodsDescribe",keywords[i]);//设置每个关键字是否与商品描述相似
				crik1.where().orLike("goodsName",keywords[i]);//设置每个关键字是否与商品名称相似
				haveKeyWord=true;
			}
			
		}
		if(!haveKeyWord){
			crik.where().orLike("goodsDescribe","");//设置每个关键字是否与商品描述相似
			crik1.where().orLike("goodsName","");//设置每个关键字是否与商品名称相似
		}
		cri.where().andIn("goodsStatus","1").andNotIn("userId", -1);//userid不等于-1 如果为-1说明该商品被删除
		cri1.where().andIn("goodsStatus","1").andNotIn("userId", -1);
		if(user!=null&&isinclude==false){//商品结果会不会包含自己发布的商品
			cri.where().andNotIn("userId",user.getUserId());
			cri1.where().andNotIn("userId",user.getUserId());
		}
		if(!Strings.isBlank(smallClass))//小类别存在的时候加入sql语句
		{
			cri.where().andIn("smallClass", smallClass);
			cri1.where().andIn("smallClass", smallClass);
		}
		if(!Strings.isBlank(bigClass)){//大类别存在的时候加入sql语句
			cri.where().andIn("bigClass", bigClass);
			cri1.where().andIn("bigClass", bigClass);
		}
		int count=0;//总共有几页
		switch (sortby) {//以不同的排序方式产生不同的结果
		case 1:goods=dao.query(Tb_goods.class, Cnd.where(cri.where().and(crik.where())).or(cri1.where().and(crik1.where())).asc("goodsTime"),pager);/*发布时间正序*/
			   count=dao.count(Tb_goods.class, Cnd.where(cri.where().and(crik.where())).or(cri1.where().and(crik1.where())).asc("goodsTime"));
			   break;
		case 2:goods=dao.query(Tb_goods.class,Cnd.where(cri.where().and(crik.where())).or(cri1.where().and(crik1.where())).desc("goodsTime"),pager);/*发布时间倒序*/
		   	   count=dao.count(Tb_goods.class,Cnd.where(cri.where().and(crik.where())).or(cri1.where().and(crik1.where())).desc("goodsTime"));
		       break;
		case 3:goods=dao.query(Tb_goods.class,Cnd.where(cri.where().and(crik.where())).or(cri1.where().and(crik1.where())).asc("goodsHeat"),pager);/*热度正序*/
	   	       count=dao.count(Tb_goods.class,Cnd.where(cri.where().and(crik.where())).or(cri1.where().and(crik1.where())).asc("goodsHeat"));
	           break;
		case 4:goods=dao.query(Tb_goods.class,Cnd.where(cri.where().and(crik.where())).or(cri1.where().and(crik1.where())).desc("goodsHeat"),pager);/*热度倒序*/
	   	       count=dao.count(Tb_goods.class,Cnd.where(cri.where().and(crik.where())).or(cri1.where().and(crik1.where())).desc("goodsHeat"));
	           break;

		default:
			break;
		}
		int yu=count%PAGESIZE;
		count=count/PAGESIZE;//页数
		if(yu!=0){
			count++;
		}
		String paths="";
		for(int i=0;i<goods.size();i++)//获取商品第一张图片的路径
		{
			paths+=dao.fetch(Tb_gphoto.class,Cnd.where("goodsId","=",goods.get(i).getGoodsId()).and("gphotoRank","=",1)).getGphotoPath()+",";
		}
		return new JspView("search?goods="+Json.toJson(goods)+"&path="+paths+"&count="+count+"&page="+page+
				"&sortby="+sortby);
		
	}

}
