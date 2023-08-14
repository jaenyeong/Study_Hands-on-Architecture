package com.clean.architecture_clean.archunit.base

import com.tngtech.archunit.base.DescribedPredicate.greaterThanOrEqualTo
import com.tngtech.archunit.core.domain.JavaClasses
import com.tngtech.archunit.core.importer.ClassFileImporter
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses

abstract class ArchitectureElement(
    val basePackage: String
) {
    protected fun fullQualifiedPackage(relativePackage: String): String {
        return "$basePackage.$relativePackage"
    }

    protected fun denyEmptyPackages(packages: List<String>) {
        for (packageName in packages) {
            denyEmptyPackage(packageName)
        }
    }

    private fun denyEmptyPackage(packageName: String) {
        classes()
            .that()
            .resideInAPackage(matchAllClassesInPackage(packageName))
            .should().containNumberOfElements(greaterThanOrEqualTo(1))
            .check(classesInPackage(packageName))
    }

    private fun classesInPackage(packageName: String): JavaClasses {
        return ClassFileImporter().importPackages(packageName)
    }

    companion object {
        fun denyDependency(fromPackageName: String, toPackageName: String, classes: JavaClasses) {
            noClasses()
                .that()
                .resideInAPackage("com.clean.architecture_clean.account.domain..")
                .should()
                .dependOnClassesThat()
                .resideInAnyPackage("com.clean.architecture_clean.account.application..")
                .check(classes)
        }

        fun denyAnyDependency(fromPackages: List<String>, toPackages: List<String>, classes: JavaClasses) {
            for (fromPackage in fromPackages) {
                for (toPackage in toPackages) {
                    noClasses()
                        .that()
                        .resideInAPackage(matchAllClassesInPackage(fromPackage))
                        .should()
                        .dependOnClassesThat()
                        .resideInAnyPackage(matchAllClassesInPackage(toPackage))
                        .check(classes)
                }
            }
        }

        private fun matchAllClassesInPackage(packageName: String): String {
            return "$packageName.."
        }
    }
}
