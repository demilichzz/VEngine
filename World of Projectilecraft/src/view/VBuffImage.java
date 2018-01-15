/**
 * 文件名称：VBuffImage.java
 * 类路径：view
 * 描述：TODO 
 * 作者：Demilichzz
 * 时间：2011-10-27上午06:15:40
 * 版本：Ver 1.0
 */
package view;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.*;
import javax.imageio.*;

/**
 * @author Demilichzz
 *
 */
public class VBuffImage implements VImageInterface{
	BufferedImage img_output;				//完整源图像
	private BufferedImage[] image_output;	//切割后的源图像
	private String fileurl;
	
	
	public VBuffImage(File f){
		try {
			img_output = ImageIO.read(f);
			fileurl = f.getName();
        } catch (Exception e) {
            e.printStackTrace();
        }
	}
	
	public void setCrop(int w,int h,int i,int j){
		// TODO 设置分割输出图片,参数分别为分割宽高,分割列数和行数
		image_output = new BufferedImage[i*j];
		for(int tj=0;tj<j;tj++){
			for(int ti=0;ti<i;ti++){
				image_output[tj*i+ti] = img_output.getSubimage(ti*w,tj*h,w,h);
			}
		}
	}
	
	public BufferedImage getImage() {
		// TODO Auto-generated method stub
		return img_output;
	}

	public BufferedImage getImage(int i) {
		// TODO Auto-generated method stub
		try {
			return image_output[i];
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public String toString(){
		return fileurl;
	}

	public int getHeight() {
		// TODO Auto-generated method stub
		return 0;
	}

	public int getWidth() {
		// TODO Auto-generated method stub
		return 0;
	}
	
}
