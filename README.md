Configurable Tagger
===================

Groovy library for graph-like data transformation developed for purpose of tagging law court documents
with advocates names and state results.

Ideas behind
------------

There is state context represented by the class `Context` which contains content represented by interface `IContent`
and with one int variable used for state signalization.

The transformation process is performed by gears (implementing `IGear`) which are ordered in general graph inside
instance of class `Tagger` which holds all the gears as well as the state diagram.

Example
-------

There is example of some tagger in `Runner.groovy` which process documents of Czech republic Supreme Court.

