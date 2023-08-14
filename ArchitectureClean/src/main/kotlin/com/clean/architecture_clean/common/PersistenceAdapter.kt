package com.clean.architecture_clean.common

import org.springframework.core.annotation.AliasFor
import org.springframework.stereotype.Component
import kotlin.annotation.AnnotationRetention.RUNTIME

@Target(AnnotationTarget.CLASS)
@Retention(value = RUNTIME)
@MustBeDocumented
@Component
annotation class PersistenceAdapter(
    /**
     * The value may indicate a suggestion for a logical component name,
     * to be turned into a Spring bean in case of an autodetected component.
     * @return the suggested component name, if any (or empty String otherwise)
     */
    @get:AliasFor(annotation = Component::class)
    val value: String = ""
)
