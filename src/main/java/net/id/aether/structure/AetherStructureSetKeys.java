package net.id.aether.structure;

import net.minecraft.structure.StructureSet;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;

import static net.id.aether.Aether.locate;

public interface AetherStructureSetKeys {
    RegistryKey<StructureSet> SLIDER_LABYRINTHS = of("slider_labyrinths");

    private static RegistryKey<StructureSet> of(String id) {
        return RegistryKey.of(Registry.STRUCTURE_SET_KEY, locate(id));
    }
}
