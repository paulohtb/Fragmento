package com.pgalaxyp.fragmento.rpg.action.command;

public sealed interface ActionCommand permits ActionStart, ActionCancel, ActionAdvance {}