//Julia Zezula styczen 2023

import java.io.*;
import java.net.*;
import java.util.Objects;
import java.lang.Object;
import java.lang.InterruptedException;

class Connector implements NetConnection{
    public int numberToLookFor;
    public int numberOfOccurences;

    public int lookForNumber(PrintWriter DataPrintWriter, BufferedReader DataBuffReader) throws IOException{
        numberToLookFor = 0;

        DataPrintWriter.println("Program"); //informujemy o trybie pracy
        String DataFromLine;
        while((DataFromLine = DataBuffReader.readLine())!=null){


            //System.out.println("Reading: " + DataFromLine);
            if(DataFromLine.startsWith("Zaczynamy")){
                String[] parsedLine = DataFromLine.split(":");
                if(parsedLine.length == 2){
                numberToLookFor = Integer.parseInt(parsedLine[1].trim());
                }
                else{
                    System.out.println("Cannot get number to look for from DataFromLine " + DataFromLine);
                }
                break;
            }
        }

        return numberToLookFor;
    }

    public int countOccurences(int numberToLookFor, BufferedReader DataBuffReader)throws IOException{
        numberOfOccurences = 0;
        String DataFromLine;
        while((DataFromLine = DataBuffReader.readLine())!=null){

            if(DataFromLine.trim().compareTo("EOD") == 0){
                break;
            }
            else{
                int currentyLookingAt = Integer.parseInt(DataFromLine);
                //System.out.println("Currently looking at number " + currentyLookingAt);
                if(currentyLookingAt == numberToLookFor){
                    numberOfOccurences++;
                }
            }
        }

        return numberOfOccurences;
    }

    public void sendNumber(PrintWriter DataPrintWriter, int numberOfOccurences)throws IOException{
        DataPrintWriter.println(numberOfOccurences);
    }

    public void getFeedback(BufferedReader DataBuffReader)throws IOException{
        String DataFromLine;
        while((DataFromLine = DataBuffReader.readLine()) != null){
            //System.out.println("Feedback: " + DataFromLine);
        }

    }

    //Metoda otwiera połączenie do serwera dostępnego
    // protokołem TCP/IP pod adresem host i numerem portu TCP port
    public void connect(String host, int port){
        try(Socket socket = new Socket(host, port)){
            InputStream inputData = socket.getInputStream();
            OutputStream outputData = socket.getOutputStream();
            PrintWriter DataPrintWriter = new PrintWriter(outputData, true);
            BufferedReader DataBuffReader = new BufferedReader(new InputStreamReader(inputData));

            numberToLookFor = lookForNumber(DataPrintWriter, DataBuffReader);
            numberOfOccurences = countOccurences(numberToLookFor, DataBuffReader);
            //System.out.println("Number has been found: " + numberOfOccurences + " times" + " was looking for " + numberToLookFor);
            sendNumber(DataPrintWriter, numberOfOccurences);

            //getFeedback(DataBuffReader);

            //close the connection
            DataPrintWriter.close();
            inputData.close();
            socket.close();

        }catch(UnknownHostException ex){
            System.out.println("Server not found: " + ex.getMessage());
        }catch(IOException ex){
            System.out.println("I/O error: " + ex.getMessage());
        }



    }

    /*
    public static void main (String[] args){
        Connector c = new Connector();
        c.connect("172.30.24.101", 8080);S
    } */
}