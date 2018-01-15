dofile("data/Script/Lib/VLib.lua")
dofile("data/Script/Lib/PathPoint.lua")
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
local boss			--Boss对象
local phase=0		--战斗阶段
local pc=GameData:getPlayer()
local slimeget={0,0,0,0,0,0}		--吸收血球状态，黄蓝红黑绿紫
local vacount=0		--虚空箭计数,控制虚空箭数量和角度
local slimetime=0	--软泥计数，控制软泥方向
local namelist={"slime_yellow.png","slime_blue.png","slime_red.png","slime_black.png","slime_green.png","slime_purple.png"}		--黄蓝红黑绿紫

local point=PathPoint:new{}
point.perodic=true
point:setNodeList({{100,150,0,30},{200,100,0,30},{300,150,0,30},{200,100,0,30}})	--法力球路径

function yor_init()
	runtime=0
	phase=1
	slimeget={0,0,0,0,0,0}
	point=PathPoint:new{}
	point.perodic=true
	point:setNodeList({{100,150,0,30},{200,100,0,30},{300,150,0,30},{200,100,0,30}})
	slimetime=0
	vacount=0
end

YorsahjAction={

setBoss=function(b)		--定义设置Boss函数
	boss=b
	pc=GameData:getPlayer()
	yor_init()
end,

action = {			--以Action形式定义Boss的行为，行为将在每次游戏状态更新时更新
	action=function()
		if phase==0 then			--P1
			bossmove()				--boss的移动函数
			if runtime%300==1 then
				--yor_action_voidarrow()	--虚空箭
			end
			if runtime%600==1 then
				if slimetime>0 then
					if slimetime%2==0 then
						BossSpeech:soundPlay("VO_DS_YORSAHJ_SPELL_02.mp3")
					else
						BossSpeech:soundPlay("VO_DS_YORSAHJ_SPELL_03.mp3")
					end
				end
				yor_action_attack()		--普通
			end
			if runtime==600 then	--6秒后进入软泥阶段
				runtime=0
				phase=2
				GlobalEvent:phaseTransition()
			end
		elseif phase==1 then
			bossmove()
			point:move()
			if slimeget[1]==1 then			--黄
				if runtime%800==1 then
					yor_action_yellow()
				end
			end
			if slimeget[2]==1 then			--蓝
				if runtime==1 then
					yor_action_blue()
				end
			end
			if slimeget[3]==1 then			--红
				if runtime%800==1 then
					yor_action_red()
				end
			end
			if slimeget[4]==1 then			--黑
				if runtime==1 then
					yor_action_black()
				end
			end
			if slimeget[5]==1 then			--绿
				if runtime%800==1 then
					yor_action_green()
				end
			end
			if slimeget[6]==1 then			--紫
				if runtime%800==1 then
					yor_action_purple()
				end
			end
			if runtime%10000==1 then		--测试
				--yor_action_yellow()
				--yor_action_red()
				yor_action_green()
				--yor_action_blue()
				--yor_action_purple()
				--yor_action_black()
			end
			if runtime==3000 then
				runtime=0
				phase=0
				GlobalEvent:phaseTransition()
			end
		elseif phase==2 then		--软泥时间
			bossmove()
			if runtime==1 then
				BossSpeech:soundPlay("VO_DS_YORSAHJ_SPELL_01.mp3")
				yor_genslime()
			end
			if runtime==1000 then
				runtime=0
				phase=1
				GlobalEvent:phaseTransition()
			end
		end
		runtime=runtime+1
	end
}

}

function bossmove()
	boss:setCor(200,300)
end

