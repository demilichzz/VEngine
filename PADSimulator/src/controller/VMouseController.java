/**
 * @author Demilichzz
 *	鼠标控制器，用于控制鼠标响应相关逻辑
 * 2013-11-9
 */
package controller;

import global.Global;

import org.lwjgl.input.Mouse;

import data.GameData;

import system.VEngine;
import ui.VMouseActionUI;
import ui.VUI;

/**
 * @author Demilichzz
 *
 * 2013-11-9
 */
public class VMouseController {
	protected VMouseActionUI activeui;		//当前拖拽的ui
	protected VMouseActionUI holdui;		//当前鼠标停留的ui
	protected boolean leftstate = false;	//左键当前状态是否为按下
	protected boolean rightstate = false;
	protected int x,y;
	protected int mousestate = 0;		//鼠标控制器状态
	
	public VMouseController(){
		
	}
	
	public void setActiveUI(VMouseActionUI ui){
		// TODO 设置当前拖拽ui
		activeui = ui;
	}
	public int getMouseX(){
		return x;
	}
	public int getMouseY(){
		return y;
	}
	public void mouseAction(){
		if(mousestate == 0){
			mouseActionNormal();
		}
		else{
			
		}
	}
	public void mouseActionNormal(){
		// TODO 在每次GS更新中捕捉鼠标状态
		x = Mouse.getX();
		y = Global.windowY - Mouse.getY();		//以左上角为0,0坐标的模式进行定义	
		VUI currentui = null;
		
		if (Mouse.isButtonDown(0)){		//左键按下
			//System.out.println("left"
			if(this.holdui!=null){
				this.holdui.uiHoldRelease();
				this.holdui = null;
			}
			if(!leftstate){		//若之前状态非按下，则判定为进行点击
				leftClick(x,y);
				leftstate = true;
				currentui = VEngine.newgame.gs.uiparent.getMAUIbyLoc(x, y);	//获取鼠标按下位置的最上层UI
				if(currentui!=null){
					currentui.uiAction("MousePress");
					if(activeui==null){
						if(currentui instanceof VMouseActionUI){
							VMouseActionUI aui = (VMouseActionUI)currentui;
							aui.uiDragPress(x,y);	//激活UI的拖拽状态,同时将控制器中的当前拖拽ui设为此ui
						}
					}
				}	
			}
			else{
				if(activeui!=null){
					activeui.uiDragMoveTo(x, y);
				}
			}
			
		}
		else{		//左键放开
			if(leftstate){	//若之前状态为按下，则判定为点击释放
				currentui = VEngine.newgame.gs.uiparent.getMAUIbyLoc(x, y);	//获取鼠标按下位置的最上层UI
				if(currentui!=null){
					currentui.uiAction("MouseRelease");
				}					
			}
			dealRelease();
		}
		if (Mouse.isButtonDown(1)){		//右键
			if(!rightstate){		//若之前状态非按下，则判定为进行点击
				rightClick(x,y);
				rightstate = true;
			}
		}
		else{
			rightstate = false;
		}			
	}
	public void dealRelease(){
		// TODO 处理鼠标释放，用于在转珠时间结束时强制释放鼠标
		
		leftstate = false;
		if(activeui!=null){
			activeui.uiDragRelease(x,y);
			activeui = null;
		}
		VMouseActionUI holdui = null;		//鼠标停留的UI
		holdui = (VMouseActionUI) VEngine.newgame.gs.uiparent.getMAUIbyLoc(x, y);
		if(holdui!=null){
			if(this.holdui==holdui){	//计算停留时间
				this.holdui.uiHoldInc();
			}
			else{
				if(this.holdui!=null){
					this.holdui.uiHoldRelease();	//释放原停留UI的计数状态
					this.holdui = holdui;
				}
				else{
					this.holdui = holdui;
				}
			}
		}
		else{
			if(this.holdui!=null){
				this.holdui.uiHoldRelease();
			}
		}
	}
	
	
	private void leftClick(int x, int y) {
		// TODO 左键单击指定坐标的响应

	}

	private void rightClick(int x, int y) {
		// TODO Auto-generated method stub
		VUI getui = VEngine.newgame.gs.uiparent.getMAUIbyLoc(x, y);
		if(getui!=null){
			System.out.println(getui.uiID);
		}
	}
}
