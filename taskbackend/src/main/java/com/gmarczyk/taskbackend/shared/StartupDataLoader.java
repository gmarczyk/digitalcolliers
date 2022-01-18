package com.gmarczyk.taskbackend.shared;

import java.util.Collection;

public interface StartupDataLoader<T> {

    Collection<T> load();

}
