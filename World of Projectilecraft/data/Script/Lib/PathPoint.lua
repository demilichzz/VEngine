dofile("data/Script/Lib/VLib.lua")
-- �����·����Ķ���

PathPoint={
	x=0,
	y=0,
	currentnode=1,	--��ǰ�ڵ�
	currentstarttime=0,	--��ǰ�ڵ����ʼʱ�����
	runtime=0,		--���м�������ʹ�ø������ʱ�豣֤move��������ÿ�θ���ʱ����
	perodic=true,	--�Ƿ���β����

	Path={
		length=1,	--·������
		nodelist={{0,0,0,50}},	--·���ڵ��б�,·����λ�ýڵ�����ֱ�Ϊx,y���꣬ͣ��ʱ��(s)���ٶ�(px/s)
	}
}

function PathPoint:setNodeList(nl)	--���ýڵ��
	if(#nl>0)then
		self.Path.nodelist=nl
		self.Path.length=#nl
	end
end

function PathPoint:addNode(nl)	--��ӽڵ��
	if(#nl>0)then
		for i=1,#nl,1 do
			self.Path.nodelist[self.Path.length+i]=nl[i]
		end
		self.Path.length=self.Path.length+#nl
	end
end

function PathPoint:move()
	if(self.runtime==0)then	--����λ����Ϊ��ʼ�ڵ�λ��
		self.x=self.Path.nodelist[1][1]
		self.y=self.Path.nodelist[1][2]
	end
	if(self.runtime-self.currentstarttime<self.Path.nodelist[self.currentnode][3]*100)then	--����ʱ�����-��ʼʱ�����<��ǰ�ڵ�ͣ��ʱ�䣬���ô��ƶ�״̬Ϊͣ����
	else		--��������һ�ڵ�����ƶ�
		if(self.Path.length>self.currentnode)then			--���·������>��ǰ�ڵ㣬������һ���ڵ�
			self:movetonode(self.Path.nodelist[self.currentnode+1])
		else	--û����һ���ڵ�
			if(self.perodic)then	--����ظ�
				self:movetonode(self.Path.nodelist[1])	--���һ�ڵ��ƶ�
			else	--���ظ���ֹͣ
			end
		end
	end
	self.runtime=self.runtime+1
end

function PathPoint:movetonode(node)
	local angle=VLib.GetAngleBetween2Points(self.x,self.y,node[1],node[2])	--��ȡ��ǰλ�õ�Ŀ��ĽǶ�
	local dist=VLib.GetDistanceBetween2Points(self.x,self.y,node[1],node[2]) --��ȡ����
	if(dist<node[4]/100)then	--����<���ƶ�����
		self.x=node[1]
		self.y=node[2]
		self.currentstarttime=self.runtime
		self.currentnode=self.currentnode+1
		if(self.currentnode>self.Path.length)then	--Խ������Ϊ1
			self.currentnode=1
		end
	else
		self.x,self.y=VLib.PolarMove(self.x,self.y,angle,node[4]/100)
	end
end

function PathPoint:new(o)	--��������ĺ���
	o=o or {}
	setmetatable(o,self)
	self.__index=self
	return o
end
