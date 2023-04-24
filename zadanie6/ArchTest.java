import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ArchTest {
    private Archivizer arch = new Archivizer();

    @Test
    public void test_compression(){
        assertTrue("Empty result file on valid conditions",arch.compress("/home/zezula/Desktop/Java/zadania", "/home/zezula/Desktop/Java/zadania.zipp/Java/zadania.zip")> 0);
    }

   @Test
    public void test_compress(){
        assertEquals(-1,arch.compress("", "/Desktop/Java/zadania.zip"));
        assertEquals(-1,arch.compress(null, "/Desktop/Java/zadania.zip"));
        assertEquals(-11,arch.compress("/Desktop/Java/NOTEXITST", null));
        assertEquals(-11,arch.compress("/Desktop/Java/NOTEXITST", ""));

        assertEquals(-2,arch.compress("/Desktop/Java/NOTEXITST", "/Desktop/Java/zadania.zip"));
        assertEquals(-14,arch.compress("/home/zezula/Desktop/Java/zadania/", "/Desktop/Java/zadania.zip"));
       //        assertEquals(-12,arch.compress("/home/zezula/Desktop/Java/zadania/", "/home/zezula/Desktop/Java/zadania"));

        //assertTrue("Empty result file on valid conditions",arch.compress("/home/zezula/Desktop/Java/zadania", "/home/zezula/Desktop/Java/zadania.zip")> 0);
    }

    @Test
    public void test_decompress(){
        arch.decompress("/home/zezula/Desktop/Java/zadania.zip", "/home/zezula/Desktop/Java/test/");
    }
}