function yor_genslime()		--软泥阶段
	slimeget={0,0,0,0,0,0}		--重置吸收标记
	UnitFactory:setLife(400)
	UnitFactory:setLifeTime(10)
	UnitFactory:setRad(10)
	local randslime={}
	for i =1,3,1 do
		local rep=true		--重复标记
		local newindex		--随机获取软泥索引
		while rep do
			rep=false
			newindex=math.random(6)
			for j=1,i-1,1 do
				if randslime[j]==newindex then
					rep=true
				end
			end
		end
		randslime[i]=newindex
	end
	local slimelist={}		--软泥列表
	local tmstat={
		action=function()
			for i=1,3,1 do
				local s_angle = i*VLib.PI*2/3+slimetime%2*VLib.PI/3+VLib.PI*7/6
				slimelist[i]:PolarMove(s_angle,0.4)
				if slimelist[i].alive==false then			--某软泥死亡
					slimeget[randslime[i]]=0			--去除对应的吸收标记
					for j=1,3,1 do
						if j~=i then
							slimelist[j]:setInvincible(true)		--设置其它两软无敌
						end
					end
				end
			end
		end
	}
	local tmdelay={
		action=function()
			for i=1,3,1 do
				local s_angle = i*VLib.PI*2/3+slimetime%2*VLib.PI/3+VLib.PI/6
				local sx,sy=VLib.PolarMove(200,300,s_angle,200)
				local slime = UnitFactory:creator()
				slime:setCor(sx,sy)
				slime:setImage(namelist[randslime[i]])
				slimelist[i]=slime
				slimeget[randslime[i]]=1		--将刷新的三个软泥对应的吸收标记设为1
			end
			VLib.CreateTimer(20,9980,true,tmstat)		--状态检测timer
		end
	}
	VLib.CreateTimer(10,10,false,tmdelay)

	slimetime=slimetime+1
end

function yor_action_black()		--黑软
	local bx=boss:GetX()
	local by=boss:GetY()
	local px=pc:GetX()
	local py=pc:GetY()
	local angle = VLib.GetAngleBetween2Points(bx,by,px,py)
	local dist = VLib.GetDistanceBetween2Points(bx,by,px,py)
	local F_black=SpFactory:getInstance()
	VLib.setFactoryImage(F_black,"SMix")
	F_black:setMode(0)
	F_black:setImageIndex(9,9)
	F_black:setLife(15)
	F_black:setDamage(3)
	F_black:setSpeed(100)
	UnitFactory:setImage("Yor_forgotten.png")
	UnitFactory:setLife(120)
	UnitFactory:setRad(10)
	UnitFactory:setLifeTime(-1)
	local tmact={
		action=function()
			local mob=UnitFactory:creator()
			local mob_x,mob_y=VLib.PolarMove(bx,by,math.random()*VLib.PI*2,math.random(100,200))
			mob_x,mob_y=VLib.restrictPoint(mob_x,mob_y,0,0,400,600)
			mob:setValue(1,1)
			mob:setCor(mob_x,mob_y)
			local mob_c=0
			local mpx,mpy
			local tarangle
			local mob_action={
				action=function()
					local n = mob_c%100
					local mbx=mob:GetX()
					local mby=mob:GetY()
					if n==0 then
						mpx=pc:GetX()
						mpy=pc:GetY()
						tarangle=VLib.GetAngleBetween2Points(mbx,mby,mpx,mpy)
						tarangle=tarangle-VLib.PI*0.2+VLib.PI*0.4*math.random()
					end
					if (n<=10 or (n>=40 and n<=50)) and n%10==0 then
						local sp=F_black:creator()
						sp:setCor(mbx,mby)
						local a=VLib.GetAngleBetween2Points(mbx,mby,mpx,mpy)
						sp:setAngle(a)
						sp:spBorn()
					end
					mob:PolarMove(tarangle,0.3)
					mob_c=mob_c+1
				end
			}
			VLib.addAction(mob,mob_action)
		end
	}
	VLib.CreateTimer(800,4000,true,tmact)
end

