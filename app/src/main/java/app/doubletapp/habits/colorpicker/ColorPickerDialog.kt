package app.doubletapp.habits.colorpicker

import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.view.View
import android.widget.HorizontalScrollView
import android.widget.LinearLayout
import app.doubletapp.habits.R

class ColorPickerDialog(private val context: Context, private val colorDisplay: View) {

    fun show(onColorSelected: (color: Int) -> Unit) {
        val colorPickerLayout = LinearLayout(context)
        colorPickerLayout.orientation = LinearLayout.HORIZONTAL
        val colors = context.resources.getIntArray(R.array.colors)

        val squareSizeInDp = 100f
        val squareSizeInPx = dpToPx(context, squareSizeInDp)

        val margin = 20f
        val marginInPx = dpToPx(context, margin)

        val layoutParams = LinearLayout.LayoutParams(squareSizeInPx, squareSizeInPx)
        layoutParams.setMargins(marginInPx, 0, marginInPx, 0)

        for (color in colors) {
            val view = View(context)
            view.layoutParams = layoutParams
            view.setBackgroundColor(color)
            view.setOnClickListener {
                colorDisplay.setBackgroundColor(color)
                onColorSelected(color)
            }
            colorPickerLayout.addView(view)
        }

        val hsvScrollView = HorizontalScrollView(context)
        hsvScrollView.addView(colorPickerLayout)

        hsvScrollView.setBackgroundColor(Color.parseColor("#EEEEFF"))

        val dialogBuilder = AlertDialog.Builder(context)
        dialogBuilder.setView(hsvScrollView)
        val dialog = dialogBuilder.create()
        dialog.show()
    }

    fun dpToPx(context: Context, dp: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (dp * scale + 0.5f).toInt()
    }
}