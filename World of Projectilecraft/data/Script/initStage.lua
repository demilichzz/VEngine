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

local bosslist={}		--boss�б�����Boss����������ͼ����������ֵ
local actionlist={}
local currentboss=0

function initStage()		--��ʼ���ؿ�
	--math.randomseed(os.time())		--�������
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
	--print("������ӣ�"..seed:getValue(0))
	for i=0,9,1 do
		local rnd = math.random()
		--print("���������"..i.."��"..rnd)
	end
end

function activateNextBoss()	--��ǰBoss����ʱ���ã�������һ��Boss
	if currentboss<5 then
		currentboss=currentboss+1
		setCurrentBoss(currentboss)
		local pc=GameData:getPlayer()
		pc:resetState()
	else
		GlobalEvent:stageEnd()
	end
end

function setCurrentBoss(i)		--�����������õ�ǰBoss
	currentboss=i
	local boss = luajava.newInstance(bosslist[i][1],bosslist[i][2],bosslist[i][3])
	boss:setCor(200,300)
	GameData:setStage(i)
	GameData:setBoss(boss)		--����Ϸ���������õ�ǰBoss
	SpFactory:setOwner(boss)		--��������������ΪBoss����
	actionlist[i].setBoss(boss)		--��Boss����ָ������Ļ�ű�
	local b_act = actionlist[i].action		--��ʼ����Ϊ����Ϊ��Ļ�ű���ָ������Ϊ����
	local b_proxy = luajava.createProxy("timer.VLuaAction", b_act)	--��������
	boss:addAction(b_proxy)	--��Boss���������Ϊ
end

-------------------����---------------------------------------------------------

--local tmac_00 = {	--Timer��Ϊ��������01
--	action = function()
		--�ڴ������Ҫִ�е�Timer��Ϊ
--	end
--}
--local tmac_proxy=luajava.createProxy("timer.VLuaAction", tmac_00)	--ͨ��������ʽ���������Ϊ����ת��Ϊʵ����VLuaAction�ӿڵĶ���
--local tm = luajava.newInstance("timer.VTimer",100,10000,true,tmac_proxy)	--ʹ��������������½�Timer����
--tm:timerStart()  --����Timer
