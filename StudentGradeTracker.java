import java.util.*;
public class StudentGradeTracker
{
	public static void main(String x[])
	{
		Scanner xyz = new Scanner(System.in);
		
		System.out.println("Enter Student Name: ");
		String name = xyz.nextLine();
		
		System.out.println("Enter Student ID: ");
		int id = xyz.nextInt();
		
		System.out.println("Enter student marks for subject 1: ");
		int sub1 = xyz.nextInt();
		
		System.out.println("Enter student marks for subject 2: ");
		int sub2 = xyz.nextInt();
		
		System.out.println("Enter student marks for subject 3: ");
		int sub3 = xyz.nextInt();
		
		System.out.println("Enter student marks for subject 4: ");
		int sub4 = xyz.nextInt();
		 
		System.out.println("Enter student marks for subject 5: ");
		int sub5 = xyz.nextInt();
		
		int total = sub1+sub2+sub3+sub4+sub5;
		int average = total / 5;
		
		String grade;
		
		if( average >= 90){
			grade = "A+";	
		}
		else if( average >= 80){
			grade = "A";
		}
		else if( average >= 70){
			grade = "B";
		}
		else if( average >= 60){
			grade = "C";
		}
		else if( average >= 50){
			grade = "D";
		}
		else{
			grade = "F";
		}	
		
		
		String result;
		if( sub1 > 40 || sub2 > 40 || sub3 > 40 || sub4 > 40 || sub5 > 40){
			result = "Pass";
		}	
		else{
			result = "Fail";
		}	
		
		
		System.out.println(" -------- Student Report -------- ");
		
		System.out.println(" Name :  " + name);
		System.out.println(" Student ID : " + id);
		System.out.println(" Total : " + total);
		System.out.println(" Average : "  + average);
		System.out.println(" Grade :  " + grade);
		System.out.println(" Result :  " + result);
		
	}
}