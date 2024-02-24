package com.caspian.client.api.manager.player.rotation;

import com.caspian.client.Caspian;
import com.caspian.client.api.event.listener.EventListener;
import com.caspian.client.api.module.RotationModule;
import com.caspian.client.impl.event.network.MovementPacketsEvent;
import com.caspian.client.impl.event.network.PacketEvent;
import com.caspian.client.impl.event.render.entity.RenderPlayerEvent;
import com.caspian.client.init.Modules;
import com.caspian.client.util.Globals;
import com.caspian.client.util.math.timer.CacheTimer;
import com.caspian.client.util.math.timer.Timer;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.math.MathHelper;

import java.util.PriorityQueue;

/**
 *
 *
 * @author linus
 * @since 1.0
 */
public class RotationManager implements Globals
{
    //
    private float yaw, pitch;
    //
    private RotationRequest rotation;
    private final PriorityQueue<RotationRequest> requests =
            new PriorityQueue<>((r1, r2) ->
            {
                if (r1.getPriority() == r2.getPriority())
                {
                    return Long.compare(r1.getTime(), r2.getTime());
                }
                return Integer.compare(r1.getPriority(), r2.getPriority());
            });
    private final Timer rotateTimer = new CacheTimer();

    /**
     *
     *
     */
    public RotationManager()
    {
        Caspian.EVENT_HANDLER.subscribe(this);
    }

    /**
     *
     *
     * @param event
     */
    @EventListener
    public void onPacketOutbound(PacketEvent.Outbound event)
    {
        if (mc.player == null || mc.world == null)
        {
            return;
        }
        if (event.getPacket() instanceof PlayerMoveC2SPacket packet
                && packet.changesLook())
        {
            yaw = packet.getYaw(yaw);
            pitch = packet.getPitch(pitch);
        }
    }

    /**
     *
     * @param event
     */
    @EventListener
    public void onMovementPackets(MovementPacketsEvent event)
    {
        rotation = getRotationActive();
        if (rotation != null)
        {
            rotateTimer.reset();
            event.cancel();
            event.setYaw(rotation.getYaw());
            event.setPitch(rotation.getPitch());
        }
    }

    /**
     *
     *
     * @param event
     */
    @EventListener
    public void onRenderPlayer(RenderPlayerEvent event)
    {
        if (event.getEntity() == mc.player)
        {
            // Match packet server rotations
            event.setYaw(getWrappedYaw());
            event.setPitch(getPitch());
            event.cancel();
        }
    }

    /**
     *
     *
     * @param requester
     * @param priority
     * @param yaw
     * @param pitch
     */
    public void setRotation(final RotationModule requester,
                            final RotationPriority priority,
                            final float yaw,
                            final float pitch)
    {
        for (RotationRequest r : requests)
        {
            if (requester == r.getRequester())
            {
                r.setYaw(yaw);
                r.setPitch(pitch);
                return;
            }
        }
        requests.add(new RotationRequest(requester, priority, yaw, pitch));
    }
    /**
     *
     *
     * @param requester
     * @param yaw
     * @param pitch
     */
    public void setRotation(final RotationModule requester,
                            final float yaw,
                            final float pitch)
    {
        setRotation(requester, RotationPriority.NORMAL, yaw, pitch);
    }

    /**
     *
     *
     * @param request
     */
    public boolean removeRotation(final RotationRequest request)
    {
        return requests.remove(request);
    }

    /**
     *
     *
     * @param requester
     */
    public void removeRotation(final RotationModule requester)
    {
        requests.removeIf(r -> requester == r.getRequester());
    }

    /**
     *
     * @param yaw
     * @param pitch
     */
    public void setRotationClient(float yaw, float pitch)
    {
        if (mc.player == null)
        {
            return;
        }
        mc.player.setYaw(yaw);
        mc.player.setHeadYaw(yaw);
        mc.player.setBodyYaw(yaw);
        mc.player.setPitch(pitch);
    }

    /**
     *
     *
     * @return
     */
    public boolean isRotating()
    {
        return !rotateTimer.passed(Modules.ROTATIONS.getPreserveTicks() * 50.0f);
    }

    /**
     *
     *
     * @return
     */
    public RotationRequest getRotationActive()
    {
        if (requests.isEmpty())
        {
            return null;
        }
        return requests.poll();
    }

    /**
     *
     * @return
     */
    public RotationModule getRotatingModule()
    {
        if (rotation == null)
        {
            return null;
        }
        return rotation.getRequester();
    }

    /**
     *
     * @return
     */
    public float getYaw()
    {
        return yaw;
    }

    /**
     *
     * @return
     */
    public float getWrappedYaw()
    {
        return MathHelper.wrapDegrees(yaw);
    }

    /**
     *
     * @return
     */
    public float getPitch()
    {
        return pitch;
    }
}
