// (c) Meta Platforms, Inc. and affiliates. Confidential and proprietary.

package com.facebook.fresco.vito.tools.liveeditor

import android.app.AlertDialog
import android.content.Context
import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.text.style.StyleSpan
import android.util.TypedValue
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast

class ImageSourceUiUtil(private val context: Context) {

  private fun createView(): ViewGroup {
    return LinearLayout(context).apply { addView(createTextView()) }
  }

  private fun createTextView(): TextView {
    return TextView(context).apply {
      id = android.R.id.text1
      textSize = 12.spToPx(context)
    }
  }

  fun createSourceDialog(source: List<Pair<String, String>>): AlertDialog? {

    if (source.isEmpty()) {
      Toast.makeText(context, "Source is Empty", Toast.LENGTH_LONG).show()
      return null
    }

    val items =
        source.map {
          val spannable = SpannableString("${it.first} \n${it.second}")
          spannable.apply {
            setSpan(
                StyleSpan(Typeface.BOLD), 0, it.first.length, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
            setSpan(
                StyleSpan(Typeface.ITALIC),
                it.first.length,
                spannable.length,
                Spannable.SPAN_INCLUSIVE_INCLUSIVE)
          }
        }

    val dialogBuilder = AlertDialog.Builder(context)
    val layout = createView()
    var dialog: AlertDialog? =
        dialogBuilder
            .setAdapter(ArrayAdapter(context, android.R.layout.simple_list_item_1, items), null)
            .create()
    dialog?.setView(layout)
    dialog?.setOnDismissListener {
      items.forEach {
        val spans = it.getSpans(0, it.length, StyleSpan::class.java)
        for (span in spans) {
          it.removeSpan(span)
        }
      }
      dialog = null
    }

    return dialog
  }

  private fun Int.spToPx(context: Context): Float =
      TypedValue.applyDimension(
          TypedValue.COMPLEX_UNIT_SP, this.toFloat(), context.resources.displayMetrics)
}
