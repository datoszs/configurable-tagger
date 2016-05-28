package cz.czechlawers.tagger.exceptions

class ConfigurationError extends RuntimeException
{
    ConfigurationError(String s) {
        super(s)
    }
}
