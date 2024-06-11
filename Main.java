import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;

public class Main {
	public static void main(String[] args) {
		//User input string for original file path
		System.out.println("Enter file path: ");
		Scanner scan = new Scanner(System.in);
		String filePath = scan.next();
		scan.close();
		
		//Creates variables for original file containing duplicates and creates the new file to put all the duplicates into.
		//The new file will be put in the same directory as the original file
		File originalFile = new File(filePath);
		String newFileDirectory = filePath.substring(0, filePath.lastIndexOf("\\"));
		String newFileName = "\\DuplicateSongs";
		File dupeFile = new File(newFileDirectory + newFileName);
		dupeFile.mkdirs();
		
		//Move all file names into array
		String[] content = originalFile.list();
		Arrays.sort(content);
		ArrayList<String> duplicates = new ArrayList<String>();
		
		//Sorts through array finds all duplicates and moves them into the duplicates array list
		for(int i=1; i<content.length; i++) {
			//System.out.println(content[i]);
			
			String bufferTitle = shorten(content[i]);
			String prevBufferTitle = shorten(content[i-1]);
			
			if(bufferTitle.equals(prevBufferTitle)) { //If the current index matches the previous index, move previous index
				duplicates.add(content[i-1]);	
			}else if(duplicates.size() > 0 && prevBufferTitle.equals(shorten(duplicates.get(duplicates.size()-1)))) { //if previous index matches last index moved, move previous index
				duplicates.add(content[i-1]);	
			}
		}
		
		//Moves duplicates to new file
		for(int i=0; i<duplicates.size(); i++) {
			//Creates the new header file
			File bufferFile = new File(dupeFile.getAbsolutePath() + File.separator + duplicates.get(i));
			bufferFile.mkdirs();
			//Establishes paths and moves the file
			Path sourcePath = Paths.get(filePath + File.separator + duplicates.get(i));
			Path finalPath = Paths.get(dupeFile.getAbsolutePath() + File.separator + duplicates.get(i));
			System.out.println("Moved: " + duplicates.get(i));
			try {
				Files.move(sourcePath, finalPath, StandardCopyOption.REPLACE_EXISTING);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	//Function to remove the charter names from the file name
	public static String shorten(String x) {
		int index = x.indexOf('(');
        if (index == -1) {
            index = x.length();
        }
		return (x.substring(0, index)).toLowerCase();
	}
}