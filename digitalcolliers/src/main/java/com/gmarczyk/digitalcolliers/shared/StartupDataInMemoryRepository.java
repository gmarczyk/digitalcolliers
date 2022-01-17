package com.gmarczyk.digitalcolliers.shared;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Collection;

@Component
@RequiredArgsConstructor
@Getter
public abstract class StartupDataInMemoryRepository<T> {

    private final Collection<T> data = new ArrayList<>();
    private final Collection<StartupDataLoader<T>> dataLoaders;

    /**
     * Will be loaded at application startup
     */
    @PostConstruct
    public void loadAtStartup() {
        this.data.addAll(dataLoaders.stream()
                                    .map(loader -> loader.load())
                                    .flatMap(Collection::stream)
                                    .toList());
    }


}
