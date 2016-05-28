package cz.czechlawers.tagger.gears

import cz.czechlawers.tagger.exceptions.ConfigurationError
import cz.czechlawers.tagger.exceptions.InputTypeError
import cz.czechlawers.tagger.state.Context
import cz.czechlawers.tagger.content.StringContent

class RegexSplit extends AGear
{
    final static int FOUND = 1
    final static int NOT_FOUND = 2

    final static int LEFT = 1
    final static int RIGHT = 2

    String regex
    int keep = 0

    RegexSplit(String name) {
        super(name)
    }

    @Override
    boolean canProcess(Context context)
    {
        if (!regex) {
            throw new ConfigurationError('Regex has to be provided.')
        }
        if (keep != LEFT && keep != RIGHT) {
            throw new ConfigurationError('What to keep has to be configured.')
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
            context.setState(FOUND)
            if (keep == LEFT) {
                content.set(content.get().substring(0, match.start()))
            } else {
                content.set(content.get().substring(match.end(), content.get().length()))
            }
        } else {
            context.setState(NOT_FOUND)
        }
    }
}
