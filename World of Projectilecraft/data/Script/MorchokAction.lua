dofile("data/Script/Lib/VLib.lua")
Debug=luajava.bindClass("global.Debug")
VEngine=luajava.bindClass("system.VEngine")
VStage=luajava.bindClass("stage.VStage")
VTimer=luajava.bindClass("timer.VTimer")
SpFClass=luajava.bindClass("factory.SpriteFactory")
SpFactory=SpFClass:getInstance()
GameData=luajava.bindClass("data.GameData")
GlobalEvent=luajava.bindClass("event.GlobalEvent")
Soundconst=luajava.bindClass("global.Soundconst")

local runtime = 0
local phase = 0 	--战斗阶段
local p1 = {200,100}
local p2 = {100,200}
local p3 = {200,300}
local p4 = {300,200}	--移动位置
local plist ={p1,p2,p3,p4}
local angle = 0
local pillarlist={}		--石柱坐标表
local palist={}			--石柱角度表
local pindex=0
local ar=0

local mck

local tar_x = 100
local tar_y = 200
local tar_index = 2
local movestate={
	count=0
}

function mck_init()	--初始化
	runtime = 0
	phase = 0
	tar_x = 100
	tar_y = 200
	tar_index = 2
	movestate.count=0
	angle = 0
	pillarlist={}
	palist={}
	pindex=0
	ar=0
end

MorchokAction={

setBoss=function(boss)		--定义设置Boss函数
	mck=boss
	mck_init()
end,

action = {			--以Action形式定义Boss的行为，行为将在每次游戏状态更新时更新
	action=function()
		if phase==0 then			--P1
			mck_move(mck)
			if runtime==1 then
				mck:setPhase(0)
			end
			if runtime%1000==0 then
				--mck_gen_01(mck)
			end
			if runtime%50==1 then
				--mck_gen_CrackArmor(mck)
			end
			if runtime%1200==200 then
				mck_gen_Crystal(mck)
			end
			if runtime%1200==600 then
				mck_gen_Stomp(mck)
			end
			if runtime==2500 then
				phase=1		--转P2
				runtime=0
				GlobalEvent:phaseTransition()
				mck:setCor(200,200)
			end
		elseif phase==1 then
			if runtime==1 then
				mck:setPhase(1)
				BossSpeech:soundPlay("VO_DS_MORCHOK_GROUND2_01.mp3")
			end
			if runtime==200 then		--石柱
				mck_gen_Pillar(mck)
			end
			if runtime==450 then
				mck_gen_BlackBlood(mck,pillarlist)	--大地黑血
			end
			if runtime==1300 then
				phase=0
				runtime=0
				GlobalEvent:phaseTransition()
			end
		end
		--SpFactory:setImageIndex(9,9)
		--local sp = SpFactory:creator()
		--sp:setAngle(runtime*runtime*VLib.PI*0.0001)
		--sp:spBorn()
		runtime=runtime+1
	end
}

}

function mck_move(mck)		--移动函数
	mck_moveTo(mck,0.5,tar_x,tar_y)
	if movestate.count>200 then	--决定在每个点的停留时间
		movestate.count=0
		tar_index=tar_index+1
		if tar_index>=5 then
			tar_index=1
		end
		local tmplist=plist[tar_index]
		local tx = tmplist[1]
		local ty = tmplist[2]
		tar_x,tar_y=VLib.PolarMove(tx,ty,math.random(0,VLib.PI*2),math.random(10,30))
	end
end

function mck_moveTo(mck,speed,x,y)	--移动到
	local mx = mck:GetX()
	local my = mck:GetY()
	local tx=0
	local ty=0
	local angle =VLib.GetAngleBetween2Points(mx,my,x,y)
	local dist = VLib.GetDistanceBetween2Points(mx,my,x,y)
	if dist<=speed then
		tx=x
		ty=y
		movestate.count=movestate.count+1
	else
		tx,ty = VLib.PolarMove(mx,my,angle,speed)
	end
	mck:setCor(tx,ty)
end



function mck_gen_01(mck)
	local f_num=0
	c = math.random(5,9)
	SpFactory:setImageIndex(c,c)
	local tmac_01 = {
		action = function()
			GenCircle(mck)
		end
	}
	local tmac_proxy=luajava.createProxy("timer.VLuaAction", tmac_01)	--通过代理形式将定义的行为函数转化为实现了VLuaAction接口的对象
	local tm = luajava.newInstance("timer.VTimer",250,250,false,tmac_proxy)	--使用上述代理对象新建Timer对象
	tm:timerStart()  --启动Timer
end

