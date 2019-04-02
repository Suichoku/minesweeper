package minesweeper;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;

/**
 * Save and read record HashMap into file using object serialization for HashMap
 * 
 * @author Tuomas Rautanen
 */
public class RecordMapper {
	
	/**
	 * Save and read record HashMap into file using object serialization for HashMap
	 * 
	 * @see <a href="https://beginnersbook.com/2013/12/how-to-serialize-hashmap-in-java/">Reference used to make serialization and deserialization</a>
	 */
	public RecordMapper() {}
	
	/**
	 * Save current records HashMap into file using serialization.
	 * 
	 * HashMap pair format = Key: String (Difficulty), Value: Integer (Time in milliseconds)
	 * 
	 * @param records HashMap of current records
	 * @param filename name of the serialization file
	 */
	public void writeRecords(HashMap<String, Integer> records, String filename) {
		
		try {
			
			FileOutputStream fos = new FileOutputStream(filename);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			
			oos.writeObject(records);
			
			oos.close();
			fos.close();
			
		} catch(IOException ioe) {
			
			ioe.printStackTrace();
		}
	}
	
	/**
	 * Load current records HashMap from file using serialization.
	 * 
	 * HashMap pair format = Key: String (Difficulty), Value: Integer (Time in milliseconds)
	 * 
	 * @param filename name of the serialization file
	 * 
	 * @return HashMap containing current records
	 * 
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	@SuppressWarnings("unchecked")
	public HashMap<String, Integer> readRecords(String filename) throws IOException, ClassNotFoundException {
		
		HashMap<String, Integer> map = new HashMap<String, Integer>();
		
		FileInputStream fis = new FileInputStream(filename);
		ObjectInputStream ois = new ObjectInputStream(fis);
		
		map = (HashMap<String, Integer>) ois.readObject();
		
		ois.close();
		fis.close();
		
		return map;	
	}	
}
