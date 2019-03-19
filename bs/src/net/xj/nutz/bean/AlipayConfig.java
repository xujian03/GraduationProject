package net.xj.nutz.bean;

import java.io.FileWriter;
import java.io.IOException;

/* *
 *类名：AlipayConfig
 *功能：基础配置类
 *详细：设置帐户有关信息及返回路径
 *修改日期：2017-04-05
 *说明：
 *以下代码只是为了方便商户测试而提供的样例代码，商户可以根据自己网站的需要，按照技术文档编写,并非一定要使用该代码。
 *该代码仅供学习和研究支付宝接口使用，只是提供一个参考。
 */

public class AlipayConfig {
	
//↓↓↓↓↓↓↓↓↓↓请在这里配置您的基本信息↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓

	// 应用ID,您的APPID，收款账号既是您的APPID对应支付宝账号
	public static String app_id = "2016091700531212";
	
	// 商户私钥，您的PKCS8格式RSA2私钥
    public static String merchant_private_key = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCzr1JiD4oBNGKesinr6wT3yE6KshWyX3gZG30Opbx1UHyFCnpXAv3pKekGf9vSaQ0w3p8mYzQrddsvW/VZ022dNqzYCTvQbVC6cbpnhKG0C+kMzv4h77KnK+Pz2axxzyofWbuyi8vDwjBqNDfS4wZXbE7iXPYqi2HCqhjTJUlEaC8CQpRkUsxVifTDBTif7wuZZfgcK+QM8YzVFtrex5nXJry3rJ71zZO+/9cW6Pur7Hgr70im1x++Q3fq7ekfb1KTsBUDJqbYvOffgE8f7uLGL3eFmcE43S+s212GOiJPSosmCsYUi1sLiSVQTbyBelEFg11CUXYy+bDp+Ea/WcLTAgMBAAECggEBAKgi/VbvLXb1GvdexeWRdIOb9wrHm4/ez9XGqB3lrlSkbPBdHp9/GDNvWTMQv5TDYmmBV7EnETE9xk0pootcUWzUNaat9T+SmwDr3L5zGJEOuBOZOaptzmGpq1OKM1gHv2W4pO9s6s4STxWCKV53u35gxewBiwPCi0cJvdMOR+HufTgZSghNNy1uwENFMD0M9EYspnppCFwCd+7lmhDmuPKxzWAGsGfF9P8+6upCgJPCu3HJQjIKOTm/UXPTJSS6Ug+axH7iZvciBbov0zjI7AIfcapPR/9b6VlsaF5ctzr3o10iovsAfwZVOP6QDEFgWRegV0czYgKzwZefG/spmtECgYEA49J+31unMyhjxvCHNKtfBzIDFCc1i1vuAEPgp2qPK4ggJTzd4Xw66L6jDDa34SRYwft8rdc2jbm24GtiAOj/RaLJlKFbqYSZdGmlxRbK+6r4+B05fD4cZkFhrnppMOhvtvLyghLDia6N3NtXYNa4SCPbp2TzVQly2+h1dyDA55sCgYEAyeioj1pASpSp/U5sHglWkQ/7OQ2sTw+N1UoRaENjZB8IZRHE38D4wpDFR2/0enQxj3kTlucJNPCDemKCsKFF8peOAZq//Y1GTvqMu8jcamfyP/+Wpizo6tOoqgvMlAL6C1ZKMjVe9o78lHZpI+12sALVAyPzgy7N4FV4KM5gMSkCgYA3QTtiQuF3h+lDO90fCh1fKmMSxTu4VBCSYeQf1rlornhVb8DEypOjFXGIKdFJOr6INc2UgCkMEKnpHkJDxD79jbDkaopHwBBT6re46IFMrrf5vWTO3GWY62ycm7XdldJahewJXjGst/hSQ6SaqRlC7ed9Xv+hdUw1R9kNjpUa1wKBgAN7OXzPR5CBo7S0Z3TVIGL77Y78R15NnFpzHn23u7z8M/7aIZTiFOf16xDiQ0rhOgGJSuctMKFzDDFOBYbaIQSfzFkGvAy5JN8zvr12JybGiAzGcWiIGbQC1kOoVGyw6HLNmBXpiauip4Q/zmxKKeO7CMU3F3nJks8tahb6B/qRAoGBAN023uO0MrSACDCXnHNiRG0PIfk9Ppk2SBrZE7zH47T3RQ00u9Y1/UxoSyVmvChLDQMMTqyCDcDwx/ugtnVWbRrsx5kpgHR22T7ChHs4grQioEQosQ1oX3wBeRwQoTdXW7ycN9S9mdshbbEm0CZMbmR9fZ1ttecm7RoBuPsVLGrl";
	
	// 支付宝公钥,查看地址：https://openhome.alipay.com/platform/keyManage.htm 对应APPID下的支付宝公钥。
    public static String alipay_public_key = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAzgas4E8Tq5ggmI3AgZ6hKa2J0KHicgzczxnDVFxlXMmIlEkPUtFCs1xPMRtU8BsAxMs3U++v6EkqkbYRwdWqvsbuMj0S7RT5JtGXXRVPlntUDADsaGvENSGVMbKCLCA05NwHE60BsFomXJEvLGEbNR50hOmXqy2bFWL9lS7PmVUPFlbENKOxkCEdcE/0MzudDa3lfYhZDAAXP/ghXnbEhvDCoCWD9XDYx+mLQAuD/6nycl0bFYa1ErW0QgbEobIO8smvJ2CHyOE1C3H+sSarjCk51orlbksZkMLDUBoVAlP1ORfFyfalAL7R3go97BJURkyFDsPAhPAHEF/xfhPXHwIDAQAB";

	// 服务器异步通知页面路径  需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
	public static String notify_url = "http://xujian.natapp1.cc/bs/order/notify";

	// 页面跳转同步通知页面路径 需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
	public static String return_url = "http://xujian.natapp1.cc/bs/order/return";

	// 签名方式
	public static String sign_type = "RSA2";
	
	// 字符编码格式
	public static String charset = "utf-8";
	
	// 支付宝网关
	public static String gatewayUrl = "https://openapi.alipaydev.com/gateway.do";
	
	// 支付宝网关
	public static String log_path = "C:\\";


//↑↑↑↑↑↑↑↑↑↑请在这里配置您的基本信息↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑

    /** 
     * 写日志，方便测试（看网站需求，也可以改成把记录存入数据库）
     * @param sWord 要写入日志里的文本内容
     */
    public static void logResult(String sWord) {
        FileWriter writer = null;
        try {
            writer = new FileWriter(log_path + "alipay_log_" + System.currentTimeMillis()+".txt");
            writer.write(sWord);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

