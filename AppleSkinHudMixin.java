package com.vishal.ultrafps.mixin;
import com.vishal.ultrafps.UltraFpsClient;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
@Mixin(InGameHud.class)
public class AppleSkinHudMixin {
    @Shadow @Final private MinecraftClient client;
    @Inject(method = "renderFood", at = @At("HEAD"))
    private void renderExhaustion(DrawContext context, PlayerEntity player, int top, int right, CallbackInfo ci) {
        if (!UltraFpsClient.hudEnabled || player == null) return;
        float saturation = player.getHungerManager().getSaturationLevel();
        float ratio = Math.min(1.0f, Math.max(0.0f, saturation / 20.0f));
        int width = (int) (ratio * 81.0f);
        if (width > 0) context.fill(right - width, top - 2, right, top - 1, 0x88FFAA00);
    }
}
