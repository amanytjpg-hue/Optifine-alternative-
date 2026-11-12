package com.vishal.ultrafps.hud;

import com.vishal.ultrafps.UltraFpsClient;
import com.vishal.ultrafps.vulkan.VulkanOptimizer;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.FoodComponent;
import net.minecraft.entity.player.HungerManager;
import net.minecraft.item.ItemStack;

public class ArmorAndFpsHud implements HudRenderCallback {
    private static final ItemStack[] cachedArmor = new ItemStack[4];
    private static final String[] cachedDurabilityText = new String[4];
    private static final int[] cachedDurabilityColor = new int[4];
    private static final boolean[] showWarning = new boolean[4];
    private static String cachedFpsBadge = "FPS: -- [Vulkan + Continuity + DynamicLights]";

    private static float cachedSaturation = 0.0f;
    private static int previewFoodRestore = 0;
    private static float previewSaturationRestore = 0.0f;

    static {
        for (int i = 0; i < 4; i++) {
            cachedArmor[i] = ItemStack.EMPTY;
            cachedDurabilityText[i] = "";
            cachedDurabilityColor[i] = 0xFFFFFFFF;
        }
    }

    public static void tickCache(MinecraftClient client) {
        if (client.player == null || !UltraFpsClient.hudEnabled) return;

        cachedFpsBadge = client.getCurrentFps() + " FPS §7| §b" + VulkanOptimizer.rendererBadge + " §a[OptiFine++ Built-in]";

        HungerManager hungerManager = client.player.getHungerManager();
        cachedSaturation = hungerManager.getSaturationLevel();

        ItemStack heldItem = client.player.getMainHandStack();
        FoodComponent foodComp = heldItem.get(DataComponentTypes.FOOD);
        if (foodComp == null) {
            heldItem = client.player.getOffHandStack();
            foodComp = heldItem.get(DataComponentTypes.FOOD);
        }
        if (foodComp != null) {
            previewFoodRestore = foodComp.nutrition();
            previewSaturationRestore = foodComp.saturation();
        } else {
            previewFoodRestore = 0;
            previewSaturationRestore = 0.0f;
        }

        for (int i = 0; i < 4; i++) {
            ItemStack stack = client.player.getInventory().getArmorStack(i);
            cachedArmor[i] = stack;

            if (!stack.isEmpty() && stack.isDamageable()) {
                int maxDamage = stack.getMaxDamage();
                int currentRemaining = maxDamage - stack.getDamage();
                float ratio = (float) currentRemaining / (float) maxDamage;

                cachedDurabilityText[i] = String.valueOf(currentRemaining);

                if (ratio > 0.6f) {
                    cachedDurabilityColor[i] = 0xFF55FF55;
                    showWarning[i] = false;
                } else if (ratio > 0.25f) {
                    cachedDurabilityColor[i] = 0xFFFFFF55;
                    showWarning[i] = false;
                } else {
                    cachedDurabilityColor[i] = 0xFFFF5555;
                    showWarning[i] = (ratio <= 0.10f);
                }
            } else {
                cachedDurabilityText[i] = "";
                showWarning[i] = false;
            }
        }
    }

    @Override
    public void onHudRender(DrawContext context, RenderTickCounter tickCounter) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null || client.options.hudHidden || !UltraFpsClient.hudEnabled) return;

        int screenWidth = context.getScaledWindowWidth();
        int screenHeight = context.getScaledWindowHeight();
        TextRenderer textRenderer = client.textRenderer;

        context.fill(4, 4, 10 + textRenderer.getWidth(cachedFpsBadge), 16, 0x66000000);
        context.drawTextWithShadow(textRenderer, cachedFpsBadge, 7, 6, 0xFF55FF55);

        if (!client.player.isCreative() && !client.player.isSpectator()) {
            int hungerRightX = (screenWidth / 2) + 91;
            int hungerY = screenHeight - 39;

            int satWidth = Math.min(81, Math.round((cachedSaturation / 20.0f) * 81.0f));
            if (satWidth > 0) {
                context.fill(hungerRightX - satWidth, hungerY + 7, hungerRightX, hungerY + 9, 0xFFFFCC00);
            }

            if (previewFoodRestore > 0) {
                String foodPreview = String.format("+%d Food | +%.1f Sat", previewFoodRestore, previewSaturationRestore);
                int fw = textRenderer.getWidth(foodPreview);
                context.fill(hungerRightX - fw - 4, hungerY - 12, hungerRightX, hungerY - 1, 0x88000000);
                context.drawTextWithShadow(textRenderer, foodPreview, hungerRightX - fw - 2, hungerY - 10, 0xFFFFD700);
            }
        }

        int baseX = (screenWidth / 2) - 115;
        int baseY = screenHeight - 22;

        int renderedCount = 0;
        for (int i = 0; i < 4; i++) {
            ItemStack stack = cachedArmor[i];
            if (stack == null || stack.isEmpty()) continue;

            int yPos = baseY - (renderedCount * 18);

            context.fill(baseX - 2, yPos - 1, baseX + 18, yPos + 17, 0x66000000);
            context.drawItem(stack, baseX, yPos);
            context.drawStackOverlay(textRenderer, stack, baseX, yPos);

            String duraText = cachedDurabilityText[i];
            if (!duraText.isEmpty()) {
                int textWidth = textRenderer.getWidth(duraText);
                context.drawTextWithShadow(textRenderer, duraText, baseX - textWidth - 5, yPos + 4, cachedDurabilityColor[i]);
            }

            if (showWarning[i]) {
                context.drawTextWithShadow(textRenderer, "!", baseX + 20, yPos + 4, 0xFFFF3333);
            }

            renderedCount++;
        }
    }
}
