/***************************/
/* FILE NAME: LEX_FILE.lex */
/***************************/

/*************/
/* USER CODE */
/*************/
import java_cup.runtime.*;

/******************************/
/* DOLAR DOLAR - DON'T TOUCH! */
/******************************/

%%

/************************************/
/* OPTIONS AND DECLARATIONS SECTION */
/************************************/
   
/*****************************************************/ 
/* Lexer is the name of the class JFlex will create. */
/* The code will be written to the file Lexer.java.  */
/*****************************************************/ 
%class Lexer

/********************************************************************/
/* The current line number can be accessed with the variable yyline */
/* and the current column number with the variable yycolumn.        */
/********************************************************************/
%line
%column

/*******************************************************************************/
/* Note that this has to be the EXACT same name of the class the CUP generates */
/*******************************************************************************/
%cupsym TokenNames

/******************************************************************/
/* CUP compatibility mode interfaces with a CUP generated parser. */
/******************************************************************/
%cup

/****************/
/* DECLARATIONS */
/****************/
/*****************************************************************************/   
/* Code between %{ and %}, both of which must be at the beginning of a line, */
/* will be copied verbatim (letter to letter) into the Lexer class code.     */
/* Here you declare member variables and functions that are used inside the  */
/* scanner actions.                                                          */  
/*****************************************************************************/   
%{
	/*********************************************************************************/
	/* Create a new java_cup.runtime.Symbol with information about the current token */
	/*********************************************************************************/
	private Symbol symbol(int type)               {return new Symbol(type, yyline, yycolumn);}
	private Symbol symbol(int type, Object value) {return new Symbol(type, yyline, yycolumn, value);}

	/*******************************************/
	/* Enable line number extraction from main */
	/*******************************************/
	public int getLine() { return yyline + 1; } 

	/**********************************************/
	/* Enable token position extraction from main */
	/**********************************************/
	public int getTokenStartPosition() { return yycolumn + 1; } 
%}

/***********************/
/* MACRO DECALARATIONS */
/***********************/
LineTerminator	= \r|\n|\r\n
WhiteSpace		= {LineTerminator} | [ \t\f]
NOT_A_NUMBER    = 0[0-9]+ | 3276[8-9] | [0-9][0-9][0-9][0-9][0-9][0-9]+
INTEGER			= 0 | [1-9][0-9]?[0-9]?[0-9]? | [1-2][0-9][0-9][0-9][0-9] | 3[0-1][0-9][0-9][0-9] | 32[0-6][0-9][0-9] | 327[0-5][0-9] | 3276[0-7]
LETTER          = [A-Za-z]
ID				= {LETTER} ({LETTER} | [0-9])*
CLASS       	= class
STRING			= \"{LETTER}*\"
/***COMMENTS****/ 
REGULAR_CHARS = \( | \) | \{ | \} | \[ | \] | \? | \! | \+ | \- | \. | \; | {LETTER} | [0-9] | {WhiteSpace}
REGULAR_CHARS_2 = \( | \) | \{ | \} | \[ | \] | \? | \! | \+ | \- | \. | \; | {LETTER} | [0-9]
TIMES_CHARS = \* ({REGULAR_CHARS}  | \* )* ({REGULAR_CHARS})+
END = [\*]+ \/
COMMENT_1 = \/\* ({REGULAR_CHARS} | \/ | {TIMES_CHARS})* {END}
COMMENT_2 = \/\/ ({REGULAR_CHARS_2} | \* | \/ | [ \t\f])* {LineTerminator}
COMMENT = {COMMENT_1} | {COMMENT_2}
/***ERRORS*****/
ILLEGAL_CHARS = \" | \, | \= | \, | \> | \: | \<
NOT_A_COMMENT = \/\/ ({REGULAR_CHARS_2} | \/ | \* | [ \t\f] | {ILLEGAL_CHARS})* {ILLEGAL_CHARS}+ ({REGULAR_CHARS_2} | \/ | \* | [ \t\f] | {ILLEGAL_CHARS})* {LineTerminator}
NOT_A_COMMENT_2 = \/\* ({REGULAR_CHARS} | \/ | {TIMES_CHARS})* {ILLEGAL_CHARS}+ ({REGULAR_CHARS} | \/ | {TIMES_CHARS})* {END}
ERROR = \n|.

/******************************/
/* DOLAR DOLAR - DON'T TOUCH! */
/******************************/

%%

/************************************************************/
/* LEXER matches regular expressions to actions (Java code) */
/************************************************************/

/**************************************************************/
/* YYINITIAL is the state at which the lexer begins scanning. */
/* So these regular expressions will only be matched if the   */
/* scanner is in the start state YYINITIAL.                   */
/**************************************************************/

<YYINITIAL> {

"+"					{ return symbol(TokenNames.PLUS);}
"-"					{ return symbol(TokenNames.MINUS);}
{COMMENT}			{ /* just skip what was found, do nothing */ }
{NOT_A_COMMENT}     { return symbol(TokenNames.ERROR);} 
{NOT_A_COMMENT_2}   { return symbol(TokenNames.ERROR);} 
"/*"  				{ return symbol(TokenNames.ERROR);} //unclosed comment or bad chars
"*"  				{ return symbol(TokenNames.TIMES);}
"/" 				{ return symbol(TokenNames.DIVIDE);}
"("					{ return symbol(TokenNames.LPAREN);}
")"					{ return symbol(TokenNames.RPAREN);}
"int"               {return symbol(TokenNames.TYPE_INT);}
"<"                 { return symbol(TokenNames.LT);}
">"                 { return symbol(TokenNames.GT);}
";"                { return symbol(TokenNames.SEMICOLON);}
":="                { return symbol(TokenNames.ASSIGN);}
"="                 { return symbol(TokenNames.EQ);}
"."                 { return symbol(TokenNames.DOT);}
","                 { return symbol(TokenNames.COMMA);}
"}"                 { return symbol(TokenNames.RBRACE);}
"{"                 { return symbol(TokenNames.LBRACE);}
"]"                 { return symbol(TokenNames.RBRACK);}
"["                 { return symbol(TokenNames.LBRACK);}
"array"             { return symbol(TokenNames.ARRAY);}
"extends"           { return symbol(TokenNames.EXTENDS);}
"return"            { return symbol(TokenNames.RETURN);}
"while"             { return symbol(TokenNames.WHILE);}
"if"                { return symbol(TokenNames.IF);}
"new"               { return symbol(TokenNames.NEW);}
"nil"               { return symbol(TokenNames.NIL);}
"string"            { return symbol(TokenNames.TYPE_STRING);}
"class" 	        { return symbol(TokenNames.CLASS);}
{NOT_A_NUMBER}      { return symbol(TokenNames.ERROR);}
{INTEGER}			{ return symbol(TokenNames.INT,  new Integer(yytext()));}
{ID}				{ return symbol(TokenNames.ID,  new String( yytext()));}
{STRING}			{ return symbol(TokenNames.STRING,  new String( yytext()));}
{WhiteSpace}		{ /* just skip what was found, do nothing */ }
{ERROR}	    	    { return symbol(TokenNames.ERROR);}
<<EOF>>				{ return symbol(TokenNames.EOF);}
}
