/**
 * 	TODO 在粒子包中使用的参数值列表结构，提供一些运算函数以便使用
 */
package struct;

import global.VMath;

/**
 * @author Demilichzz
 *
 * 2012-10-15
 */
public class ValueList {
	public double[] valuelist=null;	//值列表
	
	public ValueList(int length){
		valuelist=new double[length];
	}

	/**
	 * @param i
	 * @param angle
	 */
	public void setValue(int i, double value) {
		// TODO 设置值
		valuelist[i]=value;
	}
	public double getValue(int i){
		// TODO 获取值
		return valuelist[i];
	}
	public int size(){
		return valuelist.length;
	}
	public void addValue(double v){
		// TODO 对值列表中所有值增加
		if(valuelist!=null){
			for(int i=0;i<valuelist.length;i++){
				valuelist[i]=valuelist[i]+v;
			}
		}
	}
	public void multiplyValue(double v){
		// TODO 对值列表中所有值相乘
		if(valuelist!=null){
			for(int i=0;i<valuelist.length;i++){
				valuelist[i]=valuelist[i]*v;
			}
		}
	}
	public void randomizeValue(double min,double max){
		// TODO 随机化值列表中的所有元素
		if(valuelist!=null){
			for(int i=0;i<valuelist.length;i++){
				valuelist[i]=VMath.GetRandomDouble(min, max);
			}
		}
	}
	public void coverValue(double v){
		// TODO 将值列表中所有值覆盖为新值
		if(valuelist!=null){
			for(int i=0;i<valuelist.length;i++){
				valuelist[i]=v;
			}
		}
	}
}
