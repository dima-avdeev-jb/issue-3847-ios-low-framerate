import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.calculatePan
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.coroutineScope
import kotlin.time.DurationUnit
import kotlin.time.TimeSource

@Composable
fun App() {
    buggy()
//    EatFrames()
}

@Composable
fun buggy() {
    var pos by remember { mutableStateOf(0f) }
    Canvas(modifier = Modifier.fillMaxSize().background(Color.DarkGray).pointerInput(true) {
        coroutineScope {
            awaitEachGesture {
                do {
                    awaitFirstDown(requireUnconsumed = false)
                    do {
                        val event = awaitPointerEvent()
                        val panChange = event.calculatePan()
                        if (panChange != Offset.Zero) {
                            pos -= panChange.x
                        }
                        val canceled = event.changes.any { it.isConsumed }
                    } while (!canceled)
                } while (false)
            }
        }
    }) {
        drawRect(color = Color.Gray, size = Size(size.width - pos, size.height))
    }
}

@Composable
fun EatFrames() {
    var count by mutableStateOf(0)
    var mark by mutableStateOf(TimeSource.Monotonic.markNow())
    var rate by mutableStateOf(0)
    val textMeasurer = rememberTextMeasurer()
    val precision = 120
    Canvas(Modifier.width(32.dp).height(24.dp).background(Color.Green)) {
        if (count == precision) {
            rate = (1000 * precision / mark.elapsedNow().toLong(DurationUnit.MILLISECONDS)).toInt()
            count = 0
            mark = TimeSource.Monotonic.markNow()
        } else
            count++
        drawText(textMeasurer = textMeasurer, text = "$rate")
    }
}
