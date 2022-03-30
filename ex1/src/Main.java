   
import java.io.*;
import java.io.PrintWriter;

import java_cup.runtime.Symbol;
   
public class Main
{
	public static String toString (Symbol s)
	{
		switch(s.sym)
		{
			case 1:
				return "PLUS";
			case 2:
				return "MINUS";
			case 3:
				return "TIMES";
			case 4:
				return "DIVIDE";
			case 5:
				return "LPAREN";
			case 6:
				return "RPAREN";
			case 7:
				return ("INT(" + s.value + ")");
			case 8:
				return ("ID(" + s.value + ")");
			case 9:
				return "CLASS";
			case 10:
				return ("STRING(" + s.value + ")");
			/* 11 = comment */
			case 12:
				return "LBRACE";
			case 13:
				return "RBRACE";
			case 14:
				return "EQ";
			case 15:
				return "LBRACK";
			case 16:
				return "RBRACK";
			case 17:
				return "NIL";
			case 18:
				return "COMMA";
			case 19:
				return "DOT";
			case 20:
				 return "SEMICOLON";
			case 21:
				return "ASSIGN";
			case 22:
				return "LT";
			case 23:
				return "GT";
			case 24:
				return "ARRAY";
			case 25:
				return "EXTENDS";
			case 26:
				return "RETURN";
			case 27:
				return "WHILE";
			case 28:
				return "IF";
			case 29:
				return "NEW";
			case 30:
				return "TYPE_INT";
			case 31: 
				return "TYPE_STRING";
		}
		return "";
	}

	static public void main(String argv[])
	{
		Lexer l;
		Symbol s;
		FileReader file_reader;
		PrintWriter file_writer;
		String inputFilename = argv[0];
		String outputFilename = argv[1];
		
		try
		{
			/********************************/
			/* [1] Initialize a file reader */
			/********************************/
			file_reader = new FileReader(inputFilename);

			/********************************/
			/* [2] Initialize a file writer */
			/********************************/
			file_writer = new PrintWriter(outputFilename);
			
			/******************************/
			/* [3] Initialize a new lexer */
			/******************************/
			l = new Lexer(file_reader);

			/***********************/
			/* [4] Read next token */
			/***********************/
			s = l.next_token();

			/********************************/
			/* [5] Main reading tokens loop */
			/********************************/
			while (s.sym != TokenNames.EOF)
			{
				if (s.sym == TokenNames.ERROR)
				{
					file_writer = new PrintWriter(outputFilename);
					file_writer.print("ERROR");
					file_writer.close();
					break;
				}
				/************************/
				/* [6] Print to console */
				/************************/
				System.out.print("[");
				System.out.print(l.getLine());
				System.out.print(",");
				System.out.print(l.getTokenStartPosition());
				System.out.print("]:");
				System.out.print(toString(s)); //FOR DEBUGGING
				System.out.print("\n");
				
				/*********************/
				/* [7] Print to file */
				/*********************/
				file_writer.print(toString(s));
				file_writer.print("[");
				file_writer.print(l.getLine());
				file_writer.print(",");
				file_writer.print(l.getTokenStartPosition());
				file_writer.print("]");
				file_writer.print("\n");
				
				/***********************/
				/* [8] Read next token */
				/***********************/
				s = l.next_token();
			}
			
			/******************************/
			/* [9] Close lexer input file */
			/******************************/
			l.yyclose();

			/**************************/
			/* [10] Close output file */
			/**************************/
			file_writer.close();
    	}
			     
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}


