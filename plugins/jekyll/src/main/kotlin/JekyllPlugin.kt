package org.jetbrains.dokka.jekyll

import org.jetbrains.dokka.CoreExtensions
import org.jetbrains.dokka.base.DokkaBase
import org.jetbrains.dokka.base.renderers.DefaultRendererFactory
import org.jetbrains.dokka.base.renderers.PackageListCreator
import org.jetbrains.dokka.base.renderers.RootCreator
import org.jetbrains.dokka.base.resolvers.local.LocationProvider
import org.jetbrains.dokka.base.resolvers.shared.RecognizedLinkFormat
import org.jetbrains.dokka.gfm.CommonmarkRenderer
import org.jetbrains.dokka.gfm.GfmPlugin
import org.jetbrains.dokka.pages.*
import org.jetbrains.dokka.plugability.DokkaContext
import org.jetbrains.dokka.plugability.DokkaPlugin
import org.jetbrains.dokka.plugability.plugin
import org.jetbrains.dokka.plugability.query
import org.jetbrains.dokka.renderers.Renderer
import org.jetbrains.dokka.transformers.pages.PageTransformer
import java.lang.StringBuilder


class JekyllPlugin : DokkaPlugin() {

    val jekyllPreprocessors by extensionPoint<PageTransformer>()

    val renderer by extending {
        (CoreExtensions.renderer
                providing { JekyllRendererFactory(it) }
                override plugin<GfmPlugin>().renderer)
    }

    val rootCreator by extending {
        jekyllPreprocessors with RootCreator
    }

    val packageListCreator by extending {
        jekyllPreprocessors providing {
            PackageListCreator(it, RecognizedLinkFormat.DokkaJekyll)
        } order { after(rootCreator) }
    }
}

class JekyllRendererFactory(context: DokkaContext): DefaultRendererFactory(context) {
    override fun createRender(locationProvider: LocationProvider, root: RootPageNode): Renderer =
        CommonmarkRenderer(context, locationProvider, root)

    override val preprocessors = context.plugin<JekyllPlugin>().query { jekyllPreprocessors }
}

class JekyllRenderer(
    context: DokkaContext,
    locationProvider: LocationProvider,
    root: RootPageNode
) : CommonmarkRenderer(context, locationProvider, root) {

    override fun buildPage(page: ContentPage, content: (StringBuilder, ContentPage) -> Unit): String {
        val builder = StringBuilder()
        builder.append("---\n")
        builder.append("title: ${page.name} -\n")
        builder.append("---\n")
        content(builder, page)
        return builder.toString()
    }
}
