package cz.czechlawers.tagger.gears

import cz.czechlawers.tagger.state.Context
import cz.czechlawers.tagger.content.StringContent

class RegexExtract extends AGear
{
    final static int EXTRACTED = 1
    final static int NOT_EXTRACTED = 2
    String regex
    int group = 1

    RegexExtract(String name) {
        super(name)
    }

    @Override
    boolean canProcess(Context context)
    {
        return context.getContent() instanceof StringContent && regex
    }

    @Override
    void process(Context context)
    {
        StringContent content = (StringContent) context.getContent()
        // two states -> errorness (1), extracted (2),
        def text = content.get();
        def match = text =~ regex
        if (match && match.groupCount() >= group) {
            context.setState(EXTRACTED)
            content.setContent(match.group(group))
        } else {
            context.setState(NOT_EXTRACTED)
        }

    }
}
