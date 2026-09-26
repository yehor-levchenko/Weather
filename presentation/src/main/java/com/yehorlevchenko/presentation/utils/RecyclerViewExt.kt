package com.yehorlevchenko.presentation.utils

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.yehorlevchenko.presentation.R

fun RecyclerView.setSwipeToDelete(onSwiped: (Int) -> Unit) {
    val itemTouchHelper = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            return false
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            onSwiped(viewHolder.adapterPosition)
        }

        override fun onChildDraw(
            canvas: Canvas,
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            dX: Float,
            dY: Float,
            actionState: Int,
            isCurrentlyActive: Boolean
        ) {
            if (dX < 0) {
                val itemView = viewHolder.itemView

                ColorDrawable(Color.RED).apply {
                    val backgroundLeft = itemView.right + dX.toInt()
                    val backgroundTop = itemView.top
                    val backgroundRight = itemView.right
                    val backgroundBottom = itemView.bottom

                    setBounds(backgroundLeft, backgroundTop, backgroundRight, backgroundBottom)
                    draw(canvas)
                }

                ContextCompat.getDrawable(context, R.drawable.ic_delete)?.let {
                    val iconWidth = it.intrinsicWidth
                    val iconHeight = it.intrinsicHeight

                    val iconMargin = resources.getDimensionPixelSize(R.dimen.margin_06)

                    val iconLeft = itemView.right - iconWidth - iconMargin
                    val iconTop = itemView.top + (itemView.height / 2 - iconHeight / 2)
                    val iconRight = itemView.right - iconMargin
                    val iconBottom = itemView.top + (itemView.height / 2 + iconHeight / 2)

                    it.setBounds(iconLeft, iconTop, iconRight, iconBottom)
                    it.draw(canvas)
                }
            }

            super.onChildDraw(canvas, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
        }
    }

    ItemTouchHelper(itemTouchHelper).attachToRecyclerView(this)
}