package cz.czechlawers.tagger.exceptions

class DuplicateTransitionError extends RuntimeException
{
    DuplicateTransitionError(String s)
    {
        super(s)
    }
}
