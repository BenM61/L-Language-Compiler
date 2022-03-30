/***********/
/* PACKAGE */
/***********/
package IR;

/*******************/
/* GENERAL IMPORTS */
/*******************/

/*******************/
/* PROJECT IMPORTS */
/*******************/
import TEMP.*;
import MIPS.*;

public class IRcommand_ThisDotField extends IRcommand {
	public TEMP dst;
	String name;
	public boolean cfg = false;

	// this.field
	public IRcommand_ThisDotField(String Fname, TEMP dst) {
		this.dst = dst;
		this.name = Fname;

	}

	/***************/
	/* MIPS me !!! */
	/***************/
	public void MIPSme() {
		System.out.println("IRcommand_ThisDotField" + "- MIPSme");

		MIPSGenerator.getInstance().load_field_in_func(dst, offset);
	}
}