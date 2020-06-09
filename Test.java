import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.Exchanger;

public class Test {
	public static void main(String[] args) {
		
		String path = "C:\\Users\\lyesm\\OneDrive\\Documents\\txtFolder";
		Exchanger ex = new Exchanger();
		
		
		Producer producer = new Producer(path,ex);
		Consumer consumer = new Consumer(ex);
		
		producer.start();
		consumer.start();
		
	}
}
