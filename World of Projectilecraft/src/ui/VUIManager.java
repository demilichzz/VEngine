/**
 * �ļ����ƣ�VUIManager.java
 * ��·����ui
 * ������TODO UI��������̬��,���ڴ洢��Ĭ�ϲ�������ָ��UI�ĺ���
 * ���ߣ�Demilichzz
 * ʱ�䣺2012-3-14����03:28:44
 * �汾��Ver 1.0
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
		// TODO Ĭ��ռλ��UI
		VUI temp = new VUI("ph_block.png",id);
		return temp;
	}
	public static void loadUIState(int uistate){
		// TODO ͨ���ַ�����ʽԤ�������UI״̬��ͨ����ͬ�Ĳ������ز�ͬ��UI
		switch(uistate){
		case UI_TITLE:{
			VEngine.gs.uiparent.getUIByID("ui_bg").setActionVisible(true);		//����UI
			VEngine.gs.uiparent.getUIByID("ui_go_panel").setVisible(false);	//ѡ�����UI
			VEngine.gs.uiparent.getUIByID("ui_gamepanel").setVisible(false); //��Ϸ���
			VEngine.gs.uiparent.getUIByID("ui_replaylist").setVisible(false); //¼���б�
			VEngine.gs.uiparent.getUIByID("ui_instance_menu").setLoc(0, 0);
			VEngine.gs.uiparent.getUIByID("ui_talent_menu").setLoc(0, 0);
			/*VDynamicBarUI pclb = (VDynamicBarUI) VEngine.gs.uiparent.getUIByID("ui_pclife");
			pclb.bindValue(GameData.pc, 0);*/
			break;
		}
		case UI_LOADREPLAY:{
			VEngine.gs.uiparent.getUIByID("ui_bg").setVisible(false);		//����UI
			VEngine.gs.uiparent.getUIByID("ui_go_panel").setVisible(false);	//ѡ�����UI
			VEngine.gs.uiparent.getUIByID("ui_gamepanel").setVisible(false); //��Ϸ���
			VEngine.gs.uiparent.getUIByID("ui_replaylist").setVisible(true); //¼���б�
			break;
		}
		case UI_GAME0:{
			VEngine.gs.uiparent.getUIByID("ui_bg").setVisible(false);		//����UI
			VEngine.gs.uiparent.getUIByID("ui_go_panel").setVisible(false);	//ѡ�����UI
			VEngine.gs.uiparent.getUIByID("ui_gamepanel").setActionVisible(true); //��Ϸ���
			VEngine.gs.uiparent.getUIByID("ui_replaylist").setVisible(false); //¼���б�
			VEngine.gs.uiparent.getUIByID("ui_pclife").setVisible(false);
			VEngine.gs.uiparent.getUIByID("ui_lifeleft").setVisible(false);
			break;
		}
		case UI_GAME1:{
			VEngine.gs.uiparent.getUIByID("ui_bg").setVisible(false);		//����UI
			VEngine.gs.uiparent.getUIByID("ui_go_panel").setVisible(false);	//ѡ�����UI
			VEngine.gs.uiparent.getUIByID("ui_gamepanel").setActionVisible(true); //��Ϸ���
			VEngine.gs.uiparent.getUIByID("ui_replaylist").setVisible(false); //¼���б�
			VEngine.gs.uiparent.getUIByID("ui_pchit").setVisible(false);
			VEngine.gs.uiparent.getUIByID("ui_lifeleft").setVisible(false);
			break;
		}
		case UI_GAME2:{
			VEngine.gs.uiparent.getUIByID("ui_bg").setVisible(false);		//����UI
			VEngine.gs.uiparent.getUIByID("ui_go_panel").setVisible(false);	//ѡ�����UI
			VEngine.gs.uiparent.getUIByID("ui_gamepanel").setActionVisible(true); //��Ϸ���
			VEngine.gs.uiparent.getUIByID("ui_replaylist").setVisible(false); //¼���б�
			VEngine.gs.uiparent.getUIByID("ui_pclife").setVisible(false);
			VEngine.gs.uiparent.getUIByID("ui_pchit").setVisible(false);
			break;
		}
		case UI_GAMEMODE:{
			VEngine.gs.uiparent.getUIByID("ui_bg").setVisible(false);		//����UI
			VEngine.gs.uiparent.getUIByID("ui_go_panel").setActionVisible(true);	//ѡ�����UI
			VEngine.gs.uiparent.getUIByID("ui_gamepanel").setVisible(false); //��Ϸ���
			VEngine.gs.uiparent.getUIByID("ui_replaylist").setVisible(false); //¼���б�
			break;
		}
		}
	}
	public static void loadPauseMenu(int index){
		// TODO ������ͣ�˵�
		final VMenuUI menu = (VMenuUI) VEngine.gs.uiparent.getUIByID("ui_pause_menu");
		if(true){//!menu.visible){
			switch(index){
			case PMENU_STANDARD:{		//������ͣ�˵�
				menu.clearBind();
				menu.bindUI(VEngine.gs.uiparent.getUIByID("ui_pb_continue"));
				menu.bindUI(VEngine.gs.uiparent.getUIByID("ui_pb_exittomain"));
				menu.setActionVisible(menu.isVisible());
				menu.addCancelAction(new VLuaAction(){
					@Override
					public void action() {
						// TODO �ص���һ��
						if(VEngine.gs.getCurrentStage().getfsmState()!=FSMconst.GS_DIE){	//�ؿ�״̬����Ϊ������
							menu.setVisible(false);
							VEngine.gs.uiparent.getUIByID("ui_responser").setEnable(true);		//���¼�����Ӧ��
							VEngine.gs.getCurrentStage().fsmStateTransition(FSMconst.INPUT_CONTINUE);	//����ؿ���ͣ
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
					menu.bindUI(VEngine.gs.uiparent.getUIByID("ui_pb_exittomain"));	//¼�����ϰģʽֻ���˻����˵�
				}
				menu.setActionVisible(menu.isVisible());
				break;
			}
			case PMENU_CONFIRMREPLAY:{		//ת��Ϊ��ͣȷ�ϲ˵�
				menu.clearBind();
				if((!GameData.replaymode)&&GameData.gamemode!=0){
					menu.bindUI(VEngine.gs.uiparent.getUIByID("ui_pb_savereplay"));
					menu.bindUI(VEngine.gs.uiparent.getUIByID("ui_pb_notsave"));
				}
				menu.addCancelAction(new VLuaAction(){
					@Override
					public void action() {
						// TODO �ص���һ��
						loadPauseMenu(PMENU_STANDARD);	//������ͨ
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
