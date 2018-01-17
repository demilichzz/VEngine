/**
 * �ļ����ƣ�VUI.java
 * ��·����ui
 * ������TODO UI���࣬��Ҫ����Ϊ�û�����Ļ��Ʋ��֣�������Ӧͨ����������ʵ��
 * ���ߣ�Demilichzz
 * ʱ�䣺2011-11-4����01:56:14
 * �汾��Ver 1.0
 */
package ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;

import config.ProjectConfig;

import entities.*;
import jgui.*;
import system.*;
import timer.VLuaAction;
import global.*;
import view.*;

/**
 * @author Demilichzz
 * 
 */
public class VUI implements VActionUI {
	// -----------UI���ͳ���------------------------
	public static final int UITYPE_DEFAULT = 0; // Ĭ��UI
	public static final int UITYPE_MOUSE = 1; // ��������UI
	public static final int MENU_RESPONSE_LR = 0;	//�˵���ӦģʽΪ���Ҽ�
	public static final int MENU_RESPONSE_UD = 1;	//�˵���ӦģʽΪ���¼�
	public static final int MENU_RESPONSE_LRUD = 2;	//�˵���ӦģʽΪ�������Ҽ�
	// -----------�������-------------------------------
	public VImageInterface image; // UIͼ��
	// protected String imagestr; // UIͼ����Imageconst�ж�Ӧ�ַ���,��ͼ���ļ���
	// ���Ѳ���Ҫ�˱���,ֱ�ӵ���image.toString();
	protected double x, y; // UI���Ͻ�������ڸ�UI�����ƫ��
	protected VUIText text;

	public boolean visible; // ������
	public boolean enable; // �ɽ�����
	public String uiID; // ��UIManager�е�����
	public VArea area; // UI����,������ʾUI�߿򼰴�������¼�
	protected boolean autoupdate=true;		//�Ƿ��Զ�Ӧ��updateUI��������
	// ------------Lua�ű���Ϊ------------------------------
	protected VLuaAction action;			//������Ϊ�ű���
	protected VLuaAction visibleaction;		//���ӻ������ű�,�ڴ�UIsetVisbleʱ����
	// ------------UIʮ������ṹ----------------------------
	public VUI parent; // ��UI������
	public VUI firstChild; // ��ײ���UI,����UI����ʼ��
	public VUI nextUI; // �ϲ�UI,��ͬ��UI������һ��
	public VUI previousUI; // �²�UI,��ͬ��UI������һ��

	public VUI() {

	}

	public VUI(int width, int height, String ID) {
		// TODO ͨ����߶����ƶ���С��ͼ����ԴΪ�յ�UI
		image = null;
		x = 0;
		y = 0;
		visible = true;
		enable = true;
		uiID = ID;
		area = new VArea(0, 0, width, height);
		area.boldborder = false;
	}

	public VUI(String str, String ID) {
		// TODO ͨ��ͼ���ļ�������UI�Ĺ��캯��
		image = Imageconst.GetImageByName(str);
		x = 0;
		y = 0;
		visible = true;
		enable = true;
		uiID = ID;
		area = new VArea(0, 0, image.getWidth(), image.getHeight());
		area.boldborder = false;
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		// TODO ���ÿɼ���ʱ,�ɽ���������ͬ��,���ݹ�������UI
		this.visible = visible;
		this.enable = visible;
		for (VUI child = firstChild; child != null; child = child.nextUI) {
			child.setVisible(visible);
		}
	}

	public boolean isEnable() {
		return enable;
	}

	public void setEnable(boolean enable) {
		// TODO �ݹ�������UI
		this.enable = enable;
		for (VUI child = firstChild; child != null; child = child.nextUI) {
			child.setEnable(enable);
		}
	}

	public void setParent(VUI p) {
		// TODO ����UI�ĸ�UI,ͬʱ������븸UI����UI����ĩβ
		this.removeUIRelation();	//�Ƚ�UI��������νṹ���Ƴ�
		boolean b = false;
		if(p.getUIByID(uiID)!=null){
			b=true;
		}
		if (p != null) {
			parent = p;
			VUI child; // ��ʱUI childΪ���ڵ�ĵ�һ���ӽڵ�
			if (parent.firstChild != null) {
				for (child = parent.firstChild; child.nextUI != null; child = child.nextUI)
					;// ͨ��child�������ȡ���һ���ڵ�
				child.nextUI = this; // �踸�ڵ��������ĩβΪ��ǰ�ڵ�
				this.previousUI = child;
			} 
			else { // ��UI���ӽڵ�
				parent.firstChild = this;
			}
			this.setLoc(x, y);	//��UI�ı�ʱ�谴��ǰƫ��ֵ����area�ľ���λ��
		}
		if(b){
			p.getUIByID(uiID).removeUIRelation();//����Ѵ���ͬ������ɾ��ԭ��UI
		}
	}

	public void setParentByID(String ID) {
		// TODO ͨ��uiID���ø�UI
		setParent(VEngine.gs.uiparent.getUIByID(ID));
	}

