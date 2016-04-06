/*import static Utils.Utils.generateRandomBytes;
import static Utils.Utils.Mode.BYTE;
import static org.junit.Assert.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import Bank.NoteComparator;
import Banknote.Note;

public class checkXORTest {

	@Test
	public void test() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, Exception {
		
		Method method = NoteComparator.class.getDeclaredMethod("checkXOR", ArrayList.class);
		method.setAccessible(true);
		method.invoke(method, a("20".getBytes()));
	
	}
	
	public ArrayList<Note> a(byte[] amount) throws Exception{
		
		List<byte[]> rSafe = new ArrayList<>();                                 // = new byte[256][100];
        List<byte[]> lSafe = new ArrayList<>();
		
        ArrayList<Note> notes = new ArrayList<>();
        
        for (int i = 0; i < 100; i++) {
            rSafe.add(generateRandomBytes(256, BYTE));
            lSafe.add(generateRandomBytes(256, BYTE));
        }
		
		for (int i = 0; i < 10; i++) {
            notes.add(new Note(amount, lSafe.get(i), rSafe.get(i)));
        }
		return notes;
	}

}
*/