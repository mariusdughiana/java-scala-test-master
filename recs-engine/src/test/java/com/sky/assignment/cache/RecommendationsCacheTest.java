package com.sky.assignment.cache;

import com.sky.assignment.model.Recommendation;
import com.sky.assignment.model.Recommendations;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.*;

public class RecommendationsCacheTest {


  @Before
  public void clearCache () {
    RecommendationsCache.clearCache();
  }


  @Test
  public void testRecommendationsCache() throws Exception {
    Recommendation rec1 = new Recommendation(UUID.randomUUID().toString(), 1L, 10000L);
    List<Recommendation> recs = new ArrayList<Recommendation>();
    recs.add(rec1);

    RecommendationsCache.addRecommendationsToCache("s1", new Recommendations(recs), 5000L);

    Recommendations recsFromCache = RecommendationsCache.getRecommendationsFromCache("s1");

    Assert.assertEquals(1, recsFromCache.recommendations.size());
    Assert.assertEquals(rec1, recsFromCache.recommendations.get(0));

  }

  @Test
  public void testCacheWithMultipleEntries() throws Exception {
    Recommendation rec1 = new Recommendation(UUID.randomUUID().toString(), 1L, 10000L);
    List<Recommendation> recs = new ArrayList<Recommendation>();
    recs.add(rec1);
    RecommendationsCache.addRecommendationsToCache("s1", new Recommendations(recs), 5000L);

    Recommendation rec2 = new Recommendation(UUID.randomUUID().toString(), 1L, 10000L);
    recs = new ArrayList<Recommendation>();
    recs.add(rec2);
    RecommendationsCache.addRecommendationsToCache("s1", new Recommendations(recs), 5000L);

    Recommendations recsFromCache = RecommendationsCache.getRecommendationsFromCache("s1");

    Assert.assertEquals(2, recsFromCache.recommendations.size());
    Assert.assertEquals(rec1, recsFromCache.recommendations.get(0));
    Assert.assertEquals(rec2, recsFromCache.recommendations.get(1));

  }

  @Test
  public void testCacheWithExpiredRecommendations() throws Exception {
    Recommendation rec1 = new Recommendation(UUID.randomUUID().toString(), 1L, 10000L);
    List<Recommendation> recs = new ArrayList<Recommendation>();
    recs.add(rec1);

    RecommendationsCache.addRecommendationsToCache("s1", new Recommendations(recs), 500L);

    Thread.currentThread().sleep(1000L);
    Recommendations recsFromCache = RecommendationsCache.getRecommendationsFromCache("s1");

    Assert.assertEquals(0, recsFromCache.recommendations.size());
  }
}