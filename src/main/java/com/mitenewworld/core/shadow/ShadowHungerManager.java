package com.mitenewworld.core.shadow;
import com.mitenewworld.MITENewWorld;

public interface ShadowHungerManager {

    int getMaxFoodLevel();
    int getFoodLevel();
    int getWaterLevel();
    int getMaxWaterLevel();
    float getSaturationLevel();

    void setMaxFoodLevel(int maxFoodLevel);

    void setMaxWaterLevel(int foodLevel);

    void setWaterLevel(int water);
}
