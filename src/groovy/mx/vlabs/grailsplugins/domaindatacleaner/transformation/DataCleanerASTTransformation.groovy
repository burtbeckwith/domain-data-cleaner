package mx.vlabs.grailsplugins.domaindatacleaner.transformation

import mx.vlabs.grailsplugins.domaindatacleaner.annotation.DataCleaner;

import org.codehaus.groovy.ast.ASTNode;
import org.codehaus.groovy.ast.AnnotationNode
import org.codehaus.groovy.ast.ClassNode
import org.codehaus.groovy.ast.FieldNode
import org.codehaus.groovy.ast.MethodNode
import org.codehaus.groovy.ast.PropertyNode
import org.codehaus.groovy.control.SourceUnit;
import org.codehaus.groovy.transform.ASTTransformation

import static org.springframework.asm.Opcodes.*;

import org.codehaus.groovy.ast.expr.*;
import org.codehaus.groovy.ast.stmt.ExpressionStatement;
import org.codehaus.groovy.ast.stmt.Statement
import org.codehaus.groovy.control.CompilePhase
import org.codehaus.groovy.transform.GroovyASTTransformation

@GroovyASTTransformation(phase = CompilePhase.CANONICALIZATION)
class DataCleanerASTTransformation implements ASTTransformation{

	@Override
	public void visit(ASTNode[] nodes, SourceUnit arg1) {
		// TODO Auto-generated method stub
		if (null == nodes) return;
		if (null == nodes[0]) return;
		if (null == nodes[1]) return;
		
		if (!(nodes[0] instanceof AnnotationNode)) return;
		
		ClassNode cNode = (ClassNode) nodes[1];
	
		addMethod(cNode,"cleanupStringFields","""

			boolean removeExtraWhitepsaces = 
					this.class.getAnnotation(mx.vlabs.domaindatacleaner.annotation.DataCleaner)
					.removeExtraWhitepsaces()
				
			this.properties.each{ k,v->

				java.lang.reflect.Field f = this.class.getDeclaredField(k)

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
		
		ExpressionStatement stmnt = new ExpressionStatement(
			new ConstantExpression(methodCode)	
		)
		
		
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
