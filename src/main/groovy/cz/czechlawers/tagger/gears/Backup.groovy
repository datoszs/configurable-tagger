package cz.czechlawers.tagger.gears

import cz.czechlawers.tagger.content.HTMLContent
import cz.czechlawers.tagger.content.StringContent
import cz.czechlawers.tagger.exceptions.InputTypeError
import cz.czechlawers.tagger.state.Context

class Backup extends AGear
{
    final static int DONE = 1

    Backup(String name) {
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
        context.backupContent();
        context.setState(DONE)
    }
}
