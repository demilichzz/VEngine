package fsm;

public class FSMstate {
	private int numofTransition;
	private int[] inputList;
	private int[] outputList;
	public int stateID;

	public FSMstate(int sID, int t) { // ����״̬��
		if (t < 1) {
			numofTransition = 1;
		} else {
			numofTransition = t;
		}
		stateID = sID;
		inputList = new int[t];
		outputList = new int[t];
		for (int i : inputList) {
			i = 0;
		}
		for (int i : outputList) {
			i = 0;
		}
	}

	public void AddTransition(int input, int output) { // ����״̬ת��
		for (int i = 0; i < numofTransition; i++) {
			if (inputList[i] == 0) {
				if (i < numofTransition) {
					inputList[i] = input;
					outputList[i] = output;
				}
				break;
			}
		}
	}

	public void DeleteTransition(int output) { // ɾ��״̬ת��
		int i;
		for (i = 0; i < numofTransition; i++) {
			if (outputList[i] == output) {
				break;
			}
		}
		if (i >= numofTransition) {
			return;
		}
		inputList[i] = 0;
		outputList[i] = 0;
		for (; i < numofTransition - 1; ++i) {
			// ---------ԭ���д˴�ΪoutputList[i],�Ǵ����----------
			if (outputList[i + 1] == 0) {
				break;
			}
			inputList[i] = inputList[i + 1];
			outputList[i] = outputList[i + 1];
		}
		inputList[i] = 0;
		outputList[i] = 0;
	}

	public int GetOutput(int input) { // ������״̬ת��Ϊ���״̬
		int output = stateID;
		for (int i = 0; i < numofTransition; ++i) {
			/*if (outputList[i] == 0) {
				break;
			}*/
			if (input == inputList[i]) {
				output = outputList[i];
				break;
			}
		}
		return output;
	}
}
