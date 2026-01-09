package com.pgalaxyp.fragmento.rpg.core.event.intent;

public sealed interface DomainIntent permits ActorJoinIntent, ComboAdvanceIntent, ComboStartIntent {}