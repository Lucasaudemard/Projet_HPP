package hpp;


import java.io.UnsupportedEncodingException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class Test {
	//Test on small data sets
	public static void main(String[] args) throws UnsupportedEncodingException {
		String outputPath = "C:\\Users\\Florian\\Desktop\\HPP project\\Data HPP project\\output.dat";
		String pathComments = "C:\\Users\\Florian\\Desktop\\HPP project\\Data HPP project\\testcomments.dat";
		String pathPosts =  "C:\\Users\\Florian\\Desktop\\HPP project\\Data HPP project\\testposts.dat";
		BlockingQueue<Event> bqComments = new ArrayBlockingQueue<Event>(1000000);
		BlockingQueue<Event> bqPosts = new ArrayBlockingQueue<Event>(1000000);
		Producer1 commentProducer = new Producer1(bqComments, pathComments);
		Producer2 postProducer = new Producer2(bqPosts, pathPosts);
		commentProducer.run();
		postProducer.run();
		Ordonnanceur ord = new Ordonnanceur(bqComments,bqPosts,outputPath);
		ord.run();
	}
}
