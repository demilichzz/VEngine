dofile("data/Script/Lib/VLib.lua")
dofile("data/Script/Lib/PathPoint.lua")

local runtime=0		--更新计数
local boss			--Boss对象
local phase=0		--战斗阶段
local pc=GameData:getPlayer()
local m_cd=6		--暮光变异间隔
local m_type=0		--暮光变异样式
local p_red=3		--红色关联的弹幕生成参数，决定弹幕数量
local p_green=3     --绿色关联的弹幕生成参数，决定弹幕角度
local p_blue=3    	--蓝色关联的弹幕生成参数，决定弹幕速度

function uxion_init()		--初始化参数
	runtime=0
	phase=0
	m_cd=6
	m_type=0
	p_red=3
	p_green=3
	p_blue=3
end

UltraxionAction={

setBoss=function(b)		--定义设置Boss函数
	boss=b
	pc=GameData:getPlayer()
	uxion_init()
end,

action = {			--以Action形式定义Boss的行为，行为将在每次游戏状态更新时更新
	action=function()
		uxion_bossmove()				--移动
		if phase==0 then			--普通阶段
			if runtime==1 then
				boss:addBossSkill("Judgement")
			end
			if runtime%500==1 then
				--uxion_breath()
			end
			if runtime%(m_cd*100)==1 then
				uxion_unstablemon()
			end
		elseif phase==1 then

		end
		runtime=runtime+1
	end
}

}

function uxion_bossmove()
	boss:setCor(200,100)
end

function uxion_unstablemon()	--暮光变异
	local bx=boss:GetX()
	local by=boss:GetY()
	local px=pc:GetX()
	local py=pc:GetY()
	local angle = VLib.GetAngleBetween2Points(bx,by,px,py)
	local dist = VLib.GetDistanceBetween2Points(bx,by,px,py)
	local F_laser =SpFactory:getInstance()
	F_laser:setWarningTime(1000)
	F_laser:setLength(500)
	F_laser:setImageIndex(7,7)
	F_laser:setLife(5)
	F_laser:setMode(5)		--laser类型
	uxion_unstablemontype(m_type)		--根据type值生成每次暮光变异
end

function uxion_unstablemontype(mtype)	--暮光变异不同类型
	local F_laser =SpFactory:getInstance()
	F_laser:setWarningTime(1000)
	F_laser:setLength(500)
	F_laser:setImageIndex(7,7)
	F_laser:setLife(5)
	F_laser:setMode(5)		--laser类型
	F_laser:setInvincible(true)
	F_laser:setRestricted(false)
	if mtype==0 then
		uxion_m0gen()
	elseif mtype==1 then
	elseif mtype==2 then
	elseif mtype==3 then
	end
end

function uxion_m0gen()		--6s间隔第1分钟弹幕生成
	local F = SpFactory:getInstance()
	VLib.setFactoryImage(F,"MSFlat")
	F:setSpeed(300)
	F:setLoc(200,50)
	F:setLife(1)
	F:setImageIndex(7,7)
	F:setMode(1)
	F:setDeathActionState(1)		--仅当时间结束时执行死亡行为
	local F_sub = SpFactory:getInstance()
	VLib.setFactoryImage(F_sub,"Default")
	F_sub:setSpeed(200)
	F_sub:setLife(5)
	F_sub:setImageIndex(7,7)
	local deathact={
		action=function(dsp)
			local dx=dsp:GetX()
			local dy=dsp:GetY()
			local dangle=dsp:getAngle()
			for i=0,3,1 do
				local sp=F_sub:creator()
				sp:setCor(dx,dy)
				sp:setAngle(dangle-VLib.PI/4+VLib.PI/6*i)
				sp:spBorn()
			end
		end
	}
	local tmact={
		action=function()
			local p_amount = p_red*3+6		--根据基础关联参数计算弹幕数量
			for i=0,p_amount,1 do
				local sp=F:creator()
				sp:setAngle(VLib.PI/4+i*VLib.PI/2/p_amount)
				sp:spBorn()
				VLib.addDeathAction(sp,deathact)
			end
		end
	}
	VLib.CreateTimer(100,2000,true,tmact)
end

