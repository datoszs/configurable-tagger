package cz.czechlawers.tagger.gears

import cz.czechlawers.tagger.state.Context
import cz.czechlawers.tagger.content.StringContent

class Split extends AGear
{
    final static int FOUND = 1
    final static int NOT_FOUND = 2

    final static int LEFT = 1
    final static int RIGHT = 2

    String needle
    int keep = 0

    Split(String name) {
        super(name)
    }

    @Override
    boolean canProcess(Context context)
    {
        return context.getContent() instanceof StringContent && (keep == LEFT || keep == RIGHT)
    }

    @Override
    void process(Context context)
    {
        StringContent content = (StringContent) context.getContent()
        // two states -> not found (1), found (2),
        def position = content.get().indexOf(needle);
        if (position == -1) {
            context.setState(NOT_FOUND)
        } else {
            context.setState(FOUND)
            if (keep == LEFT) {
                content.set(content.get().substring(0, position))
            } else {
                content.set(content.get().substring(position + needle.length(), content.get().length()))
            }
        }

    }
}