	/*
	 * public void swapUI(VUI ui_a, VUI ui_b) { // TODO ����ĳ����UI����UI����������UI��˳�� if
	 * (ui_a.parent != ui_b.parent) {
	 * Debug.DebugSimpleMessage("Ҫ������UI�ĸ�UI��ͬ,�޷�����"); } else { VUI temp_a_p =
	 * ui_a.previousUI; VUI temp_a_n = ui_a.nextUI; VUI temp_b_p =
	 * ui_b.previousUI; VUI temp_b_n = ui_b.nextUI; if (temp_a_p == null) { //
	 * ui_a��firstChild ui_a.parent.firstChild = ui_b; } else if (temp_b_p ==
	 * null) { ui_b.parent.firstChild = ui_a; } ui_a.previousUI = temp_b_p;
	 * ui_a.nextUI = temp_b_n; ui_b.previousUI = temp_a_p; ui_b.nextUI =
	 * temp_a_n; if (ui_a.nextUI == ui_a || ui_b.previousUI == ui_b) {
	 * ui_a.nextUI = ui_b; ui_b.previousUI = ui_a; } else if (ui_a.previousUI ==
	 * ui_a || ui_b.nextUI == ui_b) { ui_a.previousUI = ui_b; ui_b.nextUI =
	 * ui_a; } } }
	 */
	public void insertUI(VUI tarui, int i) {
		// TODO ����ǰUI���뵽Ŀ��UIͬ����ǰ/��(��ʾ��/�ϲ�)
		if (i > 0) { // ����ǰ��
			this.nextUI = tarui;
			this.previousUI = tarui.previousUI;
			if (tarui.previousUI != null) {
				tarui.previousUI.nextUI = this;
			}
			else{
				tarui.parent.firstChild = this;
			}
			tarui.previousUI = this;
			this.parent = tarui.parent;
		} 
		else if (i < 0) { // �����
			this.previousUI = tarui;
			this.nextUI = tarui.nextUI;
			if (tarui.nextUI != null) {
				tarui.nextUI.previousUI = this;
			}
			tarui.nextUI = this;
			this.parent = tarui.parent;
		}
	}

	public void removeUIRelation() {
		// TODO ��UI��UIʮ������ṹ��ϵ�Ƴ�,����ɾ��UI����
		if (previousUI != null) {
			previousUI.nextUI = nextUI;
		}
		else {
			if(parent!=null){
				parent.firstChild = nextUI;
			}
		}
		if (nextUI != null) {
			nextUI.previousUI = previousUI;
		}
		previousUI = null;
		nextUI = null;
		parent = null;
	}

	public void sortUI(int i) {
		// TODO ΪUI������������˳��,iΪ�����ϸ�,Ϊ�����³�
		if (i > 0) {
			if (this.nextUI != null) {
				VUI t = this.nextUI;
				this.removeUIRelation();
				this.insertUI(t, -1);
			}
		} else if (i < 0) {
			if (this.previousUI != null) {
				VUI t = this.previousUI;
				this.removeUIRelation();
				this.insertUI(t, 1);
			}
		}
	}

	public boolean getPointState(int x, int y) {
		// TODO ��ȡĿ�������Ƿ��ڴ�UI��Χ��
		return area.ifvalid(x, y);
	}

	public VUI getUIByID(String ID) {
		// TODO ͨ��ID���ҵ�ǰUI����UI
		VUI temp = null;
		if (this.uiID.equals(ID)) {
			return this;
		}
		for (VUI child = firstChild; child != null; child = child.nextUI) {
			if (child.uiID.equals(ID)) {
				temp = child;
				break;
			} 
			else {
				temp = child.getUIByID(ID);
				if (temp != null) {
					break;
				}
			}
		}
		return temp;
	}

	public void setID(String ID) {
		// TODO ����uiID,����Ӱ������ṹ,ע��!!��Ҫ������UI��ΪͬһID
		this.uiID = ID;
	}

	public void setLoc(double x, double y) {
		// TODO ����UI����,��UI��������иĶ�ʱ,������area�ľ������겢�ݹ�������UI
		this.x = x;
		this.y = y;
		area.setLoc(getRealX(), getRealY());
		for (VUI child = firstChild; child != null; child = child.nextUI) {
			child.setLoc(child.GetX(),child.GetY());	//������UI�������������
		}
		if(text!=null){
			if(text.xmax!=0){
				text.setTypesetting(this);
			}
		}
	}
	public void setRealLoc(double x,double y){
		// TODO ����UI��������,��ת��Ϊ�������洢
		if(parent!=null){
			this.x = x - parent.getRealX();
			this.y = y - parent.getRealY();
		}
		else{
			this.x = x;
			this.y = y;
		}
		area.setLoc(x, y);
		for (VUI child = firstChild; child != null; child = child.nextUI) {
			child.setLoc(child.GetX(),child.GetY());	//������UI�������������
		}
	}

	public void moveLoc(double x, double y) {
		// TODO �ƶ�UI����
		setLoc(this.x + x, this.y + y);
		area.setLoc(getRealX() + x, getRealY() + y);
	}

	public double GetX() {
		// TODO ��ȡx����ƫ��
		return x;
	}

