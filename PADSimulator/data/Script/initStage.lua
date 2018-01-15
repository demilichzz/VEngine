dofile("data/Script/PADLib.lua")
dofile("data/Script/initInstance.lua")
Debug=luajava.bindClass("global.Debug")
CharacterConst=luajava.bindClass("global.CharacterConst")
GameData=luajava.bindClass("data.GameData")

local instance=nil
local orbs=nil
local gamearea=nil
local max_x=0
local max_y=0

function initStage()		--初始化关卡
	--math.randomseed(os.time())		--随机种子
	--initCharacter()
end

function initEntities()
	instance=GameData.instance
	gamearea=GameData.ga
	max_x=GameData.ga.orb_maxx
	max_y=GameData.ga.orb_maxy
end

function initInstances()
	initInstance.initInstance()
end

function initCharacter()
	local vchar = CharacterConst:getCharacter(2012)
	local askill = luajava.newInstance("entities.VActiveSkill",askill_p)
	vchar:addASkill(askill)
end

function initSkill(a_skill)
	local vchar=a_skill:getCharacter()
	local id =vchar:getIntValue(0)
	if id==1 then
	elseif id==1728 then
		local as_action={
			action=function(vchar)
				local orblist=PADLib.getOrbList(gamearea)
				for i=0,max_y-1,1 do
					for j=0,max_x-1,1 do
						if orblist[i][j]:getValue(0)==6 then
							orblist[i][j]:setValue(0,1)
						end
					end
				end
				gamearea:uiTranslateUpdate()
			end
		}
		local as_proxy = luajava.createProxy("timer.VLuaObjectAction",as_action)
		a_skill:addAction(as_proxy)
	elseif id==1747 then
		local as_action={
			action=function(vchar)
				local orblist=PADLib.getOrbList(gamearea)
				local newColorList={1,2,3,4,5}
				local newOrbList=PADLib.generateOrbList(gamearea,newColorList)
				for i=0,max_y-1,1 do
					for j=0,max_x-1,1 do
						local newcolor=newOrbList[i*max_x+j+1]
						orblist[i][j]:setValue(0,newcolor)
					end
				end
				gamearea:uiTranslateUpdate()
			end
		}
		local as_proxy = luajava.createProxy("timer.VLuaObjectAction",as_action)
		a_skill:addAction(as_proxy)
	elseif id==2012 then
		local as_action={
			action=function(vchar)
				enemylist=PADLib.getEnemyList(instance)
				for i=0,7,1 do
					if enemylist[i]~=nil then
						enemylist[i]:getDamageEvent(77777, 6)
					end
				end
				--print(enemylist[0]:getValue(3))
			end
		}
		local as_proxy = luajava.createProxy("timer.VLuaObjectAction",as_action)
		a_skill:addAction(as_proxy)
	elseif id==2078 then
		local as_action={
			action=function(vchar)
				local orblist=PADLib.getOrbList(gamearea)
				local newColorList={1,2,3,4,5,6}
				local newOrbList=PADLib.generateOrbList(gamearea,newColorList)
				for i=0,max_y-1,1 do
					for j=0,max_x-1,1 do
						local newcolor=newOrbList[i*max_x+j+1]
						orblist[i][j]:setValue(0,newcolor)
					end
				end
				gamearea:uiTranslateUpdate()
			end
		}
		local as_proxy = luajava.createProxy("timer.VLuaObjectAction",as_action)
		a_skill:addAction(as_proxy)
	end
end

-------------------范例---------------------------------------------------------

--local tmac_00 = {	--Timer行为描述函数01
--	action = function()
		--在此添加需要执行的Timer行为
--	end
--}
--local tmac_proxy=luajava.createProxy("timer.VLuaAction", tmac_00)	--通过代理形式将定义的行为函数转化为实现了VLuaAction接口的对象
--local tm = luajava.newInstance("timer.VTimer",100,10000,true,tmac_proxy)	--使用上述代理对象新建Timer对象
--tm:timerStart()  --启动Timer
--Debug:DebugSimpleMessage("test")
