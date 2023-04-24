
import org.junit.Test;
import static org.junit.Assert.*;


public class SerwisAukcyjnyTest {

@Test
    public void test1()
{
    SerwisAukcyjny sa = new SerwisAukcyjny();
    sa.dodajUżytkownika("Johny", new Aukcja.Powiadomienie() {
        @Override
        public void przebitoTwojąOfertę(Aukcja.PrzedmiotAukcji przedmiot) {
            System.out.println("PRzebito: " + przedmiot.nazwaPrzedmiotu() + przedmiot.aktualnaOferta());
        }
    });

    assertTrue(sa.users.containsKey("Johny"));
    assertFalse(sa.users.containsKey("JohnyDoe"));
}
}
