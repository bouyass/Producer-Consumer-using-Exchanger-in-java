import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.Exchanger;

public class Producer extends Thread implements Runnable {

	private String path;
	Exchanger ex;

	public Producer(String path, Exchanger ex) {
		this.path = path;
		this.ex = ex;
	}

	public void process(String myPath) throws InterruptedException {
		System.out.println(Thread.currentThread().getName() + " is Executing ...");
		System.out.println("Preparing " + path + " path scan ...");
		try {
			DirectoryStream<Path> ds = Files.newDirectoryStream(Paths.get(myPath));
			for (Path newPath : ds) {
				if (Files.isDirectory(newPath.toAbsolutePath())) {
					process(newPath.toString());
				}
				System.out.println("Transfering file to the consumer ");
				String po = (String) ex.exchange(newPath.toString());
			}

			
				
			

		} catch (Exception e) {
			e.printStackTrace();
		}
			

	}
	
	public void run() {
		try {
			process(this.path);
			String  finished = (String) ex.exchange("finished");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
