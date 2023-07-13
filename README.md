# L-Language-Compiler

A compiler for the OOP made up language L. 
Takes a program in L (examples below), and outputs the MIPS code that was generated. The MIPS code can then get executed via SPIM. 
The code uses JFlex and CUP to make the lexer and parser respectively.

Was written as a final project in TAU's "Compilation" course, with @mor282 and @OfekBar.

Example programs in L:

Bubble sort
```
array IntArray = int[];

void BubbleSort(IntArray arr, int size)
{
    int i         := 0;
    int j         := 0;
    int min :=   32767;
    int minIndex := -1;
    int temp     :=  0;
    
    while (i < size)
    {
        j := i;
        min := 32767;
        while ( j < size )
        {
            if (arr[j] < min)
            {
                min := arr[j];
                minIndex := j;
            }
            j := j + 1;
        }
        temp := arr[i];
	arr[i] := min;
	arr[minIndex] := temp;
	i := i + 1;
    }
}

void main()
{
    IntArray arr := new int[7];
    
    arr[0] := 34;
    arr[1] := 12;
    arr[2] := -600;
    arr[3] := -400;
    arr[4] := 70;
    arr[5] := 30;
    arr[6] := -580;
    
    BubbleSort(arr,7);

    int i := 0;
    while (i < 7) {
        PrintInt(arr[i]);
        i := i + 1;
    }
}


```


Another example
```
array IntArray = int[];

class Person
{
	int ID;
	int age := 18;

	IntArray lastYearSalaries;

	int getAge()  { return age;   }
	int birthday(){ age := age+1; }
}

class Student extends Person
{
	IntArray grades;
	int getAverage()
	{
		int i := 0;
		int sum := 0;
		while (i<10)
		{
			sum := sum+grades[i];
			i := i+1;
		}
		return sum/10;
	}
}

array University = Student[];

void main()
{
	Student moish := new Student;
	moish.grades := new int[10];
	moish.lastYearSalaries := new int[12];
	int COMPILATION := 6;

	moish.grades[COMPILATION] := 9;

	University TAU := new Student[37];
	
	TAU[3] := moish;
	TAU[5].lastYearSalaries[TAU[3].grades[COMPILATION]] := 999;
}

```
