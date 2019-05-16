package hpp;

import static org.junit.Assert.*;

import java.time.LocalDateTime;

import org.junit.Test;

public class TestPost {

	@Test
	public void getTheRightNumberOfCommenters() {
		Post p = new Post(LocalDateTime.parse("0000-01-01T00:00:00.000"),"test","test");
		p.addNewComment("1");
		p.addNewComment("1");
		p.addNewComment("1");
		p.addNewComment("2");
		p.addNewComment("2");
		p.addNewComment("2");
		p.addNewComment("3");
		p.addNewComment("3");
		p.addNewComment("3");
		assertEquals(p.getCommenters(), 3);
		for (int i =0;i<17;i++)p.decreaseExternScore();
		assertEquals(p.getScore(),90-17 );
		
	}

}
