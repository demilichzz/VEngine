/**
 * 	宝石对象类，包含宝石的类型，combo状态，检查状态，强化状态等参数
 */
package entities;

import view.VTexture;
import data.StaticData;
import global.Imageconst;
import interfaces.VValueInterface;

/**
 * @author Demilichzz
 *
 */
public class VOrb implements VValueInterface{
	//---------参数表----------------------
	public static final int ORB_TYPE = 0;
	public static final int ORB_STATE = 1;
	public static final int ORB_CHECK = 2;
	public static final int ORB_LOCK = 3;
	public static final int ORB_PLUS = 4;
	
	protected int[]paramlist = new int[5];
	
	public VOrb(){
		this.setValue(ORB_TYPE,0);
	}
	public VOrb(int type){
		this.setValue(ORB_TYPE,type);
	}
	public VOrb createCopy(){
		VOrb neworb = new VOrb();
		for(int i=0;i<paramlist.length;i++){
			neworb.setValue(i, this.getValue(i));
		}
		return neworb;
	}
	/* (non-Javadoc)
	 * @see interfaces.VValueInterface#getValue(int)
	 */
	@Override
	public int getValue(int index) {
		// TODO Auto-generated method stub
		return paramlist[index];
	}
	/* (non-Javadoc)
	 * @see interfaces.VValueInterface#setValue(int, int)
	 */
	@Override
	public void setValue(int index, int value) {
		// TODO Auto-generated method stub
		paramlist[index] = value;
	}
	public VTexture getImage(){
		VTexture t = Imageconst.GetImageByName(StaticData.gempiclist[this.getValue(ORB_TYPE)-1]);
		return t;
	}
}
