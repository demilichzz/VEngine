PADLib={

Const={		--常量表
ORB_FIRE=1,
ORB_WATER=2,
ORB_WOOD=3,
ORB_LIGHT=4,
ORB_DARK=5,
ORB_HEART=6,
ORB_POISON=7,
ORB_JAMMER=8,
ORB_POISONB=9,
ENEMY_MAINTYPE=0,
ENEMY_SECONDTYPE=1,
ENEMY_MAXHP=2,
ENEMY_CURRENTHP=3
},

CreateTimer=function(time,endTime,perodic,action)	--使用指定参数生成Timer并运行
	local tm_proxy=luajava.createProxy("timer.VLuaAction", action)
	local tm = luajava.newInstance("timer.VTimer",time,endTime,perodic,tm_proxy)
	tm:timerStart(1)
	return tm
end,

getEnemyList=function(instance)	--获取所有敌人的列表
	local list={}
	local index=0
	for i=0,7,1 do
		local param=instance:getEnemy(i)
		if param~=nil then
			list[index]=param
			index=index+1
		end
	end
	return list
end,

getOrbList=function(ga)	--获取宝石对象列表
	local list={}
	for i=0,ga.orb_maxy-1,1 do
		list[i]={}
		for j=0,ga.orb_maxx-1,1 do
			list[i][j]=ga:getOrb(i,j)
			--print(i.."/"..j)
		end
	end
	return list
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
		rndarray[i-m+1]=index	--获取的数组下标从1开始
	end
	return rndarray
end,

generateOrbList=function(ga,intlist)	--生成一个用于洗版的宝石颜色列表，intlist为可选择的宝石颜色组
	local list={}
	local resultlist={}
	for i=1,#intlist,1 do
		list[i]=intlist[i]
		list[i+1]=intlist[i]
		list[i+2]=intlist[i]		--首先在列表中添加每种可用颜色的宝石各3个以防止洗版断珠
	end
	local totalorb=ga.orb_maxx*ga.orb_maxy
	for i=#intlist+3,totalorb,1 do		--补全剩余宝石
		local fillindex=math.random(1,#intlist)
		list[i]=intlist[fillindex]
	end
	local indexlist=PADLib.GetRandomArray(1,totalorb)		--生成排序列表
	for i=1,totalorb,1 do		--根据排序列表分配宝石列表位置
		resultlist[i]=list[indexlist[i]]
	end
	return resultlist
end,

addObjAction=function(javaobj,action)	--添加含java对象的行为
	local action_proxy=luajava.createProxy("timer.VLuaObjectAction",action)
	javaobj:addAction(action_proxy)
end
}

