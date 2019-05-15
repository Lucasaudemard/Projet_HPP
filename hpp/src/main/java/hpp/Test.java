package hpp;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.ParsePosition;
import java.time.LocalDateTime;
import java.util.Date;

public class Test {

	public static void main(String[] args) throws UnsupportedEncodingException {
		try {
			FileInputStream fstream = new FileInputStream("C:\\Users\\Florian\\Desktop\\Data HPP project\\comments.dat");
			BufferedReader br = new BufferedReader(new InputStreamReader(fstream,"UTF-8"));
			try {
				String line = br.readLine();
				System.out.println(line);
				//Method to parse the data and get the time
				//String [] lineList = line.split("[|]");//to split around the || character [|]
				LocalDateTime dateTime = LocalDateTime.parse(line.split("[+]")[0]);
				System.out.println(dateTime);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public Test() {
		
	}
}
