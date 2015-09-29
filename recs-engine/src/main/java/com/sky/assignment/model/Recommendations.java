package com.sky.assignment.model;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement
public class Recommendations {

    public final List<Recommendation> recommendations;

    private Recommendations() {
        this(null);
    }

    public Recommendations(List<Recommendation> recommendations) {
        this.recommendations = recommendations;
    }
}
