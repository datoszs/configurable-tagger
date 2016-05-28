package cz.czechlawers.tagger.utils

import cz.czechlawers.tagger.exceptions.DuplicateGearNameError
import cz.czechlawers.tagger.exceptions.DuplicateTransitionError
import cz.czechlawers.tagger.exceptions.NoStartGearError
import cz.czechlawers.tagger.exceptions.NoSuchGearError
import cz.czechlawers.tagger.gears.AGear
import cz.czechlawers.tagger.state.Context

class Tagger
{
    Map<String, Map<Integer, String>> states = [:]
    Map<String, AGear> gears = [:]
    AGear start

    boolean debugMode = false;

    def setStart(String name)
    {
        if (gears.containsKey(name)) {
            start = gears[name];
        } else {
            throw new NoSuchGearError($/The gear with name [${name}] doesn't exist in this tagger./$)
        }
    }

    def addGear(AGear gear)
    {
        if (gears.containsKey(gear.getName())) {
            throw new DuplicateGearNameError($/The gear with name [${gear.getName()}] is already present in this tagger./$)
        }
        gears.put(gear.getName(), gear)
    }

    def getGear(String name)
    {
        if (gears.containsKey(name)) {
            return gears[name];
        } else {
            throw new NoSuchGearError($/The gear with name [${name}] doesn't exist in this tagger./$)
        }
    }

    def existGear(String name)
    {
        return gears.containsKey(name)
    }

    def addTransition(String from, int state, String to)
    {
        if (!existGear(from) || !existGear(to)) {
            throw new NoSuchGearError($/The gear with name [${from}] or [${to}] doesn't exist in this tagger./$)
        }
        if (states.containsKey(from) && states[from].containsKey(state)) {
            throw new DuplicateTransitionError($/The transition from [${from}] under state [${state}] is already defined./$)
        }
        if (!states.containsKey(from)) {
            states[from] = [:]
        }
        states[from][state] = to;
    }

    def setDebugMode(boolean state)
    {
        debugMode = state
    }

    def process(Context context)
    {
        if (!start) {
            throw new NoStartGearError('This tagger has no configured start gear.');
        }
        AGear gear = start
        while (gear != null) {
            // Check whether the current gear can process
            try {
                gear.canProcess(context)
            } catch (RuntimeException e) {
                println "[${gear.getName()}] <CANNOT PROCESSS: ${e.getMessage()}>"
                return;
            }
            // Process
            gear.process(context)
            if (debugMode) {
                println ""
                print "IN: [${gear.getName()}] <STATE: ${context.getState()}> "
            }
            // Move to the next gear according to state diagram (or finalized state)
            if (context.isFinalized()) {
                if (debugMode) {
                    println "HERE"
                }
                gear = null
            } else if (states.containsKey(gear.getName()) && states[gear.getName()].containsKey(context.getState())) {
                gear = getGear(states[gear.getName()][context.getState()])
            } else {
                gear = null
                if (debugMode) {
                    println(states)
                    println "AAA"
                }
            }
            if (debugMode) {
                if (gear) {
                    print "TRANSITION TO: [${gear.getName()}]"
                } else {
                    print "TRANSITION TO: nowhere"
                }
            }
        }
    }
}