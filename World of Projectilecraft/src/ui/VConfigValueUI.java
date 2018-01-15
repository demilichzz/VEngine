/**
 * 	TODO 与配置文件关联，并能够通过UI行为修改配置值的选项UI
 */
package ui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;

import jgui.GamePanel;
import interfaces.VInt;
import interfaces.VValueInterface;
import global.Debug;
import global.Global;
import global.Imageconst;
import system.VEngine;
import timer.VLuaAction;
import view.VImageInterface;
import config.ProjectConfig;
import controller.GameListener;

/**
 * @author Demilichzz
 *
 * 2012-11-28
 */
public class VConfigValueUI extends VUI{
	protected VValueInterface bindvalue=null;		//绑定参数值
	protected VImageInterface image_pointer;		//指针图像
	protected VLuaAction switch_action;	//菜单切换行为
	protected int pt_xmax=0;				//指针的最大图像坐标，在设置指针图像时修改
	protected int index=0;
	protected boolean perodic=false;		//是否允许循环设置值
	protected int valueinc=0;			//每次响应操作的增加值
	protected int[] valuerange;			//值范围
	protected boolean showvalue = true;	//显示值
	protected int[] pointerrange;		//指针坐标范围
	
	public VConfigValueUI(String str,String ID){
		super(str,ID);
	}
	
	public void bindValue(VValueInterface v,int index){
		this.bindvalue = v;
		this.index = index;
	}
	public void setValueIncrease(int inc){
		// TODO 设置增加值
		valueinc = inc;
	}
	public void setPointerImage(String str){
		// TODO 设置指针图像并设置其图像坐标
		image_pointer = Imageconst.GetImageByName(str);
		if(image!=null&&image_pointer!=null){
			pt_xmax = image.getWidth()-image_pointer.getWidth();
		}
	}
	public void setPerodic(boolean b){
		perodic=b;
	}
	public void setShowValue(boolean b){
		showvalue = b;
	}
	public void addSwitchAction(VLuaAction sa) {
		// TODO 添加在菜单选项之间切换时触发的行为
		switch_action=sa;
	}
	public void setPointerRange(int[] pr){
		if(pr.length>1){
			pointerrange = pr;
		}
		else{
			Debug.DebugSimpleMessage("关联的参数值范围设置不正确");
		}
	}
	public void setValueRange(int[] vr){
		if(vr.length>1){
			valuerange=vr;
		}
		else{
			Debug.DebugSimpleMessage("关联的参数值范围设置不正确");
		}
	}
	
	public void setValue(int i){
		// TODO 设置参数值
		int value = bindvalue.getValue(index);		//从绑定的动态值接口获取
		value = value + i*valueinc;
		while(value>valuerange[1]||value<valuerange[0]){
			if (value > valuerange[1]) {
				if (perodic) { // 如可重复
					value = valuerange[0]; // 折回值
				} else { // 否则
					value = valuerange[1]; // 设置为最大值
				}
			} else if (value < valuerange[0]) {
				if (perodic) { // 如可重复
					value = valuerange[1]; // 折回值
				} else { // 否则
					value = valuerange[0]; // 设置为最小值
				}
			}
		}
		bindvalue.setValue(index, value);
	}
	
	public void uiAction(String trigtype) {
		// TODO 执行UI行为，参数为传入的控制行为按键值
		if (enable) {
			if(trigtype.equals("KEY_LEFT")){		//左移
				setValue(-1);		//值减少
				if(switch_action!=null){
					switch_action.action();
				}
			}
			if(trigtype.equals("KEY_RIGHT")){		//右移
				setValue(1);		//值增加
				if(switch_action!=null){
					switch_action.action();
				}
			}
		}
	}
	public void drawUI(Graphics g, GamePanel p) {
		// TODO 绘制UI
		if (visible) {
			Graphics2D g2d = (Graphics2D) g;
			if (image != null) {	//绘制UI图像
				g.drawImage(image.getImage(), (int)getRealX(), (int)getRealY(), p);
			}
			if (ProjectConfig.getConfigValue(ProjectConfig.SHOWUIBORDER) == 1) {
				area.drawMe(g2d, p);	//绘制UI边界线
			}
			int config_value = bindvalue.getValue(index);
			if(image_pointer!=null){
				int realx = (int) getRealX();
				double scale = (double)config_value/valuerange[1];	//获取比例
				int x_offset = (int) (scale*pt_xmax);
				if(pointerrange!=null){
					realx = realx+pointerrange[0];
					x_offset = (int) (scale*(pointerrange[1]-pointerrange[0]));
				}
				g.drawImage(image_pointer.getImage(),realx+x_offset,(int) getRealY(),p);
			}
			if(text!=null){			//绘制UI文字
				text.drawMe(this,g,p);
			}
			if(showvalue){
				g2d.setFont(Global.f_config);
				g2d.setColor(Global.c_config);
				String value = config_value+"";
				g2d.drawString(value,(int)getRealX()+image.getWidth()+10,(int)getRealY()+image.getHeight()/2+10);
			}
			for (VUI child = firstChild; child != null; child = child.nextUI) {
				child.drawUI(g2d, p);	//遍历子UI绘制
			}
		}
	}	
}
