package com.zjl.mockgps.app.Common;

import java.util.Collection;

/**
 * Created by C0dEr on 15/9/26.
 */
public class CollectionExtension {
    public static <T extends Collection> boolean IsNullOrEmpty(T t) {
        return t == null || t.size() == 0;
    }
}
