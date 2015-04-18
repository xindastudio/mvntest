package unittest;

import java.util.LinkedList;
import java.util.List;

public class TestLinkedList {

	public static void main(String[] args) {
		test1(args);
		test2(args);
	}

	public static void test1(String[] args) {
		final List<Integer> l = new LinkedList<Integer>();
		l.add(1);
		l.add(2);
		l.add(3);
		l.add(4);
		l.add(5);
		new Thread() {
			public void run() {
				for (int i = 0; i < l.size(); i++) {
					System.out.println(l.get(i));
				}
			}
		}.start();
		new Thread() {
			public void run() {
				l.remove(4);
				l.remove(3);
				l.remove(2);
				l.remove(1);
				l.remove(0);
				System.out.println("all removed");
			}
		}.start();
	}

	public static void test2(String[] args) {
		final List<Integer> l = new LinkedList<Integer>();
		l.add(1);
		l.add(2);
		l.add(3);
		l.add(4);
		l.add(5);
		new Thread() {
			public void run() {
				Integer[] temp = l.toArray(new Integer[0]);
				for (int i = 0; i < temp.length; i++) {
					System.out.println(temp[i]);
				}
			}
		}.start();
		new Thread() {
			public void run() {
				l.remove(4);
				l.remove(3);
				l.remove(2);
				l.remove(1);
				l.remove(0);
				System.out.println("all removed");
			}
		}.start();
	}

}
