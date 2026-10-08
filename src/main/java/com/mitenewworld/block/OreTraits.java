package com.mitenewworld.block;

import java.util.HashMap;
import java.util.Map;

public record OreTraits(float hardness, float resistance, int veinMinGrade, int veinMaxGrade) {

    private static final Map<String, OreTraits> TRAITS = new HashMap<>();

    static {
        put("tin", 30.0f, 25.0f, 1, 5);
        put("aluminium", 30.0f, 25.0f, 1, 5);
        put("gold", 30.0f, 25.0f, 1, 5);
        put("coal", 30.0f, 25.0f, 1, 5);
        put("copper", 32.0f, 25.0f, 1, 5);
        put("silver", 33.0f, 25.0f, 1, 5);
        put("iron", 35.0f, 25.0f, 1, 5);
        put("titanium", 37.0f, 30.0f, 1, 5);
        put("mithril", 40.0f, 30.0f, 1, 3);
        put("platinum", 42.0f, 30.0f, 1, 5);
        put("starlight", 42.0f, 45.0f, 1, 5);
        put("iridium", 43.0f, 40.0f, 4, 5);
        put("adamantium", 45.0f, 30.0f, 1, 3);
        put("ancient_metal", 45.0f, 35.0f, 1, 5);
        put("diamond", 30.0f, 25.0f, 1, 5);
        put("emerald", 30.0f, 25.0f, 1, 5);
        put("lapis", 30.0f, 25.0f, 1, 5);
    }

    private static void put(String id, float hardness, float resistance, int veinMinGrade, int veinMaxGrade) {
        TRAITS.put(id, new OreTraits(hardness, resistance, veinMinGrade, veinMaxGrade));
    }

    public static OreTraits of(String id) {
        OreTraits traits = TRAITS.get(id);
        if (traits == null) {
            throw new IllegalArgumentException("Unknown ore traits: " + id);
        }
        return traits;
    }
}
