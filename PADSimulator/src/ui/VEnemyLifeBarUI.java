/**
  * 	敌人血条UI，继承动态条UI
 */
package ui;

import data.StaticData;
import entities.VEnemy;
import entities.VParty;
import global.Imageconst;
import org.newdawn.slick.Color;
/**
 * @author Demilichzz
 *
 */
public class VEnemyLifeBarUI extends VDynamicBarUI{
	protected VEnemy enemy;
	
	public VEnemyLifeBarUI(String str,String coverstr,String scover, String ID) {
		super(str, coverstr,scover,ID);
	}
	
	public static VEnemyLifeBarUI createUI(int index){
		// TODO 创建指定UI索引的UI
		VEnemyLifeBarUI elbui = new VEnemyLifeBarUI("enemylifebar_background.png","enemylifebar_cover.png","enemylifebar_staticcover.png","ui_enemylifebar_"+index);
		return elbui;
	}
	protected void drawUIImage(){
		if (image != null) {	//绘制UI图像
			if (image != null) {
				image.directDrawTexture(getRealX(), getRealY(), -1,scale,uicolor);
			}
			if(coverimage!=null&&v!=null){
				double percent = (double)v.getValue(index)/(double)xmax;
				double d = dw*percent;	//计算当前动态条长度
				Color c_main = StaticData.colorlist[enemy.getValue(VEnemy.ENEMY_MAINTYPE)-1];
				Color c_second = StaticData.colorlist[enemy.getValue(VEnemy.ENEMY_SECONDTYPE)-1];
				if(percent>0.5){
					coverimage.drawTexturePart(getRealX(), getRealY(), getRealX()+dw*0.5*scale, getRealY()+(area.h-area.y)*scale, (float)0.0,(float)0.0,(float)0.5, (float)1.0, scale,c_main);
					coverimage.drawTexturePart(getRealX()+dw*0.5*scale, getRealY(), getRealX()+dw*percent*scale, getRealY()+(area.h-area.y)*scale, (float)(1.5-percent),(float)0.0,(float)1.0, (float)1.0, scale,c_second);
				}
				else{
					coverimage.drawTexturePart(getRealX(), getRealY(), getRealX()+d*scale, getRealY()+(area.h-area.y)*scale, (float)(1-percent),(float)0.0,(float)1.0, (float)1.0, scale,c_main);
				}
				//coverimage.drawTexturePart(getRealX(), getRealY(), getRealX()+d, getRealY()+area.h-area.y, (float)(1-percent),(float)0.0,(float)1.0, (float)1.0, scale,uicolor);
			}
			if(staticcover!=null){
				staticcover.directDrawTexture(getRealX(), getRealY(), -1,scale,uicolor);
			}
			if(ifdisplayvalue&&v!=null){		//是否显示关联数值
				this.textlist.get(0).setText(v.getValue(VParty.PARTY_CURRENTHP)+"/"+v.getValue(VParty.PARTY_MAXHP));
			}
		}
		else{
			//Imageconst.nullpic.directDrawTexture(getRealX(), getRealY(), -1,scale);
		}
	}

	/**
	 * @param enemy
	 */
	public void bindEntity(VEnemy enemy) {
		// TODO 绑定敌人对象
		this.enemy = enemy;
		this.v = enemy;
		if(v!=null){
			this.setMaxValue(v.getValue(VEnemy.ENEMY_MAXHP));
			this.index = VEnemy.ENEMY_CURRENTHP;
		}
	}
}
