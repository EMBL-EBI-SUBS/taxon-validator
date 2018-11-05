package uk.ac.ebi.subs.validator.taxon.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * This is a Spring @Service component for retrieve a {@link Taxonomy} data object from ENA's database.
 * It tries to store the retrieved {@link Taxonomy} data in a local cache.
 * If the taxonomy data is in the cache, then it won't query ENA's database.
 */
@Service
public class TaxonomyService {
    private static final Logger logger = LoggerFactory.getLogger(TaxonomyService.class);
    private final String ENA_TAXONOMY_SERVICE = "http://www.ebi.ac.uk/ena/data/taxonomy/v1/taxon/";

    public TaxonomyService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    private RestTemplate restTemplate;

    private Map<String, Taxonomy> cache = new LRUCache(MAX_CACHE_ENTRIES);

    private static final int MAX_CACHE_ENTRIES = 1000;
    private static final long MILLIS_IN_SECOND = 1000;
    private static final long SECONDS_IN_MINUTE = 60;
    private static final long MINUTES_IN_HOUR = 60;
    private static final long ONE_HOUR_IN_MILLIS = MILLIS_IN_SECOND * SECONDS_IN_MINUTE * MINUTES_IN_HOUR;

    @Scheduled(initialDelay = ONE_HOUR_IN_MILLIS, fixedRate = ONE_HOUR_IN_MILLIS)
    public void clearCache(){
        logger.debug("clearing taxonomy cache");
        cache.clear();
    }

    public Taxonomy getTaxonById(String id) {

        Taxonomy taxonomy = cache.get(id);

        if (taxonomy == null) {
            logger.debug("cache miss, fetching taxonomy {}", id);
            taxonomy = restTemplate.getForObject(ENA_TAXONOMY_SERVICE + "tax-id/" + id, Taxonomy.class);
            cache.put(id, taxonomy);
        }
        else {
            logger.debug("cache hit");
        }

        return taxonomy;
    }

    /**
     * Local cache to store {@link Taxonomy} data.
     */
    private static class LRUCache extends LinkedHashMap<String, Taxonomy> {
        private int cacheSize;

        public LRUCache(int cacheSize) {
            super(16, 0.75f, true);
            this.cacheSize = cacheSize;
        }

        protected boolean removeEldestEntry(Map.Entry<String, Taxonomy> eldest) {
            return size() >= cacheSize;
        }
    }
}
