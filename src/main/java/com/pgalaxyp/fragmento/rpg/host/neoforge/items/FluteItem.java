package com.pgalaxyp.fragmento.rpg.host.neoforge.items;

import com.pgalaxyp.fragmento.rpg.core.domain.ids.ActorId;
import com.pgalaxyp.fragmento.rpg.core.events.intent.IntentEnvelope;
import com.pgalaxyp.fragmento.rpg.host.neoforge.bootstrap.client.ClientRpgRuntime;
import com.pgalaxyp.fragmento.rpg.host.neoforge.input.FluteIntentBuilder;
import com.pgalaxyp.fragmento.rpg.platform.neoforge.net.wire.NeoForgeNetWire;
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
            ActorId actorId = new ActorId(player.getUUID());
            IntentEnvelope env = FluteIntentBuilder.buildClientIntent(actorId, ClientRpgRuntime.lastSnapshot());
            NeoForgeNetWire.sendIntentToServer(env);
            return InteractionResultHolder.sidedSuccess(stack, true);
        }

        return InteractionResultHolder.sidedSuccess(stack, false);
    }
}