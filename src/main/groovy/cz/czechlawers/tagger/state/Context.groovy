package cz.czechlawers.tagger.state

import cz.czechlawers.tagger.content.IContent

class Context
{
    def content = null;
    def state = 0;
    def finalized = false

    def Context(IContent content)
    {
        this.content = content
    }

    def getContent()
    {
        return content;
    }
    def setContent(IContent content)
    {
        this.content = content;
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
