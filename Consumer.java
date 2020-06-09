import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Exchanger;

public class Consumer extends Thread {

	private List<Path> listFiles = new ArrayList<Path>();
	String path;
	Exchanger<String> ex;
	File file;
	BufferedReader br;
	InputStreamReader isr;
	FileInputStream fis;
	String data;
	int numberOfCharacter = 0;
	int numberOfFiles = 0; 

	public Consumer(Exchanger ex) {
		this.ex = ex;
	}

	public void process() throws InterruptedException, IOException {
		System.out.println("Size of list " + this.listFiles.size());
		if (listFiles.size() > 0) {
			try {

				System.out.println("Processing " + this.listFiles.get(0));

				file = new File(listFiles.get(0).toString());
				fis = new FileInputStream(file);
				isr = new InputStreamReader(fis);
				br = new BufferedReader(isr);

				while ((data = br.readLine()) != null) {
					numberOfCharacter += data.length();
				}
				numberOfFiles++;
				this.listFiles.remove(0);
			} catch (FileNotFoundException e) {
				System.out.println(this.listFiles.get(0)+" cannot be accessed ");
			}

		}

		try {
			path = (String) ex.exchange(path);
			
			System.out.println("updating list");
			this.listFiles.add(Paths.get(path));
			System.out.println("list updated");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void run() {
		while (true) {
			try {
				process();
				System.out.println("number of files proccessed: "+this.numberOfFiles+ " number of characters found:"+ this.numberOfCharacter);
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
