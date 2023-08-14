package com.clean.architecture_clean

import com.clean.architecture_clean.archunit.HexagonalArchitecture
import com.tngtech.archunit.core.importer.ClassFileImporter
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses
import io.kotest.core.spec.style.FunSpec

class DependencyRuleTests : FunSpec({
    test("validate registration context architecture") {
        HexagonalArchitecture.boundedContext("com.clean.architecture_clean.account") {
            addDomainLayer("domain")
            addAdaptersLayer("adapter") {
                incoming("in.web")
                outgoing("out.persistence")
            }
            addApplicationLayer("application") {
                services("service")
                incomingPorts("port.in")
                outgoingPorts("port.out")
            }
            addConfiguration("configuration")
            verifyLayerDependencies {
                ClassFileImporter().importPackages("com.clean.architecture_clean..")
            }
        }
    }

    test("test package dependencies") {
        noClasses()
            .that()
            .resideInAPackage("com.clean.architecture_clean.account.domain..")
            .should()
            .dependOnClassesThat()
            .resideInAnyPackage("com.clean.architecture_clean.account.application..")
            .check(ClassFileImporter().importPackages("com.clean.architecture_clean.."))
    }
})
