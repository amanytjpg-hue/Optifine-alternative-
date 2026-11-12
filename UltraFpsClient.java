package com.vishal.ultrafps;

import com.vishal.ultrafps.hud.ArmorAndFpsHud;
import com.vishal.ultrafps.vulkan.VulkanOptimizer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

public class UltraFpsClient implements ClientModInitializer {
    public static KeyBinding zoomKey;
    public static KeyBinding fullbrightKey;
    public static KeyBinding hudToggleKey;
    public static KeyBinding skin3dToggleKey;

    public static boolean fullbrightEnabled = false;
    public static boolean hudEnabled = true;
    public static boolean skinAndCapesEnabled = true;
    public static double zoomLevel = 0.23;

    @Override
    public void onInitializeClient() {
        VulkanOptimizer.initVulkanConfig();

        zoomKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.ultrafps.zoom", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_C, "category.ultrafps.general"
        ));
        fullbrightKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.ultrafps.fullbright", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_G, "category.ultrafps.general"
        ));
        hudToggleKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.ultrafps.hud_toggle", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_H, "category.ultrafps.general"
        ));
        skin3dToggleKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.ultrafps.skin3d_toggle", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_J, "category.ultrafps.general"
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (fullbrightKey.wasPressed()) {
                fullbrightEnabled = !fullbrightEnabled;
                if (client.player != null) {
                    client.player.sendMessage(Text.literal("§b[UltraFPS Vulkan] §fFullbright: " + (fullbrightEnabled ? "§aON" : "§cOFF")), true);
                }
            }
            while (hudToggleKey.wasPressed()) {
                hudEnabled = !hudEnabled;
                if (client.player != null) {
                    client.player.sendMessage(Text.literal("§b[UltraFPS Vulkan] §fArmor + AppleSkin HUD: " + (hudEnabled ? "§aON" : "§cOFF")), true);
                }
            }
            while (skin3dToggleKey.wasPressed()) {
                skinAndCapesEnabled = !skinAndCapesEnabled;
                if (client.player != null) {
                    client.player.sendMessage(Text.literal("§b[UltraFPS Vulkan] §f3D Skin & Capes: " + (skinAndCapesEnabled ? "§aON" : "§cOFF")), true);
                }
            }
            ArmorAndFpsHud.tickCache(client);
        });

        HudRenderCallback.EVENT.register(new ArmorAndFpsHud());
    }

    public static boolean isZooming() {
        return zoomKey != null && zoomKey.isPressed();
    }
}
