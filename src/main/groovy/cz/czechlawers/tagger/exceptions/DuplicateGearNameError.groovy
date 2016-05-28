package cz.czechlawers.tagger.exceptions

class DuplicateGearNameError extends RuntimeException
{
    DuplicateGearNameError(String s)
    {
        super(s)
    }
}
