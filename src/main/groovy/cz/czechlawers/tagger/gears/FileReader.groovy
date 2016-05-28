package cz.czechlawers.tagger.gears

import cz.czechlawers.tagger.exceptions.InputTypeError
import cz.czechlawers.tagger.state.Context
import cz.czechlawers.tagger.content.FileContent
import cz.czechlawers.tagger.content.StringContent

class FileReader extends AGear
{
    final static int LOADED = 1

    FileReader(String name) {
        super(name)
    }

    @Override
    boolean canProcess(Context context)
    {
        if (!(context.getContent() instanceof FileContent)) {
            throw new InputTypeError('File content is expected.');
        }
        return true
    }

    @Override
    void process(Context context)
    {
        FileContent content = (FileContent) context.getContent()
        context.setContent(new StringContent(content.getContent().text));
        context.setState(LOADED)
    }
}
