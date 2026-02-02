package com.pgalaxyp.fragmento.combat.contentModule;

import com.pgalaxyp.fragmento.combat.contentModule.bard.*;
import com.pgalaxyp.fragmento.combat.classModule.api.ClassId;
import com.pgalaxyp.fragmento.combat.contentModule.api.ContentCatalog;
import java.util.List;

public final class FragmentoDomainContent {
    public static final ContentCatalog CATALOG = ContentCatalog.of(List.of(BardContentPack.INSTANCE));
    public static final ClassId DEFAULT_CLASS_ID = BardIds.BARD;
    private FragmentoDomainContent() {}
}