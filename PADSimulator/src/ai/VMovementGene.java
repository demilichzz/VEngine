/**
 * 	移动过程数据基因组，在遗传算法AI中进行运算
 */
package ai;

import java.util.ArrayList;

/**
 * @author Demilichzz
 *
 */
public class VMovementGene {
	public boolean[] movementgene;
	public int maxlength;	//基因最高位数
	public int fitlength;	//得到最好结果的位数
	public int startorbx = -1;	//转珠坐标
	public int startorby = -1;
	
	public VMovementGene(){
		
	}
	public VMovementGene(ArrayList<Integer> movement){
		movementgene = converseMovement(movement);
		//print();
	}
	public VMovementGene createCopy(){
		// TODO 生成一个复制的基因组数据
		VMovementGene copygene = new VMovementGene();
		for(int i=0;i<movementgene.length;i++){
			copygene.movementgene[i] = movementgene[i];
		}
		copygene.maxlength = maxlength;
		copygene.fitlength = fitlength;
		copygene.startorbx = -1;
		copygene.startorby = -1;
		return copygene;
	}
	public void print(){
		int time = 0;
		for(int i=0;i<movementgene.length;i++){
			if(movementgene[i]){
				System.out.print(1);
			}
			else{
				System.out.print(0);
			}
			time++;
			if(time==4){
				time=0;
				System.out.print(",");
			}
		}
	}
	public static boolean[] converseMovement(ArrayList<Integer> movement){
		// TODO 将AI计算中使用的移动列表转化为可进行遗传运算的二进制数组
		int i=movement.size();
		boolean[] binmovement;
		if(movement.get(0)==5){	//确认起始移动
			i = i-1;
			binmovement = new boolean[i*2];
			int index = 0;
			for(int move:movement){
				if(move!=5){
					int encode = move/2-1;		//将方向数据的2/4/6/8转化为0/1/2/3，再转化为二进制数据
					boolean bin1;
					if(encode/2==0){
						bin1 = false;
					}
					else{
						bin1 = true;
					}
					boolean bin2;
					if(encode%2==0){
						bin2 = false;
					}
					else{
						bin2 = true;
					}
					binmovement[index]=bin1;
					binmovement[index+1]=bin2;
					index = index+2;
				}
			}
			return binmovement;
		}
		else{
			return null;
		}
	}
}
