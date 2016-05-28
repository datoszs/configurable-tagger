package cz.czechlawers.tagger.content

class HTMLContent implements IContent<String> {
    String content;

    HTMLContent(String content)
    {
        this.content = content
    }

    String get()
    {
        return content
    }

    String set(String content)
    {
        this.content = content
    }

    @Override
    String toString() {
        return content
    }
}
