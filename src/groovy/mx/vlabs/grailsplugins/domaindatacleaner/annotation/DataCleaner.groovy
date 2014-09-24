package mx.vlabs.grailsplugins.domaindatacleaner.annotation

import java.lang.annotation.ElementType
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy
import java.lang.annotation.Target

import org.codehaus.groovy.transform.GroovyASTTransformationClass

@Target([ElementType.TYPE,ElementType.FIELD])
@Retention(RetentionPolicy.RUNTIME)
@GroovyASTTransformationClass(["mx.vlabs.grailsplugins.domaindatacleaner.transformation.DataCleanerASTTransformation"])
@interface DataCleaner{
	boolean removeExtraWhitepsaces() default true
}
