package cz.czechlawers.tagger.state

import cz.czechlawers.tagger.content.IContent

class Context
{
    def content = null;
    def originalContent = null
    def state = 0;
    def finalized = false

    def Context(IContent content)
    {
        if (!this.content) {
            originalContent == content
        }
        this.content = content
    }

    def getContent()
    {
        return content;
    }
    def setContent(IContent content)
    {
        if (!this.content) {
            originalContent == content
        }
        this.content = content;
    }

    def backupContent()
    {
        originalContent = content
    }

    def restoreContent()
    {
        content = originalContent
    }

    def getState()
    {
        return this.state;
    }

    def setState(int state)
    {
        this.state = state;
    }

    def isFinalized()
    {
        return finalized
    }

    def markFinalized()
    {
        this.finalized = true
    }
}
