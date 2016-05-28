package cz.czechlawers.tagger.gears

import cz.czechlawers.tagger.state.Context

class Finalize extends AGear
{
    final static int FINALIZED = 1

    Finalize(String name) {
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
        context.setState(FINALIZED)
        context.markFinalized();
    }
}
