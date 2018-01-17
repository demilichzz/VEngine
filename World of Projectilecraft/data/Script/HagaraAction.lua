dofile("data/Script/Lib/VLib.lua")
dofile("data/Script/Lib/PathPoint.lua")
--[[
Debug=luajava.bindClass("global.Debug")
VEngine=luajava.bindClass("system.VEngine")
VStage=luajava.bindClass("stage.VStage")
VTimer=luajava.bindClass("timer.VTimer")
SpFClass=luajava.bindClass("factory.SpriteFactory")
SpFactory=SpFClass:getInstance()
UnitFactory=luajava.bindClass("factory.UnitFactory")
GameData=luajava.bindClass("data.GameData")
GlobalEvent=luajava.bindClass("event.GlobalEvent")
Soundconst=luajava.bindClass("global.Soundconst")]]--

local runtime=0		--更新计数
local boss			--Boss对象
local fstphase=1	--第一次转阶段
local phase=0		--战斗阶段
local pc=GameData:getPlayer()

local tar_x=200
local tar_y=300			--移动目标点
local tar_angle=0		--移动目标方向
local icespearcount=1
local speech_icephase=1
local speech_lightningphase=1
local hag_01_count=0
local pillarlist={}
local pillarindex=1
local psize=0
local nodechain={}		--传电节点链，记录x,y坐标与节点状态及剩余生命,状态1=trace,2=stand
local chainlength=0		--节点链长度
local lightcrystal={}
local icecrystal={}
local tracechain={}
local tracelength=0
local wplist={}
local focusstate=0		--专注打击状态

local lcstat={{200,50,0},{350,300,0},{200,550,0},{50,300,0},{0,0,0}}		--闪电水晶状态，顺序上右下左 状态0=未激活,1=已激活非当前,2=当前

function hag_init()
	runtime=0
	fstphase=math.random(1,2)
	phase=0
	nodechain={}
	chainlength=0
	tar_x=200
	tar_y=300
	tar_angle=0
	tracechain={}
	tracelength=0
	wplist={}
	lightcrystal={}
	icecrystal={}
	pillarlist={}
	pillarindex=1
	psize=0
	icespearcount=1
	hag_01_count=0
	speech_icephase=1
	speech_lightningphase=1
	lcstat={{200,50,0},{350,300,0},{200,550,0},{50,300,0},{0,0,0}}
	focusstate=0
end

HagaraAction={

setBoss=function(b)		--定义设置Boss函数
	boss=b
	pc=GameData:getPlayer()
	hag_init()
end,

action = {			--以Action形式定义Boss的行为，行为将在每次游戏状态更新时更新
	action=function()
		hag_bossmove()				--移动
		if phase==0 then			--P1
			if runtime%500==1 then
				hag_attack01()		--普通攻击
			end
			if runtime%1400==300 then
				hag_focusstrike()			--专注打击
			end
			if runtime%1400==700 then
				hag_icespear()			--冰霜长矛
			end
			if runtime%1600==100 then
				--hag_icegrave()			--冰墓
			end
			if runtime==3000 then
				runtime=0
				icespearcount=-icespearcount+1
				phase=fstphase			--转P
				fstphase=fstphase%2+1
				boss:appendBuff("invincible")
				GlobalEvent:phaseTransition()
			end
		elseif phase==1 then		--冰阶段
			if runtime==1 then
				BossSpeech:soundPlay("VO_DS_HAGARA_GLACIER_02.mp3")
				speech_icephase=1
				hag_icewave()
				hag_crystal()		--创建禁锢水晶
			end
		elseif phase==2 then		--电阶段
			if runtime==1 then
				BossSpeech:soundPlay("VO_DS_HAGARA_LIGHTNING_02.mp3")
				speech_lightningphase=1
				lcstat={{200,50,0},{350,300,0},{200,550,0},{50,300,0},{0,0,0}}
				hag_lightning()		--电阶段全场效果
				hag_lcrystal()		--电阶段水晶
				hag_elemental()		--闪电元素
			end
		elseif phase==3 then
			if runtime%500==101 and runtime>600 then
				hag_attack01()		--普通攻击
			end
			if runtime%1800==700 then
				hag_focusstrike()			--专注打击
			end
			if runtime%2000==800 and runtime>600 then
				hag_icespear()			--冰霜长矛
			end
			if runtime==1400 then
				hag_icegrave()			--冰墓
			end
			if runtime==3600 then
				runtime=0
				icespearcount=-icespearcount+1
				phase=fstphase
				fstphase=fstphase%2+1		--转P
				boss:appendBuff("invincible")
				GlobalEvent:phaseTransition()
			end
		end
		runtime=runtime+1
	end
}

}

