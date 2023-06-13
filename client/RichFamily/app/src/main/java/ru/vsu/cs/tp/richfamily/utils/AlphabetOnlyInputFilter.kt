package ru.vsu.cs.tp.richfamily.utils

import android.text.InputFilter
import android.text.Spanned
import java.util.regex.Pattern


class AlphabetOnlyInputFilter : InputFilter {
    private val pattern: Pattern = Pattern.compile("^[a-zA-Zа-яА-Я]{1,20}\$")

    override fun filter(
        source: CharSequence,
        start: Int,
        end: Int,
        dest: Spanned,
        dstart: Int,
        dend: Int
    ): String? {
        val builder = StringBuilder()
        for (i in start until end) {
            val c = source[i]
            if (pattern.matcher(Character.toString(c)).matches()) {
                builder.append(c)
            }
        }
        val hasInput = builder.length != end - start
        return if (hasInput) builder.toString() else null
    }
}