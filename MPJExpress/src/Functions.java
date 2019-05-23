
import java.util.*;
import java.io.*;

public class Functions {
	
	private Scanner x;
	public int [] arr;
	public int size;
	
	public void OpenFile()
	{
		try {
			x=new Scanner(new File("input.txt"));
		}
		catch(Exception e)
		{
			System.out.println("error in opening file \n");
		}
	}
	public void ReadData()
	{
		int turn=0;
		int i=0;
		while(x.hasNext())
		{
			if(turn==0)
			{
				String c=x.next();
				size=Integer.parseInt(c);
				arr=new int[size];
				turn=turn +1;
			}
			else
			{
				String a=x.next();
				arr[i]=Integer.parseInt(a);
				i=i+1;
			}
		}
	}
	public void CloseFile()
	{
		x.close();
	}
	public void PrintData()
	{
		for(int i=0;i<size;i++)
		{
			System.out.printf("%d \n", arr[i]);
		}
	}
	public int AddLocal(int []a,int LocalSize)
	{
		int sum=0;
		for(int i=0;i<LocalSize;i++)
		{
			sum=sum+a[i];
		}
		return sum;
	}

}