function hag_bossmove()
	if phase==0 then		--P1
		if focusstate==0 then
			if runtime%400==100 then	--每隔4秒换目标位置
				local pc_x=pc:GetX()
				tar_x=pc_x+math.random()*120-60
				tar_y=250-math.random()*150
				tar_x,tar_y=VLib.restrictPoint(tar_x,tar_y,20,20,380,580)
				local b_x=boss:GetX()
				local b_y=boss:GetY()
				tar_angle=VLib.GetAngleBetween2Points(b_x,b_y,tar_x,tar_y)
			end
			if runtime>100 then	--进行移动
				VLib.objMoveToPoint(boss,tar_x,tar_y,tar_angle,50)
			end
		else
			VLib.objMoveToPoint(boss,tar_x,tar_y,tar_angle,200)
		end
	elseif phase==1 then
		boss:setCor(200,300)
	elseif phase==2 then
		boss:setCor(200,300)
	elseif phase==3 then
		if focusstate==0 then
			if runtime%400==100 then	--每隔4秒换目标位置
				local pc_x=pc:GetX()
				tar_x=pc_x+math.random()*120-60
				tar_y=250-math.random()*150
				tar_x,tar_y=VLib.restrictPoint(tar_x,tar_y,20,20,380,580)
				local b_x=boss:GetX()
				local b_y=boss:GetY()
				tar_angle=VLib.GetAngleBetween2Points(b_x,b_y,tar_x,tar_y)
			end
			if runtime>600 then	--进行移动
				VLib.objMoveToPoint(boss,tar_x,tar_y,tar_angle,50)
			end
		else
			VLib.objMoveToPoint(boss,tar_x,tar_y,tar_angle,200)
		end
	end
end

function hag_setLightCore(dx,dy)		--闪电元素死亡时触发，创建传电核心
	local F=SpFactory:getInstance()
	VLib.setFactoryImage(F,"SDefault")
	F:setMode(0)
	F:setImageIndex(1,1)
	F:setInvincible(true)
	F:setRestricted(false)
	F:setLife(1)
	F:setSpeed(0)
	F:setDamage(3)
	F_core=F:getInstance()
	F_core:setLife(2)
	F_core:setSpeed(20)
	lcstat[5]={dx,dy,2}		--设置第五核心
	local corex=dx
	local corey=dy		--核心坐标
	for i=1,4,1 do
		local dist=VLib.GetDistanceBetween2Points(dx,dy,lcstat[i][1],lcstat[i][2])
		if dist<50 then		--如果离某核心过近
			BossSpeech:soundPlay("VO_DS_HAGARA_CIRCUIT_0"..speech_lightningphase..".mp3")
			speech_lightningphase=speech_lightningphase+1
			corex=lcstat[i][1]
			corey=lcstat[i][2]		--将其设为传电核心并设置为当前激活
			lcstat[i][3]=2
			lcstat[5][3]=0		--死亡时的核心不再激活
		end
	end
	local tmcount=0
	local tracex=0
	local tracey=0			--追踪点坐标
	local tmact={
		action=function()
			local px=pc:GetX()
			local py=pc:GetY()
			if chainlength==0 then		--从核心开始追踪
				for i=1,5,1 do
					if lcstat[i][3]==2 then
						corex=lcstat[i][1]
						corey=lcstat[i][2]
					end
				end
				hag_trace(corex,corey,F)
			else		--从最后一个节点开始追踪
				hag_trace(nodechain[chainlength][1],nodechain[chainlength][2].F)
			end
		end
	}
	VLib.CreateTimer(200,-1000,true,tmact)
	local consttm={			--核心效果
		action=function()
			for i=1,5,1 do
				if lcstat[i][3]~=0 then
					F_core:setLoc(lcstat[i][1],lcstat[i][2])
					for spi=0,3,1 do
						local sp=F_core:creator()
						sp:setAngle(spi*VLib.PI/2)
						sp:spBorn()
					end
				end
			end
		end
	}
	VLib.CreateTimer(400,-1000,true,consttm)
