package cnr.imaa.baron.model;

import cnr.imaa.baron.commons.BaronCommons;

import java.time.Instant;

public abstract class BaseObjBaronPartitionTable extends BaseObjBaron {
    private Instant dateOfObservation;

    public Instant getDateOfObservation() {
        return dateOfObservation;
    }

    public void setDateOfObservation(Instant dateOfObservation) {
        this.dateOfObservation = dateOfObservation;
    }

    public Integer getYear(){
        return BaronCommons.getYear(this.dateOfObservation);
    }
}
