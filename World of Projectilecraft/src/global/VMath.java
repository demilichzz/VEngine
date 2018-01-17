/**
 * �ļ����ƣ�VMath.java
 * ��·����global
 * ������TODO �Զ����һЩ��ѧ����
 * ���ߣ�Demilichzz
 * ʱ�䣺2012-3-15����07:01:50
 * �汾��Ver 1.0
 */
package global;

import java.util.Random;
import interfaces.*;

/**
 * @author Demilichzz
 *
 */
public class VMath {
	private static long randomseed;		//�������
	private static Random random=null;// = new Random(System.currentTimeMillis());
	
	public static void setRandomSeed(long seed){
		// TODO ������Java�����ɵ������������
		randomseed = seed;
		random = new Random(randomseed);
	}
	public static int getBinaryValue(int value,int digit){
		// TODO ��һ��ʮ������ʽ��ֵ�л�ȡ��Ӧλ���Ķ�����ֵ��Ӧ����ʹ��һ��intֵ��ʾ���booleanֵ�ĸ���״̬
		//		digit��0��ʾ���������λ��ʼ
		int result = 0;
		result = value%(int)Math.pow(2,digit+1);	//����ֵ��2^λ��+1���࣬��ȡ��Ӧλ����֮���ֵ
		result = result/(int)Math.pow(2, digit);
		return result;
	}
	public static float convertVolumeValue(double bvalue,int cvalue){
		// TODO ����������ֵ����������ֵ���Ӻ�ת��Ϊ
		double leftsize = bvalue+80;
		double rightsize = 0;
		double resultvolume=bvalue;
		if(bvalue<1){
			rightsize = 1-bvalue;
		}
		double cv = cvalue-50;
		if(cv>=0){
			resultvolume = bvalue+rightsize*cv/50;
		}
		else if(cv==-50){
			resultvolume=-80;
		}
		else{
			resultvolume = bvalue+leftsize*cv/50;
		}
		return (float) resultvolume;
	}
	public static double GetDistanceBetween2Points(double x, double y, double c_x, double c_y) {
		// TODO ��ȡ������
		return Math.sqrt(Math.pow(c_x-x,2)+Math.pow(c_y-y,2));
	}
	public static double GetDistanceBetween2Points(VPointInterface a,VPointInterface b){
		double d;
		d = GetDistanceBetween2Points(a.GetX(),a.GetY(),b.GetX(),b.GetY());
		return d;
	}
	public static double GetSimpleMaxDistance(VPointInterface a,VPointInterface b){
		// TODO ��ȡ������x,y�����ϵ������룬����ײ���ʱ��ʹ�ô˺����Լ��ټ�����
		double d1,d2;
		d1=Math.abs(a.GetX()-b.GetX());
		d2=Math.abs(a.GetY()-b.GetY());
		if(d1<d2){
			return d2;
		}
		else{
			return d1;
		}
	}
	public static double GetDistanceBetweenPointAndLine(VPointInterface p,VPointInterface pa,VPointInterface pb){
		// TODO ��ȡ�㵽���㶨���ֱ�ߵľ���
		double result=0;
		if(pa.GetX()==pb.GetX()&&pa.GetY()==pb.GetY()){
			return GetDistanceBetween2Points(p,pa);
		}
		else{
			double pangle = GetAngleBetween2Points(pa,pb);
			double d = GetDistanceBetween2Points(pa,p);
			double a = GetAngleBetween2Points(pa,p);
			if(StandardizationAngle(a-pangle)>=0&&StandardizationAngle(a-pangle)<Math.PI){
				result=d*Math.sin(a-pangle);
			}
			else{
				result=-d*Math.sin(a-pangle);
			}
		}
		return result;
	}
	public static double GetDistanceBetweenPointAndLine(VPointInterface p,VPointInterface pa,double angle){
		// TODO ��ȡ��p����pa�ͽǶ�angle�����ֱ�ߵľ���
		double result=0;
		if(p.GetX()==pa.GetX()&&p.GetY()==pa.GetY()){
			result=0;
		}
		else{			//���¹�����paΪԭ�㣬ֱ��Ϊx�������ϵ
			double newa = StandardizationAngle(GetAngleBetween2Points(pa,p)-angle);	//����������ϵ�е�p��Ƕ�	
			double dist = GetDistanceBetween2Points(pa,p);		//�������
			result=dist*Math.abs(Math.sin(newa));
		}
		return result;
	}
	public static double GetAngleBetween2Points(double x,double y,double x_tar,double y_tar){
		double a;
		// TODO ��ȡ�����Ƕ�
		//---------�����ƶ��Ƕ�----------
		if(GetDistanceBetween2Points(x, y, x_tar, y_tar)==0){
			a=0;
		}
		else{
			a = Math.asin((y_tar-y)/GetDistanceBetween2Points(x, y, x_tar, y_tar));
			if(x_tar==x){
				if(y_tar>y){
					a = Math.PI*0.5;
				}
				else{
					a = Math.PI*1.5;
				}
			}
			else if(x_tar<x){
				a = Math.PI-a;
			}
		}
		if(y_tar==y){
			if(x_tar<x){
				a = Math.PI;
			}
			else{
				a = 0;
			}
		}
		a=StandardizationAngle(a);
		return a;
	}
	public static double GetAngleBetween2Points(VPointInterface source,VPointInterface target){
		double a;
		a = GetAngleBetween2Points(source.GetX(),source.GetY(),target.GetX(),target.GetY());
		return a;
	}
	public static VPointInterface PolarMove(VPointInterface p,double angle,double d){
		// TODO ����ĳ����ָ������;���λ�ƺ��Ŀ���λ��
		VPointInterface ptar = new VPointProxy(p.GetX(),p.GetY());
		ptar.PolarMove(angle, d);
		return ptar;
	}
	public static double StandardizationAngle(double angle){
		// TODO ��׼���Ƕ�,ʹangleת��Ϊȡֵ��Χ[0,2PI)�ĽǶ�
		while(angle<0||angle>=Math.PI*2){
			if(angle<0){
				angle = angle + Math.PI*2;
			}
			else if(angle>=Math.PI*2){
				angle = angle - Math.PI*2;
			}
		}
		return angle;
	}
	public static int GetRandomInt(int low, int high) {
		// TODO ����low��high��������������
		return random.nextInt(high - low + 1) + low; // Random�����next��������ֵΪ0��n���Ұ뿪����
	}
	public static double GetRandomDouble(double low, double high) {
		return random.nextDouble() * (high - low) + low;
	}
	public static int[] GetRandomInts(int low,int high,int number) {
		// TODO ��ȡlow��high�����N����ͬ���������ɵ�����
		if(number>=(high-low+1)){
			number = high-low+1;
			Debug.DebugSimpleMessage("����:��ȡ��������������������С");
		}
		int[] result = new int[number];
		int r = 0;
		int mark = 0;		//���ֵȡ��0�ı��
		for(int i=0;i<number;i++){
			do{
				r = GetRandomInt(low,high);
				if(r==0){
					mark++;
				}
			}
			while(isInArray(r,result)&&mark!=1);	//rֵ���ڶ����л��ǵ�һ��ȡ0,�����ѭ��
			result[i]=r;
		}
		return result;
	}

	public static boolean isInArray(int r, int[] result) {
		// TODO ��ȡ�����Ƿ���ĳ������
		if(result!=null){
			for(int i=0;i<result.length;i++){
				if(result[i]==r){
					return true;
				}
			}
		}
		return false;
	}
}