function GenCircle(mck)
	local x=mck:GetX()
	local y=mck:GetY()
	local F_laser = SpFactory:getInstance()
	VLib.setFactoryImage(F_laser,"SCard")
	F_laser:setMode(0)
	F_laser:setRotateMode(2)
	F_laser:setImageIndex(0,0)
	F_laser:setSpeed(50)
	F_laser:setLife(15)
	F_laser:setASpeed(0)
	F_laser:setLoc(x,y)
	for i=1,8,1 do
		local sp = F_laser:creator()
		local newx,newy =VLib.PolarMove(x,y,VLib.PI/4*i+VLib.PI/2,50)
		sp:setCor(newx,newy)
		angle = VLib.PI/4*i+VLib.PI/8
		sp:setAngle(angle)
		sp:spBorn()
	end
end

function mck_gen_CrackArmor(mck)
	local x=mck:GetX()
	local y=mck:GetY()
	local px=GameData.pc:GetX()
	local py=GameData.pc:GetY()
	local angle=VLib.GetAngleBetween2Points(x,y,px,py)
	local F=SpFactory:getInstance()
	F:setLoc(x,y)
	VLib.setFactoryImage(F,"SHex")
	F:setImageIndex(0,0)
	F:setLife(20)
	F:setSpeed(80)
	for i=0,7,1 do
		for	j=0,9,1 do
			local sp = F:creator()
			sp:setAngle(i*VLib.PI/4+j*VLib.PI/30)
			sp:setSpeed(100+j*5)
			sp:setImageIndex(j,j)
			sp:spBorn()
		end
	end
end

function mck_gen_Crystal(mck)	--创建水晶
	local F = SpFactory:getInstance()
	VLib.setFactoryImage(F,"SMix")
	F:setSpeed(0)
	F:setImageIndex(0,0)
	F:setDamage(2)
	F:setLife(6)
	--------------------------
	local x = mck:GetX()
	local y = mck:GetY()
	local angle = math.random()*VLib.PI*2	--水晶角度
	local cx,cy=VLib.PolarMove(x,y,angle,math.random(50,100))	--水晶位
	VLib.restrictPoint(cx,cy,20,380,20,580)
	local dist = VLib.GetDistanceBetween2Points(x,y,cx,cy)
	local cpath_count=0
	local tm_cpath = {	--创建水晶路线并散开效果
		action = function()

		end
	}
	VLib.CreateTimer(30,600,true,tm_cpath)
	local hexgroup = VLib.CreateSpGroup(nil,0)	--水晶粒子包
	local genC_count = 0
	local tm_genC = {		--创建六角水晶
		action = function()
			local hex_a,hex_i =math.modf(genC_count/6)		--hex_a=0~2,hex_i=0~19
			local h_angle = angle - hex_a*VLib.PI*2/3	--顶点角度
			local h_angle_0 = h_angle + VLib.PI
			local spx,spy = VLib.PolarMove(cx,cy,h_angle,40)	--计算顶点坐标
			spx,spy = VLib.PolarMove(spx,spy,h_angle-VLib.PI*5/6,69.28*hex_i)	--沿边线移动
			local sp = F:creator()
			sp:setCor(spx,spy)
			sp:setAngle(h_angle-VLib.PI/3+VLib.PI/2+VLib.PI*hex_i)
			sp:spBorn()
			hexgroup:addSprite(sp)
			spx,spy = VLib.PolarMove(cx,cy,h_angle_0,40)	--计算顶点坐标
			spx,spy = VLib.PolarMove(spx,spy,h_angle_0-VLib.PI*5/6,69.28*hex_i)	--沿边线移动
			local sp = F:creator()
			sp:setCor(spx,spy)
			sp:setAngle(h_angle_0-VLib.PI/3+VLib.PI/2+VLib.PI*hex_i)
			sp:spBorn()
			hexgroup:addSprite(sp)
			genC_count = genC_count+1
		end
	}
	VLib.CreateTimer(40,720,true,tm_genC)
	local tm_spreadC = {	--水晶扩散
		action = function()
			local vl_a = hexgroup:getSpeed()
			vl_a:coverValue(15)
			hexgroup:setSpeed(vl_a)
		end
	}
	VLib.CreateTimer(1000,1000,false,tm_spreadC)
	local F_blast = SpFactory:getInstance()
	F_blast:setSpeed(70)
	F_blast:setLife(5)
	F_blast:setImageIndex(5,5)
	VLib.setFactoryImage(F_blast,"MSMix")
	local tmac_02 = {	--水晶引爆，根据距离远近创建弹幕
		action = function()
			local px= GameData.pc:GetX()
			local py= GameData.pc:GetY()	--自机位
			local dist=VLib.GetDistanceBetween2Points(px,py,cx,cy)
			local comp=dist/10		--根据距离计算弹幕复杂度
			if comp<4 then
				comp=4
			elseif comp>30 then
				comp=30
			end
			local blast_count = 0
			local tm_blast = {		--爆炸生成弹幕
				action=function()
					F_blast:setDamage(comp)		--伤害和距离关联
					for i=0,5,1 do
						local h_angle = angle - i*VLib.PI/3	--顶点角度
						local spx,spy = VLib.PolarMove(cx,cy,h_angle,400)	--计算顶点坐标
						spx,spy = VLib.PolarMove(spx,spy,h_angle-VLib.PI*5/6,69.28*blast_count)	--沿边线移动
						F_blast:setLoc(spx,spy)
						F_blast:setASpeed((blast_count%2*2-1)*VLib.PI/6)
						F_blast:setSpeed(60+comp*2)
						for j=0,comp,1 do
							local sp = F_blast:creator()
							sp:setAngle(j*VLib.PI*2/comp)
							sp:spBorn()
						end
					end
					blast_count=blast_count+1
				end
			}
			VLib.CreateTimer(200,2000,true,tm_blast)
		end
	}
	local tmac_proxy_02=luajava.createProxy("timer.VLuaAction", tmac_02)	--通过代理形式将定义的行为函数转化为实现了VLuaAction接口的对象
	local tm_02 = luajava.newInstance("timer.VTimer",7000,7000,false,tmac_proxy_02)	--使用上述代理对象新建Timer对象
	tm_02:timerStart()  --启动Timer
