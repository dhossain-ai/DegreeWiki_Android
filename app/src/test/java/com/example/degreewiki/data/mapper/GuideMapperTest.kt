package com.example.degreewiki.data.mapper

import com.example.degreewiki.data.network.dto.GuideBlockDto
import com.example.degreewiki.data.network.dto.GuideDetailDto
import com.example.degreewiki.data.network.dto.GuideInlineDto
import com.example.degreewiki.domain.model.GuideBlock
import com.example.degreewiki.domain.model.GuideInline
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class GuideMapperTest {
    @Test fun mapsSupportedBlocksAndSkipsUnknownBlock() {
        val detail = GuideDetailDto(
            id = "g", slug = "guide", title = "Guide",
            body = listOf(
                GuideBlockDto("heading", 2, children = listOf(GuideInlineDto("strong", "Heading"))),
                GuideBlockDto("paragraph", children = listOf(GuideInlineDto("text", "Paragraph"))),
                GuideBlockDto("ul", items = listOf(listOf(GuideInlineDto("text", "Bullet")))),
                GuideBlockDto("ol", items = listOf(listOf(GuideInlineDto("text", "First")))),
                GuideBlockDto("future_block")
            )
        ).toDomainOrNull()!!
        assertEquals(4, detail.body.size)
        assertTrue(detail.body[0] is GuideBlock.Heading)
        assertTrue(detail.body[1] is GuideBlock.Paragraph)
        assertTrue(detail.body[2] is GuideBlock.UnorderedList)
        assertTrue(detail.body[3] is GuideBlock.OrderedList)
    }

    @Test fun safeLinkRemainsLinkAndUnsafeLinkBecomesPlainText() {
        val detail = GuideDetailDto(
            id = "g", slug = "guide", title = "Guide",
            body = listOf(GuideBlockDto("paragraph", children = listOf(
                GuideInlineDto("link", "Official", "https://example.com"),
                GuideInlineDto("link", "Unsafe", "javascript:alert(1)")
            )))
        ).toDomainOrNull()!!
        val children = (detail.body.single() as GuideBlock.Paragraph).children
        assertTrue(children[0] is GuideInline.Link)
        assertTrue(children[1] is GuideInline.Plain)
        assertEquals("Unsafe", children[1].text)
    }
}
