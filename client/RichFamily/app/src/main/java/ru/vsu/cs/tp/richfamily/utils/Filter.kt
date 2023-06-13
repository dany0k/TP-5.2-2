package ru.vsu.cs.tp.richfamily.utils

import android.text.InputFilter
import java.util.regex.Pattern

object Filter {
    private val NAME_PATTERN = Pattern.compile("[a-zA-Zа-яА-Я ]{0,20}")
    private val TEXT_PATTERN = Pattern.compile("[a-zA-Zа-яА-Я0-9]{0,20}")

    val nameFilter = InputFilter { source, start, end, dest, dstart, dend ->
        val input = dest.subSequence(0, dstart).toString() + source.subSequence(start, end) + dest.subSequence(dend, dest.length).toString()
        val matcher = NAME_PATTERN.matcher(input)
        if (!matcher.matches()) {
            return@InputFilter ""
        }
        null
    }

    val textFilter = InputFilter { source, start, end, dest, dstart, dend ->
        val input = dest.subSequence(0, dstart).toString() + source.subSequence(start, end) + dest.subSequence(dend, dest.length).toString()
        val matcher = TEXT_PATTERN.matcher(input)
        if (!matcher.matches()) {
            return@InputFilter ""
        }
        null
    }
}