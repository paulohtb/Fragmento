package com.pgalaxyp.fragmento.cosmetic.common.model;

import java.util.Collection;
import java.util.List;

public interface CosmeticCatalog {
    CosmeticInfo get(CosmeticId id);
    List<CosmeticInfo> bySlot(CosmeticSlot slot);
    Collection<CosmeticInfo> all();
    int dataVersion();
}