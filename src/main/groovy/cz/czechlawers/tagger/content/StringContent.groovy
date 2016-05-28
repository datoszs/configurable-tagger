package cz.czechlawers.tagger.content

class StringContent implements IContent<String> {
    String content;

    StringContent(String content)
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
