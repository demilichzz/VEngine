dofile("data/Script/PADLib.lua")
VInstance=luajava.bindClass("entities.VInstance")
VEnemy=luajava.bindClass("entities.VEnemy")
GameData=luajava.bindClass("data.GameData")

local javainstance=nil

initInstance={

initInstance=function()
	javainstance=GameData.instance
	local instance = {
	name = "極限ゴッドラッシュ！",
	maxlevel = 5,
	enemylist = {}
	}
	instance.enemylist[1]={}
	instance.enemylist[1][1]={name="覚醒ガイア",id=1223,maxhp=1618454,basedamage=0,defend=7778,turn=1,cuturn=1,maintype=PADLib.Const.ORB_WOOD,secondtype=PADLib.Const.ORB_WOOD,s_action=
		{
		action=function(venemy)
			local hppercent=venemy:getHPPercent()
			local runtime = venemy:getRunTime()
			if runtime==0 then
				javainstance:addHint("大地のコア")
				venemy:addBuff(5,5,1,"Buff_Enemy_7.png")
				--venemy:addBuff(2,-1,75,"Buff_Enemy_1.png")
			else
				if hppercent>=0.7 then
					javainstance:addHint("恵みの土塊")
					venemy:dealDamage(3000)
				elseif hppercent<=0.01 then
					javainstance:addHint("根性测试")
					venemy:dealDamage(10000)
				else
					javainstance:addHint("グラウンド・リベンジ")
					venemy:dealDamage(10000)
				end
			end
		end
		}
	}
	instance.enemylist[2]={}
	instance.enemylist[2][1]={name="アテナ",id=648,maxhp=5171491,basedamage=11447,defend=1380,turn=1,cuturn=1,maintype=PADLib.Const.ORB_LIGHT,secondtype=PADLib.Const.ORB_WOOD,s_action=
		{
		action=function(venemy)
			local hppercent=venemy:getHPPercent()
			if hppercent>=0.15 then
				javainstance:addHint("イージスの盾")
				venemy:dealDamage(9158)
			else
				javainstance:addHint("スターバースト")
				venemy:dealDamage(114470)
			end
		end
		}
	}
	instance.enemylist[3]={}
	instance.enemylist[3][1]={name="アテナ",id=648,maxhp=5171491,basedamage=11447,defend=1380,turn=1,cuturn=1,maintype=PADLib.Const.ORB_LIGHT,secondtype=PADLib.Const.ORB_WOOD,s_action=
		{
		action=function(venemy)
			local hppercent=venemy:getHPPercent()
			if hppercent>=0.15 then
				javainstance:addHint("イージスの盾")
				venemy:dealDamage(9158)
			else
				javainstance:addHint("スターバースト")
				venemy:dealDamage(114470)
			end
		end
		}
	}
	instance.enemylist[4]={}
	instance.enemylist[4][1]={name="アテナ",id=648,maxhp=5171491,basedamage=11447,defend=1380,turn=1,cuturn=1,maintype=PADLib.Const.ORB_LIGHT,secondtype=PADLib.Const.ORB_WOOD,s_action=
		{
		action=function(venemy)
			local hppercent=venemy:getHPPercent()
			if hppercent>=0.15 then
				javainstance:addHint("イージスの盾")
				venemy:dealDamage(9158)
			else
				javainstance:addHint("スターバースト")
				venemy:dealDamage(114470)
			end
		end
		}
	}
	instance.enemylist[5]={}
	instance.enemylist[5][1]={name="アテナ",id=648,maxhp=5171491,basedamage=11447,defend=1380,turn=1,cuturn=1,maintype=PADLib.Const.ORB_LIGHT,secondtype=PADLib.Const.ORB_WOOD,s_action=
		{
		action=function(venemy)
			local hppercent=venemy:getHPPercent()
			if hppercent>=0.15 then
				javainstance:addHint("イージスの盾")
				venemy:dealDamage(9158)
			else
				javainstance:addHint("スターバースト")
				venemy:dealDamage(114470)
			end
		end
		}
	}
	javainstance:setName(instance.name)
	javainstance:initEnemyList(instance.maxlevel)
	for i=1,21,1 do
		for j=1,7,1 do
			if instance.enemylist[i]~=nil then
				if instance.enemylist[i][j]~=nil then
					local enemy = luajava.newInstance("entities.VEnemy")
					enemy:setName(instance.enemylist[i][j].name)
					enemy:setValues(instance.enemylist[i][j].id,
					instance.enemylist[i][j].maintype,
					instance.enemylist[i][j].secondtype,
					instance.enemylist[i][j].maxhp,
					instance.enemylist[i][j].maxhp,
					instance.enemylist[i][j].turn,
					instance.enemylist[i][j].cuturn,
					instance.enemylist[i][j].basedamage,
					instance.enemylist[i][j].defend)
					PADLib.addObjAction(enemy,instance.enemylist[i][j].s_action)
					javainstance:setEnemy(i-1,j-1,enemy)
				end
			end
		end
	end
end

}
