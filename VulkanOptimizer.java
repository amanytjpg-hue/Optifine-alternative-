package com.vishal.ultrafps.vulkan;

import net.fabricmc.loader.api.FabricLoader;
import org.lwjgl.vulkan.VK;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class VulkanOptimizer {
    public static boolean isVulkanLoaded = false;
    public static String rendererBadge = "Vulkan 1.2+ Ready";

    public static void initVulkanConfig() {
        isVulkanLoaded = FabricLoader.getInstance().isModLoaded("vulkanmod");

        try {
            if (VK.getFunctionProvider() != null) {
                rendererBadge = "Vulkan API Active";
            }
        } catch (Throwable ignored) {
            rendererBadge = isVulkanLoaded ? "VulkanMod Engine" : "Vulkan Ready";
        }

        File configDir = FabricLoader.getInstance().getConfigDir().toFile();

        // 1. VulkanMod config
        File vulkanConfig = new File(configDir, "vulkanmod_settings.json");
        if (!vulkanConfig.exists()) {
            writeConfig(vulkanConfig, "{\n  \"frameQueueSize\": 2,\n  \"indirectDraw\": true,\n  \"uniqueOpaqueLayer\": true,\n  \"entityCulling\": true,\n  \"backFaceCulling\": true\n}");
        }

        // 2. 3D Skin Layers config
        File skinLayersConfig = new File(configDir, "skinlayers.json");
        if (!skinLayersConfig.exists()) {
            writeConfig(skinLayersConfig, "{\n  \"renderDistanceLOD\": 18,\n  \"fastRender\": true\n}");
        }

        // 3. Wavey Capes config
        File waveyCapesConfig = new File(configDir, "waveycapes.json");
        if (!waveyCapesConfig.exists()) {
            writeConfig(waveyCapesConfig, "{\n  \"windMode\": \"WAVES\",\n  \"capeStyle\": \"SMOOTH\"\n}");
        }

        // 4. LambDynamicLights config (Smooth torch glowing without FPS lag)
        File dynamicLightsConfig = new File(configDir, "lambdynamiclights.json");
        if (!dynamicLightsConfig.exists()) {
            writeConfig(dynamicLightsConfig, "{\n  \"mode\": \"FAST\",\n  \"lightEntities\": true,\n  \"lightBlockEntities\": true,\n  \"lightItems\": true\n}");
        }

        // 5. Continuity config (Connected Glass & Textures enabled)
        File continuityConfig = new File(configDir, "continuity.json");
        if (!continuityConfig.exists()) {
            writeConfig(continuityConfig, "{\n  \"connectedTextures\": true,\n  \"glass\": \"CONNECTED\"\n}");
        }
    }

    private static void writeConfig(File file, String content) {
        try (FileWriter writer = new FileWriter(file)) {
            writer.write(content);
        } catch (IOException ignored) {
        }
    }
}
