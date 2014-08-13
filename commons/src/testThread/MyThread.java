package testThread;


public class MyThread extends Thread {
     @Override
     public void run() {
    	 try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
         System.out.println(this.getName() + "����ִ�С�����");
     }
}