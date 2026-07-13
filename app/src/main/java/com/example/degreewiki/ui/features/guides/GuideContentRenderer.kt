package com.example.degreewiki.ui.features.guides

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.example.degreewiki.domain.model.GuideBlock
import com.example.degreewiki.domain.model.GuideInline

@Composable
fun GuideContentRenderer(blocks: List<GuideBlock>, modifier: Modifier = Modifier) {
    Column(modifier = modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(18.dp)) {
        blocks.forEach { block ->
            when (block) {
                is GuideBlock.Heading -> GuideHeadingBlock(block)
                is GuideBlock.Paragraph -> GuideParagraphBlock(block)
                is GuideBlock.UnorderedList -> GuideListBlock(block.items, ordered = false)
                is GuideBlock.OrderedList -> GuideListBlock(block.items, ordered = true)
            }
        }
    }
}

@Composable
fun GuideHeadingBlock(block: GuideBlock.Heading) {
    val style = when (block.level) {
        2 -> MaterialTheme.typography.titleLarge
        3 -> MaterialTheme.typography.titleMedium
        else -> MaterialTheme.typography.bodyLarge
    }.copy(fontWeight = FontWeight.Bold)
    GuideInlineText(block.children, style)
}

@Composable
fun GuideParagraphBlock(block: GuideBlock.Paragraph) {
    GuideInlineText(block.children, MaterialTheme.typography.bodyLarge.copy(lineHeight = MaterialTheme.typography.bodyLarge.lineHeight * 1.2f))
}

@Composable
fun GuideListBlock(items: List<List<GuideInline>>, ordered: Boolean) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        items.forEachIndexed { index, item ->
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp), verticalAlignment = Alignment.Top) {
                Text(if (ordered) "${index + 1}." else "•", style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.primary)
                GuideInlineText(item, MaterialTheme.typography.bodyLarge.copy(lineHeight = MaterialTheme.typography.bodyLarge.lineHeight * 1.15f), Modifier.weight(1f))
            }
        }
    }
}

@Composable
@Suppress("DEPRECATION")
private fun GuideInlineText(nodes: List<GuideInline>, style: TextStyle, modifier: Modifier = Modifier) {
    val linkColor = MaterialTheme.colorScheme.primary
    val annotated = buildAnnotatedString {
        nodes.forEach { node ->
            when (node) {
                is GuideInline.Plain -> append(node.text)
                is GuideInline.Strong -> withStyle(SpanStyle(fontWeight = FontWeight.Bold)) { append(node.text) }
                is GuideInline.Emphasis -> withStyle(SpanStyle(fontStyle = FontStyle.Italic)) { append(node.text) }
                is GuideInline.Link -> {
                    pushStringAnnotation("URL", node.href)
                    withStyle(SpanStyle(color = linkColor, textDecoration = TextDecoration.Underline)) { append(node.text) }
                    pop()
                }
            }
        }
    }
    val uriHandler = LocalUriHandler.current
    ClickableText(
        text = annotated,
        modifier = modifier,
        style = style.copy(color = MaterialTheme.colorScheme.onBackground),
        onClick = { offset -> annotated.getStringAnnotations("URL", offset, offset).firstOrNull()?.item?.let { runCatching { uriHandler.openUri(it) } } }
    )
}
