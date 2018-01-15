/**
 * 	TODO �������ļ����������ܹ�ͨ��UI��Ϊ�޸�����ֵ��ѡ��UI
 */
package ui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;

import jgui.GamePanel;
import interfaces.VInt;
import interfaces.VValueInterface;
import global.Debug;
import global.Global;
import global.Imageconst;
import system.VEngine;
import timer.VLuaAction;
import view.VImageInterface;
import config.ProjectConfig;
import controller.GameListener;

/**
 * @author Demilichzz
 *
 * 2012-11-28
 */
public class VConfigValueUI extends VUI{
	protected VValueInterface bindvalue=null;		//�󶨲���ֵ
	protected VImageInterface image_pointer;		//ָ��ͼ��
	protected VLuaAction switch_action;	//�˵��л���Ϊ
	protected int pt_xmax=0;				//ָ������ͼ�����꣬������ָ��ͼ��ʱ�޸�
	protected int index=0;
	protected boolean perodic=false;		//�Ƿ�����ѭ������ֵ
	protected int valueinc=0;			//ÿ����Ӧ����������ֵ
	protected int[] valuerange;			//ֵ��Χ
	protected boolean showvalue = true;	//��ʾֵ
	protected int[] pointerrange;		//ָ�����귶Χ
	
	public VConfigValueUI(String str,String ID){
		super(str,ID);
	}
	
	public void bindValue(VValueInterface v,int index){
		this.bindvalue = v;
		this.index = index;
	}
	public void setValueIncrease(int inc){
		// TODO ��������ֵ
		valueinc = inc;
	}
	public void setPointerImage(String str){
		// TODO ����ָ��ͼ��������ͼ������
		image_pointer = Imageconst.GetImageByName(str);
		if(image!=null&&image_pointer!=null){
			pt_xmax = image.getWidth()-image_pointer.getWidth();
		}
	}
	public void setPerodic(boolean b){
		perodic=b;
	}
	public void setShowValue(boolean b){
		showvalue = b;
	}
	public void addSwitchAction(VLuaAction sa) {
		// TODO ����ڲ˵�ѡ��֮���л�ʱ��������Ϊ
		switch_action=sa;
	}
	public void setPointerRange(int[] pr){
		if(pr.length>1){
			pointerrange = pr;
		}
		else{
			Debug.DebugSimpleMessage("�����Ĳ���ֵ��Χ���ò���ȷ");
		}
	}
	public void setValueRange(int[] vr){
		if(vr.length>1){
			valuerange=vr;
		}
		else{
			Debug.DebugSimpleMessage("�����Ĳ���ֵ��Χ���ò���ȷ");
		}
	}
	
	public void setValue(int i){
		// TODO ���ò���ֵ
		int value = bindvalue.getValue(index);		//�Ӱ󶨵Ķ�ֵ̬�ӿڻ�ȡ
		value = value + i*valueinc;
		while(value>valuerange[1]||value<valuerange[0]){
			if (value > valuerange[1]) {
				if (perodic) { // ����ظ�
					value = valuerange[0]; // �ۻ�ֵ
				} else { // ����
					value = valuerange[1]; // ����Ϊ���ֵ
				}
			} else if (value < valuerange[0]) {
				if (perodic) { // ����ظ�
					value = valuerange[1]; // �ۻ�ֵ
				} else { // ����
					value = valuerange[0]; // ����Ϊ��Сֵ
				}
			}
		}
		bindvalue.setValue(index, value);
	}
	
	public void uiAction(String trigtype) {
		// TODO ִ��UI��Ϊ������Ϊ����Ŀ�����Ϊ����ֵ
		if (enable) {
			if(trigtype.equals("KEY_LEFT")){		//����
				setValue(-1);		//ֵ����
				if(switch_action!=null){
					switch_action.action();
				}
			}
			if(trigtype.equals("KEY_RIGHT")){		//����
				setValue(1);		//ֵ����
				if(switch_action!=null){
					switch_action.action();
				}
			}
		}
	}
	public void drawUI(Graphics g, GamePanel p) {
		// TODO ����UI
		if (visible) {
			Graphics2D g2d = (Graphics2D) g;
			if (image != null) {	//����UIͼ��
				g.drawImage(image.getImage(), (int)getRealX(), (int)getRealY(), p);
			}
			if (ProjectConfig.getConfigValue(ProjectConfig.SHOWUIBORDER) == 1) {
				area.drawMe(g2d, p);	//����UI�߽���
			}
			int config_value = bindvalue.getValue(index);
			if(image_pointer!=null){
				int realx = (int) getRealX();
				double scale = (double)config_value/valuerange[1];	//��ȡ����
				int x_offset = (int) (scale*pt_xmax);
				if(pointerrange!=null){
					realx = realx+pointerrange[0];
					x_offset = (int) (scale*(pointerrange[1]-pointerrange[0]));
				}
				g.drawImage(image_pointer.getImage(),realx+x_offset,(int) getRealY(),p);
			}
			if(text!=null){			//����UI����
				text.drawMe(this,g,p);
			}
			if(showvalue){
				g2d.setFont(Global.f_config);
				g2d.setColor(Global.c_config);
				String value = config_value+"";
				g2d.drawString(value,(int)getRealX()+image.getWidth()+10,(int)getRealY()+image.getHeight()/2+10);
			}
			for (VUI child = firstChild; child != null; child = child.nextUI) {
				child.drawUI(g2d, p);	//������UI����
			}
		}
	}	
}
