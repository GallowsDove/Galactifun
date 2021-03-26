package io.github.addoncommunity.galactifun.base.milkyway.solarsystem.saturn;

import io.github.addoncommunity.galactifun.Galactifun;
import io.github.addoncommunity.galactifun.api.universe.attributes.DayCycle;
import io.github.addoncommunity.galactifun.api.universe.attributes.Gravity;
import io.github.addoncommunity.galactifun.api.universe.attributes.Orbit;
import io.github.addoncommunity.galactifun.api.universe.attributes.atmosphere.Atmosphere;
import io.github.addoncommunity.galactifun.api.universe.types.CelestialType;
import io.github.addoncommunity.galactifun.api.universe.world.AlienWorld;
import io.github.addoncommunity.galactifun.core.structures.GalactifunStructureFormat;
import io.github.addoncommunity.galactifun.util.ItemChoice;
import io.github.addoncommunity.galactifun.util.Sphere;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.ChunkGenerator;

import javax.annotation.Nonnull;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Random;

/**
 * Class for the Saturnian moon Enceladus
 *
 * @author Seggan
 */
public final class Enceladus extends AlienWorld {

    private final GalactifunStructureFormat MEDIUM_CRYOVOLCANO;

    public Enceladus() {
        super("&bEnceladus", Orbit.kilometers(237_948L), CelestialType.FROZEN, new ItemChoice(Material.ICE));

        try (InputStream stream = Galactifun.class.getClassLoader().getResourceAsStream("medium_cryovolcano.gsf")) {
            ByteArrayOutputStream result = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            for (int length; (length = stream.read(buffer)) != -1; ) {
                result.write(buffer, 0, length);
            }

            MEDIUM_CRYOVOLCANO = GalactifunStructureFormat.deserialize(result.toString(StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Override
    protected void generateChunk(@Nonnull ChunkGenerator.ChunkData chunk, @Nonnull ChunkGenerator.BiomeGrid grid,
                                 @Nonnull Random random, @Nonnull World world, int chunkX, int chunkZ) {
        int x;
        int y;
        int z;
        for (x = 0; x < 16; x++) {
            for (z = 0; z < 16; z++) {

                chunk.setBlock(x, 0, z, Material.BEDROCK);
                grid.setBiome(x, 0, z, Biome.FROZEN_OCEAN);

                for (y = 1; y <= 30; y++) {
                    chunk.setBlock(x, y, z, Material.PACKED_ICE);
                    grid.setBiome(x, y, z, Biome.FROZEN_OCEAN);
                }

                for (; y <= 60; y++) {
                    chunk.setBlock(x, y, z, Material.BLUE_ICE);
                    grid.setBiome(x, y, z, Biome.FROZEN_OCEAN);
                }

                for (; y < 256; y++) {
                    grid.setBiome(x, y, z, Biome.FROZEN_OCEAN);
                }
            }
        }
    }

    @Override
    public void getPopulators(@Nonnull List<BlockPopulator> populators) {
        populators.add(new BlockPopulator() {

            private final Sphere SPHERE = new Sphere(Material.WATER);

            @Override
            public void populate(@Nonnull World world, @Nonnull Random random, @Nonnull Chunk source) {
                if (random.nextDouble() < 0.01) {
                    if (random.nextBoolean()) {
                        int x = random.nextInt(12) + 2;
                        int y = 61;
                        int z = random.nextInt(12) + 2;

                        // ice layer 1 x axis
                        source.getBlock(x - 2, y, z).setType(Material.BLUE_ICE);
                        source.getBlock(x - 1, y, z).setType(Material.BLUE_ICE);
                        source.getBlock(x + 1, y, z).setType(Material.BLUE_ICE);
                        source.getBlock(x + 2, y, z).setType(Material.BLUE_ICE);

                        // ice layer 1 z axis
                        source.getBlock(x, y, z - 2).setType(Material.BLUE_ICE);
                        source.getBlock(x, y, z - 1).setType(Material.BLUE_ICE);
                        source.getBlock(x, y, z + 1).setType(Material.BLUE_ICE);
                        source.getBlock(x, y, z + 2).setType(Material.BLUE_ICE);

                        // corner ice layer 1
                        source.getBlock(x - 1, y, z - 1).setType(Material.BLUE_ICE);
                        source.getBlock(x + 1, y, z - 1).setType(Material.BLUE_ICE);
                        source.getBlock(x - 1, y, z + 1).setType(Material.BLUE_ICE);
                        source.getBlock(x + 1, y, z + 1).setType(Material.BLUE_ICE);

                        // water layer 1
                        source.getBlock(x, y, z).setType(Material.WATER, false);

                        y++;

                        // ice layer 2 x axis
                        source.getBlock(x - 1, y, z).setType(Material.BLUE_ICE);
                        source.getBlock(x + 1, y, z).setType(Material.BLUE_ICE);

                        // ice layer 2 z axis
                        source.getBlock(x, y, z - 1).setType(Material.BLUE_ICE);
                        source.getBlock(x, y, z + 1).setType(Material.BLUE_ICE);

                        // water layer 2
                        source.getBlock(x, y, z).setType(Material.WATER, false);
                    } else {
                        MEDIUM_CRYOVOLCANO.paste(source.getBlock(4, 61, 4).getLocation());
                    }
                } else if (random.nextDouble() < 0.01) {
                    int y = random.nextInt(40) + 5;

                    SPHERE.generate(source.getBlock(8, y, 8), random, 3, 3);
                }
            }
        });
    }

    @Nonnull
    @Override
    protected DayCycle createDayCycle() {
        return DayCycle.ETERNAL_NIGHT;
    }

    @Nonnull
    @Override
    protected Atmosphere createAtmosphere() {
        return Atmosphere.NONE;
    }

    @Nonnull
    @Override
    protected Gravity createGravity() {
        return Gravity.relativeToEarth(0.0113);
    }

    @Override
    public boolean canSpawnVanillaMobs() {
        return true;
    }
}
