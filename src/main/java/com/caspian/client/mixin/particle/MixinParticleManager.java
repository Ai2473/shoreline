package com.caspian.client.mixin.particle;

import com.caspian.client.Caspian;
import com.caspian.client.impl.event.particle.ParticleEvent;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.particle.ParticleEffect;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 *
 *
 * @author linus
 * @since 1.0
 */
@Mixin(ParticleManager.class)
public class MixinParticleManager
{
    /**
     *
     *
     * @param parameters
     * @param x
     * @param y
     * @param z
     * @param velocityX
     * @param velocityY
     * @param velocityZ
     * @param cir
     */
    @Inject(method = "addParticle(Lnet/minecraft/particle/ParticleEffect;" +
            "DDDDDD)Lnet/minecraft/client/particle/Particle;", at = @At(value =
            "HEAD"), cancellable = true)
    private void hookAddParticle(ParticleEffect parameters, double x,
                                 double y, double z, double velocityX,
                                 double velocityY, double velocityZ,
                                 CallbackInfoReturnable<Particle> cir)
    {
        ParticleEvent particleEvent = new ParticleEvent(parameters);
        Caspian.EVENT_HANDLER.dispatch(particleEvent);
        if (particleEvent.isCanceled())
        {
            cir.cancel();
        }
    }
}