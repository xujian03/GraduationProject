package net.xj.nutz.ext;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.ioc.Ioc;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.Mvcs;
import org.nutz.trans.Atom;
import org.nutz.trans.Trans;

import com.sun.org.apache.bcel.internal.generic.DALOAD;
import com.sun.org.apache.bcel.internal.generic.IUSHR;

import net.xj.nutz.bean.Tb_goods;
import net.xj.nutz.bean.Tb_login;
import net.xj.nutz.bean.Tb_order;
//订单生成超过15分钟还没有支付成功就将订单状态改为支付失败 -1
@IocBean
public class OrderThread extends Thread{
	Ioc ioc=Mvcs.getIoc();
	
	private  final static long TIME=15;//超过多少时间更改订单为支付失败，单位分钟
	
	private  final static double FLUSHTIME=2;//隔多少时间，单位分钟
	
	private static final Log log = Logs.get();
	
	private Dao dao=ioc.get(Dao.class);
	
	@Override
	public void run() {
		while (true) {
			if (this.isInterrupted()) {
				log.debug("订单查询线程正在停止！");
				return;
			}
			log.debug("订单查询线程正在运行！");
		    List <Tb_order> orders=dao.query(Tb_order.class, Cnd.where("orderStatus","=","0"));
		    if(orders!=null){
		    	for(int i=0;i<orders.size();i++)
		    	{
		    		final Tb_order order=orders.get(i);
		    		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		    		Date date;
		    		try {
						date=df.parse(order.getOrderTime());
						long orderTime=date.getTime();
						long currentTime = System.currentTimeMillis();
						
						log.debug("time="+(currentTime-orderTime));
						if(currentTime-orderTime>TIME*1000*60){//订单未支付超过15分钟
							changeGoods(order.getOrderOutTradeNo());
							order.setOrderStatus(-1);
							Trans.exec(new Atom(){
							    public void run(){
							      dao.update(order);
							    }
							  });
							
						}
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		    	}
		    }
			try {
				Thread.sleep((long) (FLUSHTIME*60*1000));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 更改orderNo 所包含的商品信息
	 * @param orderOutOrderNo
	 * @return
	 */
	public boolean changeGoods(String orderOutOrderNo) {
		if(orderOutOrderNo!=null){
			Chain.make("goodsStatus", -1);
			final String aString=orderOutOrderNo;
			Trans.exec(new Atom(){
			    public void run(){
			    	dao.update(Tb_goods.class,Chain.make("orderOutTradeNo", "").add("orderStatus", "").add("goodsStatus", 1),Cnd.where("orderOutTradeNo", "=",aString));
			    }
			  });
			
			return true;
		}else return false;
	}
	
}
