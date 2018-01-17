/**
 * 	TODO ��Ļ�����飬������Lua�ű��жԶ����Ҫ��ͬ����ʽ�ı���������ӽ��в������Լ��ٵ�λʱ����Lua����Java�����Ĵ���
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
	public ArrayList<VSprite> splist;	//����������ĵ������б�
	
	public VSpriteGroup(){
		splist = new ArrayList<VSprite>();
	}
	public void addSprite(VSprite sp){
		if(sp!=null){
			splist.add(sp);
		}
		else{
			Debug.DebugSimpleMessage("��ͼ�����Ӱ������һ���Ƿ�����");
		}
	}
	public ValueList getAngle(){
		// TODO ��ȡ�Ƕ�ֵ�б�
		ValueList anglelist = null;	//ʹ��ֵ�б����ݽṹ
		if(splist!=null){
			anglelist=new ValueList(splist.size());
			for(int i=0;i<splist.size();i++){
				anglelist.setValue(i,splist.get(i).getAngle());	//������ȡ�Ƕ�
			}
		}
		return anglelist;
	}
	public void setAngle(ValueList anglelist){
		// TODO ���ýǶ�
		if(splist!=null&&splist.size()==anglelist.size()){
			for(int i=0;i<splist.size();i++){
				splist.get(i).setAngle(anglelist.getValue(i));	//�������ýǶ�
			}
		}
	}
	public ValueList getASpeed(){
		// TODO ��ȡ���ٶ�ֵ�б�
		ValueList aslist = null;	//ʹ��ֵ�б����ݽṹ
		if(splist!=null){
			aslist=new ValueList(splist.size());
			for(int i=0;i<splist.size();i++){
				aslist.setValue(i,splist.get(i).getASpeed());	//������ȡ���ٶ�
			}
		}
		return aslist;
	}
	public void setASpeed(ValueList aslist){
		// TODO ���ý��ٶ�
		if(splist!=null&&splist.size()==aslist.size()){
			for(int i=0;i<splist.size();i++){
				splist.get(i).setASpeed(aslist.getValue(i));	//�������ý��ٶ�
			}
		}
	}
	public ValueList getSpeed(){
		// TODO ��ȡ�ٶ�ֵ�б�
		ValueList vlist = null;
		if(splist!=null){
			vlist=new ValueList(splist.size());
			for(int i=0;i<splist.size();i++){
				vlist.setValue(i,splist.get(i).getSpeed());	//������ȡ�ٶ�
			}
		}
		return vlist;
	}
	public void setSpeed(ValueList vlist){
		// TODO �����ٶ�
		if(splist!=null&&splist.size()==vlist.size()){
			for(int i=0;i<splist.size();i++){
				splist.get(i).setSpeed(vlist.getValue(i));	//���������ٶ�
			}
		}
	}
	public ValueList getLocX(){
		// TODO ��ȡx�����б�
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
		// TODO ��ȡy�����б�
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
		// TODO ����xy����
		if(splist!=null&&splist.size()==x.size()&&splist.size()==y.size()){
			for(int i=0;i<splist.size();i++){
				splist.get(i).setCor(x.getValue(i),y.getValue(i));	//���������ٶ�
			}
		}
	}
	public void die(){
		// TODO ʹ����������������
		if(splist!=null){
			for(int i=0;i<splist.size();i++){
				splist.get(i).die();	//
			}
		}
	}
	public void PolarCoorTrans(double x,double y,double angle){
		// TODO ����ԭ��x,y�ͽǶ�angle���м�����任
		if(splist!=null){
			for(int i=0;i<splist.size();i++){
				VSprite sp = splist.get(i);	//
				VPointProxy center=new VPointProxy(x,y);	//����ԭ�����
				double a=VMath.GetAngleBetween2Points(center, sp);
				double dist=VMath.GetDistanceBetween2Points(center, sp);
				VPointInterface p_tar=VMath.PolarMove(center, a+angle, dist);
				sp.setCor(p_tar.GetX(), p_tar.GetY());
				sp.setAngle(a);
			}
		}
	}
}
