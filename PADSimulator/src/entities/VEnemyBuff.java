/**
 * 文件名称：VEnemyBuff.java
 * 类路径：entities
 * 描述：TODO
 * 作者：dell
 * 时间：20162016-3-1上午2:06:10
 * 版本：Ver 1.0
 */
package entities;

import interfaces.VValueInterface;

import org.newdawn.slick.Color;

import global.Imageconst;
import view.VText;
import view.VTexture;

/**
 * @author Demilichzz
 *
 */
public class VEnemyBuff implements VValueInterface{
	public static final int E_BUFF_BUFFTYPE = 1;
	public static final int E_BUFF_BUFFTURN = 2;
	public static final int E_BUFF_BUFFSUBTYPE = 3;
	
	public static final int E_BUFF_TYPE_COLORSHIELD = 1;	//属性盾 固定50%
	public static final int E_BUFF_TYPE_GUTS = 2;	//根性
	public static final int E_BUFF_TYPE_IMMUNITY = 3; //异常盾
	public static final int E_BUFF_TYPE_REDUCE = 4;	//减伤盾
	public static final int E_BUFF_TYPE_ABSORB = 5; //吸收盾
	
	protected int bufftype;
	protected int buffturn;
	protected int buffsubtype;
	protected VTexture buffimage;
	protected VText turntext;
	protected double x, y;
	
	public boolean enable = true;		//根性Buff使用的是否激活参数
	
	public VEnemyBuff(){
	}
	public VEnemyBuff(int type,int turn,int subtype,String image){
		bufftype = type;
		buffturn = turn;
		buffsubtype = subtype;
		buffimage = Imageconst.GetImageByName(image);
		turntext = new VText("0",0,0,Color.white,"font_default",true);
	}
	public void setLoc(double x,double y){
		this.x = x;
		this.y = y;
	}
	public void buffDecay(){
		// TODO Buff衰减处理
		if(buffturn>0){
			buffturn--;
		}
	}
	public void draw(){
		// TODO 绘制Buff
		if (enable) {
			buffimage.directDrawTexture(x, y, -1, 1);
			turntext.setLoc(x + 40, y + 5);
			if (buffturn == -1) {
				turntext.setText("");
			} else {
				turntext.setText("" + buffturn);
			}
			turntext.drawText();
		}
	}
	/* (non-Javadoc)
	 * @see interfaces.VValueInterface#getValue(int)
	 */
	@Override
	public int getValue(int index) {
		// TODO Auto-generated method stub
		switch(index){
		case 1:{
			return bufftype;
		}
		case 2:{
			return buffturn;
		}
		case 3:{
			return buffsubtype;
		}
		default:
			return 0;
		}
	}
	/* (non-Javadoc)
	 * @see interfaces.VValueInterface#setValue(int, int)
	 */
	@Override
	public void setValue(int index, int value) {
		// TODO Auto-generated method stub
		
	}
}
