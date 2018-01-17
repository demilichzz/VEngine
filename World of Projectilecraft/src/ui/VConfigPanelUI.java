/**
 * 	TODO 设置面板UI
 */
package ui;

import global.Soundconst;
import sound.VSound;
import system.VEngine;
import timer.VLuaAction;
import config.ProjectConfig;
import controller.GameListener;
import data.GameData;

/**
 * @author Demilichzz
 *
 * 2012-11-28
 */
public class VConfigPanelUI extends VMenuUI{
	protected String[] sp_namelist = {"HumanMale","HumanFemale","Biu"};		//被击音频包名称
	public VConfigPanelUI(String str,String ID){
		super(str,ID);
		this.setCursor("UI_cursor.png");
		this.setOffset(-240,20);
		VConfigValueUI ui_volume_music = new VConfigValueUI("UI_300x50_VolumeBar.png","ui_volume_music");
		ui_volume_music.bindValue(ProjectConfig.getConfigVValue(ProjectConfig.MUSIC_VOLUME), 0);
		ui_volume_music.setPointerImage("UI_20x50_VolumePt.png");
		ui_volume_music.setValueIncrease(5);
		ui_volume_music.setValueRange(new int[]{0,100});
		ui_volume_music.setLoc(200,120);
		ui_volume_music.addSwitchAction(new VLuaAction(){
			@Override
			public void action() {
				// TODO Auto-generated method stub
				Soundconst.setVolume(ProjectConfig.getConfigValue(ProjectConfig.MUSIC_VOLUME),VSound.TYPE_MUSIC);
			}
		});
		VConfigValueUI ui_volume_sound = new VConfigValueUI("UI_300x50_VolumeBar.png","ui_volume_sound");
		ui_volume_sound.bindValue(ProjectConfig.getConfigVValue(ProjectConfig.SOUND_VOLUME), 0);
		ui_volume_sound.setPointerImage("UI_20x50_VolumePt.png");
		ui_volume_sound.setValueIncrease(5);
		ui_volume_sound.setLoc(200,20);
		ui_volume_sound.setValueRange(new int[]{0,100});
		ui_volume_sound.addSwitchAction(new VLuaAction(){
			@Override
			public void action() {
				// TODO Auto-generated method stub
				Soundconst.setVolume(ProjectConfig.getConfigValue(ProjectConfig.SOUND_VOLUME),VSound.TYPE_SOUND);
				Soundconst.GetSoundPack(sp_namelist[ProjectConfig.getConfigValue(ProjectConfig.HIT_SOUND)]).soundPlay(0);
			}
		});
		VConfigValueUI ui_hit_sound = new VConfigValueUI("UI_300x50_hitsound.png","ui_hit_sound");
		ui_hit_sound.bindValue(ProjectConfig.getConfigVValue(ProjectConfig.HIT_SOUND), 0);
		ui_hit_sound.setPointerImage("UI_20x50_HitPt.png");
		ui_hit_sound.setValueIncrease(1);
		ui_hit_sound.setValueRange(new int[]{0,2});
		ui_hit_sound.setPerodic(true);
		ui_hit_sound.setLoc(200,220);
		ui_hit_sound.setShowValue(false);
		ui_hit_sound.setPointerRange(new int[]{0,240});
		ui_hit_sound.addSwitchAction(new VLuaAction(){
			@Override
			public void action() {
				// TODO Auto-generated method stub
				GameData.pc.setHitSound(ProjectConfig.getConfigValue(ProjectConfig.HIT_SOUND));
			}
		});
		this.bindUI(ui_volume_sound);
		this.bindUI(ui_volume_music);
		this.bindUI(ui_hit_sound);
	}
	public void uiKeyAction(int[] keystate) {
		// TODO 重载UI键盘行为，实现光标位移功能
		if(keystate[GameListener.KEY_UP]==1){	//UP
			if(!moveblock){
				this.moveCursor(-1);
				lockCursor(200);
			}
		}
		if(keystate[GameListener.KEY_DOWN]==1){	//DOWN
			if(!moveblock){
				this.moveCursor(1);
				lockCursor(200);
			}
		}
		if(keystate[GameListener.KEY_RIGHT]==1){	//RIGHT
			if(!moveblock){
				this.uiAction("KEY_RIGHT");
				lockCursor(200);
			}
		}
		if(keystate[GameListener.KEY_LEFT]==1){	//LEFT
			if(!moveblock){
				this.uiAction("KEY_LEFT");
				lockCursor(200);
			}
		}
		if(keystate[GameListener.KEY_ESC]==1){	//ESC，执行取消行为
			if(cancel_action!=null){
				cancel_action.action();
				VEngine.glistener.resetKeystate(GameListener.KEY_ESC);
			}
		}
		if(keystate[GameListener.KEY_ENTER]==1){	//Enter
			//this.uiAction("Cursor");
			//VEngine.glistener.resetKeystate(GameListener.KEY_ENTER);
		}
		// 递归对子UI执行键盘行为
		super.uiKeyAction(keystate);
	}
	
}
