package renderers.gfm

import org.jetbrains.dokka.gfm.CommonmarkRenderer
import org.jetbrains.dokka.gfm.CommonmarkRendererFactory
import org.jetbrains.dokka.pages.TextStyle
import org.junit.jupiter.api.Test
import renderers.*

class GroupWrappingTest : GfmRenderingOnlyTestBase() {

    @Test
    fun notWrapped() {
        val page = testPage {
            group {
                text("a")
                text("b")
            }
            text("c")
        }

        CommonmarkRendererFactory(context).createRenderer(page).render()

        assert(renderedContent == "//[testPage](test-page.md)\n\nabc")
    }

    @Test
    fun paragraphWrapped() {
        val page = testPage {
            group(styles = setOf(TextStyle.Paragraph)) {
                text("a")
                text("b")
            }
            text("c")
        }

        CommonmarkRendererFactory(context).createRenderer(page).render()

        assert(renderedContent == "//[testPage](test-page.md)\n\n\n\nab\n\nc")
    }

    @Test
    fun blockWrapped() {
        val page = testPage {
            group(styles = setOf(TextStyle.Block)) {
                text("a")
                text("b")
            }
            text("c")
        }

       CommonmarkRendererFactory(context).createRenderer(page).render()

        assert(renderedContent == "//[testPage](test-page.md)\n\nab  \nc")
    }

    @Test
    fun nested() {
        val page = testPage {
            group(styles = setOf(TextStyle.Block)) {
                text("a")
                group(styles = setOf(TextStyle.Block)) {
                    group(styles = setOf(TextStyle.Block)) {
                        text("b")
                        text("c")
                    }
                }
                text("d")
            }
        }

       CommonmarkRendererFactory(context).createRenderer(page).render()

//        renderedContent.match(Div("a", Div(Div("bc")), "d"))
        assert(renderedContent == "//[testPage](test-page.md)\n\nabc  \n  \nd  \n")
    }

}
