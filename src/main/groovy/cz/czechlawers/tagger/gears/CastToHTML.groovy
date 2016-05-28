package cz.czechlawers.tagger.gears

import cz.czechlawers.tagger.content.HTMLContent
import cz.czechlawers.tagger.content.StringContent
import cz.czechlawers.tagger.exceptions.InputTypeError
import cz.czechlawers.tagger.state.Context

class CastToHTML extends AGear
{
    final static int CASTED = 1

    CastToHTML(String name) {
        super(name)
    }

    @Override
    boolean canProcess(Context context)
    {
        if (!(context.getContent() instanceof StringContent)) {
            throw new InputTypeError('String content is expected.');
        }
        return true
    }

    @Override
    void process(Context context)
    {
        StringContent content = (StringContent) context.getContent()
        context.setContent(new HTMLContent(content.getContent()));
        context.setState(CASTED)
    }
}
