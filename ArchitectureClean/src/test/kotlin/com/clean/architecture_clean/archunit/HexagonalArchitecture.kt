package com.clean.architecture_clean.archunit

import com.clean.architecture_clean.archunit.base.ArchitectureElement
import com.tngtech.archunit.core.domain.JavaClasses

class HexagonalArchitecture(basePackage: String) : ArchitectureElement(basePackage) {
    private val domainPackages = mutableListOf<String>()
    private var adapters: Adapters? = null
    private var applicationLayer: ApplicationLayer? = null
    private var configurationPackage: String? = null

    fun addDomainLayer(packageName: String) {
        domainPackages.add(fullQualifiedPackage(packageName))
    }

    fun addAdaptersLayer(packageName: String, bootStrap: Adapters.() -> Unit) {
        val adapter = Adapters(fullQualifiedPackage(packageName))
        adapter.bootStrap()
        this.adapters = adapter
    }

    fun addApplicationLayer(packageName: String, bootStrap: ApplicationLayer.() -> Unit) {
        val appLayer = ApplicationLayer(fullQualifiedPackage(packageName))
        appLayer.bootStrap()
        this.applicationLayer = appLayer
    }

    fun addConfiguration(packageName: String) {
        this.configurationPackage = fullQualifiedPackage(packageName)
    }

    fun verifyLayerDependencies(importer: () -> JavaClasses) {
        val classes = importer()

        adapters?.run {
            doesNotContainEmptyPackages()
            dontDependOnEachOther(classes)
            doesNotDependOn(configurationPackage!!, classes)
        }

        applicationLayer?.run {
            doesNotContainEmptyPackages()
            doesNotDependOn(adapters?.basePackage!!, classes)
            doesNotDependOn(configurationPackage!!, classes)
            incomingAndOutgoingPortsDoNotDependOnEachOther(classes)
        }

        domainDoesNotDependOnOtherPackages(classes)
    }

    private fun domainDoesNotDependOnOtherPackages(classes: JavaClasses) {
        denyAnyDependency(domainPackages, listOfNotNull(adapters?.basePackage), classes)
        denyAnyDependency(domainPackages, listOfNotNull(applicationLayer?.basePackage), classes)
    }

    companion object {
        fun boundedContext(basePackage: String, bootstrapLayers: HexagonalArchitecture.() -> Unit): HexagonalArchitecture {
            val architecture = HexagonalArchitecture(basePackage)
            architecture.bootstrapLayers()
            return architecture
        }
    }
}
