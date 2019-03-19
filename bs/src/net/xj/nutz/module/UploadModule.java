package net.xj.nutz.module;

import java.awt.Color;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

import javax.imageio.stream.FileImageInputStream;

import org.nutz.img.Images;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.ioc.loader.json.JsonLoader;
import org.nutz.json.Json;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.annotation.AdaptBy;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Param;
import org.nutz.mvc.upload.TempFile;
import org.nutz.mvc.upload.UploadAdaptor;

import com.drew.imaging.jpeg.JpegMetadataReader;
import com.drew.imaging.jpeg.JpegProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifDirectory;




@IocBean
@At("/upload")
public class UploadModule {
	private static final Log log=Logs.get();
	
	@At("/html4")
	@AdaptBy(type=UploadAdaptor.class)
	public String html4(@Param("f")TempFile[] file){
		String path=null;
		System.out.println(file.length);
		for(int i=0;i<file.length;i++){
			
			System.out.println("文件类型"+file[i].getContentType());
		System.out.println("文件大小"+file[i].getSize());
		String uuid = UUID.randomUUID().toString().replaceAll("-", "");
		String end=".";
		if(file[i].getContentType().equals("image/jpeg")){
			end=".jpeg";
		}else if(file[i].getContentType().equals("image/png")){
			end=".png";
		}else if(file[i].getContentType().equals("image/gif")){
			end=".gif";
		}
		log.debug(end);
//		if(!end.equals(".")){
//			BufferedImage img=Images.read(file[i].getFile());
//			
//			try {
//				Metadata metadata = JpegMetadataReader.readMetadata(file[i].getFile());
//				Directory directory  = metadata.getDirectory(ExifDirectory.class);
//				if(directory.containsTag(ExifDirectory.TAG_ORIENTATION)){
//					int orientation = directory.getInt(ExifDirectory.TAG_ORIENTATION);
//					/**
//					 *  case 1: return "Top, left side (Horizontal / normal)";
//    					case 2: return "Top, right side (Mirror horizontal)";
//    					case 3: return "Bottom, right side (Rotate 180)";
//    					case 4: return "Bottom, left side (Mirror vertical)";
//    					case 5: return "Left side, top (Mirror horizontal and rotate 270 CW)";
//    					case 6: return "Right side, top (Rotate 90 CW)";
//    					case 7: return "Right side, bottom (Mirror horizontal and rotate 90 CW)";
//    					case 8: return "Left side, bottom (Rotate 270 CW)";
//					 */
//						if(orientation==6){
//							img = Images.rotate(img, 90);
//						}else if(orientation==3){
//							img = Images.rotate(img, 180);
//						}else if(orientation==8){
//							img = Images.rotate(img, 270);
//						}
//					}
//			} catch (Exception e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			img=Images.zoomScale(img, 1900, 2000, Color.BLACK);
//			img=Images.addWatermark(img, new File("d:/file/water.png"),  0.75f, Images.WATERMARK_BOTTOM_RIGHT, 10);
//			path="http://localhost:8080/bs/uploadimg/"+uuid+end;
//			Images.write((RenderedImage) img, new File("d:/file/"+uuid+end));
//												
//		}
			
		}
		
		
		return path;
		
	}
	
}
