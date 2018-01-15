/**
 * @author Demilichzz
 *	DBM���UI���������Ҳ������ʾDBM��ʱ��
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
	protected ArrayList<VDBMBarUI> preactbar = new ArrayList<VDBMBarUI>();	//DBM��ʱ���б�
	protected VDBMBarUI[] prelist = new VDBMBarUI[listlength];		//δ����״̬�б�
	protected VDBMBarUI[] actlist = new VDBMBarUI[listlength];		//����״̬��ʱ���б�
	protected int statecount = 0;		//DBM״̬����
	protected int x_offset = 10,y_offset=10;	//δ����ƫ��
	protected int x_offset_b = 10,y_offset_b=170;	//����ƫ��
	protected int bar_height = 20;
	protected int stacksize = 0;
	
	public VDBMPanelUI(String str,String ID){
		super(str,ID);
		autoupdate=true;
	}
	
	public void addSkill(String name,double duration){
		// TODO ��DBM״̬����Ӽ���
		VDBMBarUI newbar = new VDBMBarUI("UI_200x20_DBMbottom.png",this.uiID+"_"+statecount);
		newbar.setSkill(name, duration);
		newbar.setParent(this);
		newbar.setParentPanel(this);
		//newbar.setLoc(x_offset, y_offset+stacksize*bar_height);
		addNewTimer(newbar);		//�����б��Կ���λ�ƺ�ά��
		statecount++;
		stacksize++;
	}

	protected void addNewTimer(VDBMBarUI newbar) {
		// TODO ���µļ�ʱ����ӵ�δ�����б�
		for(int i=0;i<listlength;i++){
			if(prelist[i]==null){
				prelist[i]=newbar;
				newbar.setLoc(x_offset, y_offset+i*bar_height);
				return;
			}
		}
		Debug.DebugSimpleMessage("ʹ�õļ�ʱ����������DBM��������Χ");
	}
	protected void addNewActivateTimer(VDBMBarUI newbar){
		// TODO ��ָ����ʱ����ӵ������б�
		for(int i=0;i<listlength;i++){
			if(actlist[i]==null){
				actlist[i]=newbar;
				return;
			}
		}
		Debug.DebugSimpleMessage("ʹ�õļ�ʱ����������DBM��������Χ");
	}
	public void promoteTimerBar(VDBMBarUI target) {
		// TODO ��ָ����ʱ������һ������δ��������Ϊ������ɼ�������Ϊ���٣�����ÿ��target�������һ��
		if(!target.prompted){
			for (int i = 0; i < listlength; i++) {
				if (prelist[i] == target) {
					target.prompted=true;
					target.setActivate(true);
					prelist[i] = null;
					addNewActivateTimer(target);
					sortTimerList(0); // ��Ϊ��ԭ��δ�����б��Ƴ��ˣ������Ҫ����˳��
					return;
				}
			}
		}
	}
	public void removeActivatedTimerBar(VDBMBarUI target){
		// TODO �Ƴ��ѽ����ļ����ʱ��
		if(target.getActivate()){
			for (int i = 0; i < listlength; i++) {
				if (actlist[i] == target) {
					actlist[i] = null;
					sortTimerList(1); // ��Ϊ��ԭ�м����б��Ƴ��ˣ������Ҫ����˳��
					return;
				}
			}
		}
	}
	public void sortTimerList(int index){
		// TODO ������ʱ���б��˳��ʹ���޷죬����0Ϊδ�����б�����1Ϊ�����б�
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
		// TODO ���DBM״̬
		preactbar.clear();
		statecount=0;
	}
	public void updateUI(){
		// TODO ����UI,������δ����ͼ����ʱ���б��е���UI��λ�ƣ����������б��ά��
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
		super.updateUI();		//�ݹ����
	}
	public void drawUI(Graphics g, GamePanel p) {
		super.drawUI(g, p);
	}
}
