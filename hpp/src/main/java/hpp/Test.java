package hpp;

import java.io.UnsupportedEncodingException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class Test {

	public static void main(String[] args) throws UnsupportedEncodingException {
				BlockingQueue<Event> bq = new ArrayBlockingQueue<Event>(20);
				Producer2 p2 = new Producer2(bq,"C:\\Users\\Florian\\Desktop\\Data HPP project\\testposts.dat");
				p2.run();
				if (bq.peek().getClass().getName()=="hpp.Post") System.out.println("True");
	}

	public Test() {
		

	}
}
