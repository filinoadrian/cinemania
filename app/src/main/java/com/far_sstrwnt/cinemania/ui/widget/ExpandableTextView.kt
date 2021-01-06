package com.far_sstrwnt.cinemania.ui.widget

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.content.Context
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.appcompat.widget.AppCompatTextView
import com.far_sstrwnt.cinemania.R

class ExpandableTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : AppCompatTextView(context, attrs, defStyle), View.OnClickListener {

    private lateinit var animator: ValueAnimator

    private var isCollapsing: Boolean = false

    private lateinit var originalText: CharSequence

    init {
        maxLines = COLLAPSED_MAX_LINES
        setOnClickListener(this)
        initAnimator()
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        if (lineCount <= COLLAPSED_MAX_LINES) {
            deEllipsize()
            isClickable = false
        } else {
            isClickable = true
            if (!animator.isRunning && isCollapsed()) {
                ellipsizeColored()
            }
        }
    }

    override fun setText(text: CharSequence, type: BufferType?) {
        originalText = text
        super.setText(text, type)
    }

    private fun setTextNoCaching(text: CharSequence) {
        super.setText(text, BufferType.NORMAL)
    }

    private fun ellipsizeColored() {
        val end = layout.getLineEnd(COLLAPSED_MAX_LINES - 1)

        val chars = layout.getLineEnd(COLLAPSED_MAX_LINES - 1) - layout.getLineStart(
            COLLAPSED_MAX_LINES - 1)

        val additionalGap = 4

        if (chars + additionalGap < POSTFIX.length) {
            return
        }

        val builder = SpannableStringBuilder(text)
        builder.replace(end - POSTFIX.length, end, POSTFIX)
        builder.setSpan(ForegroundColorSpan(resources.getColor(R.color.orange_300)),
            end - POSTFIX.length,
            end,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        setTextNoCaching(builder)
    }

    private fun deEllipsize() {
        super.setText(originalText)
    }

    private fun initAnimator() {
        animator = ValueAnimator.ofInt(-1, -1).setDuration(500)
        animator.interpolator = AccelerateDecelerateInterpolator()

        animator.addUpdateListener { valueAnimator ->
            updateHeight(valueAnimator.animatedValue as Int)
        }

        animator.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationStart(animation: Animator?) {
                if (isCollapsed()) {
                    isCollapsing = false
                    maxLines = Int.MAX_VALUE
                    deEllipsize()
                } else {
                    isCollapsing = true
                }
            }

            override fun onAnimationEnd(animation: Animator?) {
                if (!isCollapsed() && isCollapsing) {
                    maxLines = COLLAPSED_MAX_LINES
                    ellipsizeColored()
                    isCollapsing = false
                }
                layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
            }
        })
    }

    private fun updateHeight(animatedValue: Int) {
        val layoutParams = layoutParams
        layoutParams.height = animatedValue
        setLayoutParams(layoutParams)
    }

    private fun isCollapsed(): Boolean {
        return Int.MAX_VALUE != maxLines
    }

    override fun onClick(v: View?) {
        if (animator.isRunning) {
            animatorReverse()
            return
        }

        val endPosition = animateTo()
        val startPosition = height

        animator.setIntValues(startPosition, endPosition)
        animatorStart()
    }

    private fun animatorReverse() {
        isCollapsing = !isCollapsing
        animator.reverse()
    }

    private fun animateTo(): Int {
        return if (isCollapsed()) {
            layout.height + getPaddingHeight()
        } else {
            layout.getLineBottom(COLLAPSED_MAX_LINES - 1) + layout.bottomPadding + getPaddingHeight()
        }
    }

    private fun getPaddingHeight(): Int {
        return compoundPaddingBottom + compoundPaddingTop
    }

    private fun animatorStart() {
        animator.start()
    }

    companion object {
        const val COLLAPSED_MAX_LINES = 3
        const val POSTFIX = "...see more "
    }
}