/**
 * 	TODO ��������ʽ�˵�UI���̳е��в˵�UI��������Ϸ��ʼʱ��ѡ�񸱱����츳��
 */
package ui;

import global.Debug;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;

import jgui.GamePanel;

import system.VEngine;
import timer.VLuaAction;
import timer.VTimer;
import config.ProjectConfig;
import controller.GameListener;

/**
 * @author Demilichzz
 *
 * 2012-11-6
 */
public class VScrollMenuUI extends VMenuUI{
	protected int center_x=361,center_y=261;		//����UI����
	protected int x_offset=200,y_offset=0;		//�����б���UI�ļ��
	protected boolean perodic=false;		//UI�Ƿ�ѭ����ʾ

	public VScrollMenuUI(){
		super();
	}
	public VScrollMenuUI(String str,String id){
		super(str,id);
	}
	public VScrollMenuUI(int w, int h, String str) {
		super(w,h,str);
	}
	public void bindUI(VUI ui){
		super.bindUI(ui);
		resetUILoc();
	}
	public void setIndex(int i){
		bindindex = i;
		resetUILoc();
	}
	public void setDistribution(int x,int y,int cx,int cy){
		// TODO ����UI�ֲ���x,yΪ�����cx,cyΪ����
		x_offset = x;
		y_offset = y;
		center_x = cx;
		center_y = cy;
		resetUILoc();
	}
	public void setPeridic(boolean b){
		perodic=b;
		resetUILoc();
	}
	public void resetUILoc(){
		// TODO ����UI������ز����ı�ʱ���ã������·ֲ���UIλ��
		if(perodic){
			int leftsize=uilist.size()/2;
			for(int i=0;i<uilist.size();i++){
				int newindex=(i+leftsize-bindindex+uilist.size())%uilist.size()-leftsize;	//ƫ������
				uilist.get(i).setLoc(center_x+x_offset*newindex,center_y+y_offset*newindex);
			}
		}
		else{
			for(int i=0;i<uilist.size();i++){
				uilist.get(i).setLoc(center_x+x_offset*(i-bindindex),center_y+y_offset*(i-bindindex));
			}	
		}		
	}
	public void moveCursor(int i){
		// TODO �ƶ����λ�ã�ͬʱ�����л���Ϊ
		super.moveCursor(i);
		resetUILoc();
	}
}
