/**
 * 	TODO ͼ��Ч�����࣬����Ϸ�����л���֮����л���ĳЩ����Ч��
 */
package view;

import java.awt.Graphics2D;

import system.VEngine;
import jgui.*;

/**
 * @author Demilichzz
 *
 * 2012-11-13
 */
public class VGraphicEffect {
	protected GamePanel p = null;		//���
	protected boolean destroyed = false;
	protected int startTime=0;		//��Ч������ʼʱ���
	protected int currentTime=0;	//��ǰʱ��
	protected boolean started=false;
	
	public VGraphicEffect(){
		p = VEngine.p;
		VEngine.gs.addEffect(this);		//��ʼ��ʱ����GS����Ч�б���
	}
	
	public void drawEffect(Graphics2D g2d, GamePanel p2){
		// TODO ������Ч
		
	}

	public boolean getDestoryed() {
		// TODO ��ȡ�Ƿ�Ӧ������ 
		return destroyed;
	}
	public void update(int rt){
		// TODO ������Ч״̬���������ΪGS�����м���
		if(!started){
			startTime = rt;
			started=true;
		}
		currentTime = rt;
	}
}
