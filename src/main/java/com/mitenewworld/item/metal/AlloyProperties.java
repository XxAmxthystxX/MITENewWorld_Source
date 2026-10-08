package com.mitenewworld.item.metal;
import com.mitenewworld.MITENewWorld;

import java.util.ArrayList;
import java.util.List;

/**
 * 属性草稿纸。可变，不序列化，不存储。
 * 只在融合/转换/累加的过程中临时使用，算完丢弃。
 */
public final class AlloyProperties {
    public float durability;
    public float toughness;
    public float hardness;
    public float enchantRarity;
    public float weight;
    public int color;
    public final List<String> synergies = new ArrayList<>();

    public AlloyProperties() {}

    public AlloyProperties(float dur, float tough, float hard,
                           float ench, float weight, int color) {
        this.durability = dur;
        this.toughness = tough;
        this.hardness = hard;
        this.enchantRarity = ench;
        this.weight = weight;
        this.color = color;
    }

    /** 四项物理属性乘 attrFactor，重量乘 weightFactor */
    public void scaleAll(float attrFactor, float weightFactor) {
        durability *= attrFactor;
        toughness *= attrFactor;
        hardness *= attrFactor;
        enchantRarity *= attrFactor;
        weight *= weightFactor;
    }

    /** 从已有 AlloyComponent 的固化属性创建草稿纸 */
    public static AlloyProperties fromComponent(AlloyComponent comp) {
        AlloyProperties d = new AlloyProperties(
                comp.durability(), comp.toughness(), comp.hardness(),
                comp.enchantRarity(), comp.weight(), comp.color());
        d.synergies.addAll(comp.activeSynergies());
        return d;
    }
}
