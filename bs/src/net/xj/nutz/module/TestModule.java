package net.xj.nutz.module;

import java.io.Console;

import org.json.JSONArray;
import org.json.JSONException;
import org.junit.Test;
import org.nutz.json.Json;
import org.nutz.json.JsonFormat;

import net.xj.nutz.bean.Result;

public class TestModule {
	@Test
	public static void main(String[] args) throws JSONException { {
		  String[] strings=new String[10];
		  strings[0]="abc";
		  strings[1]="def";
		  strings[2]="ghi";
		  System.out.println(Json.toJson(strings, JsonFormat.compact()));
	    }
	}
	class test{
		test1 alipay_trade_refund_response;
		String sign;
	}
	class test1{
		String code;
		String msg;
		String sub_msg;
		String out_trade_no;
		String refund_fee;
		String send_back_fee;
		String sub_code;
	}
}
