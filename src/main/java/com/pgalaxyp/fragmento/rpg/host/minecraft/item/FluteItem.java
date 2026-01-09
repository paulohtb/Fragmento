package com.pgalaxyp.fragmento.rpg.host.minecraft.item;

import com.pgalaxyp.fragmento.rpg.core.domain.ids.ActorId;
import com.pgalaxyp.fragmento.rpg.core.event.intent.ComboAdvanceIntent;
import com.pgalaxyp.fragmento.rpg.core.event.intent.ComboStartIntent;
import com.pgalaxyp.fragmento.rpg.core.event.intent.IntentEnvelope;
import com.pgalaxyp.fragmento.rpg.engine.snapshot.GameSnapshot;
import com.pgalaxyp.fragmento.rpg.platform.minecraft.ids.MinecraftActorIds;
import com.pgalaxyp.fragmento.rpg.platform.minecraft.net.FragmentoNet;
import com.pgalaxyp.fragmento.rpg.platform.minecraft.runtime.FragmentoClientRuntime;
import java.util.Optional;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public final class FluteItem extends Item {

    public FluteItem(Properties props) {
        super(props);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (level.isClientSide()) {
            ActorId actorId = MinecraftActorIds.fromUuid(player.getUUID());
            IntentEnvelope env = buildClientIntent(actorId);
            FragmentoNet.sendIntentToServer(env);
            return InteractionResultHolder.sidedSuccess(stack, true);
        }

        return InteractionResultHolder.sidedSuccess(stack, false);
    }

    private static IntentEnvelope buildClientIntent(ActorId actorId) {
        Optional<GameSnapshot> snapOpt = FragmentoClientRuntime.lastSnapshot();
        if (snapOpt.isPresent()) {
            var s = snapOpt.get().actors().get(actorId);
            if (s != null && s.combo().isPresent()) {
                return IntentEnvelope.of(actorId, new ComboAdvanceIntent(s.combo().get().actionId()));
            }
        }
        return IntentEnvelope.of(actorId, new ComboStartIntent());
    }
}