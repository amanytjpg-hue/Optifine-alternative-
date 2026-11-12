package com.vishal.ultrafps.mixin;
import com.google.common.collect.Ordering;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.entity.effect.StatusEffectInstance;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import java.util.Collection;
@Mixin(InGameHud.class)
public class StatusEffectBarMixin {
    @Shadow @Final private MinecraftClient client;
    @Inject(method = "renderStatusEffectOverlay", at = @At("TAIL"))
    private void renderBars(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) {
        if (this.client.player == null) return;
        Collection<StatusEffectInstance> col = this.client.player.getStatusEffects();
        if (col.isEmpty()) return;
        int count = 0;
        for (StatusEffectInstance effect : Ordering.natural().reverse().sortedCopy(col)) {
            if (!effect.shouldShowIcon()) continue;
            count++;
            int x = context.getScaledWindowWidth() - (25 * count);
            int dur = effect.getDuration();
            if (effect.isInfinite()) continue;
            int barWidth = Math.min(20, Math.max(0, dur / 20));
            context.fill(x + 2, 21, x + 22, 23, 0xAA000000);
            context.fill(x + 2, 21, x + 2 + barWidth, 23, (dur <= 100) ? 0xFFFF3333 : 0xFF55FF55);
        }
    }
}
