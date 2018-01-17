package global;

public class FSMconst {
	//----------------Sound-------------------------
	public static final int SOUND_STATE_PLAY = 1;
	public static final int SOUND_STATE_PAUSE = 2;
	public static final int SOUND_STATE_STOP = 3;
	
	public static final int SOUND_INPUT_PLAY = 1;
	public static final int SOUND_INPUT_PAUSE = 2;
	public static final int SOUND_INPUT_STOP = 3;
	
	//----------------GameState---------------------
	public static final int GS_GAME = 0;
	public static final int GS_PAUSE = 1;
	public static final int GS_DIE = 2;
	
	public static final int INPUT_PAUSE = 1;
	public static final int INPUT_CONTINUE =2;
	public static final int INPUT_DIE = 3;
	
	public static final int PHASE_START = 0;		//选牌阶段
	public static final int PHASE_PLAYER = 1;
	public static final int PHASE_ENEMY = 2;
	
	public static final int INPUT_PLAYEREND = 1;
	public static final int INPUT_ENEMYEND = 2;
	public static final int INPUT_PLAYER1ST = 3;
	public static final int INPUT_ENEMY1ST = 4;
	//-----------Instance----------------------------
	public static final int BATTLE_ENEMYACTION = 1;
	public static final int BATTLE_PLAYERACTION = 2;
	public static final int BATTLE_MOVETONEXTSTAGE = 3;
	public static final int BATTLE_GAMEOVER = 4;
	public static final int BATTLE_GAMEWIN = 5;
	public static final int INPUT_PLAYERALIVE = 1;
	public static final int INPUT_PLAYERDIE = 2;
	public static final int INPUT_ENEMYALIVE = 3;
	public static final int INPUT_ENEMYDIE = 4;
	public static final int INPUT_HAVENEXTSTAGE = 5;
	public static final int INPUT_NOTHAVENEXTSTAGE = 6;
	//-----------战斗阶段-----------------------------
	public static final int CU_NORMAL = 0;
	public static final int CU_MOVING = 1;
	public static final int CU_DISABLE = 2;
	public static final int CU_CHANNELING = 3;
	public static final int CU_INVINCIBLE = 4;
	public static final int CU_ICEBLOCK = 5;
	
	public static final int INPUT_CUMOVE = 1;
	public static final int INPUT_CUSTOP = 2;
	public static final int INPUT_CUDISABLE = 3;
	public static final int INPUT_CUENABLE = 4;
	public static final int INPUT_ENCHANNEL = 5;
	public static final int INPUT_DECHANNEL = 6;
	public static final int INPUT_CUINVINCIBLE = 7;
	public static final int INPUT_DEINVINCIBLE = 8;
	public static final int INPUT_ICEBLOCK = 9;
	public static final int INPUT_REMOVEBLOCK = 10;
	
	public static final int PHASE_MCK_NORMAL = 0;
	public static final int PHASE_MCK_EARTH = 1;
	
	public static final int INPUT_MCK_SWITCH = 1;
}
