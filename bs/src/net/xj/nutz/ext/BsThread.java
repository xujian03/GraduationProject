package net.xj.nutz.ext;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.ioc.Ioc;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.Mvcs;

import net.xj.nutz.bean.Tb_login;
import net.xj.nutz.bean.Tb_user;

//每隔20小时执行一次，为登录次数表作初始化
@IocBean
public class BsThread extends Thread{

	Ioc ioc = Mvcs.getIoc();
	
	private static final Log log = Logs.get();
	
	private Dao dao=ioc.get(Dao.class);
	@Override
	public void run() {
		while (true) {
			if (this.isInterrupted()) {
				log.debug("日期线程正在停止！");
				return;
			}
			log.debug("日期线程正在运行！");
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			String date=df.format(new Date());
			List<Tb_login> login=dao.query(Tb_login.class, Cnd.where("loginDate","=",date));
			if(login==null||login.size()==0)
			{
				Tb_login login2=new Tb_login();
				login2.setLoginDate(date);
				login2.setLoginCount(0);
				dao.fastInsert(login2);
			}
			try {
				Thread.sleep(1000*60*60*20);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}
}
