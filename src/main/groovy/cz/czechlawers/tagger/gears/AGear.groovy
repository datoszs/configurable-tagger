package cz.czechlawers.tagger.gears

abstract class AGear implements IGear
{
    String name

    AGear(String name)
    {
        this.name = name
    }

    String getName()
    {
        return name
    }
}
