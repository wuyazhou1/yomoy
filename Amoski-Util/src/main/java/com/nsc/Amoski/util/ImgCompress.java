package com.nsc.Amoski.util;

import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 图片压缩处理
 * @author 崔素强
 */
public class ImgCompress {
	private Image img;
	private int width;
	private int height;
	private final int defautWidth=1920;
	private final int defautHeight=1080;
	/**
	 * 构造函数
	 */
	public ImgCompress(MultipartFile file) throws IOException {
		img = ImageIO.read(file.getInputStream());      // 构造Image对象
		width = img==null?0:img.getWidth(null);    // 得到源图宽
		height = img==null?0:img.getHeight(null);  // 得到源图长
		System.out.println("+=+-+！~~~width:"+width+"===height:"+height);
	}
	/**
	 * 构造函数
	 */
	public ImgCompress(File file) throws IOException {
		img = ImageIO.read(file);      // 构造Image对象
		width = img==null?0:img.getWidth(null);    // 得到源图宽
		height = img==null?0:img.getHeight(null);  // 得到源图长
		System.out.println("+=+-+！~~~width:"+width+"===height:"+height);
	}

	/**
	 * 构造函数
	 */
	public ImgCompress(String pageFile) throws IOException {
		img = ImageIO.read(new File(pageFile));      // 构造Image对象
		width = img==null?0:img.getWidth(null);    // 得到源图宽
		height = img==null?0:img.getHeight(null);  // 得到源图长
		System.out.println("+=+-+！~~~width:"+width+"===height:"+height);
	}
	/**
	 * 按照宽度还是高度进行压缩
	 */
	public void resizeFix(String path) throws IOException {
		if(width>defautWidth){
			resizeByWidth(defautWidth,path);
		}else{
			if(height>defautHeight){
				resizeByHeight(defautHeight,path);
			}else{
				resizeByHeight(height,path);
			}
		}
		/*if (width / height > w / h) {
			resizeByWidth(w,path);
		} else {
			resizeByHeight(h,path);
		}*/
	}
	/**
	 * 以宽度为基准，等比例放缩图片
	 * @param w int 新宽度
	 */
	public void resizeByWidth(int w,String path) throws IOException {
		//if(w>width){w=width;}
		int h = (int) (height * w / width);
		resize(w, h,path);
	}
	/**
	 * 以高度为基准，等比例缩放图片
	 * @param h int 新高度
	 */
	public void resizeByHeight(int h,String path) throws IOException {
		//if(h>height){h=height;}
		int w = (int) (width * h / height);
		resize(w, h,path);
	}
	/**
	 * 强制压缩/放大图片到固定的大小
	 * @param w int 新宽度
	 * @param h int 新高度
	 * @param path String 生成文件路径
	 */
	public void resize(int w, int h,String path) throws IOException {
		// SCALE_SMOOTH 的缩略算法 生成缩略图片的平滑度的 优先级比速度高 生成的图片质量比较好 但速度慢
		BufferedImage image = new BufferedImage(w, h,BufferedImage.TYPE_INT_RGB ); 
		image.getGraphics().drawImage(img, 0, 0, w, h, null); // 绘制缩小后的图
		File destFile = new File(path);
		FileOutputStream out = new FileOutputStream(destFile); // 输出到文件流
		// 可以正常实现bmp、png、gif转jpg

		ImageIO.write(image, "jpeg", out);
		out.close();
	}


	/**
	 * 等比例强制压缩/放大图片到固定的大小
	 * @param path String 生成文件路径
	 */
	public void equalRatioResize(String path) throws IOException {
		int w =img.getWidth(null)/3;
		int h = img.getHeight(null)/3;
		w = w<200?200:w;
		w = w>800?800:w;
		h = h<200?200:h;
		h = h>800?800:h;
		// SCALE_SMOOTH 的缩略算法 生成缩略图片的平滑度的 优先级比速度高 生成的图片质量比较好 但速度慢
		BufferedImage image = new BufferedImage(w, h,BufferedImage.TYPE_INT_RGB );
		image.getGraphics().drawImage(img, 0, 0, w, h, null); // 绘制缩小后的图
		File destFile = new File(path);
		FileOutputStream out = new FileOutputStream(destFile); // 输出到文件流
		// 可以正常实现bmp、png、gif转jpg

		ImageIO.write(image, "jpeg", out);
		out.close();
	}
}