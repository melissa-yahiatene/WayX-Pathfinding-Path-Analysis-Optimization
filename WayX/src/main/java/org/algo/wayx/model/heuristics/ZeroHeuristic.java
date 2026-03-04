package org.algo.wayx.model.heuristics;

public class ZeroHeuristic implements Heuristic {
    @Override
    public double estimate(int c, int g, int ncols) {
        return 0;
    }
}



