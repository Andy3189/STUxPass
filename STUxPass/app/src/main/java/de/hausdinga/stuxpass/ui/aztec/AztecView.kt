package de.hausdinga.stuxpass.ui.aztec

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.ImageVector.Builder
import androidx.compose.ui.graphics.vector.group
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import de.hausdinga.stuxpass.ui.theme.STUxPassTheme
import java.nio.charset.Charset
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

@OptIn(ExperimentalEncodingApi::class)
@Composable
fun AztecView(
    data: String,
    modifier: Modifier
) {
    fun createBitmap(data: String): Bitmap{
        val imageData = Base64.Default.decode(data)
        return BitmapFactory.decodeByteArray(imageData,0, imageData.size)
    }

    fun createImageVector(data: String): ImageVector {
        val matrix = MultiFormatWriter().encode(Base64.Default.decode(data).toString(Charset.defaultCharset()), BarcodeFormat.AZTEC,0,0)

        val imageVectorBuilder = Builder(
            defaultWidth = matrix.width.dp,
            defaultHeight = matrix.height.dp,
            viewportWidth = matrix.width.toFloat(),
            viewportHeight = matrix.height.toFloat(),
        )
        for (x in 0 until matrix.height) {
            for (y in 0 until matrix.width) {
                imageVectorBuilder.group(name = "$x,$y") {
                    this.path(fill = SolidColor(if (matrix.get(x,y)) Black else White)){
                        moveTo(x.toFloat(), y.toFloat())
                        lineTo(x.toFloat() + 1, y.toFloat())
                        lineTo(x.toFloat() + 1,y.toFloat() + 1)
                        lineTo(x.toFloat(), y.toFloat() + 1)
                        lineTo(x.toFloat(), y.toFloat())
                    }
                }
            }
        }
        return imageVectorBuilder.build()
    }
    Image(
        bitmap = createBitmap(data = data).asImageBitmap(),
//        imageVector = createImageVector(data = data),
        contentDescription = "yes",
        modifier = modifier,
        contentScale = ContentScale.FillWidth,
        filterQuality = FilterQuality.None
    )
}
@Preview

@Composable
fun PassViewPreview() {
    STUxPassTheme {
        Surface(color = White ) {
            AztecView(
                data = "\u009E\u0081\u0080\u0091\u0096¤'ñâ\u008A\\u0006¾\u00AD\u0095g^×J{Xùÿd\\u001fÈ\u0098Jz\\u0006\\u0013.0*\u0093jÙq&\\\\\u009A`\u008C©Lýb¯\\u0007çXÔy\u0086Ò\\u0018,|\\u001at,CÇ(\u0096²¨#ÝÔ{9\\u0011¡2j\u008Da`ÅçR`¥¯\\n¦\u009A\\b×\u007FìH\u008F;v\\rt¡d\$\\\\ú\\u0011Gbó\\u0001Ùi¨À\\u001cÌ9~b¾\u008C\\u0005¸i\\u0005q\u0080M!»¼µã\u009A\\u0005VDV\\u0014\\u0000\u007F!\u0081È_7\u0081À/G\u009Dw\u0082-ó\u0081dV\\f\\u0019\u008F!A\\u001dÈéÈ]\u009A:ø\\n\u0092ÅH7UÔ6\\u000312ñ'|â\\b²ò\\u0004*û) Ãmò×ð×Ð\\u0016\\u0019\\u000büÔAA\\u0014]Â\u008D\\u0007 à\u0088\u0090Â\\u0017ÎA\u0089\u0091\\u0012Gòs\u008F+ KÒ\u0094\u0088 \u0094@\\u000f\\u001f2O\$\\u0007\u0099\u009B\u0095Ð×\\u0007.¶\\u001ePGÅgÍÊÏK×\\u001b{¯[W\\u0003³÷\u0084Ãvþ\\u0016]W´Zã\u009D³@ËÇ-¡Ç\\u001c5\\r±@\\u001f\u0092èÞÊj7\\u0010jÍ\\b¤¢I\u0097Ë%1¶\\\\·§;oÏ=IÃÕ7õçîòna\\u0014\\u0004ôn¼Ø3\u0099\\u001aí\u0093W_8\\u0001\u0081B\\bDEVDV\\u0011\\u0002!",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp)
            )
        }
    }
}