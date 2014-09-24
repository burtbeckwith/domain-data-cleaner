package mx.vlabs.grailsplugins.domaindatacleaner.transformation

import org.codehaus.groovy.ast.ASTNode
import org.codehaus.groovy.ast.AnnotationNode
import org.codehaus.groovy.ast.ClassNode
import org.codehaus.groovy.ast.MethodNode
import org.codehaus.groovy.ast.expr.ConstantExpression
import org.codehaus.groovy.ast.stmt.ExpressionStatement
import org.codehaus.groovy.control.CompilePhase
import org.codehaus.groovy.control.SourceUnit
import org.codehaus.groovy.transform.ASTTransformation
import org.codehaus.groovy.transform.GroovyASTTransformation

@GroovyASTTransformation(phase = CompilePhase.CANONICALIZATION)
class DataCleanerASTTransformation implements ASTTransformation{

	void visit(ASTNode[] nodes, SourceUnit arg1) {
		if (null == nodes || null == nodes[0] || null == nodes[1]) return

		if (!(nodes[0] instanceof AnnotationNode)) return

		ClassNode cNode = nodes[1]

		addMethod(cNode,"cleanupStringFields","""

			boolean removeExtraWhitepsaces =
					getClass().getAnnotation(mx.vlabs.domaindatacleaner.annotation.DataCleaner)
					.removeExtraWhitepsaces()

			this.properties.each{ k,v->

				java.lang.reflect.Field f = getClass().getDeclaredField(k)

				if(f!=null){

					boolean fieldRemoveExtraWhitespaces = removeExtraWhitepsaces

					mx.vlabs.domaindatacleaner.annotation.DataCleaner dca =
							f.getAnnotation(mx.vlabs.domaindatacleaner.annotation.DataCleaner)

					if(dca!=null){
						fieldRemoveExtraWhitespaces = dca.removeExtraWhitepsaces()
					}

					if(!transients.contain(k)&&v!=null&&f.type.equals(String)&&fieldRemoveExtraWhitespaces){
						this.properties[k] =
							mx.vlabs.grailsplugins.domaindatacleaner.util.DataCleanerUtil
							.cleanupString(v)
					}
				}
			}
		""")

		addMethod(cNode,"beforeInsert","""
			cleanupStringFields()
		""")
	}

	private void addMethod(ClassNode cNode, String methodName, String methodCode){

		ExpressionStatement stmnt = new ExpressionStatement(new ConstantExpression(methodCode))

		MethodNode method = new MethodNode(
			methodName,
			ACC_PUBLIC,
			new ClassNode(Object),
			null,
			null,
			stmnt
		)

		cNode.addMethod(method)
	}
}
