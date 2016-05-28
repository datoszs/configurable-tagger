import cz.czechlawers.tagger.gears.FileLoader
import cz.czechlawers.tagger.gears.Finalize
import cz.czechlawers.tagger.gears.RegexExtract
import cz.czechlawers.tagger.gears.RegexMatch
import cz.czechlawers.tagger.gears.Split
import cz.czechlawers.tagger.state.Context
import cz.czechlawers.tagger.content.FileContent
import cz.czechlawers.tagger.utils.Tagger

// Prepare tagger
def tagger = new Tagger()
tagger.setDebugMode(true)

// Configure all gears
def temp
temp = new FileLoader("LOAD_FILE_CONTENT")
tagger.addGear(temp)

temp = new RegexMatch('STRANA_DOVOLANI')
temp.regex = 'o dovolání (žalovan|věřitel|dlužníka)'
tagger.addGear(temp)

temp = new Split('ROZDELENI_VPRAVO')
temp.needle = "proti žalované" // žalovaným
temp.keep = Split.RIGHT
tagger.addGear(temp)

temp = new Split('ROZDELENI_VLEVO')
temp.needle = "proti žalované" // žalovaným
temp.keep = Split.LEFT
tagger.addGear(temp)

temp = new RegexExtract('EXTRAKCE')
temp.regex = "zastoupené (.*?),"
temp.group = 1
tagger.addGear(temp)

temp = new Finalize('FINALIZACE');
tagger.addGear(temp)

// Configure transitions
tagger.setStart("LOAD_FILE_CONTENT")
tagger.addTransition("LOAD_FILE_CONTENT", FileLoader.LOADED, "STRANA_DOVOLANI")
tagger.addTransition("STRANA_DOVOLANI", RegexMatch.MATCHED, "ROZDELENI_VPRAVO")
tagger.addTransition("STRANA_DOVOLANI", RegexMatch.NOT_MATCHED, "ROZDELENI_VLEVO")
tagger.addTransition("ROZDELENI_VPRAVO", Split.FOUND, "EXTRAKCE")
tagger.addTransition("ROZDELENI_VLEVO", Split.FOUND, "EXTRAKCE")
tagger.addTransition("EXTRAKCE", RegexExtract.EXTRACTED, "FINALIZACE")

// nelze dale transformovat is finalized context



// For loop on files in given dir
def dir = '/Users/jan/Projekty/datos/2015/cdo/documents'
new File(dir).eachFile() { file ->
    print 'Processing file ' + file.getName() + ' : '
    def fileContent = new FileContent(file)
    def context = new Context(fileContent)
    tagger.process(context)
    if (context.isFinalized()) {
        if (tagger.isDebugMode()) {
            println ""
        }
        println context.getContent().get()
    } else {
        println ""
    }
    println ""
}

