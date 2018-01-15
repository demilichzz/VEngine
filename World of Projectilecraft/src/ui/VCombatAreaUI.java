/**
 * �ļ����ƣ�VCombatAreaUI.java
 * ��·����ui
 * ������TODO ս������UI�����ڶ�����Ϸ��ս������Χ������ս����λ����Ⱦ
 * ���ߣ�Demilichzz
 * ʱ�䣺2012-8-6����11:21:02
 * �汾��Ver 1.0
 */
package ui;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import view.*;
import jgui.*;
import config.*;
import data.*;
import entities.*;
import global.*;

/**
 * @author Demilichzz
 *
 */
public class VCombatAreaUI extends VUI{
	protected AffineTransform t = new AffineTransform();	//��ת����
	protected boolean showOutScreenSprite=false;		//�Ƿ���ʾ������Ϸ�������ӣ�������
	
	public VCombatAreaUI(String str,String id){
		super(str,id);
	}
	
	public void drawUI(Graphics g, GamePanel p) {
		//--------����ʱ�俪ʼ-------------
		//Debug.DebugTestTimeStart();
		// TODO ����UI	
		if (visible) {
			Graphics2D g2d = (Graphics2D) g;
			if (image != null) { //���Ʊ���
				g.drawImage(image.getImage(), (int) getRealX(),
						(int) getRealY(), p);
			}
			if (ProjectConfig.getConfigValue(ProjectConfig.SHOWUIBORDER) == 1) {
				area.drawMe(g2d, p); //���Ʊ߽���
			}
			if (text != null) { //��������
				text.drawMe(this, g, p);
			}
			//	����ս����λ
			if (GameData.boss != null) {
				if (GameData.boss.alive) {
					GameData.boss.drawMe(g2d, p, this);
				}
			}
			if (GameData.unitdrawlist != null) {
				for (VCombatUnit u : GameData.unitdrawlist) {
					if (u.alive && u.getImageByLayer(0) != null) {
						u.drawMe(g2d, p, this);
						// int x = (int)
						// (u.GetX()-u.getImageByLayer(0).getWidth()/2+getRealX());
						// int y = (int)
						// (u.GetY()-u.getImageByLayer(0).getHeight()/2+getRealY());
						// g.drawImage(u.getImageByLayer(0).getImage(),x,y,p);
					}
				}
			}
			// ������ҵ�Ļ
			for (VSprite sp : GameData.drawList) { //����������ӵĶ���ͼ��
				if (sp.born && sp.alive && sp.getOwner() == GameData.pc
						&& (sp.getImageByLayer(0) != null)
						|| sp.getCType() == 1) {
					//�����ѳ���&&���&&�����߲������ &&�����Ӷ���ͼ�����||��������Ϊ���⣩�������
					if (showOutScreenSprite) {
						sp.drawImage(g2d, p, 0, this);
					} else {
						if (sp.getBound()) {
							sp.drawImage(g2d, p, 0, this);
						}
					}
				}
			}
			// �������
			VPlayer u = GameData.pc;
			u.drawMe(g2d, p, this);
			// ���Ƶл�����
			for (VSprite sp : GameData.drawList) { //�����������ӵĵײ�ͼ��
				if (sp.born && sp.alive && sp.getOwner() != GameData.pc
						&& (sp.getImageByLayer(1) != null)
						|| sp.getCType() == 1) {
					//�����ѳ���&&���&&�����߲������ &&�����ӵײ�ͼ�����||��������Ϊ���⣩�������
					if (showOutScreenSprite) {
						sp.drawImage(g2d, p, 1, this);
					} else {
						if (sp.getBound()) {
							sp.drawImage(g2d, p, 1, this);
						}
					}
				}
			}
			for (VSprite sp : GameData.drawList) { //�����������ӵĶ���ͼ��
				if (sp.born && sp.alive && sp.getOwner() != GameData.pc
						&& (sp.getImageByLayer(0) != null)
						|| sp.getCType() == 1) {
					//�����ѳ���&&���&&�����߲������ &&�����Ӷ���ͼ�����||��������Ϊ���⣩�������
					if (showOutScreenSprite) {
						sp.drawImage(g2d, p, 0, this);
					} else {
						if (sp.getBound()) {
							sp.drawImage(g2d, p, 0, this);
						}
					}
				}
			}
			//
			for (VUI child = firstChild; child != null; child = child.nextUI) {
				child.drawUI(g2d, p);
			}
		}
		//Debug.DebugTestTimeEnd("", true);
		//-------����ʱ�����-----------
		
	}
}