	public double GetY() {
		// TODO ��ȡY����ƫ��
		return y;
	}
	public double getRealX(){
		// TODO ��ȡʵ��x����,����Ļ��λ����
		if(parent!=null){
			return parent.getRealX()+x;
		}
		else{
			return x;
		}
	}
	public double getRealY(){
		if(parent!=null){
			return parent.getRealY()+y;
		}
		else{
			return y;
		}
	}

	public void setUIState(boolean b) {
		// TODO ����UI�Ƿ����,ͬʱ���ÿɼ���
		enable = b;
		visible = b;
	}

	public void setImage(String ad) {
		// TODO ����UIͼ����������
		image = Imageconst.GetImageByName(ad);
		area = new VArea(0, 0, image.getWidth(), image.getHeight());
		area.boldborder = false;
		area.setLoc(getRealX(), getRealY());
	}

	public void drawUI(Graphics g, GamePanel p) {
		// TODO ����UI
		if (visible) {
			Graphics2D g2d = (Graphics2D) g;
			if (image != null) {	//����UIͼ��
				g.drawImage(image.getImage(), (int)getRealX(), (int)getRealY(), p);
			}
			if (ProjectConfig.getConfigValue(ProjectConfig.SHOWUIBORDER) == 1) {
				area.drawMe(g2d, p);	//����UI�߽���
			}
			if(text!=null){			//����UI����
				text.drawMe(this,g,p);
			}
			for (VUI child = firstChild; child != null; child = child.nextUI) {
				child.drawUI(g2d, p);	//������UI����
			}
		}
	}

	public void addAction(VLuaAction action) {
		// TODO ΪUI������Lua�ű���ʵ�ֵ���Ϊ
		this.action = action;
	}

	public void removeAction(VLuaAction action) {
		// TODO �Ƴ�UI��������Ϊ
		this.action = null;
	}
	public void addVisibleAction(VLuaAction action){
		// TODO �������ӻ�ͬʱ�����Ľű���Ϊ
		this.visibleaction = action;
	}
	public void uiAction(String trigtype) {
		// TODO ����Ϊ����������ַ���
		if (enable) {
			if (!trigtype.equals("MouseEvent")) {
				if (action != null) {
					action.action();
				}
			}
		}
	}
	public void setActionVisible(boolean b){
		// TODO ����UIΪ�ɼ���ͬʱ,ִ�й����Ŀ��ӻ������ű���Ϊ
		if(b){
			this.setVisible(true);
			if(visibleaction!=null){
				visibleaction.action();
			}
		}
		else{
			this.setVisible(false);
		}
	}

	public VUI getMAUIbyLoc(int x, int y) {
		// TODO ͨ��������ȡ���ϲ�UI������VMouseActionUI������Ч
		VUI temp = null;
		if (this.getPointState(x, y)) {
			temp = this;
			if (!this.enable) {	//����UI�����ã��������±���������null
				temp = null;
			}
			/*if (!(this instanceof VMouseActionUI)|| !enable) {
				temp = null;
			}*/
		}
		for (VUI child = firstChild; child != null; child = child.nextUI) {
			if (child.getMAUIbyLoc(x, y) != null) {
				temp = child.getMAUIbyLoc(x, y);
			}
		}
		return temp;
	}
	public VUI getUIbyLoc(int x,int y){
		// TODO ͨ��������ȡ���ϲ��������Ч��UI
		VUI temp = null;
		if (this.getPointState(x, y)) {
			temp = this;
			if (!enable) {
				temp = null;
			}
		}
		for (VUI child = firstChild; child != null; child = child.nextUI) {
			if (child.getUIbyLoc(x, y) != null) {
				temp = child.getUIbyLoc(x, y);
			}
		}
		return temp;
	}

	public void addText(String text, int x, int y, Font f, Color c) {
		// TODO ΪUI���ָ����ʽ������
		VUIText t = new VUIText(text);
		t.setOffset(x, y);
		t.setStyle(f, c);
		this.text = t;
		//this.text.setTypesetting(this);
		this.text.setParent(this);
	}
	public void addText(String textid){
		// TODO ͨ��������ȡ�����ָ������
		this.text = Textconst.GetTextByName(textid);
		this.text.setTypesetting(this);
		this.text.setParent(this);
	}
	public String getText(){
		// TODO ��ȡUI��������Ϣ����������ʽ
		return text.text;
	}
	public void updateUI(){
		// TODO ����UI��ʾ����,��ÿ��GS����ʱ���ñ�֤��ʾ��ʵ������ͬ��,�ڼ̳�VUI����Ҫ������������
		// �ݹ������UI
		for (VUI child = firstChild; child != null; child = child.nextUI) {
			if(child.autoupdate){
				child.updateUI();
			}
		}
	}

	/**
	 * @param keystate
	 */
	public void uiKeyAction(int[] keystate) {
		// TODO ��GS�л�ȡ����״̬�����ݹ�ִ��UI����ļ�����Ϊ
		// �ݹ����UIִ�м�����Ϊ
		for (VUI child = firstChild; child != null; child = child.nextUI) {
			if(child.enable){
				child.uiKeyAction(keystate);
			}
		}
	}
}
