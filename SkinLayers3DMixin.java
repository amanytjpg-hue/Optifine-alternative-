package com.vishal.ultrafps.mixin;
import com.vishal.ultrafps.UltraFpsClient;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.state.PlayerEntityRenderState;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
@Mixin(PlayerEntityRenderer.class)
public class SkinLayers3DMixin {
    @Inject(method = "render(Lnet/minecraft/client/render/entity/state/PlayerEntityRenderState;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V", at = @At("HEAD"))
    private void apply3d(PlayerEntityRenderState state, MatrixStack ms, VertexConsumerProvider vcp, int light, CallbackInfo ci) {
        if (!UltraFpsClient.skinAndCapesEnabled) return;
        if (state.squaredDistanceToCamera <= 324.0) ms.scale(1.015f, 1.015f, 1.015f);
    }
}