end

function hag_trace(cx,cy,F)	--传电过程
	local x,y
	if chainlength==0 then		--如果是从核心
	else		--如是从节点进行追踪
	end
	if tracelength==0 then
	else
		cx=tracechain[tracelength][2]
		cy=tracechain[tracelength][3]
	end
	local px=pc:GetX()
	local py=pc:GetY()
	local p_angle=VLib.GetAngleBetween2Points(cx,cy,px,py)
	local p_dist=VLib.GetDistanceBetween2Points(cx,cy,px,py)
	if p_dist<100 then			--玩家距离追踪点足够近则继续追踪
		if tracelength<=120 then
			x,y=VLib.PolarMove(cx,cy,p_angle,10)
			x,y=VLib.PolarMove(x,y,math.random()*VLib.PI*2,math.random()*3)
			local sp=F:creator()
			sp:setCor(x,y)
			sp:setLife(10000)
			tracelength=tracelength+1
			if tracelength%15==0 then
				VLib.setSpriteImage(sp,"MSFlat")
			end
			tracechain[tracelength]={sp,x,y}
			sp:spBorn()
			for i=1,4,1 do
				if VLib.GetDistanceBetween2Points(x,y,lcstat[i][1],lcstat[i][2])<50 and lcstat[i][3]==0 then	--如果追踪点离某核心够近
					BossSpeech:soundPlay("VO_DS_HAGARA_CIRCUIT_0"..speech_lightningphase..".mp3")
					speech_lightningphase=speech_lightningphase+1
					lcstat[i][3]=2
					for j=1,5,1 do
						if j~=i and lcstat[j][3]==2 then
							lcstat[j][3]=1
						end
					end
					for k=1,tracelength,1 do
						tracechain[k][1]:setLife(-1)
					end
					tracechain={}
					tracelength=0
				end
			end
		end
	else
		if tracelength>0 then
			tracechain[tracelength][1]:die()
			tracelength=tracelength-1
		end
	end
end

function hag_lcrystal()			--生成闪电水晶
	UnitFactory:setImage("Hag_crystal.png")
	UnitFactory:setInvincible(true)
	UnitFactory:setIfCollD(false)
	UnitFactory:setLifeTime(-1)
	UnitFactory:setLife(50)
	UnitFactory:setRad(10)
	local unit = UnitFactory:creator()
	unit:setCor(200,50)
	lightcrystal[1]=unit
	unit = UnitFactory:creator()
	unit:setCor(200,550)
	lightcrystal[2]=unit
	unit = UnitFactory:creator()
	unit:setCor(50,300)
	lightcrystal[3]=unit
	unit = UnitFactory:creator()
	unit:setCor(350,300)
	lightcrystal[4]=unit
	local checktm={		--检测水晶状态
		action=function()
			local alldie=true
			for i=1,4,1 do
				if lcstat[i][3]==0 then
					alldie=false
				end
			end
			if alldie then
				for j=1,4,1 do
					lightcrystal[j]:die(0)
				end
				runtime=0
				phase=3
				boss:removeBuff("invincible")
				boss:appendBuff("weakness")
				GlobalEvent:phaseTransition()
			end
		end
	}
	VLib.CreateTimer(50,-1000,true,checktm)
end

function hag_elemental()	--闪电元素
	UnitFactory:setImage("hagara_le.png")
	UnitFactory:setInvincible(false)
	UnitFactory:setIfCollD(true)
	UnitFactory:setLifeTime(-1)
	UnitFactory:setLife(150)
	UnitFactory:setRad(50)
	local unit = UnitFactory:creator()
	unit:setCor(200,50)
	local movetimer
	local movecount=0
	local tarx,tary,tarangle
	local movetm={					--单位移动计时器
		action=function()
			if movecount%50==0 then
				tarx=pc:GetX()
				tary=pc:GetY()
				local b_x=unit:GetX()
				local b_y=unit:GetY()
				tar_angle=VLib.GetAngleBetween2Points(b_x,b_y,tarx,tary)
			end
			if unit:cDetection(pc)==false then
				VLib.objMoveToPoint(unit,tarx,tary,tar_angle,200)
			end
		end
	}
	movetimer=VLib.CreateTimer(20,-1000,true,movetm)
	local timer
	local ctmcount=0
	local checktm={			--检查闪电元素是否死亡
		action=function()
			if unit.alive==false then
				local dx=unit:GetX()
				local dy=unit:GetY()
				timer:die()
				movetimer:die()
				hag_setLightCore(dx,dy)
			end
			ctmcount=ctmcount+1
		end
	}
	timer=VLib.CreateTimer(50,-1000,true,checktm)
