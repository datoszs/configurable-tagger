package cz.czechlawers.tagger.gears

import cz.czechlawers.tagger.state.Context

class Restore extends AGear
{
    final static int DONE = 1

    Restore(String name) {
        super(name)
    }

    @Override
    boolean canProcess(Context context)
    {
        return true
    }

    @Override
    void process(Context context)
    {
        context.restoreContent();
        context.setState(DONE)
    }
}
