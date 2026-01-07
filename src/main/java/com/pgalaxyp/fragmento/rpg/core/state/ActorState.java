package com.pgalaxyp.fragmento.rpg.core.state;

public record ActorState(
        long actorId,
        ActionState actionState,
        ComboState comboState
) {}