package com.mitenewworld.item.metal;
import com.mitenewworld.MITENewWorld;

import net.minecraft.text.Text;

/**
 * 打造品质档位：只用于显示（名字 / tooltip / 颜色），不再持有任何倍率。
 * 倍率与耐久加成统一由 {@link ModSynergies} 的品质共鸣负责。
 *
 * <p>分数映射：&lt;0 劣质 / 0~30 普通 / 31~70 精良 / 71~150 优秀 / 151~300 史诗 / &gt;300 传说。
 */
public enum QualityLevel {

    INFERIOR(-1, "quality_inferior", 0x808080),
    COMMON(0, "quality_common", 0xFFFFFF),
    FINE(31, "quality_fine", 0x55FF55),
    EXCELLENT(71, "quality_excellent", 0x5555FF),
    EPIC(151, "quality_epic", 0xAA00AA),
    LEGENDARY(301, "quality_legendary", 0xFFAA00);

    private final int minScore;
    private final String synergyId;
    private final int color;

    QualityLevel(int minScore, String synergyId, int color) {
        this.minScore = minScore;
        this.synergyId = synergyId;
        this.color = color;
    }

    public int minScore() { return minScore; }

    /** 对应的品质共鸣 id */
    public String synergyId() { return synergyId; }

    public int color() { return color; }

    public Text displayName() {
        return Text.translatable("synergy.mitenewworld." + synergyId).withColor(color);
    }

    /** 按品质分取档位：低于最低门槛算劣质，超过最高门槛算传说 */
    public static QualityLevel fromScore(int score) {
        QualityLevel result = INFERIOR;
        for (QualityLevel level : values()) {
            if (score < level.minScore) {
                break;
            }
            result = level;
        }
        return result;
    }
}
