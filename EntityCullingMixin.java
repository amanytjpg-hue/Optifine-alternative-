package com.vishal.ultrafps.mixin;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Frustum;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.entity.Entity;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
@Mixin(EntityRenderDispatcher.class)
public class EntityCullingMixin {
    @Inject(method = "shouldRender", at = @At("HEAD"), cancellable = true)
    private void cull(Entity entity, Frustum f, double x, double y, double z, CallbackInfoReturnable<Boolean> cir) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null) return;
        if (entity instanceof PlayerEntity || entity instanceof EnderDragonEntity) return;
        if (client.player.squaredDistanceTo(entity) > 2304.0 && !entity.isGlowing()) cir.setReturnValue(false);
    }
}
