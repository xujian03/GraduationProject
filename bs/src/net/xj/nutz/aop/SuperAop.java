package net.xj.nutz.aop;

import java.util.Random;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

import org.nutz.aop.InterceptorChain;
import org.nutz.aop.MethodInterceptor;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.mvc.view.JspView;
@IocBean
//拦截器必须是IOC的bean
public class SuperAop  implements MethodInterceptor{


	public void filter(InterceptorChain chain) throws Throwable {
		chain.doChain();
	}
	
}
