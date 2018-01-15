dofile("data/Script/Lib/VLib.lua")
dofile("data/Script/MorchokAction.lua")
dofile("data/Script/ZonozzAction.lua")
dofile("data/Script/YorsahjAction.lua")
dofile("data/Script/HagaraAction.lua")
dofile("data/Script/UltraxionAction.lua")
Debug=luajava.bindClass("global.Debug")
VEngine=luajava.bindClass("system.VEngine")
VStage=luajava.bindClass("stage.VStage")
VTimer=luajava.bindClass("timer.VTimer")
SpFClass=luajava.bindClass("factory.SpriteFactory")
SpFactory=SpFClass:getInstance()
GameData=luajava.bindClass("data.GameData")
GlobalEvent=luajava.bindClass("event.GlobalEvent")
Soundconst=luajava.bindClass("global.Soundconst")
BossSpeech=Soundconst.speech

local bosslist={}		--boss列表，包含Boss对象类名，图像名，生命值
local actionlist={}
local currentboss=0

function initStage()		--初始化关卡
	--math.randomseed(os.time())		--随机种子
	bosslist[1]={"entities.VCUMorchok","Morchok.png",8000}
	actionlist[1]=MorchokAction
	bosslist[2]={"entities.VCUWarlord","Zonozz.png",19000}
	actionlist[2]=ZonozzAction
	bosslist[3]={"entities.VCUYorsahj","Yorsahj.png",10000}
	actionlist[3]=YorsahjAction
	bosslist[4]={"entities.VCUHagara","Hagara.png",8000}
	actionlist[4]=HagaraAction
	bosslist[5]={"entities.VCUUltraxion","Hagara.png",15000}
	actionlist[5]=UltraxionAction
	setCurrentBoss(1)
end

function setRandomSeed(seed)
	math.randomseed(seed:getValue(0))
	--print("随机种子："..seed:getValue(0))
	for i=0,9,1 do
		local rnd = math.random()
		--print("生成随机数"..i.."："..rnd)
	end
end

function activateNextBoss()	--当前Boss死亡时调用，激活下一个Boss
	if currentboss<5 then
		currentboss=currentboss+1
		setCurrentBoss(currentboss)
		local pc=GameData:getPlayer()
		pc:resetState()
	else
		GlobalEvent:stageEnd()
	end
end

function setCurrentBoss(i)		--根据索引设置当前Boss
	currentboss=i
	local boss = luajava.newInstance(bosslist[i][1],bosslist[i][2],bosslist[i][3])
	boss:setCor(200,300)
	GameData:setStage(i)
	GameData:setBoss(boss)		--在游戏数据类设置当前Boss
	SpFactory:setOwner(boss)		--设置粒子所有者为Boss对象
	actionlist[i].setBoss(boss)		--将Boss对象指定给弹幕脚本
	local b_act = actionlist[i].action		--初始化行为函数为弹幕脚本中指定的行为函数
	local b_proxy = luajava.createProxy("timer.VLuaAction", b_act)	--创建代理
	boss:addAction(b_proxy)	--对Boss对象添加行为
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
