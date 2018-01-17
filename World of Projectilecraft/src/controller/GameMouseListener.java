/**
 * �ļ����ƣ�GameMouseListener.java
 * ��·����controller
 * ������TODO ��������,������Ϸ��������궯��
 * ���ߣ�Demilichzz
 * ʱ�䣺2011-12-14����06:34:53
 * �汾��Ver 1.0
 */
package controller;

import global.Debug;
import global.Textconst;

import java.awt.event.*;

import config.ProjectConfig;
import system.*;
import ui.VUI;
import ui.VUIText;

/**
 * @author Demilichzz
 * 
 */
public class GameMouseListener implements MouseListener, MouseMotionListener {
	protected VEngine ve;
	protected int pressbtn = 0;
//	protected VDragableUI dragtarget;
	protected VUI promptUI;
	protected int pcount = 0; // ��ʾ����

	public int x;
	public int y;
	public boolean ifpress = false; // �Ƿ�Ϊ����״̬
	public boolean ifdrag = false; // �Ƿ�Ϊ��ק״̬

	// protected boolean buildmode = false; //����ģʽ

	public GameMouseListener(VEngine ve) {
		this.ve = ve;
	}

	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		switch (e.getButton()) {
		case MouseEvent.BUTTON1: {
			// ��
			pressbtn = 1;
			ifpress = true; 
			ifdrag = true;
			x = e.getX();
			y = e.getY();
			// Debug.DebugSimpleMessage(x+" "+y);
			break;
		}
		case MouseEvent.BUTTON2: {
			// ��
			break;
		}
		case MouseEvent.BUTTON3: {
			// ��
			pressbtn = 3;
			break;
		}
		}
	}

	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		switch (e.getButton()) {
		case MouseEvent.BUTTON1: {
			// ��
			ifdrag = false;
			pressbtn = 0;
			break;
		}
		case MouseEvent.BUTTON2: {
			// ��
			break;
		}
		case MouseEvent.BUTTON3: {
			// ��
			pressbtn = 0;
			break;
		}
		}
	}

	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		// Debug.DebugSimpleMessage(e.getX()+" "+e.getY());
		x = e.getX();
		y = e.getY();
	}

	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		x = e.getX();
		y = e.getY();
	}

/*	public void setDragTarget(VDragableUI temp) {
		// TODO ���õ�ǰ��קĿ��UI
		dragtarget = temp;
	}

	public VDragableUI getDragTarget() {
		return dragtarget;
	}*/

	/**
	 * @param temp
	 */
/*	public void setPromptUI(VUI temp) {
		// TODO ���õ�ǰ��ʾ�ı�UI
		if (promptUI != null && promptUI == temp) {
			if (pcount >= 0) {
				pcount++;
			}
		} 
		else {
			pcount = 0;
			promptUI = temp;
			VUI tmp = VEngine.gs.uiparent.getUIByID("mouseprompt");
			if (tmp != null) {
				tmp.removeUIRelation();
			}
		}
		if (pcount >= ProjectConfig.getConfigValue(ProjectConfig.PROMPT_TIME)*100) {
			if (promptUI instanceof VMotionActionUI) {
				VMotionActionUI tempins = (VMotionActionUI)promptUI;
				VUIText t = tempins.getPromptText();
				if(t!=null){
					VUI mp01 = t.toUI("mouseprompt",0);
					mp01.setLoc(x - (mp01.area.w / 2), y - mp01.area.h);
					mp01.setParent(VEngine.gs.uiparent);			
				}
				pcount = -1; // ������Ч��
			}
		}
	}*/
}
