package com.pgalaxyp.fragmento.rpg.core.events.intent;

public sealed interface DomainIntent permits ActorJoinIntent, ComboAdvanceIntent, ComboStartIntent, PerformActionIntent {}