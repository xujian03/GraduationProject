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
		if(keyword==null)keyword="";
		if(sortby==0||(sortby!=1&&sortby!=2&&sortby!=3&&sortby!=4))sortby=1;
		String[] keywords=keyword.split(" ");
		List<Tb_goods> goods=new ArrayList<>();
		Pager pager = dao.createPager(page, PAGESIZE);
		Criteria cri = Cnd.cri();
		Criteria cri1 = Cnd.cri();
		for(int i=0;i<keywords.length;i++)
		{
			if(!Strings.isBlank(keywords[i])){
				cri.where().orLike("goodsDescribe",keywords[i]);
				cri1.where().orLike("goodsName",keywords[i]);
			}
			
		}
		cri.where().andIn("goodsStatus","1").andNotIn("userId", -1);
		cri1.where().andIn("goodsStatus","1").andNotIn("userId", -1);
		if(user!=null&&isinclude==false){
			cri.where().andNotIn("userId",user.getUserId());
			cri1.where().andNotIn("userId",user.getUserId());
		}
		if(!Strings.isBlank(smallClass))
		{
			cri.where().andIn("smallClass", smallClass);
			cri1.where().andIn("smallClass", smallClass);
		}
		if(!Strings.isBlank(bigClass)){
			cri.where().andIn("bigClass", bigClass);
			cri1.where().andIn("bigClass", bigClass);
		}
		int count=0;//总共有几页
		switch (sortby) {
		case 1:goods=dao.query(Tb_goods.class, Cnd.where(cri.where()).or(cri1.where()).asc("goodsTime"),pager);/*发布时间正序*/
			   count=dao.count(Tb_goods.class, Cnd.where(cri.where()).or(cri1.where()).asc("goodsTime"));
			   break;
		case 2:goods=dao.query(Tb_goods.class, Cnd.where(cri.where()).or(cri1.where()).desc("goodsTime"),pager);/*发布时间倒序*/
		   	   count=dao.count(Tb_goods.class, Cnd.where(cri.where()).or(cri1.where()).desc("goodsTime"));
		       break;
		case 3:goods=dao.query(Tb_goods.class, Cnd.where(cri.where()).or(cri1.where()).asc("goodsHeat"),pager);/*热度正序*/
	   	       count=dao.count(Tb_goods.class, Cnd.where(cri.where()).or(cri1.where()).asc("goodsHeat"));
	           break;
		case 4:goods=dao.query(Tb_goods.class, Cnd.where(cri.where()).or(cri1.where()).desc("goodsHeat"),pager);/*热度倒序*/
	   	       count=dao.count(Tb_goods.class, Cnd.where(cri.where()).or(cri1.where()).desc("goodsHeat"));
	           break;

		default:
			break;
		}
		int yu=count%PAGESIZE;
		count=count/PAGESIZE;
		if(yu!=0){
			count++;
		}
		String paths="";
		for(int i=0;i<goods.size();i++)
		{
			paths+=dao.fetch(Tb_gphoto.class,Cnd.where("goodsId","=",goods.get(i).getGoodsId()).and("gphotoRank","=",1)).getGphotoPath()+",";
		}
		return new JspView("search?goods="+Json.toJson(goods)+"&path="+paths+"&count="+count+"&page="+page+
				"&sortby="+sortby);
		
	}

}
