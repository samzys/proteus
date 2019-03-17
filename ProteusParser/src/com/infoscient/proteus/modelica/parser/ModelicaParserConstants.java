/* Generated By:JJTree&JavaCC: Do not edit this line. ModelicaParserConstants.java */
package com.infoscient.proteus.modelica.parser;


/** 
 * Token literal values and constants.
 * Generated by org.javacc.parser.OtherFilesGen#start()
 */
public interface ModelicaParserConstants {

  /** End of File. */
  int EOF = 0;
  /** RegularExpression Id. */
  int ALGORITHM = 7;
  /** RegularExpression Id. */
  int AND = 8;
  /** RegularExpression Id. */
  int ANNOTATION = 9;
  /** RegularExpression Id. */
  int BLOCK = 10;
  /** RegularExpression Id. */
  int BREAK = 11;
  /** RegularExpression Id. */
  int CLASS = 12;
  /** RegularExpression Id. */
  int CONNECT = 13;
  /** RegularExpression Id. */
  int CONNECTOR = 14;
  /** RegularExpression Id. */
  int CONSTANT = 15;
  /** RegularExpression Id. */
  int CONSTRAINEDBY = 16;
  /** RegularExpression Id. */
  int DISCRETE = 17;
  /** RegularExpression Id. */
  int EACH = 18;
  /** RegularExpression Id. */
  int ELSE = 19;
  /** RegularExpression Id. */
  int ELSEIF = 20;
  /** RegularExpression Id. */
  int ELSEWHEN = 21;
  /** RegularExpression Id. */
  int ENCAPSULATED = 22;
  /** RegularExpression Id. */
  int END = 23;
  /** RegularExpression Id. */
  int ENUMERATION = 24;
  /** RegularExpression Id. */
  int EQUATION = 25;
  /** RegularExpression Id. */
  int EXPANDABLE = 26;
  /** RegularExpression Id. */
  int EXTENDS = 27;
  /** RegularExpression Id. */
  int EXTERNAL = 28;
  /** RegularExpression Id. */
  int FALSE = 29;
  /** RegularExpression Id. */
  int FINAL = 30;
  /** RegularExpression Id. */
  int FLOW = 31;
  /** RegularExpression Id. */
  int FOR = 32;
  /** RegularExpression Id. */
  int FUNCTION = 33;
  /** RegularExpression Id. */
  int IF = 34;
  /** RegularExpression Id. */
  int IMPORT = 35;
  /** RegularExpression Id. */
  int IN = 36;
  /** RegularExpression Id. */
  int INITIAL = 37;
  /** RegularExpression Id. */
  int INNER = 38;
  /** RegularExpression Id. */
  int INPUT = 39;
  /** RegularExpression Id. */
  int LOOP = 40;
  /** RegularExpression Id. */
  int MODEL = 41;
  /** RegularExpression Id. */
  int NOT = 42;
  /** RegularExpression Id. */
  int OR = 43;
  /** RegularExpression Id. */
  int OUTER = 44;
  /** RegularExpression Id. */
  int OUTPUT = 45;
  /** RegularExpression Id. */
  int PACKAGE = 46;
  /** RegularExpression Id. */
  int PARAMETER = 47;
  /** RegularExpression Id. */
  int PARTIAL = 48;
  /** RegularExpression Id. */
  int PROTECTED = 49;
  /** RegularExpression Id. */
  int PUBLIC = 50;
  /** RegularExpression Id. */
  int RECORD = 51;
  /** RegularExpression Id. */
  int REDECLARE = 52;
  /** RegularExpression Id. */
  int REPLACEABLE = 53;
  /** RegularExpression Id. */
  int RETURN = 54;
  /** RegularExpression Id. */
  int STREAM = 55;
  /** RegularExpression Id. */
  int THEN = 56;
  /** RegularExpression Id. */
  int TRUE = 57;
  /** RegularExpression Id. */
  int TYPE = 58;
  /** RegularExpression Id. */
  int WHEN = 59;
  /** RegularExpression Id. */
  int WHILE = 60;
  /** RegularExpression Id. */
  int WITHIN = 61;
  /** RegularExpression Id. */
  int REL_OP = 62;
  /** RegularExpression Id. */
  int PLUS = 63;
  /** RegularExpression Id. */
  int MINUS = 64;
  /** RegularExpression Id. */
  int DOT_PLUS = 65;
  /** RegularExpression Id. */
  int DOT_MINUS = 66;
  /** RegularExpression Id. */
  int ADD_OP = 67;
  /** RegularExpression Id. */
  int DOT_TIMES = 68;
  /** RegularExpression Id. */
  int TIMES = 69;
  /** RegularExpression Id. */
  int DIVIDE = 70;
  /** RegularExpression Id. */
  int DOT_DIVIDE = 71;
  /** RegularExpression Id. */
  int MUL_OP = 72;
  /** RegularExpression Id. */
  int IDENT = 73;
  /** RegularExpression Id. */
  int Q_IDENT = 74;
  /** RegularExpression Id. */
  int NONDIGIT = 75;
  /** RegularExpression Id. */
  int STRING = 76;
  /** RegularExpression Id. */
  int S_CHAR = 77;
  /** RegularExpression Id. */
  int Q_CHAR = 78;
  /** RegularExpression Id. */
  int S_ESCAPE = 79;
  /** RegularExpression Id. */
  int DIGIT = 80;
  /** RegularExpression Id. */
  int UNSIGNED_INTEGER = 81;
  /** RegularExpression Id. */
  int UNSIGNED_NUMBER = 82;

