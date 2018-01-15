/**
 * @author Demilichzz
 *	DBM面板UI，用于在右侧面板显示DBM计时条
 * 2012-12-12
 */
package ui;

import global.Debug;

import java.awt.*;
import java.util.*;

import jgui.GamePanel;

/**
 * @author Demilichzz
 *
 * 2012-12-12
 */
public class VDBMPanelUI extends VUI{
	public static final int listlength = 5;
	protected ArrayList<VDBMBarUI> preactbar = new ArrayList<VDBMBarUI>();	//DBM计时条列表
	protected VDBMBarUI[] prelist = new VDBMBarUI[listlength];		//未激活状态列表
	protected VDBMBarUI[] actlist = new VDBMBarUI[listlength];		//激活状态计时条列表
	protected int statecount = 0;		//DBM状态计数
	protected int x_offset = 10,y_offset=10;	//未激活偏移
	protected int x_offset_b = 10,y_offset_b=170;	//激活偏移
	protected int bar_height = 20;
	protected int stacksize = 0;
	
	public VDBMPanelUI(String str,String ID){
		super(str,ID);
		autoupdate=true;
	}
	
	public void addSkill(String name,double duration){
		// TODO 向DBM状态中添加技能
		VDBMBarUI newbar = new VDBMBarUI("UI_200x20_DBMbottom.png",this.uiID+"_"+statecount);
		newbar.setSkill(name, duration);
		newbar.setParent(this);
		newbar.setParentPanel(this);
		//newbar.setLoc(x_offset, y_offset+stacksize*bar_height);
		addNewTimer(newbar);		//加入列表以控制位移和维护
		statecount++;
		stacksize++;
	}

	protected void addNewTimer(VDBMBarUI newbar) {
		// TODO 将新的计时条添加到未激活列表
		for(int i=0;i<listlength;i++){
			if(prelist[i]==null){
				prelist[i]=newbar;
				newbar.setLoc(x_offset, y_offset+i*bar_height);
				return;
			}
		}
		Debug.DebugSimpleMessage("使用的计时条数量超出DBM允许的最大范围");
	}
	protected void addNewActivateTimer(VDBMBarUI newbar){
		// TODO 将指定计时条添加到激活列表
		for(int i=0;i<listlength;i++){
			if(actlist[i]==null){
				actlist[i]=newbar;
				return;
			}
		}
		Debug.DebugSimpleMessage("使用的计时条数量超出DBM允许的最大范围");
	}
	public void promoteTimerBar(VDBMBarUI target) {
		// TODO 将指定计时条提升一级，由未激活提升为激活或由激活提升为销毁，对于每个target仅会调用一次
		if(!target.prompted){
			for (int i = 0; i < listlength; i++) {
				if (prelist[i] == target) {
					target.prompted=true;
					target.setActivate(true);
					prelist[i] = null;
					addNewActivateTimer(target);
					sortTimerList(0); // 因为从原有未激活列表移除了，因此需要调整顺序
					return;
				}
			}
		}
	}
	public void removeActivatedTimerBar(VDBMBarUI target){
		// TODO 移除已结束的激活计时条
		if(target.getActivate()){
			for (int i = 0; i < listlength; i++) {
				if (actlist[i] == target) {
					actlist[i] = null;
					sortTimerList(1); // 因为从原有激活列表移除了，因此需要调整顺序
					return;
				}
			}
		}
	}
	public void sortTimerList(int index){
		// TODO 调整计时条列表的顺序使其无缝，索引0为未激活列表，索引1为激活列表
		if (index == 0) {
			for (int i = 0; i < listlength - 1; i++) {
				if (prelist[i] == null) {
					for (int j = i; j < listlength - 1; j++) {
						prelist[j] = prelist[j + 1];
					}
					prelist[listlength - 1] = null;
				}
			}
		} 
		else {
			for (int i = 0; i < listlength - 1; i++) {
				if (actlist[i] == null) {
					for (int j = i; j < listlength - 1; j++) {
						actlist[j] = actlist[j + 1];
					}
					actlist[listlength - 1] = null;
				}
			}
		}
	}
	public void clearDBMState(){
		// TODO 清除DBM状态
		preactbar.clear();
		statecount=0;
	}
	public void updateUI(){
		// TODO 更新UI,处理在未激活和激活计时条列表中的子UI的位移，及以上两列表的维护
		if(autoupdate){
			for(int i=0;i<listlength;i++){
				if(prelist[i]!=null){
					prelist[i].moveTo(x_offset, y_offset+i*bar_height,50);
				}
				if(actlist[i]!=null){
					actlist[i].moveTo(x_offset_b, y_offset_b-i*bar_height,100);
				}
			}
		}
		super.updateUI();		//递归更新
	}
	public void drawUI(Graphics g, GamePanel p) {
		super.drawUI(g, p);
	}
}
