package org.jetbrains.dokka.renderers

import org.jetbrains.dokka.pages.RootPageNode

interface Renderer {
    fun render()
}

interface RendererFactory {
    fun createRenderer(root: RootPageNode): Renderer
}