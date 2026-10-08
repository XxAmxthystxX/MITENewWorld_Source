package com.mitenewworld.screen.widget;
import com.mitenewworld.MITENewWorld;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.Identifier;

/**
 * 按钮三态纹理：1 = 正常，2 = 选中（悬停），3 = 按下与禁用共用一张。
 */
@Environment(EnvType.CLIENT)
public record ModButtonTextures(Identifier normal, Identifier hovered, Identifier pressed) {

    /** 铸造按钮：casting_button1 / 2 / 3 */
    public static final ModButtonTextures CASTING_START = of("casting_button");
    /** 左翻页：page_backward / page_backward_highlighted */
    public static final ModButtonTextures TEMPLATE_PREV = page("backward");
    /** 右翻页：page_forward / page_forward_highlighted */
    public static final ModButtonTextures TEMPLATE_NEXT = page("forward");

    /** 同前缀 + 数字后缀的三态贴图（xxx1 / xxx2 / xxx3） */
    public static ModButtonTextures of(String base) {
        return new ModButtonTextures(widget(base + "1"), widget(base + "2"), widget(base + "3"));
    }

    /** 只有两态的贴图（xxx / xxx_highlighted）：缺省的第三态回退到选中态 */
    public static ModButtonTextures page(String base) {
        Identifier normal = widget("page_" + base);
        Identifier hovered = widget("page_" + base + "_highlighted");
        return new ModButtonTextures(normal, hovered, hovered);
    }

    private static Identifier widget(String path) {
        return Identifier.of(MITENewWorld.MOD_ID, "textures/gui/widget/" + path + ".png");
    }

    /** state：1 正常 / 2 选中 / 3 按下或禁用 */
    public Identifier get(int state) {
        return switch (state) {
            case 1 -> normal;
            case 2 -> hovered;
            default -> pressed;
        };
    }
}
