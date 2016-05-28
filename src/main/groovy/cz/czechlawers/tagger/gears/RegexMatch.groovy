package cz.czechlawers.tagger.gears

import cz.czechlawers.tagger.exceptions.ConfigurationError
import cz.czechlawers.tagger.exceptions.InputTypeError
import cz.czechlawers.tagger.state.Context
import cz.czechlawers.tagger.content.StringContent

class RegexMatch extends AGear
{
    final static int MATCHED = 1
    final static int NOT_MATCHED = 2
    String regex

    RegexMatch(String name) {
        super(name)
    }

    @Override
    boolean canProcess(Context context)
    {
        if (!regex) {
            throw new ConfigurationError('Regex has to be provided.')
        }
        if (! (context.getContent() instanceof StringContent)) {
            throw new InputTypeError('String content is expected.')
        }
        return true
    }

    @Override
    void process(Context context)
    {
        StringContent content = (StringContent) context.getContent()
        def text = content.get();
        def match = text =~ regex
        if (match) {
            context.setState(MATCHED)
        } else {
            context.setState(NOT_MATCHED)
        }

    }
}