function yor_action_purple()	--紫软
	local bx=boss:GetX()
	local by=boss:GetY()
	local px=pc:GetX()
	local py=pc:GetY()
	local angle = VLib.GetAngleBetween2Points(bx,by,px,py)
	local dist = VLib.GetDistanceBetween2Points(bx,by,px,py)
	local F_purple=SpFactory:getInstance()
	VLib.setFactoryImage(F_purple,"SScale")
	F_purple:setMode(0)
	F_purple:setImageIndex(7,7)
	F_purple:setLife(15)
	F_purple:setSpeed(0)
	F_purple:setDamage(30)
	local tmcount=0
	local cu_x=200
	local cu_y=100
	local tar_x=px
	local tar_y=py
	local devi=3		--误差值
	local tmact={
		action=function()
			if tmcount%10==0 then	--决定此段跟随目标点
				tar_x=pc:GetX()
				tar_y=pc:GetY()
			end
			local move_a = VLib.GetAngleBetween2Points(cu_x,cu_y,tar_x,tar_y)
			cu_x,cu_y=VLib.PolarMove(cu_x,cu_y,move_a,7)
			cu_x,cu_y=VLib.PolarMove(cu_x,cu_y,math.random()*VLib.PI*2,math.random()*devi)		--误差移动
			local sp = F_purple:creator()
			sp:setCor(cu_x,cu_y)
			sp:setAngle(tmcount*VLib.PI/16)
			tmcount=tmcount+1
			sp:spBorn()
			local spact={
				action=function()
					sp:setSpeed(90)
					sp:setASpeed(VLib.PI/16)
					local cp_x =sp:GetX()
					local cp_y =sp:GetY()
					cp_x,cp_y = VLib.PolarMove(cp_x,cp_y,math.random()*VLib.PI*2,20)
					local cp_sp = F_purple:creator()
					cp_sp:setLife(8)
					local imr=math.random(0,2)
					if(imr==0)then
						VLib.setSpriteImage(cp_sp,"SDefault")
					elseif(imr==1)then
						VLib.setSpriteImage(cp_sp,"MSFlat")
					else
						VLib.setSpriteImage(cp_sp,"MFlat")
					end
					cp_sp:setCor(cp_x,cp_y)
					cp_sp:spBorn()
				end
			}
			VLib.CreateTimer(4000,4000,false,spact)
		end
	}
	VLib.CreateTimer(60,5000,true,tmact)
end

function yor_action_blue()		--蓝软
	UnitFactory:setLifeTime(-1)
	local bluevoid = UnitFactory:creator()
	bluevoid:setImage("Yor_manavoid.png")
	bluevoid:setLife(300)
	bluevoid:setInvincible(false)
	bluevoid:setRad(50)
	bluevoid:setValue(1,1)
	local bx=boss:GetX()
	local by=boss:GetY()
	local px=pc:GetX()
	local py=pc:GetY()
	local angle = VLib.GetAngleBetween2Points(bx,by,px,py)
	local dist = VLib.GetDistanceBetween2Points(bx,by,px,py)
	local F_blue=SpFactory:getInstance()
	VLib.setFactoryImage(F_blue,"SDefault")
	F_blue:setMode(0)
	F_blue:setImageIndex(6,6)
	F_blue:setLife(10)
	F_blue:setSpeed(100)
	F_blue:setDamage(6)
	local vcount=0
	local voidact={
		action=function()
			bluevoid:setCor(point.x,point.y)
			local countmod = math.modf(vcount/50)
			if(vcount%50==0)then
				local compli = 4--math.random(4)+2		--复杂度参数
				local speed=100--math.random()*100+50
				local sp
				F_blue:setLoc(point.x,point.y)
				for i=0,7,1 do
					angle_i = i*VLib.PI/4+countmod%2*VLib.PI/8
					for j=-2,2,1 do
						sp = F_blue:creator()
						sp:setAngle(angle_i+j*VLib.PI/20)
						sp:setSpeed(100-math.abs(j)*10)
						sp:spBorn()
					end
				end
			end
			vcount=vcount+1
		end
	}
	VLib.addAction(bluevoid,voidact)
end

