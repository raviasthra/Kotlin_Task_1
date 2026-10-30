package com.example.kotlinClean

import com.lemonappdev.konsist.api.Konsist
import com.lemonappdev.konsist.api.ext.list.withPackage
import com.lemonappdev.konsist.api.verify.assertFalse
import org.junit.Test

class ArchitectureTest {

    @Test
    fun `domain must not depend on android, retrofit, okhttp, or data`() {
        Konsist.scopeFromProduction()
            .files
            .withPackage("..domain..")
            .assertFalse { file ->
                file.imports.any { imp ->
                    imp.name.startsWith("android.") ||
                            imp.name.startsWith("androidx.") ||
                            imp.name.startsWith("retrofit2.") ||
                            imp.name.startsWith("okhttp3.") ||
                            imp.name.startsWith("kotlinx.serialization") ||
                            imp.name.contains(".data.") ||
                            imp.name.contains(".presentation.")
                }
            }
    }

    @Test
    fun `presentation must not depend on data`() {
        Konsist.scopeFromProduction()
            .files
            .withPackage("..presentation..")
            .assertFalse { file ->
                file.imports.any { imp -> imp.name.contains(".data.") }
            }
    }
}