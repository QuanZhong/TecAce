package testThread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TestFixedThreadPool {
	  public static void main(String[] args) {
	         // ����һ�������ù̶��߳�����̳߳�
	         ExecutorService pool = Executors.newFixedThreadPool(10);
//	         ExecutorService pool = Executors.newCachedThreadPool();
	         // ����ʵ����Runnable�ӿڶ���Thread����ȻҲʵ����Runnable�ӿ�
	         for(int i=1;i <= 10000;i++){
	        	 Thread t1 = new MyThread();
		         t1.setName("t_"+i+"_");
		         // ���̷߳�����н���ִ��
		         pool.execute(t1); 
	         }
	         
	         System.out.println("over");
	         // �ر��̳߳�
	         pool.shutdown();
	         System.out.println("over2");
	         
	     }
}