function uxion_m0genLaser(i,F_laser)
	local F_orb = SpFactory:getInstance()
	VLib.setFactoryImage(F_orb,"Large")
	F_orb:setSpeed(200)
	F_orb:setLife(4)
	F_orb:setRestricted(false)
	F_orb:setInvincible(true)
	F_orb:setImageIndex(9,9)
	local F_scale = SpFactory:getInstance()
	VLib.setFactoryImage(F_scale,"SScale")
	F_scale:setLife(4)
	F_scale:setSpeed(100)
	F_scale:setImageIndex(7,7)
	F_laser:setLength(800)
	local baseangle = i*VLib.PI/4
	local offset = math.random(-100,100)
	local offsetangle = math.random()*VLib.PI/8-VLib.PI/16
	local x=200
	local y=300
	local px,py=VLib.PolarMove(x,y,baseangle,offset)
	local px,py=VLib.PolarMove(px,py,baseangle-VLib.PI/2,400)
	local spl = F_laser:creator()
	spl:setAngle(baseangle+VLib.PI/2+offsetangle)
	spl:setCor(px,py)
	spl:spBorn()
	local tmact_orb={
		action=function()
			local sp_orb = F_orb:creator()
			sp_orb:setCor(px,py)
			sp_orb:setAngle(baseangle+VLib.PI/2+offsetangle)
			sp_orb:spBorn()
			local tmact_scale={
				action=function()
					local ox=sp_orb:GetX()
					local oy=sp_orb:GetY()
					local oangle=sp_orb:getAngle()
					if VLib.isPointBounded(ox,oy) then
						local sp_s = F_scale:creator()
						sp_s:setAngle(oangle+VLib.PI/2)
						sp_s:setASpeed(VLib.PI/6)
						sp_s:setCor(ox,oy)
						sp_s:spBorn()
						sp_s = F_scale:creator()
						sp_s:setAngle(oangle-VLib.PI/2)
						sp_s:setASpeed(-VLib.PI/6)
						sp_s:setCor(ox,oy)
						sp_s:spBorn()
					end
				end
			}
			VLib.CreateTimer(100,4000,true,tmact_scale)
		end
	}
	VLib.CreateTimer(1000,1000,false,tmact_orb)
end

function uxion_breath()	--吐息
	local bx=boss:GetX()
	local by=boss:GetY()
	local px=pc:GetX()
	local py=pc:GetY()
	local angle = VLib.GetAngleBetween2Points(bx,by,px,py)
	local dist = VLib.GetDistanceBetween2Points(bx,by,px,py)
	local F=SpFactory:getInstance()
	VLib.setFactoryImage(F,"Large")
	F:setSpeed(30)
	F:setMode(1)		--ex
	F:setLife(5)
	local spreadarray =VLib.GetRandomArray(0,15)		--获取随机队列
	for i=0,15,1 do
		uxion_breathsub(bx+60-spreadarray[i]*8,by,VLib.PI*spreadarray[i]/30+VLib.PI/4,i*20+1)	--
	end
end

function uxion_breathsub(bx,by,angle,delay)		--子吐息
	local F=SpFactory:getInstance()
	VLib.setFactoryImage(F,"MRad")
	F:setLoc(bx,by)
	F:setAngle(angle)
	F:setSpeed(300)
	F:setMode(1)		--ex
	F:setLife(5)
	F:setInvincible(true)
	F:setImageIndex(9,9)
	local F_tail=SpFactory:getInstance()
	VLib.setFactoryImage(F_tail,"SDefault")
	F_tail:setLife(1)
	F_tail:setSpeed(60)
	F_tail:setImageIndex(7,7)
	F_tail:setMode(1)
	F_tail:setDeathActionState(1)
	local tmact={
		action=function()
			local sp = F:creator()
			sp:spBorn()
			local tmcount=0
			local tmactsub = {
				action = function()
					local spx = sp:GetX()
					local spy = sp:GetY()
					local spangle = sp:getAngle()
					for i=1,2,1 do
						local subangle = spangle+VLib.PI*2/3*i
						local sp_tail = F_tail:creator()
						sp_tail:setCor(spx,spy)
						sp_tail:setAngle(subangle)
						sp_tail:spBorn()
					end
					tmcount = tmcount+1
				end
			}
			VLib.CreateTimer(100,5000,true,tmactsub)
		end
	}
	VLib.CreateTimer(delay,delay,false,tmact)
end

function uxion_test()
	local bx=boss:GetX()
	local by=boss:GetY()
	local px=pc:GetX()
	local py=pc:GetY()
	local angle = VLib.GetAngleBetween2Points(bx,by,px,py)
	local dist = VLib.GetDistanceBetween2Points(bx,by,px,py)
	local F=SpFactory:getInstance()
	VLib.setFactoryImage(F,"SMix")
	F:setLoc(bx,by)
	F:setAngle(-VLib.PI/2)
	F:setSpeed(200)
	F:setLife(10)
	F:setRestricted(true)
	F:setMode(2)
	local uact={
		action=function(dsp)
			local count = dsp:getUpdateCount()
			if count==70 then
				local dx=dsp:GetX()
				local dy=dsp:GetY()
				dsp:setSpeed(40)
			end
		end
	}
	local tmcount=0
	local tmact={
		action=function()
			local baseangle = math.random()*VLib.PI
			for i=0,32,1 do
				local sp = F:creator()
				sp:setAngle(baseangle+VLib.PI/16*i)
				local uaction = VLib.CreateSPAction(uact)
				sp:addUpdateAction(uaction)
				sp:addInvTimeNode(1,0,0,200)
				sp:addTimeNode(1000,70,0,200)
				sp:spBorn()
			end
			tmcount=tmcount+1
		end
	}
	VLib.CreateTimer(100,500,true,tmact)
end
