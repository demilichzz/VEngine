/**
 * 文件名称：VPartyLifeBarUI.java
 * 类路径：ui
 * 描述：TODO  队伍血条UI
 * 作者：dell
 * 时间：20152015-9-15下午8:05:22
 * 版本：Ver 1.0
 */
package ui;

import interfaces.VValueInterface;
import entities.VParty;

/**
 * @author Demilichzz
 *
 */
public class VPartyLifeBarUI extends VDynamicBarUI{
	protected int maxvalueindex;
	protected double currentvalue;		//当前值，在更新UI时控制血条过渡状态等
	protected double transvalue=0;		//过渡值
	protected double targetvalue=0;		//过渡时目标值
	protected int updateCount=0;
	/**
	 * @param string
	 * @param string2
	 * @param string3
	 * @param string4
	 */
	public VPartyLifeBarUI(String str,String coverstr,String scover, String ID) {
		// TODO Auto-generated constructor stub
		super(str,coverstr,scover,ID);
	}

	public void bindValue(VValueInterface v,int index){
		this.v = v;
		this.index = index;
	}
	public void bindMaxValue(VValueInterface v,int index){
		this.v = v;
		this.maxvalueindex = index;
	}
	public void updateUI(){
		updateCount++;
		if(updateCount%3==0){
			currentvalue=currentvalue-transvalue;
			if(Math.abs(currentvalue-targetvalue)<1){
				currentvalue = targetvalue;
				transvalue=0;
			}
		}
		if(transvalue==0&&currentvalue!=v.getValue(index)){
			targetvalue = v.getValue(index);
			transvalue = (currentvalue-targetvalue)/10;	//控制数值过渡时间,时间为n*20ms
		}
		super.updateUI();
	}
	protected void drawUIImage(){
		if (image != null) {	//绘制UI图像
			if (image != null) {
				image.directDrawTexture(getRealX(), getRealY(), -1,scale,uicolor);
			}
			if(coverimage!=null&&v!=null){
				double percent = (currentvalue/(double)v.getValue(maxvalueindex));
				double d = dw*percent;	//计算当前动态条长度
				if(d<0){
					d=0;
				}
				coverimage.drawTexturePart(getRealX(), getRealY(), getRealX()+d*scale, getRealY()+(area.h-area.y)*scale, (float)(1-percent),(float)0.0,(float)1.0, (float)1.0, scale,uicolor);
			}
			if(staticcover!=null){
				staticcover.directDrawTexture(getRealX(), getRealY(), -1,scale,uicolor);
			}
			if(ifdisplayvalue&&v!=null){		//是否显示关联数值
				this.textlist.get(0).setText((int)currentvalue+"/"+v.getValue(VParty.PARTY_MAXHP));
				int rec = v.getValue(VParty.PARTY_CURRENTRECOVERY);
				if(rec>0){
					this.textlist.get(1).setText("+"+v.getValue(VParty.PARTY_CURRENTRECOVERY));
				}
				else{
					this.textlist.get(1).setText("");
				}
			}
		}
		else{
			//Imageconst.nullpic.directDrawTexture(getRealX(), getRealY(), -1,scale);
		}
	}
}
