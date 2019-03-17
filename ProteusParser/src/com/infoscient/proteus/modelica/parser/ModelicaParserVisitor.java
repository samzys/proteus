/* Generated By:JavaCC: Do not edit this line. ModelicaParserVisitor.java Version 4.1d1 */
package com.infoscient.proteus.modelica.parser;

public interface ModelicaParserVisitor
{
  public Object visit(SimpleNode node, Object data);
  public Object visit(OMStoredDefinition node, Object data);
  public Object visit(OMClassDefinition node, Object data);
  public Object visit(OMClassSpecifier node, Object data);
  public Object visit(OMEnumList node, Object data);
  public Object visit(OMEnumLiteral node, Object data);
  public Object visit(OMComposition node, Object data);
  public Object visit(OMExternalFuncCall node, Object data);
  public Object visit(OMElementList node, Object data);
  public Object visit(OMElement node, Object data);
  public Object visit(OMImportClause node, Object data);
  public Object visit(OMExtendsClause node, Object data);
  public Object visit(OMConstrainingClause node, Object data);
  public Object visit(OMComponentClause node, Object data);
  public Object visit(OMTypePrefix node, Object data);
  public Object visit(OMComponentList node, Object data);
  public Object visit(OMComponentDecl node, Object data);
  public Object visit(OMCondAttr node, Object data);
  public Object visit(OMDeclaration node, Object data);
  public Object visit(OMModification node, Object data);
  public Object visit(OMClassModification node, Object data);
  public Object visit(OMArgumentList node, Object data);
  public Object visit(OMArgument node, Object data);
  public Object visit(OMElementModificationOrReplaceable node, Object data);
  public Object visit(OMElementModification node, Object data);
  public Object visit(OMElementRedeclaration node, Object data);
  public Object visit(OMElementReplaceable node, Object data);
  public Object visit(OMComponentClause1 node, Object data);
  public Object visit(OMComponentDecl1 node, Object data);
  public Object visit(OMEquationSection node, Object data);
  public Object visit(OMAlgorithmSection node, Object data);
  public Object visit(OMEquation node, Object data);
  public Object visit(OMStatement node, Object data);
  public Object visit(OMIfEquation node, Object data);
  public Object visit(OMIfStatement node, Object data);
  public Object visit(OMForEquation node, Object data);
  public Object visit(OMForStatement node, Object data);
  public Object visit(OMForIndices node, Object data);
  public Object visit(OMForIndex node, Object data);
  public Object visit(OMWhileStatement node, Object data);
  public Object visit(OMWhenEquation node, Object data);
  public Object visit(OMWhenStatement node, Object data);
  public Object visit(OMConnectClause node, Object data);
  public Object visit(OMExpression node, Object data);
  public Object visit(OMSimpleExpression node, Object data);
  public Object visit(OMLogicalExpression node, Object data);
  public Object visit(OMLogicalTerm node, Object data);
  public Object visit(OMLogicalFactor node, Object data);
  public Object visit(OMRelation node, Object data);
  public Object visit(OMArithmeticExpression node, Object data);
  public Object visit(OMTerm node, Object data);
  public Object visit(OMFactor node, Object data);
  public Object visit(OMPrimary node, Object data);
  public Object visit(OMComponentRef node, Object data);
  public Object visit(OMFunctionCallArgs node, Object data);
  public Object visit(OMFunctionArguments node, Object data);
  public Object visit(OMNamedArgument node, Object data);
  public Object visit(OMSubscript node, Object data);
  public Object visit(OMComment node, Object data);
  public Object visit(OMAnnotation node, Object data);
}
/* JavaCC - OriginalChecksum=8ebbecec5416b00002739cbc8980d5d5 (do not edit this line) */
