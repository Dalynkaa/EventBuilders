package com.otsosity.spbuildrevrited.utils.dataClasses;

import java.util.UUID;

public final class PlotVote {
    private final PlotPlayer voter;
    private final UUID voted;
    private final UUID plot_id;
    private final Integer type;

    public PlotVote(PlotPlayer voter, UUID voted, UUID plotId, Integer type) {
        this.voter = voter;
        this.voted = voted;
        this.plot_id = plotId;
        this.type = type;
    }

    public Integer getType() {
        return type;
    }

    public UUID getPlotId() {
        return plot_id;
    }

    public UUID getVoted() {
        return voted;
    }

    public PlotPlayer getVoter() {
        return voter;
    }
}