function yor_action_green()	--绿软
	local bx=boss:GetX()
	local by=boss:GetY()
	local px=pc:GetX()
	local py=pc:GetY()
	local angle = VLib.GetAngleBetween2Points(bx,by,px,py)
	local dist = VLib.GetDistanceBetween2Points(bx,by,px,py)
	local F_green=SpFactory:getInstance()
	VLib.setFactoryImage(F_green,"SDefault")
	F_green:setSpeed(0)
	F_green:setMode(3)
	F_green:setLife(3)
	F_green:setDamage(5)
	F_green:setImageIndex(3,3)
	F_green:setRestricted(false)
	F_green:setInvincible(true)
	----------获取点位置表----------------------------------------
	local plist={}
	for i=0,23,1 do
		local ac_x,ac_y
		local collide=true
		while collide==true do
			ac_x,ac_y=green_getRandomPoint(px,py)		--初次获取点
			collide=false
			for j=0,i-1,1 do
				if VLib.GetSimpleDistanceMax(ac_x,ac_y,plist[j][1],plist[j][2])<=50 then
					collide=true
				end
			end
		end
		plist[i]={ac_x,ac_y}
	end
	----------粒子死亡行为----------------------------------------
	F_death=F_green:getInstance()
	F_death:setInvincible(true)
	F_death:setMode(0)
	F_death:setASpeed(VLib.PI/5)
	F_death:setDamage(25)
	local aspeed=VLib.PI/5
	VLib.setFactoryImage(F_death,"Large")
	F_death:setLife(10)
	local balllist={}
	local deathcount=0
	local deathact={
		action=function(dsp)
			local temptm={
				action=function()
					local dx=dsp:GetX()
					local dy=dsp:GetY()
					local nsp =F_death:creator()
					aspeed=-aspeed
					nsp:setASpeed(aspeed)
					nsp:setCor(dx,dy)
					nsp:spBorn()
					balllist[deathcount]=nsp
					deathcount=deathcount+1
				end
			}
			VLib.CreateTimer(10,10,false,temptm)
		end
	}
	local d_proxy=luajava.createProxy("timer.VLuaSPAction", deathact)
	-----------------------------------------------------------------
	local tmcount=0
	local tmact={
		action=function()
			local sp=F_green:creator()
			sp:setCor(plist[tmcount][1],plist[tmcount][2])
			sp:spBorn()
			sp:addDeathAction(d_proxy)
			tmcount=tmcount+1
		end
	}
	VLib.CreateTimer(50,1200,true,tmact)
	-----------------------------------------------------------------
	local tmtreestart={
		action=function()
			local g = VLib.CreateSpGroup(balllist,23)
			local angle_l = g:getAngle()
			angle_l:randomizeValue(0,VLib.PI*2)
			g:setAngle(angle_l)
			angle_l:coverValue(100)
			g:setSpeed(angle_l)
		end
	}
	VLib.CreateTimer(6000,6000,false,tmtreestart)
end

function green_getRandomPoint(px,py)
	local x,y = VLib.PolarMove(px,py,math.random()*VLib.PI*2,math.random(10,400))
	x,y=VLib.restrictPoint(x,y,0,0,400,600)
	return x,y
end

function green_tree()

end

function yor_action_yellow()	--黄软
	local bx=boss:GetX()
	local by=boss:GetY()
	local px=pc:GetX()
	local py=pc:GetY()
	local angle = VLib.GetAngleBetween2Points(bx,by,px,py)
	local dist = VLib.GetDistanceBetween2Points(bx,by,px,py)
	for i=0,1,1 do
		local stx=math.random(50,350)
		local sty=math.random(25,75)
		local r_angle=math.random()*VLib.PI*0.5+VLib.PI*0.25
		voidarrow_e(stx,sty,r_angle)
	end
end

function voidarrow_e(x,y,angle)		--强化虚空箭
	local F = SpFactory:getInstance()
	F:setMode(1)
	F:setImageIndex(1,1)
	F:setDamage(5)
	local sp = F:creator()
	sp:setLife(3)
	sp:setCor(x,y)
	sp:setAngle(angle)
	sp:setSpeed(300)
	sp:setInvincible(true)
	VLib.setSpriteImage(sp,"MSMix")
	sp:spBorn()
	local F_new=F:getInstance()		--尾焰粒子
	F_new:setMode(0)
	F_new:setSpeed(40)
	F_new:setLife(2)
	VLib.setFactoryImage(F_new,"SMix")
	local F_circ=F:getInstance()	--环绕粒子
	F_circ:setMode(0)
	F_circ:setLife(10)
	F_circ:setSpeed(0)
	VLib.setFactoryImage(F_circ,"SScale")
	local tmcount=0
	local tmact={
		action=function()
			local cx=sp:GetX()
			local cy=sp:GetY()
			local x1,y1 = VLib.PolarMove(cx,cy,VLib.PI*0.25*tmcount,10)
			local x2,y2 = VLib.PolarMove(cx,cy,VLib.PI+VLib.PI*0.25*tmcount,10)
			local sptm1 = F_circ:creator()		--环绕1
			sptm1:setSpeed(0)
			sptm1:setAngle(VLib.PI/7*tmcount)
			sptm1:setCor(x1,y1)
			sptm1:spBorn()
			local sptm2 = F_circ:creator()		--环绕2
			sptm2:setSpeed(0)
			sptm2:setAngle(VLib.PI/7*tmcount)
			sptm2:setCor(x2,y2)
			sptm2:spBorn()
			local tailsp = F_new:creator()		--尾焰
			tailsp:setCor(cx+math.random()*10-5,cy+math.random()*10-5)
			tailsp:setAngle(sp:getAngle()+VLib.PI*0.4+VLib.PI*0.2*math.random())
			tailsp:spBorn()
			tailsp = F_new:creator()		--尾焰
			tailsp:setCor(cx+math.random()*10-5,cy+math.random()*10-5)
			tailsp:setAngle(sp:getAngle()-VLib.PI*0.6+VLib.PI*0.2*math.random())
			tailsp:spBorn()
			tmcount=tmcount+1
			local tmact_1={
				action=function()
					sptm1:setSpeed(100)
					sptm2:setSpeed(100)
				end
			}
			VLib.CreateTimer(2000,2000,false,tmact_1)
		end
	}
	VLib.CreateTimer(50,3000,true,tmact)
