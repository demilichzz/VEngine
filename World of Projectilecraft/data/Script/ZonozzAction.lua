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

local runtime=0		--���¼���
local zoz
local phase=0		--ս���׶�
local wrath=0		--���֮ŭ����
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

setBoss=function(boss)		--��������Boss����
	zoz=boss
	pc=GameData:getPlayer()
	zoz_init()
end,

action = {			--��Action��ʽ����Boss����Ϊ����Ϊ����ÿ����Ϸ״̬����ʱ����
	action=function()
		if phase==0 then			--P1
			zoz_move(zoz)
			if runtime==10 then
				if(p2time>0) then
					BossSpeech:soundPlay("VO_DS_ZONOZZ_SPELL_01.mp3")
				end
				zoz_createVoid()	--��������
			end
			if runtime==10 then	--����
				zoz_attack()
			end
			if runtime%1000==50 then
				zoz_createAbsorb()	--��������
			end
			if runtime%1000==550 then
				zoz_createShadow()	--����֮Ӱ
			end
		elseif phase==1 then
			zoz_move(zoz)
			if runtime==0 then
			end
			if runtime==1 then
				BossSpeech:soundPlay("VO_DS_ZONOZZ_SPELL_03.mp3")
				zoz_createBlood()	--��Ѫ
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

function zoz_createVoid()	--�������������Ϊ
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
	sp:setLife(-1)	--�������ó���
	sp:spBorn()
	c_lock=0		--��ײ������Ϊ0ʱ�ż����ײ
	local tmac={
		action=function()		--���ƺ���״̬��������������ײ���
			if sp.alive then
				c_sidedetect(sp)	--ײǽ���
				if c_lock==0 then			--��ֹ���ʱ�����
					if runtime>500 then
						detectAbsorb(sp)
					end
					local ca,ifc = c_detect(sp,pc)		--������ײ���
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

function detectAbsorb(sp)	--������ռ��
	local ax=sp:GetX()
	local ay=sp:GetY()
	local bx=zoz:GetX()
	local by=zoz:GetY()
	if VLib.GetSimpleDistanceMax(ax,ay,bx,by)<70 then	--תP2
		phase=1
		runtime=0
		wrath=0
		p2time=p2time+1
		GlobalEvent:phaseTransition()
	end
end

function c_sidedetect(sp)	--ײǽ���
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

function disaster(x,y)		--ײǽ
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

function voidSpread(sp)	--����ײ��
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

function zoz_attack()		--�������������֮ŭ����
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

function zoz_createAbsorb()	--��������
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

function zoz_createBlood()		--P2 ��Ѫ�׶�
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
	F_Large:setReflectMode(15)		--ȫ����
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
	local rate = 12-p2time			--���ŵ����Ӷȹ�������p2����
			if(rate<4)then
				rate=4
			end
	local tmsubcount=0
	local tmsubact={			--���ŵ�����
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

function zoz_createShadow()	--P1 ����֮Ӱ

end
