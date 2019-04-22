package net.xj.nutz;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.FileChannel;

import org.nutz.dao.Dao;
import org.nutz.dao.entity.annotation.Table;
import org.nutz.dao.util.Daos;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.NutConfig;
import org.nutz.mvc.Setup;
import org.nutz.resource.Scans;

import com.ndktools.javamd5.Mademd5;

import net.xj.nutz.bean.Tb_admin;
import net.xj.nutz.bean.Tb_user;
import net.xj.nutz.ext.BsThread;
import net.xj.nutz.ext.OrderThread;


public class XjSetup implements Setup{
	private static final Log log = Logs.get();

	private final String DIR="d://file";
	
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
		
		Tb_admin admin=config.getIoc().get(Tb_admin.class);//获取admin账号密码
		
		if(dao.count(Tb_admin.class)==0){//数据库没有admin账号的时候自动生成一个从ioc中获取的admin账号
			Mademd5 md = new Mademd5();
			String password=md.toMd5(admin.getPassword());
			admin.setPassword(password);
			dao.fastInsert(admin);
		}
		
		File dir = new File(DIR);
		judeDirExists(dir);
		File dir1 = new File(DIR+"/avatar");
		judeDirExists(dir1);
		File dir2 = new File(DIR+"/buying");
		judeDirExists(dir2);
		File dir3 = new File(DIR+"/goods");
		judeDirExists(dir3);
		judeFileExists(new File(DIR+"/buyingwater.png"),"buyingwater.png");
		judeFileExists(new File(DIR+"/water2.png"),"water2.png");

		
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
		log.debug("*************************线程关闭***************************");
	}
	    // 判断文件夹是否存在
	 public void judeDirExists(File file) {
	 
	         if (file.exists()) {
	             if (file.isDirectory()) {
	                 System.out.println("dir exists");
	            } else {
	                 System.out.println("the same name file exists, can not create dir");
	             }
	         } else {
	             System.out.println("dir not exists, create it ...");
	             file.mkdir();
	         }
	 
	     }
	 
	 //文件是否存在
	      public void judeFileExists(File file,String dir) {
		  
		          if (file.exists()) {
		              System.out.println("file exists");
		          } else {
		              System.out.println("file not exists, create it ...");
		              try {
		  				String classpath = this.getClass().getResource("/").getPath().replaceFirst("/", "");
		  				fileChannelCopy(classpath+dir,DIR+"/"+dir);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		          }
		  
		      }
	      //复制文件
	      public void fileChannelCopy(String sFile, String tFile) {
	  		FileInputStream fi = null;
	  		FileOutputStream fo = null;
	  		FileChannel in = null;
	  		FileChannel out = null;
	  		File s = new File(sFile);
	  		File t = new File(tFile);
	  		if (s.exists() && s.isFile()) {
	  			try {
	  				fi = new FileInputStream(s);
	  				fo = new FileOutputStream(t);
	  				in = fi.getChannel();// 得到对应的文件通道
	  				out = fo.getChannel();// 得到对应的文件通道
	  				in.transferTo(0, in.size(), out);// 连接两个通道，并且从in通道读取，然后写入out通道
	  			} catch (IOException e) {
	  				e.printStackTrace();
	  			} finally {
	  				try {
	  					fi.close();
	  					in.close();
	  					fo.close();
	  					out.close();
	  				} catch (IOException e) {
	  					e.printStackTrace();
	  				}
	  			}
	  		}
	  	}

}
