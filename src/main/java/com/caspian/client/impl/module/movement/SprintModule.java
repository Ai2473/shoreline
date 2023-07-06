package com.caspian.client.impl.module.movement;

import com.caspian.client.api.config.Config;
import com.caspian.client.api.config.setting.EnumConfig;
import com.caspian.client.api.event.EventStage;
import com.caspian.client.api.event.listener.EventListener;
import com.caspian.client.api.module.ToggleModule;
import com.caspian.client.api.module.ModuleCategory;
import com.caspian.client.impl.event.TickEvent;
import com.caspian.client.init.Managers;
import com.caspian.client.util.player.InputUtil;
import com.caspian.client.util.string.EnumFormatter;
import net.minecraft.entity.effect.StatusEffects;

/**
 *
 *
 * @author linus
 * @since 1.0
 */
public class SprintModule extends ToggleModule
{
    //
    Config<SprintMode> modeConfig = new EnumConfig<>("Mode",
            "Sprinting mode. Rage allows for multi-directional sprinting.",
            SprintMode.LEGIT, SprintMode.values());
    /**
     *
     */
    public SprintModule()
    {
        super("Sprint", "Automatically sprints", ModuleCategory.MOVEMENT);
    }

    /**
     *
     *
     * @return
     */
    @Override
    public String getMetaData()
    {
        return EnumFormatter.formatEnum(modeConfig.getValue());
    }

    /**
     *
     */
    @EventListener
    public void onTick(TickEvent event)
    {
        if (event.getStage() == EventStage.PRE)
        {
            if (!Managers.POSITION.isSprinting()
                    && !Managers.POSITION.isSneaking()
                    && InputUtil.isInputtingMovement()
                    && mc.player.getHungerManager().getFoodLevel() > 6.0F
                    && !mc.player.hasStatusEffect(StatusEffects.BLINDNESS))
            {
                switch (modeConfig.getValue())
                {
                    case LEGIT ->
                    {
                        if (mc.player.input.hasForwardMovement()
                                && (!mc.player.horizontalCollision
                                || mc.player.collidedSoftly))
                        {
                            mc.player.setSprinting(true);
                        }
                    }
                    case RAGE -> mc.player.setSprinting(true);
                }
            }
        }
    }

    public enum SprintMode
    {
        LEGIT,
        RAGE
    }
}
