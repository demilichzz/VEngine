dofile("data/Script/Lib/VLib.lua")
Debug=luajava.bindClass("global.Debug")
VEngine=luajava.bindClass("system.VEngine")
VStage=luajava.bindClass("stage.VStage")
VTimer=luajava.bindClass("timer.VTimer")
SpFClass=luajava.bindClass("factory.SpriteFactory")
SpFactory=SpFClass:getInstance()
UnitFactory=luajava.bindClass("factory.UnitFactory")
GameData=luajava.bindClass("data.GameData")
GlobalEvent=luajava.bindClass("event.GlobalEvent")
Soundconst=luajava.bindClass("global.Soundconst")

local runtime=0		--更新计数
local zoz
local phase=0		--战斗阶段
local wrath=0		--汇聚之怒层数
local p2time=0
local pc=GameData:getPlayer()
local c_lock=0

function zoz_init()
	runtime=0
	phase=0
	wrath=0
	c_lock=0
	p2time=0
end

ZonozzAction={

setBoss=function(boss)		--定义设置Boss函数
	zoz=boss
	pc=GameData:getPlayer()
	zoz_init()
end,

action = {			--以Action形式定义Boss的行为，行为将在每次游戏状态更新时更新
	action=function()
		if phase==0 then			--P1
			zoz_move(zoz)
			if runtime==10 then
				if(p2time>0) then
					BossSpeech:soundPlay("VO_DS_ZONOZZ_SPELL_01.mp3")
				end
				zoz_createVoid()	--创建黑球
			end
			if runtime==10 then	--攻击
				zoz_attack()
			end
			if runtime%1000==50 then
				zoz_createAbsorb()	--心灵吸收
			end
			if runtime%1000==550 then
				zoz_createShadow()	--崩解之影
			end
		elseif phase==1 then
			zoz_move(zoz)
			if runtime==0 then
			end
			if runtime==1 then
				BossSpeech:soundPlay("VO_DS_ZONOZZ_SPELL_03.mp3")
				zoz_createBlood()	--黑血
			end
			if runtime==2000 then
				phase=0
				runtime=0
				GlobalEvent:phaseTransition()
			end
		end
		runtime=runtime+1
	end
}

}

function zoz_move(zoz)
	zoz:setCor(200,200)
end

function zoz_createVoid()	--创建黑球并添加行为
	local F=SpFactory:getInstance()
	local x=200
	local y=220
	local sp=SpFactory:creator()
	sp:setRestricted(false)
	sp:setImage("Sprite_Shadow.png","")
	sp:setImageIndex(0,0)
	sp:setCor(x,y)
	sp:setInvincible(true)
	sp:setRad(0)
	local angle=math.random()*VLib.PI/24+VLib.PI/2-VLib.PI/48
	sp:setAngle(angle)
	sp:setSpeed(30)
	sp:setRotateMode(2)
	sp:setRotateSpeed(VLib.PI*2)
	sp:setLife(-1)	--粒子永久持续
	sp:spBorn()
	c_lock=0		--碰撞锁定，为0时才检测碰撞
	local tmac={
		action=function()		--控制黑球状态，包括自旋和碰撞检测
			if sp.alive then
				c_sidedetect(sp)	--撞墙检测
				if c_lock==0 then			--禁止检测时间结束
					if runtime>500 then
						detectAbsorb(sp)
					end
					local ca,ifc = c_detect(sp,pc)		--进行碰撞检测
					if ifc then
						c_lock=3000
						local a=ca-VLib.PI/48+math.random()*VLib.PI/24
						sp:setAngle(a)
						voidSpread(sp)
					end
				else
					c_lock=c_lock-20
					if c_lock<=0 then
						c_lock=0
					end
				end
			end
		end
	}
	VLib.CreateTimer(20,100000,true,tmac)
end

function detectAbsorb(sp)	--吸收虚空检测
	local ax=sp:GetX()
	local ay=sp:GetY()
	local bx=zoz:GetX()
	local by=zoz:GetY()
	if VLib.GetSimpleDistanceMax(ax,ay,bx,by)<70 then	--转P2
		phase=1
		runtime=0
		wrath=0
		p2time=p2time+1
		GlobalEvent:phaseTransition()
	end
end

function c_sidedetect(sp)	--撞墙检测
	local ax=sp:GetX()
	local ay=sp:GetY()
	if ax<32 or ax>368 or ay<32 or ay>568 then
		c_lock=3000
		local bx=zoz:GetX()
		local by=zoz:GetY()
		local a=VLib.GetAngleBetween2Points(ax,ay,bx,by)
		sp:setAngle(a)
		disaster(ax,ay)
	end
end

function disaster(x,y)		--撞墙
	local F=SpFactory:getInstance()
	VLib.setFactoryImage(F,"SDefault")
	F:setLife(10)
	F:setRestricted(true)
	F:setImageIndex(9,9)
	F:setLoc(x,y)
	F:setDamage(50)
	local tmac={
		action=function()
			for i=0,127,1 do
				local bsp=F:creator()
				bsp:setAngle(VLib.PI*i/32+VLib.PI/32*math.random())
				local spd=math.random(500,700)
				bsp:setSpeed(spd)
				bsp:spBorn()
			end
		end
	}
	VLib.CreateTimer(100,1000,true,tmac)
end

