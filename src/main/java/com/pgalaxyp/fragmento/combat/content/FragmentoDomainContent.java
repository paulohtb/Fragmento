package com.pgalaxyp.fragmento.combat.content;

import com.pgalaxyp.fragmento.combat.content.api.ContentCatalog;
import com.pgalaxyp.fragmento.combat.content.bard.BardContentPack;
import java.util.List;

public final class FragmentoDomainContent {
    public static final ContentCatalog CATALOG = ContentCatalog.of(List.of(BardContentPack.INSTANCE));
    private FragmentoDomainContent() {}
}