package com.gmarczyk.digitalcolliers.shared;

import java.util.Collection;

public interface StartupDataLoader<T> {

    Collection<T> load();

}
