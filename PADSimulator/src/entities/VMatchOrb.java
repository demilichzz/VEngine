/**
 * 	在进行消除的过程中，消除的每一串宝石对象
 */
package entities;

import ui.VOrbUI;
import event.GlobalEvent;
import interfaces.VValueInterface;

/**
 * @author Demilichzz
 *
 */
public class VMatchOrb implements VValueInterface{
	//---------参数表----------------------
	public static final int ORB_TYPE = 0;	//类型
	public static final int ORB_LINE = 1;	//是否为横行
	public static final int ORB_DOUBLE = 2;	//是否为2体（四珠）
	public static final int ORB_FIVE = 3;	//是否为5珠
	public static final int ORB_CROSS = 4;	//是否为十字型
	public static final int ORB_PLUSSUM = 5;	//加珠数量
	public static final int ORB_ORBSUM = 6;	//总数
	
	protected int[]paramlist = new int[7];
	protected double[]coords = new double[2];
	
	public VMatchOrb(){
		
	}
	public VMatchOrb(int type){
		this.setValue(ORB_TYPE, type);
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
	public void printOrb(){
		// TODO 测试用，输出消除串信息
		for(int i=0;i<7;i++){
			System.out.print(paramlist[i]+",");
		}
		System.out.println();
	}
	public double[] getCoords(){
		// TODO 获取宝石平均坐标
		return coords;
	}
	/**
	 * @param coords
	 */
	public void setCoords(double[] c) {
		// TODO 使用传入的网格坐标计算平均坐标
		c[0] = VOrbUI.orbwidth*(c[0]+0.5);
		c[1] = VOrbUI.orbwidth*(c[1]+0.5);
		this.coords = c;
	}
}
