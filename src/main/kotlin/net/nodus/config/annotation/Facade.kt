package net.nodus.config.annotation

import org.springframework.stereotype.Component
import java.lang.annotation.Inherited

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
@Component
@Inherited
annotation class Facade (
    val value: String = ""
)