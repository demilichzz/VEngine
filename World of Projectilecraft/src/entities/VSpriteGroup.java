/**
 * 	TODO 弹幕粒子组，用于在Lua脚本中对多个需要以同样形式改变参数的粒子进行操作，以减少单位时间内Lua调用Java函数的次数
 */
package entities;

import interfaces.VPointInterface;
import interfaces.VPointProxy;

import java.util.*;
import struct.*;
import global.*;
/**
 * @author Demilichzz
 *
 */
public class VSpriteGroup {
	public ArrayList<VSprite> splist;	//粒子组包含的的粒子列表
	
	public VSpriteGroup(){
		splist = new ArrayList<VSprite>();
	}
	public void addSprite(VSprite sp){
		if(sp!=null){
			splist.add(sp);
		}
		else{
			Debug.DebugSimpleMessage("试图向粒子包中添加一个非法粒子");
		}
	}
	public ValueList getAngle(){
		// TODO 获取角度值列表
		ValueList anglelist = null;	//使用值列表数据结构
		if(splist!=null){
			anglelist=new ValueList(splist.size());
			for(int i=0;i<splist.size();i++){
				anglelist.setValue(i,splist.get(i).getAngle());	//遍历获取角度
			}
		}
		return anglelist;
	}
	public void setAngle(ValueList anglelist){
		// TODO 设置角度
		if(splist!=null&&splist.size()==anglelist.size()){
			for(int i=0;i<splist.size();i++){
				splist.get(i).setAngle(anglelist.getValue(i));	//遍历设置角度
			}
		}
	}
	public ValueList getASpeed(){
		// TODO 获取角速度值列表
		ValueList aslist = null;	//使用值列表数据结构
		if(splist!=null){
			aslist=new ValueList(splist.size());
			for(int i=0;i<splist.size();i++){
				aslist.setValue(i,splist.get(i).getASpeed());	//遍历获取角速度
			}
		}
		return aslist;
	}
	public void setASpeed(ValueList aslist){
		// TODO 设置角速度
		if(splist!=null&&splist.size()==aslist.size()){
			for(int i=0;i<splist.size();i++){
				splist.get(i).setASpeed(aslist.getValue(i));	//遍历设置角速度
			}
		}
	}
	public ValueList getSpeed(){
		// TODO 获取速度值列表
		ValueList vlist = null;
		if(splist!=null){
			vlist=new ValueList(splist.size());
			for(int i=0;i<splist.size();i++){
				vlist.setValue(i,splist.get(i).getSpeed());	//遍历获取速度
			}
		}
		return vlist;
	}
	public void setSpeed(ValueList vlist){
		// TODO 设置速度
		if(splist!=null&&splist.size()==vlist.size()){
			for(int i=0;i<splist.size();i++){
				splist.get(i).setSpeed(vlist.getValue(i));	//遍历设置速度
			}
		}
	}
	public ValueList getLocX(){
		// TODO 获取x坐标列表
		ValueList vlist = null;
		if(splist!=null){
			vlist=new ValueList(splist.size());
			for(int i=0;i<splist.size();i++){
				vlist.setValue(i,splist.get(i).GetX());
			}
		}
		return vlist;
	}
	public ValueList getLocY(){
		// TODO 获取y坐标列表
		ValueList vlist = null;
		if(splist!=null){
			vlist=new ValueList(splist.size());
			for(int i=0;i<splist.size();i++){
				vlist.setValue(i,splist.get(i).GetY());
			}
		}
		return vlist;
	}
	public void setLoc(ValueList x,ValueList y){
		// TODO 设置xy坐标
		if(splist!=null&&splist.size()==x.size()&&splist.size()==y.size()){
			for(int i=0;i<splist.size();i++){
				splist.get(i).setCor(x.getValue(i),y.getValue(i));	//遍历设置速度
			}
		}
	}
	public void die(){
		// TODO 使组中所有粒子死亡
		if(splist!=null){
			for(int i=0;i<splist.size();i++){
				splist.get(i).die();	//
			}
		}
	}
	public void PolarCoorTrans(double x,double y,double angle){
		// TODO 基于原点x,y和角度angle进行极坐标变换
		if(splist!=null){
			for(int i=0;i<splist.size();i++){
				VSprite sp = splist.get(i);	//
				VPointProxy center=new VPointProxy(x,y);	//建立原点对象
				double a=VMath.GetAngleBetween2Points(center, sp);
				double dist=VMath.GetDistanceBetween2Points(center, sp);
				VPointInterface p_tar=VMath.PolarMove(center, a+angle, dist);
				sp.setCor(p_tar.GetX(), p_tar.GetY());
				sp.setAngle(a);
			}
		}
	}
}
