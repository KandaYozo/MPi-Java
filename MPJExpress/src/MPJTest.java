import java.io.*;
import mpi.*;

public class MPJTest {
	@SuppressWarnings("null")
	public static void main(String[] args) throws FileNotFoundException, IOException, ClassNotFoundException {

		MPI.Init(args);
		Functions F=new Functions();
		int rank = MPI.COMM_WORLD.Rank();
		int size = 2 * MPI.COMM_WORLD.Size();
		int root = 0;
		int[] sendbuf;
		int[] recvbuf;
		int global = 0;
		// REAAAAADING INPUT FILEEEEEEEEEEEEEEEEE
		if (rank == root) {
			F.OpenFile();
			F.ReadData();
			F.CloseFile();
			F.PrintData();
			int[] message = new int[2];
			// REAAAAADING INPUT FILE EEEEEEEEEEEEEEEE
			// first need to send to all the size of array
			message[0] = F.size / 3;
			System.out.println("Iam a root and i send Size of Array/3 : " + message[0]);
			MPI.COMM_WORLD.Bcast(message, 0, message.length, MPI.INT, 0);

			// =============== Scatter information to all processes ================
			// This send buffer of the root process should have enough space to
			// hold the data sent to all processes
			int currSize=F.size* MPI.COMM_WORLD.Size();
			sendbuf = new int[currSize];
			System.out.println(size);
			System.out.println("Curr Size"+currSize);
			for (int i = 0; i < F.size; i++)
				sendbuf[i] = F.arr[i];
			// This receive buffer of the root process should have enough space to
			// hold the data sent by all processes
			recvbuf = new int[currSize];
			int dummy[] = new int[currSize];
			System.out.println("iam a scatter and iam sending");
			MPI.COMM_WORLD.Scatter(sendbuf, 0, F.size / 3, MPI.INT, dummy, 0, F.size / 3, MPI.INT, 0);
			System.out.println("iam a root and iam summing ");
			int LocalSum = 0;
			for (int i = 0; i < F.size / 3; i++) {
				LocalSum += sendbuf[i];
			}
			if(F.size%3 !=0)
			{
				int i=0;
				for(i=F.size-1;i>=0;i--)
				{
					if(i%3==0)
						break;// find first divisible by 3					
				}
				for(;i<F.size;i++)
				{
					LocalSum+=sendbuf[i];
				}
			}
			System.out.println("Hi my Rank is  <0> iam a root." + "and my sum is" + LocalSum + " have a nice day :)");

			float sendbuff[] = new float[size];
			float result[] = new float[size];
			
			
			sendbuff[0] = LocalSum;
	

			MPI.COMM_WORLD.Reduce(sendbuff, 0, result, 0, 3, MPI.FLOAT, MPI.SUM, 0);
			System.out.println("Total Sum is : "+result[0]);
			// MPI.COMM_WORLD.Gather(sendbuf,0,3,MPI.INT,recvbuf,0,3,MPI.INT,root);

		} else {

			int me = MPI.COMM_WORLD.Rank();
			int[] message = new int[2];// i dont know why i made it array its 1 number representing size/3 of array
			MPI.COMM_WORLD.Bcast(message, 0, message.length, MPI.INT, 0);
			System.out.println("Iam a process of Rank " + me + "  and i receive  Size of Array : " + message[0]);
			int mArraySize = message[0];
			int[] dummy = new int[mArraySize* MPI.COMM_WORLD.Size()]; // iam dummy
			int[] Scatter_recvbuf = new int[mArraySize* MPI.COMM_WORLD.Size()];
			int sum = 0;

			MPI.COMM_WORLD.Scatter(dummy, 0, mArraySize, MPI.INT, Scatter_recvbuf, 0, mArraySize, MPI.INT, 0);
			size = 4;
			System.out.println("Iam a process of Rank " + me + "  and i received ArrayElements : ");

			for (int i = 0; i < mArraySize; i++)
				System.out.println(Scatter_recvbuf[i]);

			for (int i = 0; i < mArraySize; i++) {
				sum += Scatter_recvbuf[i];
			}
//			MPI.COMM_WORLD.Gather(sum,0,1,MPI.INT,dummy,0,1,MPI.INT,root);	

			System.out.println("Hi my Rank is  <" + me + ">" + "and my sum is" + sum + " have a nice day :)");
			float sendbuff[] = new float[size];
			float result[] = new float[size];
			sendbuff[0] = sum;

			MPI.COMM_WORLD.Reduce(sendbuff, 0, result, 0, 3, MPI.FLOAT, MPI.SUM, 0);
			MPI.Finalize();
		}
	}
}
