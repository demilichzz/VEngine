VLib={

PI=3.14159265359,

movePoint = function(x,y,x1,y1)
	x=x+x1
	y=y+y1
	return x,y
end,

isPointBounded=function(x,y)	--判断坐标点是否处于默认游戏区域范围
	if x>0 and x<400 and y>0 and y<600 then
		return true
	else
		return false
	end
end,

restrictPoint=function(x,y,x0,y0,x1,y1)	--将坐标点限制于矩形范围内
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

objMoveToPoint=function(obj,x,y,angle,speed)		--令指定对象向目标点以指定速度移动，对象必须为VPointProxy的子类
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

GetRandomArray=function(m,n)		--获取范围在[m,n]内所有整数排成的随机队列
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

GetSimpleDistanceMin=function(x1,y1,x2,y2)		--获取x轴或y轴距离中最近者
	local distx=math.abs(x1-x2)
	local disty=math.abs(y1-y2)
	if distx>disty then
		return disty
	else
		return distx
	end
end,

GetSimpleDistanceMax=function(x1,y1,x2,y2)		--获取x轴或y轴距离中最远者
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
	---------计算移动角度----------
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

addAction=function(javaobj,action)	--添加行为
	local action_proxy=luajava.createProxy("timer.VLuaAction",action)
	javaobj:addAction(action_proxy)
end,
addDeathAction = function(javaobj,action)	--添加死亡行为
	local action_proxy=luajava.createProxy("timer.VLuaSPAction",action)
	javaobj:addDeathAction(action_proxy)
end,
addSPAction=function(javaobj,action)	--添加行为
	local action_proxy=luajava.createProxy("timer.VLuaSPAction",action)
	javaobj:addAction(action_proxy)
end,

CreateTimer=function(time,endTime,perodic,action)	--使用指定参数生成Timer并运行
	local tm_proxy=luajava.createProxy("timer.VLuaAction", action)
	local tm = luajava.newInstance("timer.VTimer",time,endTime,perodic,tm_proxy)
	tm:timerStart()
	return tm
end,

CreateSpGroup=function(splist,max)	--以下标从0开始的粒子table作为参数创建SpriteGroup对象
	local g = luajava.newInstance("entities.VSpriteGroup")
	if(splist~=nil)then
		for i=0,max,1 do
			g:addSprite(splist[i])
		end
	end
	return g
end,

setFactoryImage=function(f,name)		--使用易于理解的字符串参数定义粒子工厂使用的粒子图像
	f:setRotateMode(0)	--默认设置不旋转，因为无需旋转粒子较多
	f:setRad(3)		--默认16x16小弹碰撞半径为3
	if(name=="Large") then			--大玉
		f:setRad(16)
		f:setImage("Sprite_LBall_a_top.png","Sprite_LBall_a_bottom.png")
	elseif(name=="MFlat")then		--白底中玉
		f:setRad(11)
		f:setImage("Sprite_MBall.png","null")
	elseif(name=="MRad")then		--渐变底中玉
		f:setRad(11)
		f:setImage("Sprite_MBall_a.png","null")
	elseif(name=="MSMix")then		--24x24混合
		f:setRad(7)
		f:setImage("Sprite_MS_top.png","Sprite_MS_bottom.png")
	elseif(name=="MSFlat")then		--24x24普通
		f:setRad(7)
		f:setImage("Sprite_MS_bottom.png","null")
	elseif(name=="SDefault")then		--默认小玉
		f:setImage("Sprite_A_combine.png","null")
	elseif(name=="SMix")then			--混合小玉
		f:setImage("Sprite_A_top.png","Sprite_A_bottom.png")
	elseif(name=="SCard")then		--卡弹
		f:setIfRotate(true)
		f:setImage("Sprite_Card_combine.png","null")
	elseif(name=="SScale")then		--鳞弹
		f:setIfRotate(true)
		f:setImage("Sprite_Scale_combine_B.png","null")
	elseif(name=="SOval")then		--椭圆弹
		f:setIfRotate(true)
		f:setImage("Sprite_Oval_combine.png","null")
	elseif(name=="SPoint")then		--点弹
		f:setIfRotate(true)
		f:setImage("Sprite_S_Point.png","null")
	elseif(name=="SStar")then		--16x16星弹
		f:setRotateMode(2)
		f:setImage("Sprite_Star_small.png","null")
	elseif(name=="MSStar")then		--24x24星弹
		f:setRad(6)
		f:setRotateMode(2)
		f:setImage("Sprite_MS_Star.png","null")
	elseif(name=="MStar")then		--32x32星弹
		f:setRad(8)
		f:setRotateMode(2)
		f:setImage("Sprite_M_Star.png","null")
	elseif(name=="MRect")then		--32x32方弹
		f:setRad(10)
		f:setRotateMode(1)
		f:setImage("Sprite_M_Rect.png","null")
	elseif(name=="MRadDrop")then		--渐变滴弹A型
		f:setRad(6)
		f:setRotateMode(1)
		f:setImage("Sprite_M_RadDrop.png","null")
	elseif(name=="MChaosFlat")then		--32x32混沌弹单层
		f:setRad(6)
		f:setImage("Sprite_M_Chaos_combine.png","null")
	elseif(name=="MChaosMix")then		--32x32混沌弹混合
		f:setRad(6)
		f:setImage("Sprite_M_Chaos_top.png","Sprite_M_Chaos_bottom.png")
	elseif(name=="MDouble")then		--32x32双环弹
		f:setRad(6)
		f:setImage("Sprite_M_Double.png","null")
	elseif(name=="SHex")then		--六角弹
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
