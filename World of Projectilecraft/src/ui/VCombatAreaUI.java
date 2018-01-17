/**
 * 文件名称：VCombatAreaUI.java
 * 类路径：ui
 * 描述：TODO 战斗区域UI，用于定义游戏的战斗区域范围并负责战斗单位的渲染
 * 作者：Demilichzz
 * 时间：2012-8-6下午11:21:02
 * 版本：Ver 1.0
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
	protected AffineTransform t = new AffineTransform();	//旋转矩阵
	protected boolean showOutScreenSprite=false;		//是否显示超出游戏区域粒子，调试用
	
	public VCombatAreaUI(String str,String id){
		super(str,id);
	}
	
	public void drawUI(Graphics g, GamePanel p) {
		//--------测试时间开始-------------
		//Debug.DebugTestTimeStart();
		// TODO 绘制UI	
		if (visible) {
			Graphics2D g2d = (Graphics2D) g;
			if (image != null) { //绘制背景
				g.drawImage(image.getImage(), (int) getRealX(),
						(int) getRealY(), p);
			}
			if (ProjectConfig.getConfigValue(ProjectConfig.SHOWUIBORDER) == 1) {
				area.drawMe(g2d, p); //绘制边界线
			}
			if (text != null) { //绘制文字
				text.drawMe(this, g, p);
			}
			//	绘制战斗单位
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
			// 绘制玩家弹幕
			for (VSprite sp : GameData.drawList) { //绘制玩家粒子的顶层图像
				if (sp.born && sp.alive && sp.getOwner() == GameData.pc
						&& (sp.getImageByLayer(0) != null)
						|| sp.getCType() == 1) {
					//满足已出生&&存活&&发射者不是玩家 &&（粒子顶层图像存在||粒子类型为激光），则绘制
					if (showOutScreenSprite) {
						sp.drawImage(g2d, p, 0, this);
					} else {
						if (sp.getBound()) {
							sp.drawImage(g2d, p, 0, this);
						}
					}
				}
			}
			// 绘制玩家
			VPlayer u = GameData.pc;
			u.drawMe(g2d, p, this);
			// 绘制敌机粒子
			for (VSprite sp : GameData.drawList) { //绘制所有粒子的底层图像
				if (sp.born && sp.alive && sp.getOwner() != GameData.pc
						&& (sp.getImageByLayer(1) != null)
						|| sp.getCType() == 1) {
					//满足已出生&&存活&&发射者不是玩家 &&（粒子底层图像存在||粒子类型为激光），则绘制
					if (showOutScreenSprite) {
						sp.drawImage(g2d, p, 1, this);
					} else {
						if (sp.getBound()) {
							sp.drawImage(g2d, p, 1, this);
						}
					}
				}
			}
			for (VSprite sp : GameData.drawList) { //绘制所有粒子的顶层图像
				if (sp.born && sp.alive && sp.getOwner() != GameData.pc
						&& (sp.getImageByLayer(0) != null)
						|| sp.getCType() == 1) {
					//满足已出生&&存活&&发射者不是玩家 &&（粒子顶层图像存在||粒子类型为激光），则绘制
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
		//-------测试时间结束-----------
		
	}
}
