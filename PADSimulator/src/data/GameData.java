/**
 * @author Demilichzz
 *	游戏数据类，存储游戏过程中使用的场景和角色等一切可能进行动态变化的数据
 * 2013-6-25
 */
package data;

import java.util.HashMap;

import ai.*;
import entities.*;
import event.*;
import global.*;
import system.*;
import ui.*;

/**
 * @author Demilichzz
 *
 * 2013-6-25
 */
public class GameData {
	public static GameArea ga;
	public static VPathAI pathAI;
	public static VGeneticPathAI gPathAI;
	public static int combolevel = 0;
	public static VPathAI currentAI;
	public static VInstance instance;
	public static VParty party;
	
	public static int gameareaWidth = 510;
	public static int gameareaHeight = 425;
	
	public static void preinitGameData(){
		// TODO 预先初始化游戏数据中需要在后续初始化中使用的参数
		gameareaWidth = 510;
		gameareaHeight = 425;
	}
	
	public static void initGameData(){
		// TODO 初始化游戏数据
		Debug.DebugTestTimeStart();
		//Debug.DebugSimpleMessage("初始化游戏数据");
		ga = new GameArea();
		ga.bindUI(GlobalEvent.getGameAreaUI());
		ga.setPanelSize(GameArea.PSIZE_6x5);
		pathAI = new VPathAI();
		pathAI.bindUI(VEngine.newgame.gs.uiparent.getUIByID("ui_arrowpanel"));
		gPathAI = new VGeneticPathAI();
		//gPathAI.bindUI(VEngine.newgame.gs.uiparent.getUIByID("ui_arrowpanel"));
		Debug.DebugTestTimeEnd("初始化游戏数据", true);
	}
	

}
