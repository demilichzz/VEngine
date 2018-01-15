/**
 * 
 */
package ui;

import org.lwjgl.opengl.GL11;

import data.StaticData;
import system.VEngine;
import view.VText;
import entities.VCharacter;
import entities.VEnemy;

/**
 * @author Demilichzz
 *
 */
public class VCharacterUI extends VMouseActionUI{
	protected VCharacter vchar;
	
	public VCharacterUI(int width, int height, String ID) {
		super(width, height, ID);
		allowdrag = false;
		initDamageText();
	}
	public VCharacterUI(String str,String ID){
		super(str,ID);
		allowdrag = false;
		initDamageText();
	}
	public void initDamageText(){
		VText dmtext1 = new VText("0");
		dmtext1.setFont("font_dm1");
		dmtext1.setLayout(VText.Layout_CENTER);
		dmtext1.setLoc(49, 34);
		dmtext1.setColor(255,255,255);
		this.addText(dmtext1,0);
		VText dmtext2 = new VText("0");
		dmtext2.setFont("font_dm2");
		dmtext2.setLayout(VText.Layout_CENTER);
		dmtext2.setLoc(49, 59);
		dmtext2.setColor(255,255,255);
		this.addText(dmtext2,1);
		VText cdtext = new VText("0");
		cdtext.setFont("font_default");
		cdtext.setLayout(VText.Layout_CENTER);
		cdtext.setLoc(49, 90);
		cdtext.setColor(200,200,255);
		this.addText(cdtext,2);
	}
	/**
	 * @param vEnemy
	 */
	public void bindEntity(VCharacter vchar) {
		// TODO Auto-generated method stub
		this.vchar = vchar;
		if(vchar!=null){
			textlist.get(0).setColor(StaticData.colorlist[vchar.getIntValue(VCharacter.PARAM_COLOR1)-1]);
			int color2 = vchar.getIntValue(VCharacter.PARAM_COLOR2)-1;
			if(color2==-1){
				textlist.get(1).setVisible(false);
			}
			else{
				textlist.get(1).setVisible(true);
				textlist.get(1).setColor(StaticData.colorlist[color2]);
			}
		}
	}
	
	public void drawUI() {
		// TODO 绘制UI
		if (visible) {
			drawUIImage();
			if(vchar!=null){
				vchar.getImage().directDrawTexture(getRealX(), getRealY(), -1,scale,uicolor);
			}
			if(vchar.getCoolDown()==0){			
				GL11.glBindTexture(GL11.GL_TEXTURE_2D,0);		//重置绑定的纹理
				GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK ,GL11.GL_FILL);
				float trans = ((float)Math.abs(VEngine.newgame.gs.runTime%200-100))/200.0f;
				GL11.glColor4f(1.0f,1.0f,1.0f,trans);
				GL11.glBegin(GL11.GL_QUADS);
				GL11.glVertex2d(area.x,area.y);	//左上
				GL11.glVertex2d(area.w,area.y);	//右上
				GL11.glVertex2d(area.w,area.h);	//右下
				GL11.glVertex2d(area.x,area.h);	//左下
				GL11.glEnd();
			}
			drawUIText();
			drawUIBorder();		
			drawSubUI();
		}
	}
	public void drawUIText(){
		if (textlist.size()>0){
			int damage1 = vchar.getDamageValue(0);
			textlist.get(0).setText(""+damage1);
			if(damage1>0){
				textlist.get(0).drawText();
			}
			int damage2 = vchar.getDamageValue(1);
			textlist.get(1).setText(""+damage2);
			if(damage2>0){
				textlist.get(1).drawText();
			}
			textlist.get(2).setText(""+vchar.getCoolDown());
			textlist.get(2).drawText();
		}
	}
	public void uiAction(String trigtype) {
		// TODO 参数为调用者类别字符串
		if (enable) {
			if(trigtype.equals("MouseRelease")){
				//System.out.print("press");
				if (vchar!= null) {
					vchar.activeSkillAction();
				}
			}
			if(trigtype.equals("MouseMove")){
				
			}
		}
	}
}
