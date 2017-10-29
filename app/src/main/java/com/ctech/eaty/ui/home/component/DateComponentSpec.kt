package com.ctech.eaty.ui.home.component

import android.graphics.Color
import com.ctech.eaty.R
import com.ctech.eaty.ui.home.viewmodel.DateItemViewModel
import com.facebook.litho.Column
import com.facebook.litho.ComponentContext
import com.facebook.litho.annotations.LayoutSpec
import com.facebook.litho.annotations.OnCreateLayout
import com.facebook.litho.annotations.Prop
import com.facebook.litho.widget.Text
import com.facebook.yoga.YogaEdge


@LayoutSpec
class DateComponentSpec {
    companion object {

        @JvmStatic
        @OnCreateLayout
        fun onCreateLayout(c: ComponentContext, @Prop viewModel: DateItemViewModel) =
                Column.create(c)
                        .backgroundColor(Color.WHITE)
                        .paddingRes(YogaEdge.BOTTOM, R.dimen.content_padding_vertical)
                        .paddingRes(YogaEdge.TOP, R.dimen.content_padding_vertical)
                        .paddingRes(YogaEdge.LEFT, R.dimen.content_padding_horizontal)
                        .paddingRes(YogaEdge.START, R.dimen.content_padding_horizontal)
                        .child(
                                Text.create(c, 0, R.style.TextAppearance_Section)
                                        .text(viewModel.date)
                        ).build()

    }
}