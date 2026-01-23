package com.pgalaxyp.fragmento.combat.contentModule;

import com.pgalaxyp.fragmento.combat.contentModule.api.ContentCatalog;
import com.pgalaxyp.fragmento.combat.contentModule.bard.BardContentPack;
import java.util.List;

public final class FragmentoDomainContent {
    public static final ContentCatalog CATALOG = ContentCatalog.of(List.of(BardContentPack.INSTANCE));
    private FragmentoDomainContent() {}
}