/**
 * 文件名称：VSkillHintUI.java
 * 类路径：ui
 * 描述：TODO	显示敌人技能名的UI
 * 作者：dell
 * 时间：20152015-9-27上午11:33:31
 * 版本：Ver 1.0
 */
package ui;

import org.newdawn.slick.Color;

import view.VText;

/**
 * @author Demilichzz
 *
 */
public class VSkillHintUI extends VUI{
	protected int expireTime=0;
	
	public VSkillHintUI(int width, int height, String ID) {
		super(width, height, ID);
	}
	public VSkillHintUI(String str,String ID){
		super(str,ID);
	}
	public void updateUI(){
		if (expireTime > 0) {
			expireTime--;
			this.setTransparency(3.0*(double)expireTime/100);
			if(expireTime==0){
				
			}
		}
		super.updateUI();
	}
	public void setTransparencyCount(int i) {
		// TODO 设置死亡或出现时渐隐计数，根据怪物生存状态判断
		expireTime = i;
	}
	/**
	 * @param hint
	 */
	public void addText(String hint) {
		// TODO Auto-generated method stub
		if(textlist.get(0)==null){
			VText text = new VText(hint);
			text.setColor(Color.white);
			text.setFont("font_dm1");
			text.setLayout(VText.Layout_CENTER);
			text.setLoc(200,20);
			this.addText(text);
		}
		else{
			textlist.get(0).setText(hint);
		}
	}
	public void drawUI() {
		// TODO 绘制UI
		super.drawUI();
	}
	protected void drawUIImage(){
		if (image != null) {	//绘制UI图像
			image.directDrawTexture(getRealX(), getRealY(), -1,scale,uicolor);
			//float transp = (float) (2*Math.abs(0.5-(double)expireTime%25/25));
			//image.directDrawColor(getRealX(), getRealY(), -1,scale,new Color(1.0f,1.0f,1.0f,transp));
		}
		else{
			//Imageconst.nullpic.directDrawTexture(getRealX(), getRealY(), -1,scale);
		}
	}
}