function c_detect(sp,pc)
	local ax=sp:GetX()
	local ay=sp:GetY()
	local bx=pc:GetX()
	local by=pc:GetY()
	local min=VLib.GetSimpleDistanceMin(ax,ay,bx,by)
	if min<=58 then
		local dist=VLib.GetDistanceBetween2Points(ax,ay,bx,by)
		if dist<=56 then
			local angle=VLib.GetAngleBetween2Points(bx,by,ax,ay)
			return angle,true
		else
			return 0,false
		end
	else
		return 0,false
	end
end

function voidSpread(sp)	--黑球撞人
	local F = SpFactory:getInstance()
	VLib.setFactoryImage(F,"SMix")
	F:setImageIndex(8,8)
	F:setLife(5)
	wrath=wrath+1
	F:setDamage(wrath*5)
	local x=sp:GetX()
	local y=sp:GetY()
	F:setLoc(x,y)
	zoz:appendBuff("wrath")
	local speedrange = wrath
	for i=0,23,1 do
		local bsp=F:creator()
		bsp:setAngle(VLib.PI*i/12+VLib.PI/24)
		local spd=80
		bsp:setSpeed(spd+speedrange*10)
		bsp:spBorn()
	end

	for j=1,wrath,1 do
		for k=0,15,1 do
			local wsp=F:creator()
			wsp:setAngle(VLib.PI*k/8+VLib.PI/16/wrath*j)
			local spd=math.random(40,70+speedrange*10)
			wsp:setSpeed(spd)
			wsp:spBorn()
		end
	end
end

function zoz_attack()		--攻击，关联汇聚之怒层数
	local F = SpFactory:getInstance()
	VLib.setFactoryImage(F,"SScale")
	F:setImageIndex(9,9)
	F:setLife(5)
	F:setMode(0)
	F:setDamage(wrath*3+1)
	local bx=zoz:GetX()
	local by=zoz:GetY()
	F:setLoc(bx,by)
	F:setSpeed(120)
	-----------------------------------------------------
	local tmcount=0
	local tmact={
		action=function()
			F:setSpeed(120+wrath*40)
			for i=1,wrath*2+3,1 do
				local tm_a = -VLib.PI/2+tmcount*VLib.PI/8+VLib.PI*2*i/(wrath*2+3)
				local sp=F:creator()
				sp:setAngle(tm_a)
				sp:spBorn()
			end
			tmcount=tmcount+1
		end
	}
	VLib.CreateTimer(200,-1000,true,tmact)
end

function zoz_createAbsorb()	--心灵吸收
	local F=SpFactory:getInstance()
	VLib.setFactoryImage(F,"MSMix")
	F:setImageIndex(9,9)
	F:setRestricted(false)
	F:setSpeed(100)
	F:setLife(2.7)
	F:setDamage(5)
	local angle
	local px=pc:GetX()
	local py=pc:GetY()
	local bx=zoz:GetX()
	local by=zoz:GetY()
	local absact={
		action=function()
			angle=VLib.GetAngleBetween2Points(bx,by,px,py)-VLib.PI/3
			for i=0,7,1 do
				angle=angle+VLib.PI/12
				local tx,ty=VLib.PolarMove(bx,by,angle,400)
				local sp=F:creator()
				sp:setCor(tx,ty)
				sp:setAngle(angle-VLib.PI)
				sp:spBorn()
			end
		end
	}
	VLib.CreateTimer(500,4000,true,absact)
end

function zoz_createBlood()		--P2 黑血阶段
	local F=SpFactory:getInstance()
	VLib.setFactoryImage(F,"SDefault")
	F:setImageIndex(9,9)
	F:setSpeed(90+p2time*10)
	F:setLife(8)
	F:setRestricted(false)
	F:setDamage(7)
	F_Large=F:getInstance()
	VLib.setFactoryImage(F_Large,"SScale")
	F_Large:setSpeed(150)
	F_Large:setMode(1)
	F_Large:setReflectMode(15)		--全反射
	F_Large:setRestricted(true)
	F_Large:setLife(4)
	F_Large:setInvincible(false)
	F_Large:setDamage(15)
	F_Large:setReflectTime(2)
	local x=zoz:GetX()
	local y=zoz:GetY()
	F:setLoc(x,y)
	F_Large:setLoc(x,y)
	local a=0.001
	local rt=0
	local tmcount=0
	local tmact={
		action=function()
			F:setASpeed(VLib.PI/16*(tmcount%3-1))
			for i=0,23+4*p2time,1 do
				local sp=F:creator()
				sp:setAngle(VLib.PI/64*tmcount+VLib.PI/(12+2*p2time)*i)
				sp:spBorn()
			end
			tmcount=tmcount+1
		end
	}
	VLib.CreateTimer(200,20000,true,tmact)
	local rate = 12-p2time			--干扰弹复杂度关联进入p2次数
			if(rate<4)then
				rate=4
			end
	local tmsubcount=0
	local tmsubact={			--干扰弹部分
		action=function()
			for i=0,2,1 do
				for j=0,2,1 do
					local suba =VLib.PI/2-VLib.PI/9+VLib.PI/9*j+VLib.PI*2/3*i+VLib.PI/19*tmsubcount*(p2time%2*2-1)
					for k=0,1,1 do
						local sp=F_Large:creator()
						sp:setSpeed(k*20+150)
						sp:setAngle(suba)
						sp:spBorn()
					end
				end
			end
			tmsubcount=tmsubcount+1
		end
	}
	VLib.CreateTimer(rate*50,20000,true,tmsubact)
end

function zoz_createShadow()	--P1 崩解之影

end
