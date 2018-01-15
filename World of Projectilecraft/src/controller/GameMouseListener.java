/**
 * 文件名称：GameMouseListener.java
 * 类路径：controller
 * 描述：TODO 鼠标监听器,监听游戏过程中鼠标动作
 * 作者：Demilichzz
 * 时间：2011-12-14下午06:34:53
 * 版本：Ver 1.0
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
	protected int pcount = 0; // 提示计数

	public int x;
	public int y;
	public boolean ifpress = false; // 是否为按下状态
	public boolean ifdrag = false; // 是否为拖拽状态

	// protected boolean buildmode = false; //建筑模式

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
			// 左
			pressbtn = 1;
			ifpress = true; 
			ifdrag = true;
			x = e.getX();
			y = e.getY();
			// Debug.DebugSimpleMessage(x+" "+y);
			break;
		}
		case MouseEvent.BUTTON2: {
			// 中
			break;
		}
		case MouseEvent.BUTTON3: {
			// 右
			pressbtn = 3;
			break;
		}
		}
	}

	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		switch (e.getButton()) {
		case MouseEvent.BUTTON1: {
			// 左
			ifdrag = false;
			pressbtn = 0;
			break;
		}
		case MouseEvent.BUTTON2: {
			// 中
			break;
		}
		case MouseEvent.BUTTON3: {
			// 右
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
		// TODO 设置当前拖拽目标UI
		dragtarget = temp;
	}

	public VDragableUI getDragTarget() {
		return dragtarget;
	}*/

	/**
	 * @param temp
	 */
/*	public void setPromptUI(VUI temp) {
		// TODO 设置当前提示文本UI
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
				pcount = -1; // 起锁定效果
			}
		}
	}*/
}
