package com.vishal.ultrafps.mixin;
import com.vishal.ultrafps.UltraFpsClient;
import net.minecraft.client.render.entity.feature.CapeFeatureRenderer;
import net.minecraft.client.render.entity.state.PlayerEntityRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.util.math.RotationAxis;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
@Mixin(CapeFeatureRenderer.class)
public class WaveyCapesPhysicsMixin {
    @Inject(method = "render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;ILnet/minecraft/client/render/entity/state/PlayerEntityRenderState;FF)V", at = @At("HEAD"))
    private void applyWave(MatrixStack ms, VertexConsumerProvider vcp, int light, PlayerEntityRenderState state, float la, float ld, CallbackInfo ci) {
        if (!UltraFpsClient.skinAndCapesEnabled) return;
        float timeWave = (float) Math.sin((state.age * 0.15f) + la * 0.4f) * (3.5f + ld * 6.0f);
        ms.multiply(RotationAxis.POSITIVE_X.rotationDegrees(timeWave));
    }
}
