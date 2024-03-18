package net.shoreline.client.impl.module.movement;

import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.c2s.play.HandSwingC2SPacket;
import net.minecraft.network.packet.c2s.play.UpdateSelectedSlotC2SPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Vec3d;
import net.shoreline.client.api.config.Config;
import net.shoreline.client.api.config.setting.EnumConfig;
import net.shoreline.client.api.event.EventStage;
import net.shoreline.client.api.event.listener.EventListener;
import net.shoreline.client.api.module.ModuleCategory;
import net.shoreline.client.api.module.ToggleModule;
import net.shoreline.client.impl.event.TickEvent;
import net.shoreline.client.impl.event.network.PacketEvent;
import net.shoreline.client.impl.event.network.PlayerTickEvent;
import net.shoreline.client.impl.event.network.PlayerUpdateEvent;
import net.shoreline.client.init.Managers;
import net.shoreline.client.mixin.accessor.AccessorPlayerMoveC2SPacket;
import net.shoreline.client.util.string.EnumFormatter;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.world.World;
import net.shoreline.client.util.Globals;

/**
 *
 *
 * @author linus
 * @since 1.0
 */
public class NoFallModule extends ToggleModule
{
    //
    Config<NoFallMode> modeConfig = new EnumConfig<>("Mode", "The mode to " +
            "prevent fall damage", NoFallMode.ANTI, NoFallMode.values());

    /**
     *
     */
    public NoFallModule()
    {
        super("NoFall", "Prevents all fall damage", ModuleCategory.MOVEMENT);
    }

    @Override
    public String getModuleData()
    {
        return EnumFormatter.formatEnum(modeConfig.getValue());
    }

    @EventListener
    public void onPlayerUpdate(PlayerUpdateEvent event)
    {
        if (event.getStage() != EventStage.PRE || mc.player.fallDistance <= mc.player.getSafeFallDistance()
                || mc.player.isOnGround() || mc.player.isFallFlying())
        {
            return;
        }
        if (modeConfig.getValue() == NoFallMode.LATENCY)
        {
            if (mc.world.getRegistryKey() == World.NETHER)
            {
                Managers.NETWORK.sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(
                        mc.player.getX(), 0, mc.player.getZ(), true));
            }
            else
            {
                Managers.NETWORK.sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(
                        0, 64, 0, true));
            }
            mc.player.fallDistance = 0.0f;
        }
        else if (modeConfig.getValue() == NoFallMode.GRIM)
        {
            Managers.NETWORK.sendPacket(new PlayerMoveC2SPacket.Full(mc.player.getX(), mc.player.getY() + 1.0e-9,
                    mc.player.getZ(), mc.player.getYaw(), mc.player.getPitch(), true));
            mc.player.onLanding();
        }
    }

    @EventListener
    public void onPacketOutbound(PacketEvent.Outbound event)
    {
        if (mc.player == null || mc.player.fallDistance <= mc.player.getSafeFallDistance()
                || mc.player.isOnGround() || mc.player.isFallFlying())
        {
            return;
        }
        if (event.getPacket() instanceof PlayerMoveC2SPacket packet)
        {
            if (modeConfig.getValue() == NoFallMode.PACKET)
            {
                ((AccessorPlayerMoveC2SPacket) packet).hookSetOnGround(true);
            }
            else if (modeConfig.getValue() == NoFallMode.ANTI)
            {
                double y = packet.getY(mc.player.getY());
                ((AccessorPlayerMoveC2SPacket) packet).hookSetY(y + 0.10000000149011612);
            }
        }
    }

    public enum NoFallMode
    {
        ANTI,
        LATENCY,
        PACKET,
        GRIM
    }
}
