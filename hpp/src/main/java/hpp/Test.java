package hpp;

import java.io.UnsupportedEncodingException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class Test {

	public static void main(String[] args) throws UnsupportedEncodingException {
				BlockingQueue<Event> bq = new ArrayBlockingQueue<Event>(20);
				Producer1 p2 = new Producer1(bq,"C:\\Users\\Florian\\Desktop\\Data HPP project\\testcomments.dat");
				p2.run();
				Event e = bq.peek();
					
	}

	public Test() {
		

	}
}
