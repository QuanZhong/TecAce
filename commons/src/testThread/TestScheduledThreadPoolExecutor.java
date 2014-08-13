package testThread;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class TestScheduledThreadPoolExecutor {
	public static void main(String[] args) {
		ScheduledThreadPoolExecutor exec = new ScheduledThreadPoolExecutor(3);
		exec.scheduleAtFixedRate(new Runnable() {// ÿ��һ��ʱ��ʹ����쳣
					@Override
					public void run() {
//						 throw new RuntimeException();
						System.out.println("================");
					}
				}, 1000, 5000, TimeUnit.MILLISECONDS);
		
		exec.scheduleAtFixedRate(new Runnable() {// ÿ��һ��ʱ���ӡϵͳʱ�䣬֤�������ǻ���Ӱ���
					@Override
					public void run() {
						System.out.println(System.nanoTime());
					}
				}, 1000, 2000, TimeUnit.MILLISECONDS);
	}
}