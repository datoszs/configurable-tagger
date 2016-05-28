package cz.czechlawers.tagger.content

class FileContent implements IContent<File> {
    File content;

    FileContent(File content)
    {
        this.content = content
    }

    File get()
    {
        return content
    }

    File set(File content)
    {
        this.content = content
    }
}