end

function mck_gen_Stomp(mck)   --践踏
	local F=SpFactory:getInstance()
	F:setImageIndex(9,9)
	VLib.setFactoryImage(F,"SStar")
	F:setLife(4)
	F:setSpeed(50)
	F:setDamage(30)
	-------------------------------------
	local tmcount=0
	local tmst_act={
		action=function()
			local x=mck:GetX()
			local y=mck:GetY()
			F:setLoc(x,y)
			F:setASpeed((tmcount%3-1)*VLib.PI/4)
			for i=0,7,1 do
				local sp = F:creator()
				sp:setAngle(VLib.PI*i/4+VLib.PI/32*tmcount)
				sp:spBorn()
			end
			tmcount=tmcount+1
		end
	}
	VLib.CreateTimer(60,1200,true,tmst_act)
end

function mck_gen_Pillar(mck)	--生成石柱
	local F = SpFactory:getInstance()
	VLib.setFactoryImage(F,"MRad")
	F:setImageIndex(7,7)
	F:setInvincible(true)
	pillarlist={}
	palist={}
	pindex=0
	local tmact={
		action = function()
			pindex=pindex+1
			local x=mck:GetX()
			local y=mck:GetY()
			local sp = F:creator()
			local angle=math.random()*VLib.PI*2
			palist[pindex]=angle
			local point={}	--石柱目标点
			point[1],point[2]=VLib.PolarMove(x,y,angle,120)
			pillarlist[pindex]=point
			sp:setCor(x,y)
			sp:setAngle(angle)
			sp:setSpeed(400)
			sp:setLife(19)
			sp:spBorn()
			local tmact_sub={
				action=function()
					local spd=sp:getSpeed()-80
					sp:setSpeed(spd)
				end
			}
			VLib.CreateTimer(100,500,true,tmact_sub)
		end
	}
	local tmact_proxy=luajava.createProxy("timer.VLuaAction", tmact)
	local tm = luajava.newInstance("timer.VTimer",300,2400,true,tmact_proxy)
	tm:timerStart()
end

function mck_gen_BlackBlood(mck,pillarlist)	--大地黑血
	local F = SpFactory:getInstance()
	VLib.setFactoryImage(F,"SDefault")
	F:setImageIndex(9,9)
	F:setSpeed(120)
	F:setLife(6)
	F:setDamage(20)
	local tmact={
		action=function()
			local splist={}
			local x=mck:GetX()
			local y=mck:GetY()
			local r=math.random()*VLib.PI
			F:setLoc(x,y)
			for i=0,63,1 do
				local sp=F:creator()
				splist[i]=sp
				sp:setAngle(VLib.PI/32*i+r)
				sp:spBorn()
			end
			local tmsub={
				action=function()
					--local cx=pillarlist[i][1]
					--local cy=pillarlist[i][2]
					for j=0,63,1 do
						local a=splist[j]:getAngle()
						local alive=1
						for i=1,8,1 do
							if math.abs(a-palist[i])<VLib.PI/48 then	--如果角度与石柱角度差足够小
								splist[j]:die()
								alive=0
							end
						end
						if alive==2 then
							local nx=splist[j]:GetX()
							local ny=splist[j]:GetY()
							local newsp1=F:creator()
							newsp1:setCor(nx,ny)
							newsp1:setAngle(a+VLib.PI/64)
							newsp1:spBorn()
							local newsp2=F:creator()
							newsp2:setCor(nx,ny)
							newsp2:setAngle(a-VLib.PI/64)
							newsp2:spBorn()
						end
					end
				end
			}
			local tmsub_proxy=luajava.createProxy("timer.VLuaAction", tmsub)
			local tmsub01=luajava.newInstance("timer.VTimer",1000,1000,false,tmsub_proxy)
			tmsub01:timerStart()
		end
	}
	local tmact_proxy=luajava.createProxy("timer.VLuaAction", tmact)
	local tm=luajava.newInstance("timer.VTimer",100,14000,true,tmact_proxy)
	tm:timerStart()
end
