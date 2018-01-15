package fsm;

import java.util.*;

public class FSMclass {
	private ArrayList<FSMstate> fsmList = new ArrayList<FSMstate>(); // ����״̬��������״̬
	private int currentState; // ��ǰ״̬

	public FSMclass(int iStateID) { // ��ʼ��״̬
		currentState = iStateID;
	}

	// ���ص�ǰ״̬ID
	public int GetCurrentState() {
		return currentState;
	}

	// ���õ�ǰ״̬ID
	public void SetCurrentState(int iStateID) {
		currentState = iStateID;
	}

	// ����FSMstate����ָ��
	public FSMstate GetState(int iStateID) {
		for (FSMstate state : fsmList) {
			if (state.stateID == iStateID) {
				return state;
			}
		}
		return fsmList.get(0);
	}

	// ����״̬����ָ��
	public void AddState(FSMstate pState) {
		fsmList.add(pState);
	}

	// ɾ��״̬����ָ��
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

	// ���ݡ���ǰ״̬���͡����롱��ɡ�״̬����ת����
	public int StateTransition(int input) {
		return GetState(currentState).GetOutput(input);
	}
}