end

function hag_lightning()	--电阶段全场效果
	local F=SpFactory:getInstance()
	local F_std=SpFactory:getInstance()
	VLib.setFactoryImage(F,"SScale")
	F:setMode(0)
	F:setDamage(10)
	F:setImageIndex(5,5)
	F:setRestricted(false)
	F:setLife(1)
	F:setSpeed(140)
	VLib.setFactoryImage(F_std,"SMix")
	F_std:setImageIndex(5,5)
	F_std:setDamage(10)
	F_std:setRestricted(false)
	F_std:setInvincible(true)
	F_std:setLife(10000)
	F_std:setSpeed(0)
	local matrix={}		--闪电生成矩阵
	for ix=0,4,1 do
		for iy=0,6,1 do
			local sp=F_std:creator()
			sp:setCor(ix*100,iy*100)
			sp:spBorn()
		end
	end
	local tmcount=0
	local tmact={				--生成全屏闪电
		action=function()
			local stdline = math.random(0,1)	--起始双重列
			for j=1,6,1 do
				local ix=math.random(1,4)
				local ix2=ix+2
				local dir
				if ix2>4 then
					ix2=ix2-4
				end
				dir=math.random(1,4)
				hag_genlight(ix,j,dir,F)
				if j%2==stdline then
					dir=math.random(1,4)
				hag_genlight(ix2,j,dir,F)
				end
			end
		end
	}
	VLib.CreateTimer(1000,-1000,true,tmact)
end

function hag_genlight(ix,iy,index,F)	--制造每个子闪电效果
	local F_sub=F:getInstance()
	local stx
	local sty
	local angle
	if index==1 then		--根据坐标和方向索引计算闪电起始坐标与方向
		stx=ix*100-100
		sty=iy*100-100
		angle=VLib.PI/4
	elseif index==2 then
		stx=ix*100
		sty=iy*100
		angle=VLib.PI*5/4
	elseif index==3 then
		stx=ix*100
		sty=iy*100-100
		angle=VLib.PI*3/4
	elseif index==4 then
		stx=ix*100-100
		sty=iy*100
		angle=VLib.PI*7/4
	end
	local rc=5
	F_sub:setLoc(stx,sty)
	F_sub:setAngle(angle)
	local tmcount=0
	local tmact={			--生成每道子闪电
		action=function()
			local rnd = math.random(0,1)
			if rnd==1 then
				rc=2
			else
				rc=5
			end
			local sp=F_sub:creator()
			sp:setSpeed(math.random()*140)
			sp:setImageIndex(rc,rc)
			sp:spBorn()
		end
	}
	VLib.CreateTimer(50,800,true,tmact)
end

function hag_crystal()		--冰阶段水晶
	UnitFactory:setImage("Hag_crystal.png")
	UnitFactory:setInvincible(false)
	UnitFactory:setIfCollD(true)
	UnitFactory:setLifeTime(-1)
	UnitFactory:setLife(50)
	UnitFactory:setRad(10)
	local unit = UnitFactory:creator()
	unit:setCor(200,40)
	icecrystal[1]=unit
	unit = UnitFactory:creator()
	unit:setCor(200,560)
	icecrystal[2]=unit
	unit = UnitFactory:creator()
	unit:setCor(40,300)
	icecrystal[3]=unit
	unit = UnitFactory:creator()
	unit:setCor(360,300)
	icecrystal[4]=unit
	local checktm={		--检测水晶状态
		action=function()
			local alldie=true
			local dienum=0
			for i=1,4,1 do
				if icecrystal[i].alive then
					alldie=false
				else
					dienum=dienum+1
				end
			end
			if dienum>=speech_icephase then
				BossSpeech:soundPlay("VO_DS_HAGARA_CRYSTALDEAD_0"..speech_icephase..".mp3")
				speech_icephase=speech_icephase+1
			end
			if alldie then
				runtime=0
				phase=3
				boss:removeBuff("invincible")
				boss:appendBuff("weakness")
				GlobalEvent:phaseTransition()
			end
		end
	}
	VLib.CreateTimer(50,-1000,true,checktm)
