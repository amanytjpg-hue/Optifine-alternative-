package com.vishal.ultrafps.mixin;
import com.vishal.ultrafps.UltraFpsClient;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
@Mixin(GameRenderer.class)
public class ZoomAndFpsMixin {
    @Shadow @Final MinecraftClient client;
    @Inject(method = "getFov(Lnet/minecraft/client/render/Camera;FZ)D", at = @At("RETURN"), cancellable = true)
    private void zoom(Camera camera, float td, boolean cf, CallbackInfoReturnable<Double> cir) {
        if (UltraFpsClient.isZooming()) {
            this.client.options.smoothCameraEnabled = true;
            cir.setReturnValue(cir.getReturnValue() * UltraFpsClient.zoomLevel);
        } else if (UltraFpsClient.zoomKey != null && !UltraFpsClient.zoomKey.isPressed()) {
            this.client.options.smoothCameraEnabled = false;
        }
    }
}
