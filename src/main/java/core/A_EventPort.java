package core;


import dependencies.I_Event;

import java.util.Collection;


public abstract class A_EventPort {


    // -+- CREATION -+- //

    public A_EventPort(EventFilter filter) {
        if (filter == null) filter = new EventFilter();
        this.filter = filter;
    }


    // -+- PARAMETERS -+- //

    // NON-FINALS //

    public EventFilter filter;


    // -+- EVENT MANAGEMENT -+- //

    protected void p_push(I_Event event) {
        if (!filter.check(event)) return;

        p_pushValidEvent(event);
    }
    protected void p_push(Collection<I_Event> events) {
        events.forEach(this::p_push);
    }
    protected abstract void p_pushValidEvent(I_Event event);


}