end

function hag_icewave()		--冰浪
	hag_icewavecenter()		--中心部分
	hag_icepillar()			--冰柱
	local F=SpFactory:getInstance()
	VLib.setFactoryImage(F,"SOval")
	F:setMode(0)
	F:setImageIndex(5,5)
	F:setRestricted(false)
	F:setLife(0.5)
	F:setDamage(90)
	F:setSpeed(40)
	local tmcount=0
	local tmact={
		action=function()
			local wx,wy
			for i=1,4,1 do
				if tmcount<=30 then
					wx,wy=VLib.PolarMove(200,300,i*VLib.PI/2,10*tmcount)
				else
					wx,wy=VLib.PolarMove(200,300,i*VLib.PI/2+(tmcount-30)*VLib.PI/80,300)	--转速
				end
				wplist[i]={wx,wy}
			end
			tmcount=tmcount+1
		end
	}
	VLib.CreateTimer(100,-1000,true,tmact)
	local tmact_1={
		action=function()
		local spx,spy
		local gen_a
			for i=1,4,1 do
				gen_a=VLib.GetAngleBetween2Points(wplist[i][1],wplist[i][2],200,300)
				for j=-1,4,1 do
					if tmcount<=30 then
						spx,spy=VLib.PolarMove(wplist[i][1],wplist[i][2],gen_a,j*tmcount*1.66)
					else
						spx,spy=VLib.PolarMove(wplist[i][1],wplist[i][2],gen_a,40*j)
					end
					spx,spy=VLib.PolarMove(spx,spy,math.random()*VLib.PI/3-VLib.PI/6+gen_a,math.random()*30)
					local rc=math.random(5,6)
					F:setLoc(spx,spy)
					F:setImageIndex(rc,rc)
					local ra=math.random(0,1)*VLib.PI/6
					for k=0,2,1 do
						local sp=F:creator()
						sp:setAngle(VLib.PI*2/3*k+ra)
						sp:spBorn()
					end
				end
			end
		end
	}
	VLib.CreateTimer(100,-1000,true,tmact_1)
end

function hag_icepillar()		--冰柱
	local F=SpFactory:getInstance()
	VLib.setFactoryImage(F,"MSMix")
	F:setDamage(15)
	F:setMode(0)
	F:setInvincible(true)
	F:setImageIndex(6,6)
	F:setLife(4)
	F:setSpeed(20)
	local tmcount=0
	local tmact={
		action=function()
			local g=luajava.newInstance("entities.VSpriteGroup")
			local gx,gy=hag_getpillarloc()
			F:setLoc(gx,gy)
			for i=0,15,1 do
				local sp=F:creator()
				sp:setAngle(VLib.PI/8*i+VLib.PI/16)
				sp:spBorn()
				g:addSprite(sp)
			end
			local stopact={
				action=function()
					local v=g:getSpeed()
					v:coverValue(0)
					g:setSpeed(v)
				end
			}
			VLib.CreateTimer(2000,2000,false,stopact)
		end
	}
	VLib.CreateTimer(1500,-1000,true,tmact)
end

function hag_getpillarloc()
	local hx,hy
	local px=pc:GetX()
	local py=pc:GetY()
	local angle=VLib.GetAngleBetween2Points(200,300,px,py)
	local ifclose=true		--过近标记
	while ifclose do
		ifclose=false
		rnd=math.random(0,1)*2-1		-- -1或1的随机数
		hx,hy=VLib.PolarMove(200,300,angle+(VLib.PI/6+math.random()*VLib.PI/3),math.random(100,300))	--初次取值
		hx,hy=VLib.restrictPoint(hx,hy,2,2,398,598)			--限制范围
	end		--直到成功获取一个点
	return hx,hy
end

