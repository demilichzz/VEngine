/**
 * �ļ����ƣ�VBuffImage.java
 * ��·����view
 * ������TODO 
 * ���ߣ�Demilichzz
 * ʱ�䣺2011-10-27����06:15:40
 * �汾��Ver 1.0
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
	BufferedImage img_output;				//����Դͼ��
	private BufferedImage[] image_output;	//�и���Դͼ��
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
		// TODO ���÷ָ����ͼƬ,�����ֱ�Ϊ�ָ���,�ָ�����������
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
