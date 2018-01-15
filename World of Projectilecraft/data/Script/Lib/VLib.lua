VLib={

PI=3.14159265359,

movePoint = function(x,y,x1,y1)
	x=x+x1
	y=y+y1
	return x,y
end,

isPointBounded=function(x,y)	--�ж�������Ƿ���Ĭ����Ϸ����Χ
	if x>0 and x<400 and y>0 and y<600 then
		return true
	else
		return false
	end
end,

restrictPoint=function(x,y,x0,y0,x1,y1)	--������������ھ��η�Χ��
	if x<x0 then
		x=x0
	elseif x>x1 then
		x=x1
	end
	if y<y0 then
		y=y0
	elseif y>y1 then
		y=y1
	end
	return x,y
end,

objMoveToPoint=function(obj,x,y,angle,speed)		--��ָ��������Ŀ�����ָ���ٶ��ƶ����������ΪVPointProxy������
	local dist=speed/100
	local ox=obj:GetX()
	local oy=obj:GetY()
	if ox==x and oy==y then
		return
	end
	if VLib.GetSimpleDistanceMax(ox,oy,x,y)<=dist then
		obj:setCor(ox,oy)
	else
		obj:PolarMove(angle,dist)
	end
end,

GetRandomArray=function(m,n)		--��ȡ��Χ��[m,n]�����������ųɵ��������
	local rndarray = {}
	local arraystate = {}
	for i=m,n,1 do
		arraystate[i]=i
	end
	for i=m,n,1 do
		local index = math.random(m,n)
		while arraystate[index]==nil do
			index=index+1
			if index>n then
				index=m
			end
		end
		arraystate[index]=nil
		rndarray[i-m]=index
	end
	return rndarray
end,

GetSimpleDistanceMin=function(x1,y1,x2,y2)		--��ȡx���y������������
	local distx=math.abs(x1-x2)
	local disty=math.abs(y1-y2)
	if distx>disty then
		return disty
	else
		return distx
	end
end,

GetSimpleDistanceMax=function(x1,y1,x2,y2)		--��ȡx���y���������Զ��
	local distx=math.abs(x1-x2)
	local disty=math.abs(y1-y2)
	if distx>disty then
		return distx
	else
		return disty
	end
end,

GetDistanceBetween2Points = function(x1,y1,x2,y2)
	dist = math.sqrt((x2-x1)^2+(y2-y1)^2)
	return dist
end,

GetAngleBetween2Points = function(x,y,x_tar,y_tar)
	local a;
	---------�����ƶ��Ƕ�----------
	if VLib.GetDistanceBetween2Points(x, y, x_tar, y_tar)==0 then
		a=0;
	else
		a = math.asin((y_tar-y)/VLib.GetDistanceBetween2Points(x, y, x_tar, y_tar));
		if x_tar==x then
			if y_tar>y then
				a = math.pi*0.5;
			else
				a = math.pi*1.5;
			end
		elseif x_tar<x then
			a = math.pi-a;
		end
	end
	if(y_tar==y) then
		if x_tar<x then
			a = math.pi;
		else
			a = 0;
		end
	end
	a=VLib.stdAngle(a);
	return a;
end,

stdAngle = function(angle)
	while angle<0 or angle>=math.pi*2 do
		if angle<0 then
				angle = angle + math.pi*2;

		elseif angle>=math.pi*2 then
				angle = angle - math.pi*2;
		end
	end
	return angle
end,

PolarMove =function(x,y,angle,dist)
	x = x+dist*math.cos(angle)
	y = y+dist*math.sin(angle)
	return x,y
end,

CreateSPAction=function(action)
	local action_proxy=luajava.createProxy("timer.VLuaSPAction",action)
	return action_proxy
end,

addAction=function(javaobj,action)	--�����Ϊ
	local action_proxy=luajava.createProxy("timer.VLuaAction",action)
	javaobj:addAction(action_proxy)
end,
addDeathAction = function(javaobj,action)	--���������Ϊ
	local action_proxy=luajava.createProxy("timer.VLuaSPAction",action)
	javaobj:addDeathAction(action_proxy)
end,
addSPAction=function(javaobj,action)	--�����Ϊ
	local action_proxy=luajava.createProxy("timer.VLuaSPAction",action)
	javaobj:addAction(action_proxy)
end,

CreateTimer=function(time,endTime,perodic,action)	--ʹ��ָ����������Timer������
	local tm_proxy=luajava.createProxy("timer.VLuaAction", action)
	local tm = luajava.newInstance("timer.VTimer",time,endTime,perodic,tm_proxy)
	tm:timerStart()
	return tm
end,

CreateSpGroup=function(splist,max)	--���±��0��ʼ������table��Ϊ��������SpriteGroup����
	local g = luajava.newInstance("entities.VSpriteGroup")
	if(splist~=nil)then
		for i=0,max,1 do
			g:addSprite(splist[i])
		end
	end
	return g
end,

setFactoryImage=function(f,name)		--ʹ�����������ַ��������������ӹ���ʹ�õ�����ͼ��
	f:setRotateMode(0)	--Ĭ�����ò���ת����Ϊ������ת���ӽ϶�
	f:setRad(3)		--Ĭ��16x16С����ײ�뾶Ϊ3
	if(name=="Large") then			--����
		f:setRad(16)
		f:setImage("Sprite_LBall_a_top.png","Sprite_LBall_a_bottom.png")
	elseif(name=="MFlat")then		--�׵�����
		f:setRad(11)
		f:setImage("Sprite_MBall.png","null")
	elseif(name=="MRad")then		--���������
		f:setRad(11)
		f:setImage("Sprite_MBall_a.png","null")
	elseif(name=="MSMix")then		--24x24���
		f:setRad(7)
		f:setImage("Sprite_MS_top.png","Sprite_MS_bottom.png")
	elseif(name=="MSFlat")then		--24x24��ͨ
		f:setRad(7)
		f:setImage("Sprite_MS_bottom.png","null")
	elseif(name=="SDefault")then		--Ĭ��С��
		f:setImage("Sprite_A_combine.png","null")
	elseif(name=="SMix")then			--���С��
		f:setImage("Sprite_A_top.png","Sprite_A_bottom.png")
	elseif(name=="SCard")then		--����
		f:setIfRotate(true)
		f:setImage("Sprite_Card_combine.png","null")
	elseif(name=="SScale")then		--�۵�
		f:setIfRotate(true)
		f:setImage("Sprite_Scale_combine_B.png","null")
	elseif(name=="SOval")then		--��Բ��
		f:setIfRotate(true)
		f:setImage("Sprite_Oval_combine.png","null")
	elseif(name=="SPoint")then		--�㵯
		f:setIfRotate(true)
		f:setImage("Sprite_S_Point.png","null")
	elseif(name=="SStar")then		--16x16�ǵ�
		f:setRotateMode(2)
		f:setImage("Sprite_Star_small.png","null")
	elseif(name=="MSStar")then		--24x24�ǵ�
		f:setRad(6)
		f:setRotateMode(2)
		f:setImage("Sprite_MS_Star.png","null")
	elseif(name=="MStar")then		--32x32�ǵ�
		f:setRad(8)
		f:setRotateMode(2)
		f:setImage("Sprite_M_Star.png","null")
	elseif(name=="MRect")then		--32x32����
		f:setRad(10)
		f:setRotateMode(1)
		f:setImage("Sprite_M_Rect.png","null")
	elseif(name=="MRadDrop")then		--����ε�A��
		f:setRad(6)
		f:setRotateMode(1)
		f:setImage("Sprite_M_RadDrop.png","null")
	elseif(name=="MChaosFlat")then		--32x32���絯����
		f:setRad(6)
		f:setImage("Sprite_M_Chaos_combine.png","null")
	elseif(name=="MChaosMix")then		--32x32���絯���
		f:setRad(6)
		f:setImage("Sprite_M_Chaos_top.png","Sprite_M_Chaos_bottom.png")
	elseif(name=="MDouble")then		--32x32˫����
		f:setRad(6)
		f:setImage("Sprite_M_Double.png","null")
	elseif(name=="SHex")then		--���ǵ�
		f:setRad(7)
		f:setRotateMode(1)
		f:setImage("Sprite_S_Hex.png","null")
	else
		f:setImage("Sprite_A_combine.png","null")
	end
end,

setSpriteImage=function(sp,name)
	return VLib.setFactoryImage(sp,name)
end
}
