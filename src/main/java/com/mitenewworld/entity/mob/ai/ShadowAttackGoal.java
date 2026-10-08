package com.mitenewworld.entity.mob.ai;
import com.mitenewworld.MITENewWorld;

import com.mitenewworld.entity.mob.ShadowEntity;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;

public class ShadowAttackGoal extends MeleeAttackGoal {
    private final ShadowEntity shadow;
    private int ticks;

    public ShadowAttackGoal(ShadowEntity shadow, double speed, boolean pauseWhenMobIdle) {
        super(shadow, speed, pauseWhenMobIdle);
        this.shadow = shadow;
    }

    @Override
    public void start() {
        super.start();
        this.ticks = 0;
    }

    @Override
    public void stop() {
        super.stop();
        this.shadow.setAttacking(false);
    }

    @Override
    public void tick() {
        super.tick();
        ++this.ticks;
        this.shadow.setAttacking(this.ticks >= 5 && this.getCooldown() < this.getMaxCooldown() / 2);

    }

    @Override
    protected int getMaxCooldown() {
        return 30;
    }


}



