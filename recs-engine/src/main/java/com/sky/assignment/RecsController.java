package com.sky.assignment;

import static com.sky.assignment.cache.RecommendationsCache.*;

import com.sky.assignment.model.Recommendations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/recs")
public class RecsController {

    private RecsEngine recsEngine;

    @Autowired
    public RecsController(RecsEngine recsEngine) {
        this.recsEngine = recsEngine;
    }

    @RequestMapping(value = {"/personalised"}, method = RequestMethod.GET)
    @ResponseBody
    public Recommendations getPersonalisedRecommendations(@RequestParam("num") Long numberOfRecs,
                                                          @RequestParam("start") Long start,
                                                          @RequestParam("end") Long end,
                                                          @RequestParam("subscriber") String subscriber) {
        Recommendations recommendations = getRecommendationsFromCache(subscriber);
        if (recommendations.recommendations.isEmpty()) {
          recommendations = recsEngine.recommend(numberOfRecs, start, end);

          long cachePeriod = 5*60*1000;
          if (end < (start + cachePeriod)) {
            cachePeriod = end - start;
          }
          addRecommendationsToCache(subscriber, recommendations, cachePeriod);
        }
        return recommendations;
    }
}
