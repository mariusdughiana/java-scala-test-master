package com.sky.assignment.cache;

import com.sky.assignment.model.Recommendation;
import com.sky.assignment.model.Recommendations;

import java.util.*;

public class RecommendationsCache {

  private static final Map<String, Set<CacheEntry>> CACHE = new HashMap<String, Set<CacheEntry>>();


  public static void addRecommendationsToCache(final String subscriber,
                                               final Recommendations recommendations,
                                               final Long period) {
    if (!CACHE.containsKey(subscriber)) {
      CACHE.put(subscriber, new HashSet<CacheEntry>());
    }
    CacheEntry cacheEntry = new CacheEntry(period, recommendations);
    CACHE.get(subscriber).add(cacheEntry);
  }

  public static Recommendations getRecommendationsFromCache(final String subscriber) {
    final List<Recommendation> recommendations = new ArrayList<Recommendation>();

    if (CACHE.containsKey(subscriber)) {
      Set<CacheEntry> cacheEntries = CACHE.get(subscriber);
      for (Iterator<CacheEntry> it = cacheEntries.iterator(); it.hasNext(); ) {
        CacheEntry cacheEntry = it.next();
        if (cacheEntry.isExpired()) {
          it.remove();
        } else {
          recommendations.addAll(cacheEntry.getRecommendations().recommendations);
        }
      }
    }

    return new Recommendations(recommendations);
  }

  private static class CacheEntry {
    private final Long timeCreated;
    private final Long cachePeriod;
    private final Recommendations recommendations;

    public CacheEntry(Long cachePeriod, Recommendations recommendations) {
      timeCreated = new Date().getTime();
      this.cachePeriod = cachePeriod;
      this.recommendations = recommendations;
    }

    public boolean isExpired() {
      return (new Date().getTime() - timeCreated) > cachePeriod;
    }

    public Recommendations getRecommendations() {
      return recommendations;
    }
  }

  public static void clearCache() {
    CACHE.clear();
  }
}
