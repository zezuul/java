
public interface NetConnection {
	/**
	 * Metoda otwiera połączenie do serwera dostępnego protokołem TCP/IP pod adresem
	 * host i numerem portu TCP port.
	 * 
	 * @param host adres IP lub nazwa komputera
	 * @param port numer portu, na którym serwer oczekuje na począczenie
	 */
	public void connect(String host, int port);
}
