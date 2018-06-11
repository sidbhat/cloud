package org.grails.prettysize

import java.text.NumberFormat
import java.text.DecimalFormat

class PrettySizeTagLib {

    static namespace = "prettysize"

    static final def BYTE = 1D
    static final def KILO_BYTE = 1024D
    static final def MEGA_BYTE = 1048576D
    static final def GIGA_BYTE = 1073741824D
    static final def TERA_BYTE = 1099511627776D
    static final def PETA_BYTE = 1125899906842624D
    static final def EXA_BYTE = 1152921504606846976D
    static final def ZETTA_BYTE = 1180591620717411303424D
    static final def YOTTA_BYTE = 1208925819614629174706176D

    def display = {attrs ->
        def size = attrs.remove('size') as double
        def abbr = Boolean.valueOf(attrs.remove('abbr'))
        def formatter = attrs.remove('format')
        if (formatter) formatter = new DecimalFormat(formatter)

        if (!size || size < 0) {
            outMsg('prettysize.byte', 0, abbr, formatter)
        } else if (size >= YOTTA_BYTE) {
            outMsg('prettysize.yotta.byte', size.div(YOTTA_BYTE), abbr, formatter)
        } else if (size >= ZETTA_BYTE) {
            outMsg('prettysize.zetta.byte', size.div(ZETTA_BYTE), abbr, formatter)
        } else if (size >= EXA_BYTE) {
            outMsg('prettysize.exa.byte', size.div(EXA_BYTE), abbr, formatter)
        } else if (size >= PETA_BYTE) {
            outMsg('prettysize.peta.byte', size.div(PETA_BYTE), abbr, formatter)
        } else if (size >= TERA_BYTE) {
            outMsg('prettysize.tera.byte', size.div(TERA_BYTE), abbr, formatter)
        } else if (size >= GIGA_BYTE) {
            outMsg('prettysize.giga.byte', size.div(GIGA_BYTE), abbr, formatter)
        } else if (size >= MEGA_BYTE) {
            outMsg('prettysize.mega.byte', size.div(MEGA_BYTE), abbr, formatter)
        } else if (size >= KILO_BYTE) {
            outMsg('prettysize.kilo.byte', size.div(KILO_BYTE), abbr, formatter)
        } else {
            outMsg('prettysize.byte', size, abbr, formatter)
        }
    }

    def outMsg(code, units, abbr, formatter) {
        if (units <= 0) {
            out << message(code: 'prettysize.none')
        } else {
            def sb = new StringBuilder(code)
            if (units > 1) sb << 's'
            if (abbr) sb << '.short'
            if (formatter) units = formatter.format(units)
            out << message(code: sb.toString(), args: [units])
        }
    }

}

