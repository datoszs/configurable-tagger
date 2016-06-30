import cz.czechlawers.tagger.gears.CastToHTML
import cz.czechlawers.tagger.gears.ErrorStater
import cz.czechlawers.tagger.gears.FileReader
import cz.czechlawers.tagger.gears.Finalize
import cz.czechlawers.tagger.gears.RegexExtract
import cz.czechlawers.tagger.gears.RegexMatch
import cz.czechlawers.tagger.gears.RegexSplit
import cz.czechlawers.tagger.gears.StripHTML
import cz.czechlawers.tagger.state.Context
import cz.czechlawers.tagger.content.FileContent
import cz.czechlawers.tagger.utils.Tagger

// Prepare tagger
def tagger = new Tagger()

// Configure all gears
def temp
temp = new FileReader("LOAD_FILE_CONTENT")
tagger.addGear(temp)

temp = new CastToHTML("PRETYPUJ_NA_HTML")
tagger.addGear(temp)

temp = new StripHTML("PREVED_DO_TEXTU")
tagger.addGear(temp)

temp = new RegexSplit("VYTAHNOUT_ZAHLAVI")
temp.regex = /takto/
temp.keep = RegexSplit.LEFT
tagger.addGear(temp)

temp = new RegexMatch('STRANA_DOVOLANI')
temp.regex = 'o dovolání(ch)? (žalovan|(zástavního )?dlužníka|povinn)'
tagger.addGear(temp)

temp = new RegexMatch('DOVOLANI_ZALOBCE')
temp.regex = 'o dovolání(ch)? (žalob|věřitel|oprávněné(ho)?)'
tagger.addGear(temp)

temp = new RegexMatch('DOVOLANI_NAVRHOVATEL')
temp.regex = 'o dovolání(ch)? navrhovatel(e|ky|ů)'
tagger.addGear(temp)

temp = new RegexSplit('ROZDELENI_UCASTNIKU')
temp.regex = /za účasti/
temp.keep = RegexSplit.LEFT
tagger.addGear(temp)

temp = new RegexSplit('ROZDELENI_VPRAVO')
temp.regex = /proti (žalovan|povinn|(zástavnímu )?dlužník(ovi|u))[^\\s]*? /
temp.keep = RegexSplit.RIGHT
tagger.addGear(temp)

temp = new RegexSplit('ROZDELENI_VLEVO')
temp.regex = /proti (žalovan|povinn|(zástavnímu )?dlužník(ovi|u))[^\\s]*? /
temp.keep = RegexSplit.LEFT
tagger.addGear(temp)

temp = new RegexExtract('EXTRAKCE')
temp.regex = /zasto[^\\s]*? (.*?),/
temp.group = 1
tagger.addGear(temp)

temp = new RegexMatch('MA_ADVOKATA')
temp.regex = /advokát/
tagger.addGear(temp)

temp = new ErrorStater('NEMA_ADVOKATA')
temp.message = 'NO ADVOCATE'
tagger.addGear(temp)

temp = new ErrorStater('FAILED_DOVOLANI_ZALOBCE')
temp.message = 'FAILED_DOVOLANI_ZALOBCE'
tagger.addGear(temp)

temp = new ErrorStater('FAILED_DOVOLANI_NAVRHOVATEL')
temp.message = 'FAILED_DOVOLANI_NAVRHOVATEL'
tagger.addGear(temp)

temp = new ErrorStater('FAILED_ROZDELENI_VPRAVO')
temp.message = 'FAILED_ROZDELENI_VPRAVO'
tagger.addGear(temp)

temp = new ErrorStater('FAILED_ROZDELENI_VLEVO')
temp.message = 'FAILED_ROZDELENI_VlEVO'
tagger.addGear(temp)

temp = new Finalize('FINALIZACE');
tagger.addGear(temp)

// Configure transitions
tagger.setStart("LOAD_FILE_CONTENT")
tagger.addTransition("LOAD_FILE_CONTENT", FileReader.LOADED, "PRETYPUJ_NA_HTML")
tagger.addTransition("PRETYPUJ_NA_HTML", CastToHTML.CASTED, "PREVED_DO_TEXTU")
tagger.addTransition("PREVED_DO_TEXTU", StripHTML.STRIPPED, "VYTAHNOUT_ZAHLAVI")
tagger.addTransition("VYTAHNOUT_ZAHLAVI", RegexSplit.FOUND, "STRANA_DOVOLANI");
tagger.addTransition("STRANA_DOVOLANI", RegexMatch.MATCHED, "ROZDELENI_VPRAVO")
tagger.addTransition("STRANA_DOVOLANI", RegexMatch.NOT_MATCHED, "DOVOLANI_ZALOBCE")
tagger.addTransition("DOVOLANI_ZALOBCE", RegexMatch.MATCHED, "ROZDELENI_VLEVO")
tagger.addTransition("DOVOLANI_ZALOBCE", RegexMatch.NOT_MATCHED, "DOVOLANI_NAVRHOVATEL")
tagger.addTransition("DOVOLANI_NAVRHOVATEL", RegexMatch.MATCHED, "ROZDELENI_UCASTNIKU")
tagger.addTransition("DOVOLANI_NAVRHOVATEL", RegexMatch.NOT_MATCHED, "FAILED_DOVOLANI_ZALOBCE")
tagger.addTransition("ROZDELENI_UCASTNIKU", RegexSplit.FOUND, "EXTRAKCE")
tagger.addTransition("ROZDELENI_UCASTNIKU", RegexSplit.NOT_FOUND, "FAILED_DOVOLANI_NAVRHOVATEL")
tagger.addTransition("ROZDELENI_VPRAVO", RegexSplit.FOUND, "EXTRAKCE")
tagger.addTransition("ROZDELENI_VPRAVO", RegexSplit.NOT_FOUND, "FAILED_ROZDELENI_VPRAVO")
tagger.addTransition("ROZDELENI_VLEVO", RegexSplit.FOUND, "EXTRAKCE")
tagger.addTransition("ROZDELENI_VLEVO", RegexSplit.NOT_FOUND, "FAILED_ROZDELENI_VLEVO")
tagger.addTransition("EXTRAKCE", RegexExtract.EXTRACTED, "FINALIZACE")
tagger.addTransition("EXTRAKCE", RegexExtract.NOT_EXTRACTED, "MA_ADVOKATA")
tagger.addTransition("MA_ADVOKATA", RegexMatch.NOT_MATCHED, "NEMA_ADVOKATA")

tagger.addTransition("NEMA_ADVOKATA", ErrorStater.DONE, "FINALIZACE")



// For loop on files in given dir
def finished = 0;
def one
def dir = '/Users/jan/Projekty/datos/2015/cdo/documents'
new File(dir).eachFile() { file ->
    if (one && file.getName() != one ) {
        return;
    } else {
        //tagger.setDebugMode(true)
    }
    print 'Processing file ' + file.getName() + ' : '
    def fileContent = new FileContent(file)
    def context = new Context(fileContent)
    tagger.process(context)
    if (context.isFinalized()) {
        finished++
        if (tagger.isDebugMode()) {
            println ""
        }
        def info = "";
        if (context.getErrorState() != null) {
            info = " : " + context.getErrorState()
        }
        println context.getContent().get() + info
    } else {
        println ""  + " : " + context.getErrorState()
    }
    //println ""
}

println finished
