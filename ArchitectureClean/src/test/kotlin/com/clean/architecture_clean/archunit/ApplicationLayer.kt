package com.clean.architecture_clean.archunit

import com.clean.architecture_clean.archunit.base.ArchitectureElement
import com.tngtech.archunit.core.domain.JavaClasses

class ApplicationLayer(basePackage: String) : ArchitectureElement(basePackage) {
    private val incomingPortsPackages = mutableListOf<String>()
    private val outgoingPortsPackages = mutableListOf<String>()
    private val servicePackages = mutableListOf<String>()
    private val allPackages: List<String>
        get() = listOf(
            *incomingPortsPackages.toTypedArray(),
            *outgoingPortsPackages.toTypedArray(),
            *servicePackages.toTypedArray()
        )

    fun incomingPorts(packageName: String): ApplicationLayer {
        this.incomingPortsPackages.add(fullQualifiedPackage(packageName))
        return this
    }

    fun outgoingPorts(packageName: String): ApplicationLayer {
        this.outgoingPortsPackages.add(fullQualifiedPackage(packageName))
        return this
    }

    fun services(packageName: String): ApplicationLayer {
        this.servicePackages.add(fullQualifiedPackage(packageName))
        return this
    }

    fun doesNotContainEmptyPackages() {
        denyEmptyPackages(allPackages)
    }

    fun doesNotDependOn(packageName: String, classes: JavaClasses) {
        denyDependency(this.basePackage, packageName, classes)
    }

    fun incomingAndOutgoingPortsDoNotDependOnEachOther(classes: JavaClasses) {
        denyAnyDependency(this.incomingPortsPackages, this.outgoingPortsPackages, classes)
        denyAnyDependency(this.outgoingPortsPackages, this.incomingPortsPackages, classes)
    }
}
