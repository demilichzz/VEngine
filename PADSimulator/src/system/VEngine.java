package system;
/**
 * @author Demilichzz
 *
 * 2013-6-8
 */

import script.LuaScript;
import jgui.JEditPanelFrame;
import controller.*;
import global.CharacterConst;
import global.Debug;
import global.Fontconst;
import global.Imageconst;
import display.*;
import driver.*;
import event.GlobalEvent;

/**
 * @author Demilichzz
 * 
 *         2013-6-8
 */
public class VEngine {
	public static VEngine newgame;
	
	public Simulator s;		// 仿真器
	public GameState gs;	// 游戏状态
	public JEditPanelFrame editframe;	//面板编辑窗体
	public VDisplay renderer;	//渲染器
	public VMouseController mcontroller;	//鼠标控制器
	public VKeyController kcontroller;	//键盘控制器
	
	public VEngine() {
		Init();
	}
	public static void main(String[] args) {
		//new VEngine().start();
		newgame = new VEngine();
		GlobalEvent.initEvent();
		newgame.gameloop();
	}
	public void Init() {
		// TODO 初始化游戏状态和仿真器以运行游戏状态更新与渲染逻辑 
		gs = new GameState(this,10);
		s = new Simulator(this);
		editframe = new JEditPanelFrame();
		mcontroller = new VMouseController();
		kcontroller = new VKeyController();
		renderer = new VDisplay(this);
		//gs.Init();
	}
	private void gameloop() {
		// TODO Auto-generated method stub
		new Thread(s).start();
	}
	public void initResource() {
		Imageconst.Init();
		Fontconst.Init();
		CharacterConst.Init();
	}
}
