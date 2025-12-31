package com.pgalaxyp.fragmento.combat.engine.adapter.action;

public interface WeaponActionListener {

    void onActionStarted(int localActionId);

    void onActionCancelled(int localActionId);
}