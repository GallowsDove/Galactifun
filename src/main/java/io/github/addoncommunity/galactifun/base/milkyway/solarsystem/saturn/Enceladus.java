package io.github.addoncommunity.galactifun.base.milkyway.solarsystem.saturn;

import io.github.addoncommunity.galactifun.api.universe.Moon;
import io.github.addoncommunity.galactifun.api.universe.attributes.Atmosphere;
import io.github.addoncommunity.galactifun.api.universe.attributes.DayCycle;
import io.github.addoncommunity.galactifun.api.universe.attributes.Gravity;
import io.github.addoncommunity.galactifun.api.universe.attributes.terrain.Terrain;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.entity.EntityType;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.generator.BlockPopulator;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Random;

/**
 * Class for the Saturnian moon Enceladus
 *
 * @author Seggan
 */
public class Enceladus extends Moon {

    public Enceladus() {
        super("Enceladus", 237_948L, 308_359L, new Gravity(0.0113), Material.ICE, DayCycle.ETERNAL_NIGHT, Atmosphere.NONE, Terrain.FLAT);
    }

    @Nonnull
    @Override
    protected Material generateBlock(@Nonnull Random random, int top, int x, int y, int z) {
        if (y >= 30) {
            return Material.PACKED_ICE;
        } else {
            return Material.BLUE_ICE;
        }
    }

    @Nonnull
    @Override
    protected Biome getBiome(@Nonnull Random random, int chunkX, int chunkZ) {
        return Biome.FROZEN_OCEAN;
    }

    @Override
    protected void getPopulators(@Nonnull List<BlockPopulator> populators) {

    }

    @Override
    public void onMobSpawn(@Nonnull CreatureSpawnEvent e) {
        if (e.getSpawnReason() == CreatureSpawnEvent.SpawnReason.NATURAL) {
            if (e.getEntityType() == EntityType.POLAR_BEAR) {
                e.setCancelled(true);
            }
        }
    }
}
