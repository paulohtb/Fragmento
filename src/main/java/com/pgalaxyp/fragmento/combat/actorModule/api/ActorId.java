package com.pgalaxyp.fragmento.combat.actorModule.api;

import java.util.*;

public record ActorId(UUID value) {  public ActorId { Objects.requireNonNull(value); } }