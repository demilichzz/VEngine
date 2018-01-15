/**
 * 文件名称：VMath.java
 * 类路径：global
 * 描述：TODO 自定义的一些数学函数
 * 作者：Demilichzz
 * 时间：2012-3-15上午07:01:50
 * 版本：Ver 1.0
 */
package global;

import java.util.Random;
import interfaces.*;

/**
 * @author Demilichzz
 *
 */
public class VMath {
	private static long randomseed;		//随机种子
	private static Random random=null;// = new Random(System.currentTimeMillis());
	
	public static void setRandomSeed(long seed){
		// TODO 设置在Java中生成的随机数的种子
		randomseed = seed;
		random = new Random(randomseed);
	}
	public static int getBinaryValue(int value,int digit){
		// TODO 从一个十进制形式的值中获取对应位数的二进制值，应用于使用一个int值表示多个boolean值的复合状态
		//		digit从0表示二进制最低位开始
		int result = 0;
		result = value%(int)Math.pow(2,digit+1);	//计算值对2^位数+1求余，获取对应位数及之后的值
		result = result/(int)Math.pow(2, digit);
		return result;
	}
	public static float convertVolumeValue(double bvalue,int cvalue){
		// TODO 将基础音量值和设置音量值叠加后转换为
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
		// TODO 获取两点间距
		return Math.sqrt(Math.pow(c_x-x,2)+Math.pow(c_y-y,2));
	}
	public static double GetDistanceBetween2Points(VPointInterface a,VPointInterface b){
		double d;
		d = GetDistanceBetween2Points(a.GetX(),a.GetY(),b.GetX(),b.GetY());
		return d;
	}
	public static double GetSimpleMaxDistance(VPointInterface a,VPointInterface b){
		// TODO 获取两点在x,y两轴上的最大距离，在碰撞检测时先使用此函数以减少计算量
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
		// TODO 获取点到两点定义的直线的距离
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
		// TODO 获取点p到点pa和角度angle定义的直线的距离
		double result=0;
		if(p.GetX()==pa.GetX()&&p.GetY()==pa.GetY()){
			result=0;
		}
		else{			//重新构建以pa为原点，直线为x轴的坐标系
			double newa = StandardizationAngle(GetAngleBetween2Points(pa,p)-angle);	//计算新坐标系中的p点角度	
			double dist = GetDistanceBetween2Points(pa,p);		//计算距离
			result=dist*Math.abs(Math.sin(newa));
		}
		return result;
	}
	public static double GetAngleBetween2Points(double x,double y,double x_tar,double y_tar){
		double a;
		// TODO 获取两点间角度
		//---------计算移动角度----------
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
		// TODO 计算某点向指定方向和距离位移后的目标点位置
		VPointInterface ptar = new VPointProxy(p.GetX(),p.GetY());
		ptar.PolarMove(angle, d);
		return ptar;
	}
	public static double StandardizationAngle(double angle){
		// TODO 标准化角度,使angle转化为取值范围[0,2PI)的角度
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
		// TODO 返回low到high闭区间的随机整数
		return random.nextInt(high - low + 1) + low; // Random对象的next函数返回值为0到n的右半开区间
	}
	public static double GetRandomDouble(double low, double high) {
		return random.nextDouble() * (high - low) + low;
	}
	public static int[] GetRandomInts(int low,int high,int number) {
		// TODO 获取low到high区间的N个不同随机整数组成的数组
		if(number>=(high-low+1)){
			number = high-low+1;
			Debug.DebugSimpleMessage("错误:获取的随机数数量大于区间大小");
		}
		int[] result = new int[number];
		int r = 0;
		int mark = 0;		//随机值取过0的标记
		for(int i=0;i<number;i++){
			do{
				r = GetRandomInt(low,high);
				if(r==0){
					mark++;
				}
			}
			while(isInArray(r,result)&&mark!=1);	//r值不在队列中或是第一次取0,则结束循环
			result[i]=r;
		}
		return result;
	}

	public static boolean isInArray(int r, int[] result) {
		// TODO 获取整数是否在某数组中
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
