package net.id.aether.world.feature.structure;

import com.mojang.serialization.Codec;
import net.id.aether.structure.SkyrootTowerGenerator;
import net.minecraft.structure.*;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.Heightmap;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.StructureFeature;

public class SkyrootTowerFeature extends StructureFeature<DefaultFeatureConfig> {
    public SkyrootTowerFeature(Codec<DefaultFeatureConfig> codec) {
        super(codec, StructureGeneratorFactory.simple(StructureGeneratorFactory.checkForBiomeOnTop(Heightmap.Type.WORLD_SURFACE_WG), SkyrootTowerFeature::addPieces));
    }

    private static final int X_OFFSET = 4;
    private static final int Z_OFFSET = 4;

    private static void addPieces(StructurePiecesCollector collector, StructurePiecesGenerator.Context<DefaultFeatureConfig> context) {
        ChunkPos pos = context.chunkPos();
        int y = context.chunkGenerator().getHeight(pos.getStartPos().getX() - X_OFFSET, pos.getStartPos().getZ() - Z_OFFSET, Heightmap.Type.WORLD_SURFACE_WG, context.world());
        if (y < 0) { // DON'T PLACE ON THE BOTTOM OF THE WORLD
            return;
        }
        BlockPos newPos = new BlockPos(pos.getStartPos().getX() - X_OFFSET, y, pos.getStartPos().getZ() - Z_OFFSET);
        SkyrootTowerGenerator.addPieces(context.structureManager(), collector, BlockRotation.NONE, newPos);
    }

    @Override
    public GenerationStep.Feature getGenerationStep() {
        return GenerationStep.Feature.SURFACE_STRUCTURES;
    }
}