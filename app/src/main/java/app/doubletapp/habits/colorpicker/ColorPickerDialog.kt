package app.doubletapp.habits.colorpicker

import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.View
import android.widget.HorizontalScrollView
import android.widget.LinearLayout
import app.doubletapp.habits.R

class ColorPickerDialog(private val context: Context, private val colorDisplay: View) {

    fun show(onColorSelected: (color: Int) -> Unit) {
        val colorPickerLayout = LinearLayout(context)
        colorPickerLayout.orientation = LinearLayout.HORIZONTAL
        val colors = context.resources.getIntArray(R.array.colors)

        val displayMetrics = context.resources.displayMetrics
        val screenWidth = displayMetrics.widthPixels

        val squareSize = (screenWidth - 5 * 40) / 4
        val layoutParams = LinearLayout.LayoutParams(squareSize, squareSize)
        layoutParams.setMargins(40, 0, 40, 0)

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

        val hsvGradientDrawable = GradientDrawable()
        hsvGradientDrawable.setColor(Color.parseColor("#EEEEFF"))
        hsvGradientDrawable.cornerRadius = 0f

        hsvScrollView.background = hsvGradientDrawable

        val dialogBuilder = AlertDialog.Builder(context)
        dialogBuilder.setView(hsvScrollView)
        val dialog = dialogBuilder.create()
        dialog.show()
    }
}