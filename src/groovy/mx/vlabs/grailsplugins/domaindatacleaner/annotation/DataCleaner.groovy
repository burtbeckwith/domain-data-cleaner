package mx.vlabs.grailsplugins.domaindatacleaner.annotation

import org.codehaus.groovy.transform.GroovyASTTransformationClass;
import java.lang.annotation.*;

@Target([ElementType.TYPE,ElementType.FIELD])
@Retention(RetentionPolicy.RUNTIME)
@GroovyASTTransformationClass(["mx.vlabs.grailsplugins.domaindatacleaner.transformation.DataCleanerASTTransformation"])
@interface DataCleaner{
	boolean removeExtraWhitepsaces() default true
}