  /** Lexical state. */
  int DEFAULT = 0;

  /** Literal token values. */
  String[] tokenImage = {
    "<EOF>",
    "\" \"",
    "\"\\t\"",
    "\"\\n\"",
    "\"\\r\"",
    "<token of kind 5>",
    "<token of kind 6>",
    "\"algorithm\"",
    "\"and\"",
    "\"annotation\"",
    "\"block\"",
    "\"break\"",
    "\"class\"",
    "\"connect\"",
    "\"connector\"",
    "\"constant\"",
    "\"constrainedby\"",
    "\"discrete\"",
    "\"each\"",
    "\"else\"",
    "\"elseif\"",
    "\"elsewhen\"",
    "\"encapsulated\"",
    "\"end\"",
    "\"enumeration\"",
    "\"equation\"",
    "\"expandable\"",
    "\"extends\"",
    "\"external\"",
    "\"false\"",
    "\"final\"",
    "\"flow\"",
    "\"for\"",
    "\"function\"",
    "\"if\"",
    "\"import\"",
    "\"in\"",
    "\"initial\"",
    "\"inner\"",
    "\"input\"",
    "\"loop\"",
    "\"model\"",
    "\"not\"",
    "\"or\"",
    "\"outer\"",
    "\"output\"",
    "\"package\"",
    "\"parameter\"",
    "\"partial\"",
    "\"protected\"",
    "\"public\"",
    "\"record\"",
    "\"redeclare\"",
    "\"replaceable\"",
    "\"return\"",
    "\"stream\"",
    "\"then\"",
    "\"true\"",
    "\"type\"",
    "\"when\"",
    "\"while\"",
    "\"within\"",
    "<REL_OP>",
    "\"+\"",
    "\"-\"",
    "\".+\"",
    "\".-\"",
    "<ADD_OP>",
    "\".*\"",
    "\"*\"",
    "\"/\"",
    "\"./\"",
    "<MUL_OP>",
    "<IDENT>",
    "<Q_IDENT>",
    "<NONDIGIT>",
    "<STRING>",
    "<S_CHAR>",
    "<Q_CHAR>",
    "<S_ESCAPE>",
    "<DIGIT>",
    "<UNSIGNED_INTEGER>",
    "<UNSIGNED_NUMBER>",
    "\";\"",
    "\"=\"",
    "\"(\"",
    "\":\"",
    "\")\"",
    "\"der\"",
    "\",\"",
    "\":=\"",
    "\"^\"",
    "\".^\"",
    "\"[\"",
    "\"]\"",
    "\"{\"",
    "\"}\"",
    "\"terminal\"",
    "\".\"",
  };

}
