package com.booboot.vndbandroid.ui.vnengine

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import java.util.concurrent.atomic.AtomicInteger

class VnEngine(
    private val layoutInflater: LayoutInflater,
    private val background: AppCompatImageView,
    private val character: AppCompatImageView,
    private val characterName: AppCompatTextView,
    private val text: AppCompatTextView,
    private val customContainer: ViewGroup,
    private val steps: List<Step>,
    private val onFinish: () -> Unit
) {
    lateinit var stepIndex: AtomicInteger

    fun show() {
        if (stepIndex.get() < 0) stepIndex.set(0)
        else if (stepIndex.get() >= steps.size) return onFinish()

        val step = steps[stepIndex.get()]
        if (step.showIf()) {
            background.setImageResource(step.background)
            character.setImageResource(step.character)
            characterName.setText(step.characterName)
            text.text = step.text
            customContainer.removeAllViews()
            if (step.customLayout > 0) {
                val customView = layoutInflater.inflate(step.customLayout, customContainer, false)
                customContainer.addView(customView)
                step.onShow(customView)
            } else {
                customContainer.removeAllViews()
                step.onShow(background)
            }

            val onNext = { if (step.skipOnTap) nextStep() }
            customContainer.setOnClickListener { onNext() }
            text.setOnClickListener { onNext() }
            background.setOnClickListener { onNext() }
        } else {
            nextStep()
        }
    }

    fun currentStep(): Step = steps[stepIndex.get() % steps.size]

    fun nextStep() {
        stepIndex.incrementAndGet()
        show()
    }
}

data class Step(
    @DrawableRes val background: Int = 0,
    @DrawableRes val character: Int,
    @StringRes val characterName: Int,
    var text: String,
    val skipOnTap: Boolean = true,
    val showIf: () -> Boolean = { true },
    @LayoutRes val customLayout: Int = 0,
    val onShow: (View) -> Unit = {}
)