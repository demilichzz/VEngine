/**
 * 	TODO �����Ӱ���ʹ�õĲ���ֵ�б�ṹ���ṩһЩ���㺯���Ա�ʹ��
 */
package struct;

import global.VMath;

/**
 * @author Demilichzz
 *
 * 2012-10-15
 */
public class ValueList {
	public double[] valuelist=null;	//ֵ�б�
	
	public ValueList(int length){
		valuelist=new double[length];
	}

	/**
	 * @param i
	 * @param angle
	 */
	public void setValue(int i, double value) {
		// TODO ����ֵ
		valuelist[i]=value;
	}
	public double getValue(int i){
		// TODO ��ȡֵ
		return valuelist[i];
	}
	public int size(){
		return valuelist.length;
	}
	public void addValue(double v){
		// TODO ��ֵ�б�������ֵ����
		if(valuelist!=null){
			for(int i=0;i<valuelist.length;i++){
				valuelist[i]=valuelist[i]+v;
			}
		}
	}
	public void multiplyValue(double v){
		// TODO ��ֵ�б�������ֵ���
		if(valuelist!=null){
			for(int i=0;i<valuelist.length;i++){
				valuelist[i]=valuelist[i]*v;
			}
		}
	}
	public void randomizeValue(double min,double max){
		// TODO �����ֵ�б��е�����Ԫ��
		if(valuelist!=null){
			for(int i=0;i<valuelist.length;i++){
				valuelist[i]=VMath.GetRandomDouble(min, max);
			}
		}
	}
	public void coverValue(double v){
		// TODO ��ֵ�б�������ֵ����Ϊ��ֵ
		if(valuelist!=null){
			for(int i=0;i<valuelist.length;i++){
				valuelist[i]=v;
			}
		}
	}
}
