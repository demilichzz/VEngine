/**
 * 文件名称：VUIManager.java
 * 类路径：ui
 * 描述：TODO UI管理器静态类,用于存储以默认参数创建指定UI的函数
 * 作者：Demilichzz
 * 时间：2012-3-14上午03:28:44
 * 版本：Ver 1.0
 */
package ui;

import data.GameData;
import system.VEngine;
import timer.VLuaAction;
import global.*;

/**
 * @author Demilichzz
 *
 */
public class VUIManager {
	public static final int UI_TITLE = 0;
	public static final int UI_LOADREPLAY = 1;
	public static final int UI_GAME0 = 2;
	public static final int UI_GAME1 = 3;
	public static final int UI_GAME2 = 4;
	public static final int UI_GAMEMODE = 5;
	
	public static final int PMENU_STANDARD = 0;
	public static final int PMENU_REPLAY = 1;
	public static final int PMENU_DIE = 2;
	public static final int PMENU_CONFIRMREPLAY = 3;
	public static final int PMENU_SAVED = 4;
	
	public static VUI createDefaultPlaceHolderUI(String id){
		// TODO 默认占位用UI
		VUI temp = new VUI("ph_block.png",id);
		return temp;
	}
	public static void loadUIState(int uistate){
		// TODO 通过字符串形式预定义加载UI状态，通过不同的参数加载不同的UI
		switch(uistate){
		case UI_TITLE:{
			VEngine.gs.uiparent.getUIByID("ui_bg").setActionVisible(true);		//背景UI
			VEngine.gs.uiparent.getUIByID("ui_go_panel").setVisible(false);	//选项面板UI
			VEngine.gs.uiparent.getUIByID("ui_gamepanel").setVisible(false); //游戏面板
			VEngine.gs.uiparent.getUIByID("ui_replaylist").setVisible(false); //录像列表
			VEngine.gs.uiparent.getUIByID("ui_instance_menu").setLoc(0, 0);
			VEngine.gs.uiparent.getUIByID("ui_talent_menu").setLoc(0, 0);
			/*VDynamicBarUI pclb = (VDynamicBarUI) VEngine.gs.uiparent.getUIByID("ui_pclife");
			pclb.bindValue(GameData.pc, 0);*/
			break;
		}
		case UI_LOADREPLAY:{
			VEngine.gs.uiparent.getUIByID("ui_bg").setVisible(false);		//背景UI
			VEngine.gs.uiparent.getUIByID("ui_go_panel").setVisible(false);	//选项面板UI
			VEngine.gs.uiparent.getUIByID("ui_gamepanel").setVisible(false); //游戏面板
			VEngine.gs.uiparent.getUIByID("ui_replaylist").setVisible(true); //录像列表
			break;
		}
		case UI_GAME0:{
			VEngine.gs.uiparent.getUIByID("ui_bg").setVisible(false);		//背景UI
			VEngine.gs.uiparent.getUIByID("ui_go_panel").setVisible(false);	//选项面板UI
			VEngine.gs.uiparent.getUIByID("ui_gamepanel").setActionVisible(true); //游戏面板
			VEngine.gs.uiparent.getUIByID("ui_replaylist").setVisible(false); //录像列表
			VEngine.gs.uiparent.getUIByID("ui_pclife").setVisible(false);
			VEngine.gs.uiparent.getUIByID("ui_lifeleft").setVisible(false);
			break;
		}
		case UI_GAME1:{
			VEngine.gs.uiparent.getUIByID("ui_bg").setVisible(false);		//背景UI
			VEngine.gs.uiparent.getUIByID("ui_go_panel").setVisible(false);	//选项面板UI
			VEngine.gs.uiparent.getUIByID("ui_gamepanel").setActionVisible(true); //游戏面板
			VEngine.gs.uiparent.getUIByID("ui_replaylist").setVisible(false); //录像列表
			VEngine.gs.uiparent.getUIByID("ui_pchit").setVisible(false);
			VEngine.gs.uiparent.getUIByID("ui_lifeleft").setVisible(false);
			break;
		}
		case UI_GAME2:{
			VEngine.gs.uiparent.getUIByID("ui_bg").setVisible(false);		//背景UI
			VEngine.gs.uiparent.getUIByID("ui_go_panel").setVisible(false);	//选项面板UI
			VEngine.gs.uiparent.getUIByID("ui_gamepanel").setActionVisible(true); //游戏面板
			VEngine.gs.uiparent.getUIByID("ui_replaylist").setVisible(false); //录像列表
			VEngine.gs.uiparent.getUIByID("ui_pclife").setVisible(false);
			VEngine.gs.uiparent.getUIByID("ui_pchit").setVisible(false);
			break;
		}
		case UI_GAMEMODE:{
			VEngine.gs.uiparent.getUIByID("ui_bg").setVisible(false);		//背景UI
			VEngine.gs.uiparent.getUIByID("ui_go_panel").setActionVisible(true);	//选项面板UI
			VEngine.gs.uiparent.getUIByID("ui_gamepanel").setVisible(false); //游戏面板
			VEngine.gs.uiparent.getUIByID("ui_replaylist").setVisible(false); //录像列表
			break;
		}
		}
	}
	public static void loadPauseMenu(int index){
		// TODO 加载暂停菜单
		final VMenuUI menu = (VMenuUI) VEngine.gs.uiparent.getUIByID("ui_pause_menu");
		if(true){//!menu.visible){
			switch(index){
			case PMENU_STANDARD:{		//常规暂停菜单
				menu.clearBind();
				menu.bindUI(VEngine.gs.uiparent.getUIByID("ui_pb_continue"));
				menu.bindUI(VEngine.gs.uiparent.getUIByID("ui_pb_exittomain"));
				menu.setActionVisible(menu.isVisible());
				menu.addCancelAction(new VLuaAction(){
					@Override
					public void action() {
						// TODO 回到上一级
						if(VEngine.gs.getCurrentStage().getfsmState()!=FSMconst.GS_DIE){	//关卡状态不能为已死亡
							menu.setVisible(false);
							VEngine.gs.uiparent.getUIByID("ui_responser").setEnable(true);		//重新激活响应器
							VEngine.gs.getCurrentStage().fsmStateTransition(FSMconst.INPUT_CONTINUE);	//解除关卡暂停
						}
					}
				});
				break;
			}
			case PMENU_REPLAY:{
				menu.clearBind();
				menu.bindUI(VEngine.gs.uiparent.getUIByID("ui_pb_continue"));
				menu.bindUI(VEngine.gs.uiparent.getUIByID("ui_pb_exitreplay"));
				menu.setActionVisible(menu.isVisible());
				break;
			}
			case PMENU_DIE:{
				menu.clearBind();
				if(!GameData.replaymode&&GameData.gamemode!=0){
					menu.bindUI(VEngine.gs.uiparent.getUIByID("ui_pb_savereplay"));
					menu.bindUI(VEngine.gs.uiparent.getUIByID("ui_pb_notsave"));
				}
				else{
					menu.bindUI(VEngine.gs.uiparent.getUIByID("ui_pb_exittomain"));	//录像和练习模式只有退回主菜单
				}
				menu.setActionVisible(menu.isVisible());
				break;
			}
			case PMENU_CONFIRMREPLAY:{		//转换为暂停确认菜单
				menu.clearBind();
				if((!GameData.replaymode)&&GameData.gamemode!=0){
					menu.bindUI(VEngine.gs.uiparent.getUIByID("ui_pb_savereplay"));
					menu.bindUI(VEngine.gs.uiparent.getUIByID("ui_pb_notsave"));
				}
				menu.addCancelAction(new VLuaAction(){
					@Override
					public void action() {
						// TODO 回到上一级
						loadPauseMenu(PMENU_STANDARD);	//载入普通
					}
				});
				menu.setActionVisible(menu.isVisible());
				break;
			}
			case PMENU_SAVED:{
				menu.clearBind();
				if((!GameData.replaymode)&&GameData.gamemode!=0){
					//menu.bindUI(VEngine.gs.uiparent.getUIByID("ui_pb_savereplay"));
					menu.bindUI(VEngine.gs.uiparent.getUIByID("ui_pb_notsave"));
				}
				menu.setActionVisible(menu.isVisible());
				break;
			}
			}
		}
	}
}
