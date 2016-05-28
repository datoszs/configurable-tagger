package cz.czechlawers.tagger.gears

import cz.czechlawers.tagger.content.HTMLContent
import cz.czechlawers.tagger.content.StringContent
import cz.czechlawers.tagger.exceptions.InputTypeError
import cz.czechlawers.tagger.state.Context
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

class StripHTML extends AGear
{
    final static int STRIPPED = 1

    StripHTML(String name) {
        super(name)
    }

    @Override
    boolean canProcess(Context context)
    {
        if (!(context.getContent() instanceof HTMLContent)) {
            throw new InputTypeError('HTML content is expected.');
        }
        return true
    }

    @Override
    void process(Context context)
    {
        HTMLContent content = (HTMLContent) context.getContent()
        Document document = Jsoup.parse(content.get())
        context.setContent(new StringContent(document.text()));
        context.setState(STRIPPED);
    }
}
