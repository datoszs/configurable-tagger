import cz.czechlawers.tagger.gears.CastToHTML
import cz.czechlawers.tagger.gears.ErrorStater
import cz.czechlawers.tagger.gears.FileReader
import cz.czechlawers.tagger.gears.Finalize
import cz.czechlawers.tagger.gears.RegexExtract
import cz.czechlawers.tagger.gears.RegexMatch
import cz.czechlawers.tagger.gears.RegexSplit
import cz.czechlawers.tagger.gears.Stater
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

temp = new RegexSplit("VYTAHNOUT_ROZHODNUTI")
temp.regex = /takto/
temp.keep = RegexSplit.RIGHT
tagger.addGear(temp)

temp = new RegexSplit("OREZAT_HLAVICKU")
temp.regex = /(Odůvodnění|odůvodnění|(o d ů v o d n ě n í)|(O d ů v o d n ě n í))/
temp.keep = RegexSplit.LEFT
tagger.addGear(temp)

temp = new RegexMatch("SE_ODMITA")
temp.regex = / odmítá/
tagger.addGear(temp)

temp = new RegexMatch("SE_ZASTAVUJE")
temp.regex = / zastavuje/
tagger.addGear(temp)

// pro pripad se odmita
temp = new RegexMatch("MA_RIMSKA_DVA")
temp.regex = /II. /
tagger.addGear(temp)

temp = new RegexMatch("MA_RIMSKA_TRI")
temp.regex = /III. /
tagger.addGear(temp)

temp = new RegexSplit("ROZDEL_NA_TRI")
temp.regex = /III. /
temp.keep = RegexSplit.LEFT
tagger.addGear(temp)

temp = new RegexMatch("MA_NAHRAD")
temp.regex = /náhrad/
tagger.addGear(temp)

// pro pripad se zamita

temp = new RegexMatch("MA_RIMSKA_DVA_2")
temp.regex = /II. /
tagger.addGear(temp)

temp = new RegexMatch("MA_RIMSKA_TRI_2")
temp.regex = /III. /
tagger.addGear(temp)

temp = new RegexSplit("ROZDEL_NA_TRI_2")
temp.regex = /III. /
temp.keep = RegexSplit.LEFT
tagger.addGear(temp)

temp = new RegexMatch("MA_NAHRAD_2")
temp.regex = /náhrad/
tagger.addGear(temp)



temp = new Stater("NEGATIVE")
temp.message = "NEGATIVE";
tagger.addGear(temp)

temp = new Stater("POSITIVE")
temp.message = "POSITIVE";
tagger.addGear(temp)

temp = new Stater("NEUTRAL")
temp.message = "NEUTRAL";
tagger.addGear(temp)

temp = new Finalize('FINALIZACE');
tagger.addGear(temp)

// Configure transitions
tagger.setStart("LOAD_FILE_CONTENT")
tagger.addTransition("LOAD_FILE_CONTENT", FileReader.LOADED, "PRETYPUJ_NA_HTML")
tagger.addTransition("PRETYPUJ_NA_HTML", CastToHTML.CASTED, "PREVED_DO_TEXTU")
tagger.addTransition("PREVED_DO_TEXTU", StripHTML.STRIPPED, "VYTAHNOUT_ROZHODNUTI")
tagger.addTransition("VYTAHNOUT_ROZHODNUTI", RegexSplit.FOUND, "OREZAT_HLAVICKU");
tagger.addTransition("OREZAT_HLAVICKU", RegexSplit.FOUND, "SE_ODMITA");
tagger.addTransition("SE_ODMITA", RegexMatch.MATCHED, "MA_RIMSKA_DVA");
tagger.addTransition("SE_ODMITA", RegexMatch.NOT_MATCHED, "SE_ZASTAVUJE");

tagger.addTransition("MA_RIMSKA_DVA", RegexMatch.NOT_MATCHED, "NEGATIVE");
tagger.addTransition("MA_RIMSKA_DVA", RegexMatch.MATCHED, "MA_RIMSKA_TRI");
tagger.addTransition("MA_RIMSKA_TRI", RegexMatch.NOT_MATCHED, "MA_NAHRAD");
tagger.addTransition("MA_RIMSKA_TRI", RegexMatch.MATCHED, "ROZDEL_NA_TRI");
tagger.addTransition("MA_NAHRAD", RegexMatch.MATCHED, "NEGATIVE");
tagger.addTransition("MA_NAHRAD", RegexMatch.NOT_MATCHED, "POSITIVE");
tagger.addTransition("ROZDEL_NA_TRI", RegexSplit.FOUND, "MA_NAHRAD");

tagger.addTransition("SE_ZASTAVUJE", RegexMatch.MATCHED, "MA_RIMSKA_DVA_2");
tagger.addTransition("SE_ZASTAVUJE", RegexMatch.NOT_MATCHED, "POSITIVE");

tagger.addTransition("MA_RIMSKA_DVA_2", RegexMatch.NOT_MATCHED, "NEUTRAL");
tagger.addTransition("MA_RIMSKA_DVA_2", RegexMatch.MATCHED, "MA_RIMSKA_TRI_2");
tagger.addTransition("MA_RIMSKA_TRI_2", RegexMatch.MATCHED, "ROZDEL_NA_TRI_2");
tagger.addTransition("MA_RIMSKA_TRI_2", RegexMatch.NOT_MATCHED, "MA_NAHRAD_2");
tagger.addTransition("ROZDEL_NA_TRI_2", RegexSplit.FOUND, "MA_NAHRAD_2");
tagger.addTransition("MA_NAHRAD_2", RegexMatch.MATCHED, "NEUTRAL");
tagger.addTransition("MA_NAHRAD_2", RegexMatch.NOT_MATCHED, "POSITIVE");


tagger.addTransition("POSITIVE", Stater.DONE, "FINALIZACE");
tagger.addTransition("NEUTRAL", Stater.DONE, "FINALIZACE");
tagger.addTransition("NEGATIVE", Stater.DONE, "FINALIZACE");



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
