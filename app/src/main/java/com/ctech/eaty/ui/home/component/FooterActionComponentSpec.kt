package com.ctech.eaty.ui.home.component

import android.content.Intent
import android.util.Log
import com.ctech.eaty.R
import com.ctech.eaty.ui.comment.view.CommentActivity
import com.ctech.eaty.ui.home.viewmodel.ProductItemViewModel
import com.facebook.litho.ClickEvent
import com.facebook.litho.ComponentContext
import com.facebook.litho.ComponentLayout
import com.facebook.litho.Row
import com.facebook.litho.annotations.LayoutSpec
import com.facebook.litho.annotations.OnCreateLayout
import com.facebook.litho.annotations.OnEvent
import com.facebook.litho.annotations.Prop
import com.facebook.litho.widget.Image
import com.facebook.litho.widget.Text
import com.facebook.yoga.YogaAlign
import com.facebook.yoga.YogaEdge
import com.facebook.yoga.YogaJustify


@LayoutSpec
class FooterActionComponentSpec {

    companion object {

        @JvmStatic
        @OnCreateLayout
        fun onCreateLayout(c: ComponentContext, @Prop viewModel: ProductItemViewModel, @Prop actionResId: Int): ComponentLayout {
            return Row.create(c)
                    .child(
                            Image.create(c)
                                    .drawableRes(actionResId)

                    )
                    .child(
                            Text.create(c, 0, R.style.TextAppearance_FooterIndicator)
                                    .text(indicatorResolver(viewModel, actionResId))
                                    .withLayout()
                                    .marginPx(YogaEdge.START, c.resources.getDimensionPixelSize(R.dimen.space_small))

                    )
                    .flex(1F)
                    .alignItems(YogaAlign.CENTER)
                    .justifyContent(YogaJustify.CENTER)
                    .clickHandler(FooterActionComponent.onClick(c))
                    .build()

        }

        private fun indicatorResolver(viewModel: ProductItemViewModel, actionResId: Int): String {
            return when (actionResId) {
                R.drawable.ic_heart_solid_grey -> viewModel.votesCount
                R.drawable.ic_comment -> viewModel.commentsCount
                R.drawable.ic_share -> ""
                else -> ""
            }
        }

        @JvmStatic
        @OnEvent(ClickEvent::class)
        fun onClick(
                c: ComponentContext,
                @Prop viewModel: ProductItemViewModel,
                @Prop actionResId: Int) {

            when (actionResId) {
                R.drawable.ic_comment -> {
                    val intent = CommentActivity.newIntent(c, viewModel.id)
                    c.startActivity(intent)
                }
                R.drawable.ic_share -> {
                    val shareIntent = Intent(Intent.ACTION_SEND)
                    shareIntent.type = "text/plain"
                    shareIntent.putExtra(Intent.EXTRA_TEXT, viewModel.shareUrl)
                    c.startActivity(Intent.createChooser(shareIntent, "Share link using"))
                }
            }
        }

    }
}