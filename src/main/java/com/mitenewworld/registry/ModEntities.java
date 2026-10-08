package com.mitenewworld.registry;
import com.mitenewworld.entity.mob.ShadowEntity;
import com.mitenewworld.entity.mob.SkeletonKnightEntity;

import com.mitenewworld.MITENewWorld;
import com.mitenewworld.entity.blockentity.portal.UnderWorldPortalCoreEntity;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.datafixer.TypeReferences;
import net.minecraft.entity.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.world.Heightmap;

public class ModEntities {

    public static final EntityType<UnderWorldPortalCoreEntity> UNDER_WORLD_PORTAL_CORE_ENTITY = createEntityType(
            "under_world_portal_core_entity",
            EntityType.Builder.create(UnderWorldPortalCoreEntity::new, null)
                    .dimensions(0.1F, 0.1F)

    );

    public static final EntityType<ShadowEntity> SHADOW_ENTITY = createEntityType(
            "shadow_entity",
            EntityType.Builder.create(ShadowEntity::new, SpawnGroup.MONSTER)
                    .dimensions(0.6F, 1.99F)
                    .eyeHeight(1.74F)
                    .vehicleAttachment(-0.7F)
                    .maxTrackingRange(8)

    );

    public static final EntityType<SkeletonKnightEntity> SKELETON_KNIGHT_ENTITY = createEntityType(
            "skeleton_knight_entity",
            EntityType.Builder.create(SkeletonKnightEntity::new, SpawnGroup.MONSTER)
                    .dimensions(0.8F, 2.15F)
                    .eyeHeight(1.94F)
                    .vehicleAttachment(-0.7F)
                    .maxTrackingRange(8)
                    .notAllowedInPeaceful()
    );



    private static <T extends Entity> EntityType<T> createEntityType(String id , EntityType.Builder<T> type) {
        Util.getChoiceType(TypeReferences.ENTITY, id);
        Identifier identifier = Identifier.of(MITENewWorld.MOD_ID, id);
        RegistryKey<EntityType<?>> key = RegistryKey.of(RegistryKeys.ENTITY_TYPE,identifier);
        return Registry.register(Registries.ENTITY_TYPE, key, type.build(key));
    }
    public static void registerEntities() {

    }

    public static void registerDefaultAttribute() {
        FabricDefaultAttributeRegistry.register(ModEntities.SHADOW_ENTITY, ShadowEntity.createShadowAttributes());
        FabricDefaultAttributeRegistry.register(ModEntities.SKELETON_KNIGHT_ENTITY, SkeletonKnightEntity.createShadowAttributes());
    }

    public static void registerSpawnRestriction() {
        SpawnRestriction.register(ModEntities.SHADOW_ENTITY, SpawnLocationTypes.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, ShadowEntity::canMobSpawn);
        SpawnRestriction.register(ModEntities.SKELETON_KNIGHT_ENTITY, SpawnLocationTypes.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, SkeletonKnightEntity::canMobSpawn);

    }
}
