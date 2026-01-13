package com.pgalaxyp.fragmento.rpg.action.runtime;

public sealed interface ActionCommand permits StartAction, AdvanceAction, CancelAction {}