function hag_icewavecenter()	--冰浪中心
	local F=SpFactory:getInstance()
	VLib.setFactoryImage(F,"SMix")
	F:setMode(0)
	F:setLoc(200,300)
	F:setImageIndex(5,5)
	F:setLife(6)
	F:setDamage(20)
	F:setSpeed(20)
	local tmcount=0
	local tmact={
		action=function()
			local g1=luajava.newInstance("entities.VSpriteGroup")
			local g2=luajava.newInstance("entities.VSpriteGroup")
			for i=0,7,1 do
				local sp=F:creator()
				sp:setAngle(VLib.PI/4*i)
				if tmcount%2==0 then
					g1:addSprite(sp)
				else
					g2:addSprite(sp)
				end
				sp:spBorn()
			end
			local angleact={
				action=function()
					g1:PolarCoorTrans(200,300,VLib.PI/96)
					g2:PolarCoorTrans(200,300,-VLib.PI/96)
				end
			}
			VLib.CreateTimer(50,6000,true,angleact)
			tmcount=tmcount+1
		end
	}
	VLib.CreateTimer(300,-1000,true,tmact)
end

function hag_icegrave()	--冰墓
	BossSpeech:soundPlay("VO_DS_HAGARA_ICETOMB_01.mp3")
	local px=pc:GetX()
	local py=pc:GetY()
	local F=SpFactory:getInstance()
	VLib.setFactoryImage(F,"MSMix")
	F:setMode(3)
	F:setImageIndex(5,5)
	F:setRestricted(false)
	F:setInvincible(true)
	F:setLife(4)
	F:setSpeed(0)
	F:setDamage(40)
	F_snow=F:getInstance()
	VLib.setFactoryImage(F_snow,"SOval")
	F_snow:setSpeed(10)
	F_snow:setMode(0)
	F_snow:setLife(6)
	F_snow:setDamage(20)
	local tmcount=0
	local s_angle=math.random()*VLib.PI*2	--初始顶点方向
	local deathact={					--冰墓外圈死亡行为
		action=function(dsp)
			local dx=dsp:GetX()
			local dy=dsp:GetY()
			local da=dsp:getAngle()
			local dtm={
				action=function()
					local sp_d=F:creator()
					sp_d:setCor(dx,dy)
					sp_d:setAngle(da-math.random(0,2)*VLib.PI/6)
					sp_d:setSpeed(30)
					sp_d:spBorn()
				end
			}
			VLib.CreateTimer(150,450,true,dtm)
		end
	}
	UnitFactory:setImage("icetomb_unit.png")
	UnitFactory:setRad(10)
	UnitFactory:setLife(40)
	UnitFactory:setLifeTime(5)
	UnitFactory:setInvincible(false)
	local unitdeath={
		action=function(dsp)
			local x=dsp:GetX()
			local y=dsp:GetY()
			local a=dsp:getAngle()
			for i=0,3,1 do
				for j=0,2,1 do
					local sp = F:creator()
					sp:setCor(x,y)
					sp:setAngle(a-VLib.PI/3*i)
					sp:setSpeed(10+j*10)
					sp:spBorn()
				end
			end
		end
	}
	local tmact={
		action=function()					--生成外圈
			local unit_n = math.modf(tmcount/10)  --单位顶点索引
			local unit_i = tmcount%10			--单位位移索引0-9
			local n = math.modf(tmcount/10)		--顶点索引0-2
			local index=tmcount%10		--位移索引0-9
			local pt_angle=s_angle-VLib.PI/3*n
			local ptx,pty=VLib.PolarMove(px,py,pt_angle,100)
			ptx,pty=VLib.PolarMove(ptx,pty,pt_angle-VLib.PI*2/3,10*index)
			if unit_i%2==0 then		--每边生成5个单位
				local u=UnitFactory:creator()
				u:setCor(ptx,pty)
				u:setAngle(pt_angle-VLib.PI*2/3)
				VLib.addDeathAction(u,unitdeath)
			end
			local sp=F:creator()
			sp:setCor(ptx,pty)
			sp:setAngle(pt_angle-VLib.PI*2/3)
			--VLib.addSPAction(sp,deathact)
			sp:spBorn()
			tmcount=tmcount+1
		end
	}
	VLib.CreateTimer(20,1200,true,tmact)
	--------------外围------------------------
	local tmdelay={
		action=function()
			local to_count=0
			local tmout={
				action=function()
					local o_d=140+to_count*30
					local snowx,snowy
					for oa=0,5,1 do
						local o_angle=oa*VLib.PI/3+s_angle
						for i=-3,3,1 do
							snowx,snowy=VLib.PolarMove(px,py,o_angle,o_d)
							snowx,snowy=VLib.PolarMove(snowx,snowy,o_angle+VLib.PI/2,i*to_count*10)
							snowx,snowy=VLib.PolarMove(snowx,snowy,math.random()*VLib.PI*2,math.random()*10+10)
							hag_grave_sub(snowx,snowy,F_snow)
						end
					end
					to_count=to_count+1
				end
			}
			VLib.CreateTimer(400,2400,true,tmout)
		end
	}
	VLib.CreateTimer(1500,1500,false,tmdelay)
