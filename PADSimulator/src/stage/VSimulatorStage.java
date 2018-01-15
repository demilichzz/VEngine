/**
 * 	战斗模拟器模式关卡，可以进行自定义队伍及战斗模拟
 */
package stage;

import data.*;
import entities.*;
import event.*;
import system.*;
import ui.*;
import view.VText;

/**
 * @author Demilichzz
 *
 */
public class VSimulatorStage extends VStage{
	public VSimulatorStage(){
		super();
	}
	public VSimulatorStage(GameState gs,String sid){
		super(gs,sid);
	}
	
	public void initStage(){
		// TODO 在关卡状态间切换时初始化关卡
		//----------------UI处理------------------------------------
		//GlobalEvent.getGameAreaUI().setLoc(100, 375);
		GlobalEvent.getUIByID("ui_editbutton").setVisible(false);
		GlobalEvent.getUIByID("ui_resetbutton").setVisible(false);
		GlobalEvent.getUIByID("ui_playback").setVisible(false);
		GlobalEvent.getUIByID("ui_aipf").setVisible(false);
		GlobalEvent.getUIByID("ui_simmode_buttonlayer").setVisible(true);
		//----------------------------------------------------------------
		GameData.instance = new VInstance();
		VInstanceUI instanceUI = (VInstanceUI) VInstance.createUI();
		instanceUI.setParent(GlobalEvent.getUIByID("uiparent"));
		instanceUI.setLoc(104,0);
		GameData.instance.bindUI(instanceUI);
		GameData.instance.initInstance();
		
		
		GameData.party = new VParty();
		GameData.party.initParty(new int[6]);
		VPartyUI partyUI = (VPartyUI)VParty.createUI();
		partyUI.setParent(instanceUI);
		switch(GameArea.currentPSIZE){
		case GameArea.PSIZE_5x4:
			partyUI.setLoc(0,295);
			break;
		case GameArea.PSIZE_6x5:
			partyUI.setLoc(0,270);
			break;
		case GameArea.PSIZE_7x6:
			partyUI.setLoc(0,275);
			break;
		}
		
		GameData.party.bindUI(partyUI);
		VPartyLifeBarUI dbarui = new VPartyLifeBarUI("lifebar_background.png","lifebar_bar.png","lifebar_cover.png","ui_lifebar");	//建立血条UI
		dbarui.setParent(partyUI);
		dbarui.setLoc(0,85);
		dbarui.bindValue(GameData.party, VParty.PARTY_CURRENTHP);
		dbarui.bindMaxValue(GameData.party, VParty.PARTY_MAXHP);
		dbarui.setMaxValue(GameData.party.getValue(VParty.PARTY_MAXHP));
		dbarui.setValueDisplay(true, 0, 0);
		VText partyhptext = new VText("0/0");
		partyhptext.setFont("font_default");
		partyhptext.setLayout(VText.Layout_RIGHT);
		partyhptext.setLoc(512, 10);
		partyhptext.setColor(255,255,255);
		dbarui.addText(partyhptext);
		VText recoverytext = new VText("+0");
		recoverytext.setFont("font_default");
		recoverytext.setLayout(VText.Layout_CENTER);
		recoverytext.setLoc(256,9);
		recoverytext.setColor(100,255,100);
		dbarui.addText(recoverytext);
		//gameStageStart();
		Init();
	}
	public void updateStage(){
		// TODO 随着GS的状态更新当前关卡
		if(!inited){
			Init();
		}
		tp.process();
	}
	public void processKeyAction(int[] keystate) {
		super.processKeyAction(keystate);
	}
	public void processInput(){
		super.processInput();
	}
}
