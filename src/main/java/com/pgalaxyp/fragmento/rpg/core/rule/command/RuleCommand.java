package com.pgalaxyp.fragmento.rpg.core.rule.command;

public sealed interface RuleCommand
        permits RequestTargeting, ApplyEffect {}