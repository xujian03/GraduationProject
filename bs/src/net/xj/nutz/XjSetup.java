package net.xj.nutz;

import org.nutz.dao.Dao;
import org.nutz.dao.entity.annotation.Table;
import org.nutz.dao.util.Daos;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.NutConfig;
import org.nutz.mvc.Setup;
import org.nutz.resource.Scans;

import net.xj.nutz.bean.Tb_user;
import net.xj.nutz.ext.BsThread;
import net.xj.nutz.ext.OrderThread;


public class XjSetup implements Setup{
	private static final Log log = Logs.get();

	private BsThread bsThread;
	private OrderThread orderThread;
	@Override
	public void init(NutConfig config) {
		// TODO Auto-generated method stub
		//log.debug("config ioc=" + config.getIoc());
		
		Dao dao = config.getIoc().get(Dao.class);
		for (Class<?> klass : Scans.me().scanPackage("net.xj.nutz")) {
			if (null != klass.getAnnotation(Table.class)) 
				dao.create(klass, false);
		}
		/**----------------------日期线程启动---------------------- **/
		BsThread bsThread=new BsThread();
		bsThread.start();
		/**----------------------订单线程启动---------------------- **/
		orderThread=new OrderThread();
		orderThread.start();
		
		//Daos.migration(dao, "net.xj.nutz.bean", true, false, false);//表字段新增后使用
		
		/*User admin=config.getIoc().get(User.class);
		System.out.println("-------Ioc----"+admin);
		if(dao.count(User.class) == 0){
//			 User admin =new User();
//			 admin.setName("root");
//			 admin.setPasswd("123456");
			 dao.insert(admin);
		}
		*/
	}

	
	@Override
	public void destroy(NutConfig config) {
		// TODO Auto-generated method stub
		bsThread.interrupt();
		orderThread.interrupt();
	}

}
