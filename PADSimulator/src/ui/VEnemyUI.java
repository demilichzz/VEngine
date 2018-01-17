/**
 * 
 */
package ui;

import org.newdawn.slick.Color;

import data.GameData;
import view.VText;
import entities.VEnemy;
import entities.VEnemyBuff;
import entities.VParty;

/**
 * @author Demilichzz
 *
 */
public class VEnemyUI extends VMouseActionUI{
	protected VEnemy enemy;
	protected VEnemyLifeBarUI enemylb_ui;
	protected int expireTime=0;
	protected VText moveturn;
	
	public VEnemyUI(int width, int height, String ID) {
		super(width, height, ID);
		allowdrag = false;
	}
	public VEnemyUI(String str,String ID){
		super(str,ID);
		allowdrag = false;
	}
	/**
	 * @param vEnemy
	 */
	public static VEnemyUI createUI(int index){
		VEnemyUI ui = new VEnemyUI(0,0,"ui_enemy_"+index);
		ui.enemylb_ui = VEnemyLifeBarUI.createUI(index);
		ui.enemylb_ui.setLoc(0,220);
		ui.enemylb_ui.setScale(0.8);
		ui.enemylb_ui.setParent(ui);
		VText turntext = new VText("あと");
		turntext.setLoc(0, 20);
		turntext.setColor(Color.red);
		ui.moveturn = new VText("0");
		ui.moveturn.setLoc(80, 20);
		ui.moveturn.setColor(Color.red);
		ui.addText(turntext,0);
		ui.addText(ui.moveturn,1);
		
		//ui.enemylb_ui.bindEntity(ui.enemy);
		return ui;
	}
	public void bindEntity(VEnemy enemy) {
		// TODO Auto-generated method stub
		if(enemy!=null){
			enemy.bindUI(this);
		}
		this.enemy = enemy;
		this.enemylb_ui.bindEntity(enemy);
		if(enemy!=null){
			this.setAreaSize(enemy.getImage().getWidth(), enemy.getImage().getHeight());
		}
	}
	public void clearText(){
		super.clearText();
	}
	public void drawUI() {
		// TODO 绘制UI
		if (visible) {
			drawUIImage();
			if(enemy!=null){
				enemy.getImage().directDrawTexture(getRealX(), getRealY(), -1,scale,uicolor);
			}
			for(VEnemyBuff b:enemy.bufflist){		//根据Buff类型划分Buff位置并绘制
				b.draw();
			}
			moveturn.setText(""+enemy.getValue(VEnemy.ENEMY_CURRENTTURN));
			drawUIText();
			drawUIBorder();
			drawSubUI();
		}
	}
	public void drawUIImage(){
		
	}
	public void updateUI(){
		// TODO 更新UI显示内容,在每次GS更新时调用保证显示与实际数据同步,在继承VUI的需要的子类中重载
		//		并用于实现子UI中的UI效果
		// 递归更新子UI
		if (enemy != null) {
			if (expireTime > 0) {
				expireTime--;
				if(expireTime==0){
					//GameData.instance.checkBattleEnd();
				}
			}
			if (enemy.getAlive()) {
				this.setTransparency(1.0 * (double) (30 - expireTime) / 30);
			} 
			else {
				this.setTransparency(1.0 * (double) expireTime / 30);
			}
		}
		super.updateUI();
	}
	/**
	 * @param i
	 */
	public void setTransparencyCount(int i) {
		// TODO 设置死亡或出现时渐隐计数，根据怪物生存状态判断
		expireTime = i;
	}
}
