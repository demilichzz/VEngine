/**
 * �ļ����ƣ�VImage.java
 * ��·����view
 * ������TODO ʹ��Imageicon��ͼ����
 * ���ߣ�Demilichzz
 * ʱ�䣺2011-12-3����05:05:57
 * �汾��Ver 1.0
 */
package view;

import java.awt.*;
import java.awt.image.*;
import java.io.*;

import javax.swing.*;

import system.VEngine;

import jgui.GamePanel;

/**
 * @author Demilichzz
 *
 */
public class VImage implements VImageInterface{
	protected Image image_source;			//Դͼ��
	protected int index;
	private Image[] image_output;			//�ָ�ͼ��
	private String fileurl;					//Դͼ���ļ���,toString����ֵ
	protected int width,height;				//ͼ��ӿڿ��,��ͼ���ѷָ���Ϊ�ָ�ͼ��Ŀ�͸�
	
	public VImage(){
		
	}
	public VImage(File f){
		ImageIcon newicon =  new ImageIcon(f.getPath());	//�½���ʱImageIcon
		width = newicon.getIconWidth();		
		height = newicon.getIconHeight();	//��ȡImageIcon�Ŀ�͸���ΪĬ�Ͽ��
		image_source = newicon.getImage();	//��ȡImageIcon��Image
		fileurl = f.getName();				//���ļ����洢����ת��Ϊ�ַ���
	}
	public VImage(String ad){
		image_source = new ImageIcon(ad).getImage();
		fileurl = ad;
	}
	
	public void setCrop(int w,int h,int i,int j){
		// TODO ���÷ָ����ͼƬ,�����ֱ�Ϊ�ָ���,�ָ�����������
		width = w;
		height = h;		//���������ͼ��ķָ�,��ʹ��ÿ��ָ�Ŀ����Ϊͼ����
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
		// TODO ��ȡ�ָ�ͼ���б���
		return image_output.length;
	}
	
	public String toString(){
		// TODO ת��Ϊ�ļ�url�ַ���
		return fileurl;
	}
	public void drawMe(Graphics2D g2d, int x, int y, JPanel p) {
		// TODO Auto-generated method stub
		g2d.drawImage(getImage(),x,y,p);
	}

	public Image getImage() { 
		// TODO �޲����򷵻�Դͼ��
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
