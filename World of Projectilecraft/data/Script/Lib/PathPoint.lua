dofile("data/Script/Lib/VLib.lua")
-- 定义带路径点的对象

PathPoint={
	x=0,
	y=0,
	currentnode=1,	--当前节点
	currentstarttime=0,	--当前节点的起始时间计数
	runtime=0,		--运行计数，在使用该类对象时需保证move函数将在每次更新时调用
	perodic=true,	--是否首尾相连

	Path={
		length=1,	--路径长度
		nodelist={{0,0,0,50}},	--路径节点列表,路径的位置节点参数分别为x,y坐标，停留时间(s)，速度(px/s)
	}
}

function PathPoint:setNodeList(nl)	--设置节点表
	if(#nl>0)then
		self.Path.nodelist=nl
		self.Path.length=#nl
	end
end

function PathPoint:addNode(nl)	--添加节点表
	if(#nl>0)then
		for i=1,#nl,1 do
			self.Path.nodelist[self.Path.length+i]=nl[i]
		end
		self.Path.length=self.Path.length+#nl
	end
end

function PathPoint:move()
	if(self.runtime==0)then	--将点位置设为初始节点位置
		self.x=self.Path.nodelist[1][1]
		self.y=self.Path.nodelist[1][2]
	end
	if(self.runtime-self.currentstarttime<self.Path.nodelist[self.currentnode][3]*100)then	--运行时间计数-起始时间计数<当前节点停留时间，即该次移动状态为停留。
	else		--否则向下一节点进行移动
		if(self.Path.length>self.currentnode)then			--如果路径长度>当前节点，则有下一个节点
			self:movetonode(self.Path.nodelist[self.currentnode+1])
		else	--没有下一个节点
			if(self.perodic)then	--如果重复
				self:movetonode(self.Path.nodelist[1])	--向第一节点移动
			else	--不重复则停止
			end
		end
	end
	self.runtime=self.runtime+1
end

function PathPoint:movetonode(node)
	local angle=VLib.GetAngleBetween2Points(self.x,self.y,node[1],node[2])	--获取当前位置到目标的角度
	local dist=VLib.GetDistanceBetween2Points(self.x,self.y,node[1],node[2]) --获取距离
	if(dist<node[4]/100)then	--距离<将移动距离
		self.x=node[1]
		self.y=node[2]
		self.currentstarttime=self.runtime
		self.currentnode=self.currentnode+1
		if(self.currentnode>self.Path.length)then	--越界则设为1
			self.currentnode=1
		end
	else
		self.x,self.y=VLib.PolarMove(self.x,self.y,angle,node[4]/100)
	end
end

function PathPoint:new(o)	--建立对象的函数
	o=o or {}
	setmetatable(o,self)
	self.__index=self
	return o
end
