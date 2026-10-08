package com.mitenewworld.loot;
import com.mitenewworld.MITENewWorld;

/**
 * 战利品函数注册入口。
 */
public final class ModLootFunctions {

    private ModLootFunctions() {}

    public static void register() {
        AlloyNuggetLootFunction.register();
    }
}
