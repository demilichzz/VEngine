/**
 * 文件名称：VImage.java
 * 类路径：view
 * 描述：TODO 使用Imageicon的图像类
 * 作者：Demilichzz
 * 时间：2011-12-3上午05:05:57
 * 版本：Ver 1.0
 */
package view;

import java.awt.*;
import java.awt.image.*;
import java.io.*;

import javax.swing.*;

import system.VEngine;

/**
 * @author Demilichzz
 *
 */
public class VImage implements VImageInterface{
	protected Image image_source;			//源图像
	protected int index;
	private Image[] image_output;			//分割图像
	private String fileurl;					//源图像文件名,toString返回值
	protected int width,height;				//图像接口宽高,若图像已分割则为分割图像的宽和高
	
	public VImage(){
		
	}
	public VImage(File f){
		ImageIcon newicon =  new ImageIcon(f.getPath());	//新建临时ImageIcon
		width = newicon.getIconWidth();		
		height = newicon.getIconHeight();	//获取ImageIcon的宽和高作为默认宽高
		image_source = newicon.getImage();	//获取ImageIcon的Image
		fileurl = f.getName();				//将文件名存储用于转化为字符串
	}
	public VImage(String ad){
		image_source = new ImageIcon(ad).getImage();
		fileurl = ad;
	}
	
	public void setCrop(int w,int h,int i,int j){
		// TODO 设置分割输出图片,参数分别为分割宽高,分割列数和行数
		width = w;
		height = h;		//如果设置了图像的分割,则使用每块分割的宽高作为图像宽高
		image_output = new Image[i*j];
		ImageFilter cropFilter;
		for(int tj=0;tj<j;tj++){
			for(int ti=0;ti<i;ti++){
				cropFilter = new CropImageFilter((ti * w), (tj * h), w, h);
				Image tempi = Toolkit.getDefaultToolkit().createImage(
						new FilteredImageSource(image_source.getSource(),
								cropFilter));
				image_output[tj*i+ti] = tempi;
			}
		}
	}
	public int getCropSize(){
		// TODO 获取分割图像列表长度
		return image_output.length;
	}
	
	public String toString(){
		// TODO 转化为文件url字符串
		return fileurl;
	}
	public void drawMe(Graphics2D g2d, int x, int y, JPanel p) {
		// TODO Auto-generated method stub
		g2d.drawImage(getImage(),x,y,p);
	}

	public Image getImage() { 
		// TODO 无参数则返回源图像
		return image_source;
	}
	public Image getImage(int i) {
		// TODO Auto-generated method stub
		return image_output[i];
	}
	public int getHeight() {
		// TODO Auto-generated method stub
		return height;
	}
	public int getWidth() {
		// TODO Auto-generated method stub
		return width;
	}
}