end

function hag_grave_sub(x,y,F)
	local ra=math.random(0,1)
	for i=0,2,1 do
		local sp=F:creator()
		if ra==1 then
			sp:setImageIndex(6,6)
		end
		sp:setCor(x,y)
		sp:setAngle(VLib.PI*2/3*i+VLib.PI/6+VLib.PI/3*ra)
		sp:spBorn()
	end
end

function hag_icespear()	--冰霜长矛
	if icespearcount>0 then		--控制语音
		local ici = icespearcount%3+1
		BossSpeech:soundPlay("VO_DS_HAGARA_FROSTRAY_0"..ici..".mp3")
		icespearcount=-icespearcount
	end

	local plist={{200,590},{20,300},{380,300}}		--碎冰起始点
	for i=1,3,1 do
		hag_createice(plist[i][1],plist[i][2])		--
	end
	local tmact={
		action=function()
			for i=1,3,1 do
				hag_icesub(plist[i][1],plist[i][2])		--
			end
		end
	}
	VLib.CreateTimer(700,2800,true,tmact)
end

function hag_createice(x,y)
	UnitFactory:setImage("Hag_smallcrystal.png")
	UnitFactory:setRad(10)
	UnitFactory:setInvincible(true)
	UnitFactory:setLifeTime(3)
	local unit_is = UnitFactory:creator()
	unit_is:setCor(x,y)
end

function hag_icesub(x,y)
	local px=pc:GetX()
	local py=pc:GetY()
	local angle = VLib.GetAngleBetween2Points(x,y,px,py)
	local F=SpFactory:getInstance()
	VLib.setFactoryImage(F,"SOval")
	F:setMode(0)
	F:setImageIndex(6,6)
	F:setLife(5)
	F:setDamage(10)
	local F_path=F:getInstance()
	F:setRestricted(false)
	F:setInvincible(true)
	F:setSpeed(300)
	F:setAngle(angle)
	F_path:setLife(1)
	F_path:setSpeed(20)
	--------生成冰块-----------------------
	for j=-0,3,1 do
		for i=0,3,1 do
			local sp=F:creator()
			local spx,spy
			local pn=0
			if j>1 then
				pn=1
			end
			spx,spy=VLib.PolarMove(x,y,VLib.PI*pn+angle,23-7*i)
			spx,spy=VLib.PolarMove(spx,spy,angle+(j%2*2-1)*VLib.PI/2,3*i)
			sp:setCor(spx,spy)
			sp:spBorn()
		end
	end
	local spc=F:creator()
	spc:setCor(x,y)
	VLib.setSpriteImage(spc,"MSMix")
	spc:spBorn()
	--------生成路径---------------------------
	local tmact={
		action=function()
			local tmx=spc:GetX()
			local tmy=spc:GetY()
			tmx,tmy=VLib.PolarMove(tmx,tmy,math.random()*VLib.PI/2,math.random()*30)
			local ra=math.random(0,1)*VLib.PI/6
			local rc=math.random(5,6)
			for i=0,5,1 do
				local sp=F_path:creator()
				sp:setCor(tmx,tmy)
				sp:setImageIndex(rc,rc)
				sp:setAngle(i*VLib.PI/3+ra)
				sp:spBorn()
			end
		end
	}
	VLib.CreateTimer(150,3000,true,tmact)
end

