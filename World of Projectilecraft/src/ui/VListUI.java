/**
 * 	TODO �б�UI��Ӧ����ѡ��¼���ļ�����
 */
package ui;

import java.awt.Color;
import java.util.ArrayList;

import system.VEngine;
import controller.GameListener;

import entities.VPlayer;
import event.GlobalEvent;
import global.Global;

/**
 * @author Demilichzz
 *
 * 2012-11-9
 */
public class VListUI extends VUI{
	protected ArrayList<String> strlist;		//UI�������ַ����б�
	protected int page=1;		//�б���ʾ��ҳ��
	protected int index=0;		//�б���ͣ������Ŀ����
	protected VUI[]items = new VUI[20];			//�б���UI
	protected VUI listcursor;
	protected int x_offset=-30,y_offset=-5;		//���ƫ��
	
	public VListUI(){
		super();
		strlist = new ArrayList<String>();
		initSubUI();
	}
	public VListUI(String str,String id){
		super(str,id);
		strlist = new ArrayList<String>();
		initSubUI();
	}
	public VListUI(int w, int h, String str) {
		// TODO Auto-generated constructor stub
		super(w,h,str);
		strlist = new ArrayList<String>();
		initSubUI();
	}
	public void setVisible(boolean b){
		super.setVisible(b);
		page = 1;
		index = 0;
		listcursor.setLoc(items[index].GetX()+x_offset, items[index].GetY()+y_offset);	//���ù��λ��
	}
	public void initSubUI(){
		// TODO ��ʼ���б��е���Ŀ��UI���������ǵĸ�UI����Ϊ�б���
		for(int i=0;i<20;i++){
			items[i]=new VUI("ph_block.png",uiID+"_listitem_"+i);
			items[i].setParent(this);
			items[i].setLoc(50, 20+i*28);
		}
		listcursor=new VUI("UI_cursor.png",uiID+"_cursor");		//�б���
		listcursor.setParent(this);
		listcursor.setLoc(items[index].GetX()+x_offset, items[index].GetY()+y_offset);
	}
	public void setStringList(ArrayList<String> str){
		// TODO �����ַ����б�
		if(str!=null){
			strlist=str;
		}
		else{
			strlist.clear();
		}
		page=1;
		index=0;
	}
	public void updateUI(){
		// TODO �����б���UI
		for(int i=0;i<20;i++){
		    if((page-1)*20+i<strlist.size()){
		    	String str = strlist.get((page-1)*20+i);
		    	items[i].addText(str,30,10,Global.f,Global.c);		//�����б�������
		    }
		    else{
		    	items[i].addText("",30,10,Global.f,Global.c);
		    }
		    items[i].text.setStroke(false, new Color(0,0,0));
		}
		//setVisible(this.visible);	//��Ϊ�����������UI�����½����Ķ���,���Ҫ���䰴��ǰUI�Ŀ���������
		super.updateUI();
	}
	public void moveCursor(int value){
		index=index+value;
		if(index>19){
			index=index-20;
		}
		else if(index<0){
			index=index+20;
		}
		listcursor.setLoc(items[index].GetX()+x_offset, items[index].GetY()+y_offset);	//���ù��λ��
	}
	public void page(int i){
		// TODO ��ҳ,iΪ����ҳ������
		page = page + i;
		adjustPage();
	}
	public void setPage(int i){
		page = i;
		adjustPage();
	}
	private void adjustPage(){
		// TODO ����ҳ��ʹ��Ϸ�
		if(page<1||strlist.size()<=20){
			page = 1;
		}
		else if((page-1)*20>=strlist.size()){	//����
			page = (strlist.size()-1)/20+1;
		}
		// ���ݵ������ҳ�����¸���UI
		updateUI();
	}
	public void uiKeyAction(int[] keystate) {
		// TODO ����UI������Ϊ��ʵ�ֹ��λ�ƹ���
		if(keystate[GameListener.KEY_LEFT]==1){	
			this.page(-1);
			VEngine.glistener.resetKeystate(GameListener.KEY_LEFT);
		}
		if(keystate[GameListener.KEY_RIGHT]==1){	
			this.page(1);
			VEngine.glistener.resetKeystate(GameListener.KEY_RIGHT);
		}
		if(keystate[GameListener.KEY_DOWN]==1){	
			this.moveCursor(1);
			VEngine.glistener.resetKeystate(GameListener.KEY_DOWN);
		}
		if(keystate[GameListener.KEY_UP]==1){	
			this.moveCursor(-1);
			VEngine.glistener.resetKeystate(GameListener.KEY_UP);
		}
		if(keystate[GameListener.KEY_ESC]==1){	//ESC��ִ��ȡ����Ϊ
			VEngine.gs.uiparent.getUIByID("ui_bg").setActionVisible(true);
			VEngine.gs.uiparent.getUIByID("ui_replaylist").setVisible(false);
			VEngine.glistener.resetKeystate(GameListener.KEY_ESC);
		}
		if(keystate[GameListener.KEY_ENTER]==1){	//Enter
			if(items[index].getText()!=""&&items[index].getText()!=null){			
				GlobalEvent.loadReplay("data/Replay/"+items[index].getText());
			}
			VEngine.glistener.resetKeystate(GameListener.KEY_ENTER);
		}
		// �ݹ����UIִ�м�����Ϊ
		super.uiKeyAction(keystate);
	}
}
