package hpp;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

public class Test {

	public static void main(String[] args) throws UnsupportedEncodingException {
		String path = "C:\\Users\\Florian\\Desktop\\Data HPP project\\test.dat";
		File f = new File(path);
		f.delete();

		for (int i = 0; i < 10; i++) {
			write(path);
		}

	}

	public static void write(List<String> top3) {
		for (int i = 0; i < 3; i++) {
			String s = top3.get(i);
			String fileContent = "";
			if (s == "") {
				fileContent = "-,-,-";
			} else {

			}
		}

		FileWriter fw;
		try {
			fw = new FileWriter(path, true);
			fw.write(fileContent);
			fw.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
