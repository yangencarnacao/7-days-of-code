package com.angelozero.daysofcode.usecase;

import com.angelozero.daysofcode.gateway.ImdbApiClient;
import com.angelozero.daysofcode.config.ImdbProperties;
import com.angelozero.daysofcode.usecase.domain.ImdbDomain;
import com.angelozero.daysofcode.usecase.mapper.ImdbUseCaseMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class GetTop250Movies {

    private final ImdbApiClient imdbApiClient;
    private final ImdbProperties imdbProperties;
    private final ImdbUseCaseMapper imdbUseCaseMapper;

    @Cacheable("imdb250MoviesCache")
    public List<ImdbDomain> execute() {
        try {
            log.info("\n[INFO] - Getting the 250 top movies\n");
            return imdbApiClient.getTop250Movies(imdbProperties.getApiKey())
                    .getItems()
                    .stream()
                    .map(imdbUseCaseMapper::toDomain)
                    .collect(Collectors.toList());

        } catch (Exception ex) {
            log.error("\n[ERROR] - IMDB API CLIENT: " + ex.getMessage() + "\n");
            throw new RuntimeException(ex);
        }
    }
}
