package com.ProcessUnit;

/**
 * <p>
 *
 * @author BowenDeng
 * @version 1.0
 * @date 2021/12/20 14:02
 **/
public class CommandTable {

    static final String COMMAND_SPLIT = ";";
    static final String NEXT_COMMAND = ",";

    static final String LOAD_SIGN = "L";
    static final String BASE_LOCK = "w";
    static final String BASE_UNLOCK = "s";
    static final String BASE_DESTROYED = "b";
    static final String OBJECT_STATE = "n";
    static final String BULLET_STATE = "t";
    static final String SHIELD_STATE = "i";
    static final String BOMB_STATE = "o";
    static final String BOMB_SMALL = "small";

    static final String PLAYER_IMPORT = "p";
    static final String SERVER_IMPORT = "m";
    static final String WIN_SIGN = "g";
    static final String FAIL_SIGN = "a";
    static final String RESTART_SIGN = "j";
    static final String PAUSE_SIGN = "x";
}
