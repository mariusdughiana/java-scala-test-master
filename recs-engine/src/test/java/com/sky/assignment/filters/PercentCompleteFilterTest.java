package com.sky.assignment.filters;

import com.sky.assignment.model.Recommendation;
import org.junit.Assert;
import org.junit.Test;

import java.util.Date;
import java.util.UUID;

import static org.junit.Assert.fail;

public class PercentCompleteFilterTest {

  private final static PercentCompleteFilter FILTER = new PercentCompleteFilter();

  @Test
  public void testRecEndsBeforeTimeSlot() throws Exception {
    Long currentTime = new Date().getTime();
    Recommendation rec = new Recommendation(UUID.randomUUID().toString(), currentTime, currentTime + 100);
    Assert.assertFalse(FILTER.isRelevant(rec, currentTime + 1000, currentTime + 1500));
  }

  @Test
  public void testRecStartsAfterTimeSlot() throws Exception {
    Long currentTime = new Date().getTime();
    Recommendation rec = new Recommendation(UUID.randomUUID().toString(), currentTime + 2000, currentTime + 2100);
    Assert.assertFalse(FILTER.isRelevant(rec, currentTime + 1000, currentTime + 1500));
  }

  @Test
  public void testTimeSlotEndsAfterRec() throws Exception {
    Long currentTime = new Date().getTime();
    Recommendation rec = new Recommendation(UUID.randomUUID().toString(), currentTime, currentTime + 100);
    Assert.assertFalse(FILTER.isRelevant(rec, currentTime, currentTime + 150));
  }

  @Test
  public void testRecPassed60Prc() throws Exception {
    Long currentTime = new Date().getTime();
    Recommendation rec = new Recommendation(UUID.randomUUID().toString(), currentTime, currentTime + 100);
    Assert.assertFalse(FILTER.isRelevant(rec, currentTime + 60, currentTime + 80));
  }

  @Test
  public void testRecMatchesTimeSlot() throws Exception {
    Long currentTime = new Date().getTime();
    Recommendation rec = new Recommendation(UUID.randomUUID().toString(), currentTime, currentTime + 100);
    Assert.assertTrue(FILTER.isRelevant(rec, currentTime + 59, currentTime + 80));
  }

}
