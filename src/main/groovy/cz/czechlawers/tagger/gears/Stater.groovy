package cz.czechlawers.tagger.gears

import cz.czechlawers.tagger.content.StringContent
import cz.czechlawers.tagger.state.Context

class Stater extends AGear
{
    final static int DONE = 1

    String message = ""

    Stater(String name) {
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
        context.setContent(new StringContent(message))
        context.setState(DONE)
    }
}
