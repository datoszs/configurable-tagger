package cz.czechlawers.tagger.gears

import cz.czechlawers.tagger.content.StringContent
import cz.czechlawers.tagger.state.Context

class ErrorStater extends AGear
{
    final static int DONE = 1

    String message = ""
    boolean empty = true

    ErrorStater(String name) {
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
        if (empty) {
            context.setContent(new StringContent(""))
        }
        context.setErrorState(message)
        context.setState(DONE)
    }
}
