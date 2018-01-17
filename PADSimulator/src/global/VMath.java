/**
 * 文件名称：VMath.java
 * 类路径：global
 * 描述：TODO 自定义的一些数学函数
 * 作者：Demilichzz
 * 时间：2012-3-15上午07:01:50
 * 版本：Ver 1.0
 */
package global;

import java.util.*;

import entities.VOrb;
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
	public static boolean GetRandomBoolean(double d){
		// TODO 根据给定概率值获取随机boolean值
		if(random.nextDouble()<d){
			return true;
		}
		return false;
	}
	public static int GetRandomInt(int low, int high) {
		// TODO 返回low到high闭区间的随机整数
		return random.nextInt(high - low + 1) + low; // Random对象的next函数返回值为0到n的右半开区间
	}
	public static double GetRandomDouble(double low, double high) {
		return random.nextDouble() * (high - low) + low;
	}
	public static int GetRandomIntFromGroup(ArrayList<Integer> group){
		int size = group.size();
		int randomindex = random.nextInt(size);
		return group.get(randomindex);
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
	public static void printMatrix(int[][] matrix){
		for(int i=0;i<matrix.length;i++){
			int[]arr = matrix[i];
			for(int j=0;j<arr.length;j++){
				System.out.print(arr[j]+",");
			}
			System.out.println();
		}
	}
	public static void printMatrix(VValueInterface[][] matrix,int index){
		for(int i=0;i<matrix.length;i++){
			VValueInterface[]arr = matrix[i];
			for(int j=0;j<arr.length;j++){
				System.out.print(arr[j].getValue(index)+",");
			}
			System.out.println();
		}
	}
	public static double restrictDouble(double value,double min,double max){
		// TODO 将数值限定在min到max的范围内
		if(min>max){
			Debug.DebugSimpleMessage("错误：限定范围最小值大于最大值");
			return 0;
		}
		else{
			if(value<min){
				value = min;
			}
			if(value>max){
				value = max;
			}
			return value;
		}
	}
	public static int[][] restrictMatrixValue(int[][] orbstate) {
		// TODO 调整矩阵值，使combo值等差递增
		int y = orbstate.length;		//行
		int x = orbstate[0].length;		//列
		int[][] newstate = new int[y][x];	//建立临时新矩阵数据
		int maxcombo = 0;		
		int mincombo = 1;
		for(int i=0;i<y;i++){
			for(int j=0;j<x;j++){
				if(orbstate[i][j]>maxcombo){
					maxcombo = orbstate[i][j];	//获取最高combo值
				}
				if(orbstate[i][j]<mincombo){
					mincombo = orbstate[i][j];	//获取是否有combo值为0者，即未消去的宝石
				}
			}
		}
		int newcomboindex = mincombo;	//重新计算的矩阵中最小值为获取的最低combo值
		for(int i=0;i<=maxcombo;i++){
			boolean b=false;
			for(int cy=0;cy<y;cy++){
				for(int cx=0;cx<x;cx++){
					if(orbstate[cy][cx]==i){
						newstate[cy][cx]=newcomboindex;
						b = true;
					}
				}
			}
			if(b){
				newcomboindex++;
			}
		}
		int[][] standardstate = new int[y][x];	//建立标准化combo状态矩阵，以将计算过的调整矩阵转化为消除顺序从左下开始的矩阵
		for(int cy=0;cy<y;cy++){
			for(int cx=0;cx<x;cx++){
				standardstate[cy][cx]=-1;		//使用-1填充
			}
		}
		int stdcombovalue = 0;		//标准化矩阵中宝石位置的combo值
		for(int cy=y-1;cy>=0;cy--){
			for(int cx=0;cx<x;cx++){
				if(standardstate[cy][cx]==-1){	//如果标准化矩阵的当前位置并未填充
					if(newstate[cy][cx]==0){	//当前位置combo值为0，则非消除区域，在新矩阵中同样为0
						standardstate[cy][cx]=0;
					}
					else{	//当前位置combo值不为0
						int tempcombo = newstate[cy][cx];	//获取调整矩阵当前combo值
						stdcombovalue++;	//新combo值加1
						for(int ty=y-1;ty>=0;ty--){
							for(int tx=0;tx<x;tx++){	//遍历调整矩阵寻找与当前combo值相同的坐标	
								if(newstate[ty][tx]==tempcombo){	//combo值相同
									standardstate[ty][tx]=stdcombovalue;	//为标准化矩阵赋予新combo值
								}
							}
						}
					}
				}
			}
		}
		return standardstate;
	}
	public static void restrictMatrixValue(VValueInterface[][] matrix,int index){ 
		// TODO 调整矩阵值，使combo值等差递增，调用此函数会修改动态值矩阵中指定索引的值
		int[][] tempmatrix = getMartixValueCopy(matrix,index);
		int[][] valuematrix = restrictMatrixValue(tempmatrix);
		matrixCover(matrix, valuematrix, index, true);
	}
	
	public static int[][] matrixDrop(int[][] orbs) {
		// TODO 使矩阵数值下沉，用于宝珠下落的计算
		int y = orbs.length;		//行
		int x = orbs[0].length;		//列
		int[][] newstate = orbs.clone();//new int[y][x];	//建立临时新矩阵数据
		for(int i=0;i<y;i++){
			newstate = matrixDropOnce(newstate);
		}
		return newstate;
	}
	public static int[][] matrixDropOnce(int[][]orbs){
		// TODO 使矩阵数值下沉一行
		int y = orbs.length;		//行
		int x = orbs[0].length;		//列
		//int[][] newstate = new int[y][x];	//建立临时新矩阵数据
		for(int i=y-1;i>0;i--){
			for(int j=0;j<x;j++){
				if(orbs[i][j]<=0){	//如果当前宝珠为空
					int temp = orbs[i-1][j];
					orbs[i-1][j]=orbs[i][j];
					orbs[i][j] = temp;		//交换当前宝珠与上方宝珠的值
				}
			}
		}
		return orbs;
	}
	public static int[][] matrixFloat(int[][] orbs){
		// TODO 使矩阵数值上浮，用于生成新宝珠的计算
		int y = orbs.length;		//行
		int x = orbs[0].length;		//列
		int[][] newstate = orbs.clone();//new int[y][x];	//建立临时新矩阵数据
		for(int i=0;i<y;i++){
			newstate = matrixFloatOnce(newstate);
		}
		return newstate;
	}
	public static int[][] matrixFloatOnce(int[][]orbs){
		// TODO 使矩阵数值下沉一行
		int y = orbs.length;		//行
		int x = orbs[0].length;		//列
		//int[][] newstate = new int[y][x];	//建立临时新矩阵数据
		for(int i=0;i<y-1;i++){
			for(int j=0;j<x;j++){
				if(orbs[i][j]<=0){	//如果当前宝珠为空
					int temp = orbs[i+1][j];
					orbs[i+1][j]=orbs[i][j];
					orbs[i][j] = temp;		//交换当前宝珠与下方宝珠的值
				}
			}
		}
		return orbs;
	}
	public static VValueInterface[][] matrixDrop(VValueInterface[][] orbs,int index) {
		// TODO 使动态值对象矩阵数值基于某个索引下沉，用于宝珠下落的计算
		int y = orbs.length;		//行
		int x = orbs[0].length;		//列
		VValueInterface[][] newstate = orbs.clone();//new int[y][x];	//建立临时新矩阵数据
		for(int i=0;i<y;i++){
			newstate = matrixDropOnce(newstate,index);
		}
		return newstate;
	}
	public static VValueInterface[][] matrixDropOnce(VValueInterface[][] orbs,int index){
		// TODO 使矩阵数值下沉一行
		int y = orbs.length;		//行
		int x = orbs[0].length;		//列
		//int[][] newstate = new int[y][x];	//建立临时新矩阵数据
		for(int i=y-1;i>0;i--){
			for(int j=0;j<x;j++){
				if(orbs[i][j].getValue(index)<=0){	//如果当前宝珠为空
					VValueInterface temp = orbs[i-1][j];
					orbs[i-1][j]=orbs[i][j];
					orbs[i][j] = temp;		//交换当前宝珠与上方宝珠的值
				}
			}
		}
		return orbs;
	}
	public static VValueInterface[][] matrixFloat(VValueInterface[][] orbs,int index){
		// TODO 使矩阵数值上浮，用于生成新宝珠的计算
		int y = orbs.length;		//行
		int x = orbs[0].length;		//列
		VValueInterface[][] newstate = orbs.clone();//new int[y][x];	//建立临时新矩阵数据
		for(int i=0;i<y;i++){
			newstate = matrixFloatOnce(newstate,index);
		}
		return newstate;
	}
	public static VValueInterface[][] matrixFloatOnce(VValueInterface[][] orbs,int index){
		// TODO 使矩阵数值下沉一行
		int y = orbs.length;		//行
		int x = orbs[0].length;		//列
		//int[][] newstate = new int[y][x];	//建立临时新矩阵数据
		for(int i=0;i<y-1;i++){
			for(int j=0;j<x;j++){
				if(orbs[i][j].getValue(index)<=0){	//如果当前宝珠为空
					VValueInterface temp = orbs[i+1][j];
					orbs[i+1][j]=orbs[i][j];
					orbs[i][j] = temp;		//交换当前宝珠与下方宝珠的值
				}
			}
		}
		return orbs;
	}
	public static int getMatrixMax(int[][]orbs){
		// TODO 获取矩阵中数值最大值
		int y = orbs.length;		//行
		int x = orbs[0].length;		//列
		int maxvalue = 0;
		for(int i=0;i<y;i++){
			for(int j=0;j<x;j++){
				if(orbs[i][j]>maxvalue){
					maxvalue = orbs[i][j];
				}
			}
		}
		return maxvalue;
	}
	public static int getMatrixMax(VValueInterface[][]orbs,int index){
		// TODO 获取矩阵中数值最大值
		int y = orbs.length;		//行
		int x = orbs[0].length;		//列
		int maxvalue = 0;
		for(int i=0;i<y;i++){
			for(int j=0;j<x;j++){
				if(orbs[i][j].getValue(index)>maxvalue){
					maxvalue = orbs[i][j].getValue(index);
				}
			}
		}
		return maxvalue;
	}
	public static void matrixCover(VValueInterface[][]source,int[][]cover,int index,boolean b){
		// TODO 使用值矩阵覆盖动态值对象中的某个类型值，boolean值b表示为覆盖矩阵值为0时是否覆盖
		int y = source.length;		//行
		int x = source[0].length;		//列
		for(int i=0;i<y;i++){
			for(int j=0;j<x;j++){
				if(cover[i][j]!=0||b==true){	//如果覆盖矩阵的该值不为0，则覆盖
					source[i][j].setValue(index, cover[i][j]);
				}
			}
		}
	}
	public static void matrixCover(VValueInterface[][]source,VValueInterface[][]cover,int index,boolean b){
		// TODO 使用动态值矩阵覆盖动态值对象中的某个对象，index为覆盖基于的参数，boolean值b表示为覆盖矩阵值为0时是否覆盖
		int y = source.length;		//行
		int x = source[0].length;		//列
		for(int i=0;i<y;i++){
			for(int j=0;j<x;j++){
				if(cover[i][j].getValue(index)!=0||b==true){	//如果覆盖矩阵的该值不为0，则覆盖
					source[i][j]=cover[i][j];
				}
			}
		}
	}
	public static int[][] getMatrixCopy(int[][] orbs){
		// TODO 获取一个矩阵的复制
		return orbs.clone();
	}
	public static int[][] getMartixValueCopy(VValueInterface[][]orbs,int index){
		// TODO 获取动态值矩阵指定索引的复制矩阵
		int y = orbs.length;
		int x = orbs[0].length;
		int[][] neworbs = new int[y][x];
		for(int i=0;i<y;i++){
			for(int j=0;j<x;j++)
				neworbs[i][j] = orbs[i][j].getValue(index);
		}
		return neworbs;
	}
}