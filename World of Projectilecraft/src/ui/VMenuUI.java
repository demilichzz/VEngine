/**
 * 	TODO ���в˵�UI�࣬�̳л���UI�ಢʵ�����л����ĵ��в˵���������
 */
package ui;

import global.Debug;
import global.Imageconst;

import java.awt.Graphics;
import java.util.ArrayList;

import jgui.*;
import controller.*;
import system.*;
import timer.*;
/**
 * @author Demilichzz
 *
 * 2012-11-16
 */
public class VMenuUI extends VUI{
	protected ArrayList<VUI> uilist;	//��UI�б�
	protected VLuaAction cancel_action;	//�˵�ȡ����Ϊ
	protected VLuaAction switch_action;	//�˵��л���Ϊ
	protected int bindindex=0;	//��ǰUI����
	protected boolean ifreset=false;	//��ʾ�˵�ʱ�Ƿ����ù��
	protected boolean moveblock = false;	//�˵���ǰ״̬�Ƿ��ֹ����ƶ������ڿ��ƹ���ٶ�
	protected int responsemode = VUI.MENU_RESPONSE_LR;	//������Ӧģʽ��Ĭ��Ϊ���Ҽ�
	protected VUI cursor = null;		//���UI
	protected int x_offset=0,y_offset=0;//���ƫ��
	
	
	public VMenuUI(){
		super();
		uilist = new ArrayList<VUI>();
	}
	public VMenuUI(String str,String id){
		super(str,id);
		uilist = new ArrayList<VUI>();
	}
	public VMenuUI(int w, int h, String str) {
		// TODO Auto-generated constructor stub
		super(w,h,str);
		uilist = new ArrayList<VUI>();
	}
	public void setCursor(String image){
		// TODO ���ò˵����
		if(Imageconst.GetImageByName(image)!=null){
			cursor = new VUI(image,uiID+"_cursor");
			cursor.setParent(this);
		}
		else{
			cursor = null;
		}
	}
	public void setVisible(boolean v){
		super.setVisible(v);
		if(v){
			moveblock = false;
			if(ifreset){		//�����ʾʱ���ù��
				resetCursor();
			}
		}
	}
	public void setIfResetCursor(boolean b){
		ifreset = b;
	}
	public void setOffset(int x,int y){
		x_offset = x;
		y_offset = y;
		if(uilist.size()>0){
			VUI cuui = uilist.get(bindindex);
			cursor.setLoc(cuui.GetX()+x_offset, cuui.GetY()+y_offset);
		}
	}
	public int getIndex(){
		// TODO ��ȡ��ǰ��UI����
		return bindindex;
	}
	public void setIndex(int i){
		// TODO ��������
		bindindex = i;
		if(uilist.size()>0){
			VUI cuui = uilist.get(bindindex);
			cursor.setLoc(cuui.GetX()+x_offset, cuui.GetY()+y_offset);
		}
	}
	public void addCancelAction(VLuaAction ca){
		// TODO ��Ӳ˵�ȡ��ʱ����Ϊ
		cancel_action=ca;
	}
	public void addSwitchAction(VLuaAction sa) {
		// TODO ����ڲ˵�ѡ��֮���л�ʱ��������Ϊ
		switch_action=sa;
	}
	public void bindUI(VUI ui){
		// TODO ��Ӱ�UI
		if(ui!=null){
			uilist.add(ui);
			ui.setParent(this);		//��UIʱ���丸UI����Ϊ�˵�UI���Ӷ���draw�����п����Ƿ���ʾ
		}
		if(uilist.size()>0&&cursor!=null){	//���ù��
			VUI firstUI = uilist.get(0);
			cursor.setLoc(firstUI.GetX()+x_offset, firstUI.GetY()+y_offset);
		}
		bindindex=0;
	}
	public void unbindUI(VUI ui){
		// TODO �Ƴ���UI�������Ƴ���UI����δʹ��UI��
		if(uilist.contains(ui)){
			uilist.remove(ui);
			ui.setParent(VEngine.gs.uiparent.getUIByID("ui_unused"));
		}
		else{
			Debug.DebugSimpleMessage("δ������UI");
		}
		if(uilist.size()>0&&cursor!=null){	//���ù��
			VUI firstUI = uilist.get(0);
			cursor.setLoc(firstUI.GetX()+x_offset, firstUI.GetY()+y_offset);
		}
	}
	public void clearBind(){
		// TODO ������а�UI���ڲ˵����ز�ͬ�˵���ʱ������ٰ���Ҫ�Ĳ˵���UI
		for(VUI ui:uilist){
			ui.setParent(VEngine.gs.uiparent.getUIByID("ui_unused"));
		}
		uilist.clear();
	}
	public void setResponseMode(int i){
		// TODO ���ð�����Ӧģʽ
		responsemode = i;
	}
	public void moveCursor(int i){
		// TODO �ƶ����λ�ã�ͬʱ�����л���Ϊ
		if (!moveblock&&uilist.size()>0) { // �ǽ�ֹ�ƶ�״̬
			if (i == 1) { // ǰ��
				if (uilist.size() <= bindindex + 1) {
					bindindex = 0;
				} else {
					bindindex++;
				}
			} else if (i == -1) { // ����
				if (bindindex - 1 < 0) {
					bindindex = uilist.size() - 1;
				} else {
					bindindex--;
				}
			}
			if (switch_action != null) {	//ִ���л���Ϊ
				switch_action.action();
			}
			if(cursor!=null){	//�ƶ����λ��
				cursor.setLoc(uilist.get(bindindex).GetX()+x_offset,uilist.get(bindindex).GetY()+y_offset);
			}
		}
		//resetUILoc();
	}
	public void resetCursor(){
		// TODO ���ù��
		bindindex=0;
		if(cursor!=null){	//�ƶ����λ��
			cursor.setLoc(uilist.get(bindindex).GetX()+x_offset,uilist.get(bindindex).GetY()+y_offset);
		}
	}
	public void lockCursor(int time){
		// TODO �ڹ��λ�ƺ����������UI����Ϊ���������ӳ�
		moveblock = true;
		VTimer tm = new VTimer(time,time,false,new VLuaAction(){
			public void action() {
				// TODO Auto-generated method stub
				moveblock=false;
			}
		});
		tm.timerStart(0);
	}
	public void uiAction(String trigtype) {
		// TODO ִ��UI��Ϊ������Ϊ����������ַ���
		if (enable) {
			if(uilist.size()>0){
				VUI currentUI = uilist.get(bindindex);
				if(currentUI!=null){
					currentUI.uiAction(trigtype);
				}
			}
		}
	}
	public void uiKeyAction(int[] keystate) {
		// TODO ����UI������Ϊ��ʵ�ֹ��λ�ƹ���
		if(keystate[GameListener.KEY_UP]==1){	//UP
			if(!moveblock&&(responsemode==VUI.MENU_RESPONSE_UD||responsemode==VUI.MENU_RESPONSE_LRUD)){
				this.moveCursor(-1);
				lockCursor(200);
			}
		}
		if(keystate[GameListener.KEY_DOWN]==1){	//DOWN
			if(!moveblock&&(responsemode==VUI.MENU_RESPONSE_UD||responsemode==VUI.MENU_RESPONSE_LRUD)){
				this.moveCursor(1);
				lockCursor(200);
			}
		}
		if(keystate[GameListener.KEY_RIGHT]==1){	//RIGHT
			if(!moveblock&&(responsemode==VUI.MENU_RESPONSE_LR||responsemode==VUI.MENU_RESPONSE_LRUD)){
				this.moveCursor(1);
				lockCursor(200);
			}
		}
		if(keystate[GameListener.KEY_LEFT]==1){	//LEFT
			if(!moveblock&&(responsemode==VUI.MENU_RESPONSE_LR||responsemode==VUI.MENU_RESPONSE_LRUD)){
				this.moveCursor(-1);
				lockCursor(200);
			}
		}
		if(keystate[GameListener.KEY_ESC]==1){	//ESC��ִ��ȡ����Ϊ
			if(cancel_action!=null){
				cancel_action.action();
				VEngine.glistener.resetKeystate(GameListener.KEY_ESC);
			}
		}
		if(keystate[GameListener.KEY_ENTER]==1){	//Enter
			this.uiAction("Cursor");
			VEngine.glistener.resetKeystate(GameListener.KEY_ENTER);
		}
		// �ݹ����UIִ�м�����Ϊ
		super.uiKeyAction(keystate);
	}
	public void drawUI(Graphics g, GamePanel p) {
		// TODO ���Ʋ˵�UI�����ݲ������ư��б�UI����ʾ���
		super.drawUI(g, p);
	}
}
