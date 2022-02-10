package net.id.aether.client.rendering.shader;

import com.mojang.datafixers.util.Pair;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.id.aether.Aether;
import net.id.aether.duck.client.ShaderDuck;
import net.minecraft.client.render.Shader;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.resource.ResourceManager;

import java.io.IOException;
import java.util.List;
import java.util.function.Consumer;

@Environment(EnvType.CLIENT)
public final class AetherShaders{
    private AetherShaders(){}
    
    static{
        AetherRenderPhases.init();
        AetherRenderLayers.init();
    }
    
    private static Shader aural;
    private static Shader auralCutoutMipped;
    
    static String locate(String name){
        return Aether.MOD_ID + ':' + name;
    }
    
    public static List<Pair<Shader, Consumer<Shader>>> getShaders(ResourceManager manager){
        try{
            return List.of(
                Pair.of(new Shader(manager, locate("aural"), VertexFormats.POSITION_COLOR_TEXTURE_LIGHT_NORMAL), (shader)->aural = shader),
                Pair.of(new Shader(manager, locate("aural_cutout_mipped"), VertexFormats.POSITION_COLOR_TEXTURE_LIGHT_NORMAL), (shader)->auralCutoutMipped = shader)
            );
        }catch(IOException e){
            System.err.print("Failed to load Aether shaders\n");
            e.printStackTrace();
            System.exit(1);
            return List.of();
        }
    }
    
    public static Shader getAural(){
        return aural;
    }
    
    public static Shader getAuralCutoutMipped(){
        return auralCutoutMipped;
    }
    
    private static float auralTime = 0;
    
    public static void preRender(float tickDelta){
        auralTime += tickDelta;
        
        var time = ((ShaderDuck)aural).the_aether$getTime();
        if(time != null){
            time.set(auralTime);
        }
        time = ((ShaderDuck)auralCutoutMipped).the_aether$getTime();
        if(time != null){
            time.set(auralTime);
        }
    }
}
