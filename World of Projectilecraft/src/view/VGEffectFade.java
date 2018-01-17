/**
 * 	TODO ���뵭����Ч��Ϊ��Ч���������
 */
package view;

import java.awt.Color;
import java.awt.Graphics2D;
import system.VEngine;
import jgui.GamePanel;

/**
 * @author Demilichzz
 *
 * 2012-11-14
 */
public class VGEffectFade extends VGraphicEffect{
	protected int fadetime;		//����������ÿһ�׶γ�����ʱ��
	protected int x=0,y=0,w=800,h=600;		//��Χ����
	protected int alpha;	//͸����
	protected Color c;		//������ɫ
	
	public VGEffectFade(){
		super();
	}
	public VGEffectFade(int x,int y,int w,int h,int fadetime){
		super();
		this.x=x;
		this.y=y;
		this.w=w;
		this.h=h;
		this.fadetime = fadetime/VEngine.gs.getMSecond();
	}
	public void update(int rt){
		// TODO ������Ч״̬���������ΪGS�����м���
		super.update(rt);			//���³�ʼʱ��͵�ǰʱ��
		int fadeindex = (currentTime-startTime)/fadetime;
		if(fadeindex<=10){
			c=new Color(0,0,0,fadeindex*25);
		}
		else if(fadeindex>20&&fadeindex<=30){
			c=new Color(0,0,0,(30-fadeindex)*25);
		}
		else if(fadeindex>30){
			destroyed=true;
		}		
	}
	public void drawEffect(Graphics2D g2d, GamePanel p2){
		// TODO ������Ч
		g2d.setColor(c);
		g2d.fillRect(x,y,w,h);
	}
}
