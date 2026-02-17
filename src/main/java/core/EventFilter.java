package core;


import dependencies.I_Event;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.function.Function;


public class EventFilter {


    // -+- CREATION -+- //

    public EventFilter() {
        _INTERESTS = new HashSet<>();
        _CALLBACKS = new HashSet<>();

        _CACHE = new IdentityHashMap<>();
    }


    // -+- PARAMETERS -+- //

    // FINALS //

    private final HashSet<Class<? extends I_Event>> _INTERESTS;
    private final HashSet<Function<I_Event, Boolean>> _CALLBACKS;

    private final IdentityHashMap<I_Event, Boolean> _CACHE;


    // -+- INTERESTS MANAGEMENT -+- //

    public void addInterest(Class<? extends I_Event> interest) {
        if (interest == null) return;

        _INTERESTS.add(interest);
    }
    public void addInterests(Collection<Class<? extends I_Event>> interests) {
        if (interests == null) return;

        interests.forEach(this::addInterest);
    }

    public void rmvInterest(Class<? extends I_Event> interest) {
        if (interest == null) return;

        _CACHE.clear();

        _INTERESTS.remove(interest);
    }
    public void rmvInterests(Collection<Class<? extends I_Event>> interests) {
        if (interests == null) return;

        _CACHE.clear();

        interests.forEach(this::rmvInterest);
    }

    public void clearInterests() {
        _INTERESTS.clear();

        _CACHE.clear();
    }

    public HashSet<Class<? extends I_Event>> getInterests() {
        return new HashSet<>(_INTERESTS);
    }

    public boolean contains(Class<? extends I_Event> interest) {
        return _INTERESTS.contains(interest);
    }


    // -+- CALLBACK MANAGEMENT -+- //

    public void addCallback(Function<I_Event, Boolean> callback) {
        if (callback == null) return;

        _CALLBACKS.add(callback);

        _CACHE.clear();
    }
    public void addCallbacks(Collection<Function<I_Event, Boolean>> callbacks) {
        if (callbacks == null) return;

        callbacks.forEach(this::addCallback);
    }

    public void rmvCallback(Function<I_Event, Boolean> callback) {
        _CALLBACKS.remove(callback);
    }
    public void rmvCallbacks(Collection<Function<I_Event, Boolean>> callbacks) {
        callbacks.forEach(this::rmvCallback);
    }

    public void clearCallbacks() {
        _CALLBACKS.clear();
    }

    public boolean contains(Function<I_Event, Boolean> callback) {
        return _CALLBACKS.contains(callback);
    }


    // -+- FILTER LOGIC -+- //

    public boolean check(I_Event event) {
        if (!_INTERESTS.isEmpty()) {
            if (!_INTERESTS.contains(event.getClass())) {
                _CACHE.put(event, false);

                return false;
            }
        }

        for (Function<I_Event, Boolean> callback : _CALLBACKS) {
            if (!callback.apply(event)) {
                _CACHE.put(event, false);

                return false;
            }
        }

        _CACHE.put(event, true);

        return true;
    }
    public ArrayList<I_Event> check(Collection<I_Event> events) {
        ArrayList<I_Event> validEvents;

        validEvents = new ArrayList<>();

        for (I_Event event : events) {
            if (!check(event)) continue;

            validEvents.add(event);
        }

        return validEvents;
    }

    public void clear() {
        clearInterests();
        clearCallbacks();

        _CACHE.clear();
    }


}