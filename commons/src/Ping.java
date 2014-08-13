


import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.regex.Pattern;

/**
 * 
 * @author QuanZhong <br>
 *         2011-12-30
 */
public class Ping {
	static int DAYTIME_PORT = 13;
	static int port = DAYTIME_PORT;
	private static long result = -1l;

	static class Target {
		InetSocketAddress address;
		SocketChannel channel;
		Exception failure;
		long connectStart;
		long connectFinish = 0;
		boolean shown = false;

		Target(String host) {
			try {
				address = new InetSocketAddress(InetAddress.getByName(host),port);
			} catch (IOException x) {
				failure = x;
			}
		}

		Long show() {
			if (connectFinish != 0){
				result = connectFinish - connectStart;
				System.out.println("ping"+address+":"+result+"ms");
			}
			else if (failure != null){
				result = -2l;
				System.out.println("Connected refused on port "+port);
			}
			else{
				result = -1l;
				System.out.println("ping"+address+":Timed out");
			}
			shown = true;
			return result;
		}
	}

	static class Printer extends Thread {
		LinkedList<Target> pending = new LinkedList<Target>();

		Printer() {
			setName("Printer");
			setDaemon(true);
		}

		void add(Target t) {
			synchronized (pending) {
				pending.add(t);
				pending.notify();
			}
		}

		public void run() {
			try {
				for (;;) {
					Target t = null;
					synchronized (pending) {
						while (pending.size() == 0)
							pending.wait();
						t = (Target) pending.removeFirst();
					}
					t.show();
				}
			} catch (InterruptedException x) {
				return;
			}
		}
	}

	static class Connector extends Thread {
		Selector sel;
		Printer printer;
		LinkedList<Target> pending = new LinkedList<Target>();

		Connector(Printer pr) throws IOException {
			printer = pr;
			sel = Selector.open();
			setName("Connector");
		}

		void add(Target t) {
			SocketChannel sc = null;
			try {
				sc = SocketChannel.open();
				sc.configureBlocking(false);
				boolean connected = sc.connect(t.address);
				t.channel = sc;
				t.connectStart = System.currentTimeMillis();
				if (connected) {
					t.connectFinish = t.connectStart;
					sc.close();
					printer.add(t);
				} else {
					synchronized (pending) {
						pending.add(t);
					}
					sel.wakeup();
				}
			} catch (IOException x) {
				if (sc != null) {
					try {
						sc.close();
					} catch (IOException xx) {
					}
				}
				t.failure = x;
				printer.add(t);
			}
		}

		void processPendingTargets() throws IOException {
			synchronized (pending) {
				while (pending.size() > 0) {
					Target t = (Target) pending.removeFirst();
					try {
						t.channel.register(sel, SelectionKey.OP_CONNECT, t);
					} catch (IOException x) {
						t.channel.close();
						t.failure = x;
						printer.add(t);
					}
				}
			}
		}

		void processSelectedKeys() throws IOException {
			for (Iterator<?> i = sel.selectedKeys().iterator(); i.hasNext();) {
				SelectionKey sk = (SelectionKey) i.next();
				i.remove();
				Target t = (Target) sk.attachment();
				SocketChannel sc = (SocketChannel) sk.channel();
				try {
					if (sc.finishConnect()) {
						sk.cancel();
						t.connectFinish = System.currentTimeMillis();
						sc.close();
						printer.add(t);
					}
				} catch (IOException x) {
					sc.close();
					t.failure = x;
					printer.add(t);
				}
			}
		}

		volatile boolean shutdown = false;

		void shutdown() {
			shutdown = true;
			sel.wakeup();
		}

		public void run() {
			for (;;) {
				try {
					int n = sel.select();
					if (n > 0)
						processSelectedKeys();
					processPendingTargets();
					if (shutdown) {
						sel.close();
						return;
					}
				} catch (IOException x) {
					x.printStackTrace();
				}
			}
		}
	}
	public static boolean ping(String host) throws Exception {
		InetAddress address = InetAddress.getByName(host);
		boolean result = address.isReachable(5000);
		return result;
	}

	public static void main(String[] args) throws Exception {
//		args = new String[]{"80","192.168.1.15"};
		
		if (args.length < 1) {
			System.err.println("Usage: java Ping [port] host...");
			return;
		}
		int firstArg = 0;
		if (Pattern.matches("[0-9]+", args[0])) {
			port = Integer.parseInt(args[0]);
			firstArg = 1;
		}
		Printer printer = new Printer();
		printer.start();
		Connector connector = new Connector(printer);
		connector.start();
		LinkedList<Target> targets = new LinkedList<Target>();
		for (int i = firstArg; i < args.length; i++) {
			Target t = new Target(args[i]);
			targets.add(t);
			connector.add(t);
		}
		Thread.sleep(2000);
		connector.shutdown();
		connector.join();
		for (Iterator<Target> i = targets.iterator(); i.hasNext();) {
			Target t = (Target) i.next();
			if (!t.shown)
				t.show();
		}
	}
}