function hag_cut()			--切割
	local bx=boss:GetX()
	local by=boss:GetY()
	local px=pc:GetX()
	local py=pc:GetY()
	local angle = VLib.GetAngleBetween2Points(bx,by,px,py)
	local dist = VLib.GetDistanceBetween2Points(bx,by,px,py)
	local F=SpFactory:getInstance()
	VLib.setFactoryImage(F,"SOval")
	F:setMode(0)
	F:setImageIndex(1,1)
	F:setLife(5)
	F:setSpeed(300)
	local tmcount=0
	local tmact={
		action=function()
			local cx=boss:GetX()
			local cy=boss:GetY()
			local pcx=pc:GetX()
			local pcy=pc:GetY()
			local f_angle = VLib.GetAngleBetween2Points(cx,cy,pcx,pcy)
			F:setLoc(cx,cy)
			for i=-3,3,1 do
				for j=1,4,1 do
					local sp=F:creator()
					sp:setAngle(f_angle+i*VLib.PI/16)
					sp:setSpeed(300+(j-1)*40)
					sp:spBorn()
				end
			end
			tmcount=tmcount+1
		end
	}
	VLib.CreateTimer(400,2000,true,tmact)
end

function hag_attack01()
	local bx=boss:GetX()
	local by=boss:GetY()
	local px=pc:GetX()
	local py=pc:GetY()
	local angle = VLib.GetAngleBetween2Points(bx,by,px,py)
	local dist = VLib.GetDistanceBetween2Points(bx,by,px,py)
	local F=SpFactory:getInstance()
	VLib.setFactoryImage(F,"SMix")
	F:setMode(0)
	F:setImageIndex(6,6)
	F:setLife(6)
	F:setSpeed(100)
	F:setDamage(5)
	F_ice=F:getInstance()
	--VLib.setFactoryImage(F_ice,"SOval")
	F_ice:setLife(4)
	F_ice:setSpeed(30)
	F_ice:setMode(0)
	local gen_index =hag_01_count%2*2-1
	local tmcount=0
	local tmact={
		action=function()
				bx=boss:GetX()
				by=boss:GetY()
				local spx,spy=VLib.PolarMove(bx,by,VLib.PI/25*tmcount*gen_index,50)
				local sp=F:creator()
				sp:setCor(spx,spy)
				sp:setAngle(VLib.PI/25*tmcount+VLib.PI/3*tmcount)
				sp:setASpeed(VLib.PI/9*gen_index)
				sp:spBorn()
				tmcount=tmcount+1
		end
	}
	VLib.CreateTimer(40,4000,true,tmact)
	hag_01_count=hag_01_count+1
end

function hag_focusstrike()		--专注打击
	focusstate=1
	local bx=boss:GetX()
	local by=boss:GetY()
	local px=pc:GetX()
	local py=pc:GetY()
	local angle = VLib.GetAngleBetween2Points(bx,by,px,py)
	local dist = VLib.GetDistanceBetween2Points(bx,by,px,py)
	local F=SpFactory:getInstance()
	F:setMode(0)
	VLib.setFactoryImage(F,"SScale")
	F:setImageIndex(6,6)
	F:setLife(3)
	F:setDamage(30)
	F:setSpeed(50)
	local F_b = F:getInstance()
	local F_a = F:getInstance()
	VLib.setFactoryImage(F_a,"MSFlat")
	VLib.setFactoryImage(F_b,"SPoint")
	F_a:setSpeed(120)
	F_b:setSpeed(20)
	F_b:setLife(2)
	local tmcount=0
	local tmact={
		action=function()
			bx=boss:GetX()
			by=boss:GetY()
			px=pc:GetX()
			py=pc:GetY()
			tar_x = px
			tar_y = py
			tar_angle = VLib.GetAngleBetween2Points(bx,by,px,py)

			tmcount=tmcount+1
			F:setLoc(bx,by)
			F_a:setLoc(bx,by)
			F_b:setLoc(bx,by)
			for i=0,2,1 do
				for j=0,2,1 do
				local sp = F:creator()
				local imageindex=tmcount%4+3
				sp:setImageIndex(imageindex,imageindex)
				sp:setASpeed(-VLib.PI/4+VLib.PI/4*j)
				sp:setAngle(tar_angle-VLib.PI/12+VLib.PI/12*i)
				sp:spBorn()
				end
			end
			if tmcount%2==0 then
				for i=0,2,1 do
					local sp = F_a:creator()
					sp:setAngle(tar_angle-VLib.PI/3+VLib.PI/3*i)
					sp:spBorn()
				end
			end
			for i=0,15,1 do
				local sp = F_b:creator()
				sp:setAngle(tar_angle+VLib.PI/4*i+math.random()*VLib.PI/8)
				sp:spBorn()
			end
			if tmcount==20 then
				focusstate=0
			end
		end
	}
	VLib.CreateTimer(100,2000,true,tmact)
end