end

function yor_action_red()		--红软
	local F_red = SpFactory:getInstance()
	F_red:setLoc(200,300)
	F_red:setSpeed(200)
	F_red:setLife(4)
	F_red:setMode(3)
	F_red:setRAction(true)
	F_red:setImageIndex(0,0)
	F_red:setDamage(8)
	VLib.setFactoryImage(F_red,"SOval")
	local bx=boss:GetX()
	local by=boss:GetY()
	local px=pc:GetX()
	local py=pc:GetY()
	local angle = VLib.GetAngleBetween2Points(bx,by,px,py)
	local dist = VLib.GetDistanceBetween2Points(bx,by,px,py)
	local deathact={
		action=function(dsp)
			local dx=dsp:GetX()
			local dy=dsp:GetY()
			dx,dy=VLib.restrictPoint(dx,dy,0,0,400,600)
			local da=dsp:getAngle()
			local dtm={
				action=function()
					local spd
					for i=1,3,1 do
						spd=F_red:creator()
						spd:setCor(dx,dy)
						spd:setAngle(da+VLib.PI*0.3+i*VLib.PI*0.3)
						spd:setSpeed(math.random(50,100))
						spd:spBorn()
					end
					for i=0,2,1 do
						spd=F_red:creator()
						spd:setCor(dx,dy)
						spd:setAngle(da-VLib.PI/2+VLib.PI/3*i)
						spd:setSpeed(50)
						spd:spBorn()
					end
				end
			}
			VLib.CreateTimer(10,10,false,dtm)
		end
	}
	local tmcount=0
	local tmact={
		action=function()
			local sp
			for i=1,5,1 do
				local sp_a = angle+i*VLib.PI*0.4+tmcount*VLib.PI*0.01
				local sp_x,sp_y = VLib.PolarMove(bx,by,sp_a,60)
				sp=F_red:creator()
				sp:setCor(sp_x,sp_y)
				sp:setAngle(sp_a)
				sp:setLife(1.2)
				sp:spBorn()
				VLib.addSPAction(sp,deathact)
			end
			tmcount=tmcount+1
		end
	}
	VLib.CreateTimer(200,7000,true,tmact)
end

function red_sub(F_red,bx,by,px,py,angle,part,perdist,spact_proxy)		--生成每条曲线
	local tmcount=0
	local tmact={
		action=function()
			local cx,cy=VLib.PolarMove(bx,by,angle,perdist*tmcount)
			local cx,cy=VLib.PolarMove(cx,cy,angle+VLib.PI*0.5,math.sin(tmcount/part*VLib.PI)*dist/5)
			local circangle=VLib.GetAngleBetween2Points(cx,cy,px,py)
			local sp
			for i=1,3,1 do
				sp=F_red:creator()
				sp:setAngle(circangle+VLib.PI*2/3*i+VLib.PI/3*tmcount%2)
				sp:setCor(cx,cy)
				sp:addAction(spact_proxy)
				sp:spBorn()
			end
			tmcount=tmcount+1
		end
	}
	VLib.CreateTimer(50,part*50+1,true,tmact)
end

function yor_action_attack()
	local bx=boss:GetX()
	local by=boss:GetY()
	local F_atk	=SpFactory:getInstance()
	VLib.setSpriteImage(F_atk,"SScale")
	F_atk:setImageIndex(2,2)
	F_atk:setSpeed(50)
	F_atk:setLife(10)
	F_atk:setDamage(8)
	F_atk:setLoc(bx,by)
	local tmcount=0
	local tmact={
		action=function()
			local sp
			for i=0,3,1 do
				local angle = VLib.PI/2*i+tmcount*VLib.PI/8
				local cx,cy=VLib.PolarMove(bx,by,angle,100)
				for j=0,41,1 do
					local angle_i=math.modf(j/6)		--0~5
					local sub_i=j%7		--0-6
					local a=angle+angle_i*VLib.PI/3+sub_i*VLib.PI/18-VLib.PI/6
					local spx,spy=VLib.PolarMove(cx,cy,a,60-math.abs(sub_i-3)*10)
					local spangle=angle+angle_i*VLib.PI/3-VLib.PI/4+sub_i*VLib.PI/6
					local speed=(6-math.abs(sub_i-3))*20
					local sp=F_atk:creator()
					sp:setCor(spx,spy)
					sp:setAngle(spangle)
					sp:setSpeed(speed)
					sp:spBorn()
				end
			end
			tmcount=tmcount+1
		end
	}
	VLib.CreateTimer(1200,6000,true,tmact)
