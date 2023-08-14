package com.clean.architecture_clean.archunit

import com.clean.architecture_clean.archunit.base.ArchitectureElement
import com.tngtech.archunit.core.domain.JavaClasses

class Adapters(basePackage: String) : ArchitectureElement(basePackage) {
    private val incomingAdapterPackages = mutableListOf<String>()
    private val outgoingAdapterPackages = mutableListOf<String>()
    private val allAdapterPackages: List<String>
        get() = listOf(
            *incomingAdapterPackages.toTypedArray(),
            *outgoingAdapterPackages.toTypedArray()
        )

    fun outgoing(packageName: String): Adapters {
        this.incomingAdapterPackages.add(fullQualifiedPackage(packageName))
        return this
    }

    fun incoming(packageName: String): Adapters {
        this.outgoingAdapterPackages.add(fullQualifiedPackage(packageName))
        return this
    }

    fun doesNotContainEmptyPackages() {
        denyEmptyPackages(allAdapterPackages)
    }

    fun dontDependOnEachOther(classes: JavaClasses) {
        val allAdapters = allAdapterPackages
        for (adapter1 in allAdapters) {
            for (adapter2 in allAdapters) {
                if (adapter1 != adapter2) {
                    denyDependency(fromPackageName = adapter1, toPackageName = adapter2, classes = classes)
                }
            }
        }
    }

    fun doesNotDependOn(packageName: String, classes: JavaClasses) {
        denyDependency(fromPackageName = this.basePackage, toPackageName = packageName, classes = classes)
    }
}
