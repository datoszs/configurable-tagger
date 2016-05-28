package cz.czechlawers.tagger.gears

import cz.czechlawers.tagger.state.Context

interface IGear
{

    /**
     * Returns whether the gear is ready (e.g. configured properly, and the input content is of proper type).
     * @param context Context to check against
     * @return
     */
    boolean canProcess(Context context)

    /**
     * Perform gear operation on given context)
     * @param context Context to transform
     */
    void process(Context context)
}