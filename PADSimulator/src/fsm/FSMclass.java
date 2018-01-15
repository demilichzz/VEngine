package fsm;

import java.util.*;

public class FSMclass {
	private ArrayList<FSMstate> fsmList = new ArrayList<FSMstate>(); // 包含状态机的所有状态
	private int currentState; // 当前状态
	private int defaultState; // 默认状态

	public FSMclass(int iStateID) { // 初始化状态
		currentState = iStateID;
		defaultState = iStateID;
	}

	
	public void resetState(){	//重置为默认状态
		SetCurrentState(defaultState);
	}
	// 返回当前状态ID
	public int GetCurrentState() {
		return currentState;
	}

	// 设置当前状态ID
	public void SetCurrentState(int iStateID) {
		currentState = iStateID;
	}

	// 返回FSMstate对象指针
	public FSMstate GetState(int iStateID) {
		for (FSMstate state : fsmList) {
			if (state.stateID == iStateID) {
				return state;
			}
		}
		return fsmList.get(0);
	}

	// 增加状态对象指针
	public void AddState(FSMstate pState) {
		fsmList.add(pState);
	}

	// 删除状态对象指针
	public void DeleteState(int iStateID) {
		FSMstate temp = new FSMstate(1, 1);
		for (FSMstate state : fsmList) {
			if (state.stateID == iStateID) {
				temp = state;
				break;
			}
		}
		fsmList.remove(temp);
	}

	// 根据“当前状态”和“输入”完成“状态”的转换。
	public int StateTransition(int input) {
		return GetState(currentState).GetOutput(input);
	}
}
