import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TaskList {
	List<Task> lst;
	
	public boolean loadTaskList(String filename, ObjectMapper mapper) {
		if (Files.isReadable(Paths.get(filename))) {
			try {
				lst = Arrays.asList(mapper.readValue(
						Paths.get(filename).toFile(),
						Task[].class));
				return true;
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
		} else {
			return false;
		}
	}
}