end

function yor_action_voidarrow()	--虚空箭
	local bx=boss:GetX()
	local by=boss:GetY()
	local px=pc:GetX()
	local py=pc:GetY()
	VLib.setFactoryImage(SpFactory,"SScale")
	SpFactory:setLife(10)
	SpFactory:setSpeed(200)
	SpFactory:setImageIndex(6,6)
	local a = VLib.GetAngleBetween2Points(bx,by,px,py)
	if vacount%4==0 then
		voidarrow(bx,by,a-VLib.PI*0.1)
		voidarrow(bx,by,a+VLib.PI*0.1)
	elseif vacount%4==1 then
		voidarrow(bx,by,a-VLib.PI*0.2)
		local act_1={
			action=function()
				voidarrow(bx,by,a)
			end
		}
		VLib.CreateTimer(250,250,false,act_1)
		local act_2={
			action=function()
				voidarrow(bx,by,a+VLib.PI*0.2)
			end
		}
		VLib.CreateTimer(500,500,false,act_2)
	elseif vacount%4==2 then
		voidarrow(bx,by,a-VLib.PI*0.1)
		voidarrow(bx,by,a+VLib.PI*0.1)
	elseif vacount%4==3 then
		voidarrow(bx,by,a+VLib.PI*0.2)
		local act_1={
			action=function()
				voidarrow(bx,by,a)
			end
		}
		VLib.CreateTimer(250,250,false,act_1)
		local act_2={
			action=function()
				voidarrow(bx,by,a-VLib.PI*0.2)
			end
		}
		VLib.CreateTimer(500,500,false,act_2)
	end
	vacount=vacount+1;
end

function voidarrow(x,y,angle)		--单支虚空箭
	local sp = SpFactory:creator()
	sp:setCor(x,y)
	sp:setAngle(angle)
	sp:setInvincible(true)
	VLib.setSpriteImage(sp,"MSMix")
	sp:spBorn()
	local F_new=SpFactory:getInstance()
	F_new:setSpeed(50)
	F_new:setLife(2)
	F_new:setImageIndex(1,1)
	VLib.setFactoryImage(F_new,"SMix")
	local tmcount=0
	local tmact={
		action=function()
			local cx=sp:GetX()
			local cy=sp:GetY()
			--[[local x1,y1 = VLib.PolarMove(cx,cy,VLib.PI*0.1*tmcount,40)
			local x2,y2 = VLib.PolarMove(cx,cy,VLib.PI+VLib.PI*0.1*tmcount,40)
			local sptm1 = SpFactory:creator()		--环绕1
			sptm1:setSpeed(0)
			sptm1:setAngle(VLib.PI*0.2*tmcount)
			sptm1:setCor(x1,y1)
			sptm1:spBorn()
			local sptm2 = SpFactory:creator()		--环绕2
			sptm2:setSpeed(0)
			sptm2:setAngle(VLib.PI*0.2*tmcount)
			sptm2:setCor(x2,y2)
			sptm2:spBorn()]]--
			local tailsp = F_new:creator()		--尾焰
			tailsp:setCor(cx+math.random()*10-5,cy+math.random()*10-5)
			tailsp:setAngle(angle+VLib.PI*0.9+VLib.PI*0.2*math.random())
			--[[tailsp:setSpeed(50)
			tailsp:setLife(2)
			VLib.setSpriteImage(tailsp,"SMix")]]--
			tailsp:spBorn()
			tmcount=tmcount+1
			--[[local tmact_1={
				action=function()
					sptm1:setSpeed(100)
					sptm2:setSpeed(100)
				end
			}
			VLib.CreateTimer(2000,2000,false,tmact_1)]]--
		end
	}
	VLib.CreateTimer(50,3000,true,tmact)
end
