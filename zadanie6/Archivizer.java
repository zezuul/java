//Julia Zezula listopad 2022

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;


public class Archivizer implements ArchivizerInterface {


    public static final int BUFF_SIZE = 512;

    public int compress(String dir, String filename) {

        if (dir == null || dir.length() == 0) return -1;
        if (filename == null || filename.length() == 0) return -11;
        Path sourcePath = Paths.get(dir);
        if (!Files.isDirectory(sourcePath)) return -2;
        File outputFile = new File(filename);
        //if (Files.exists(outputFile.toPath())) return -12;
        if (!Files.isDirectory(outputFile.getParentFile().toPath())) return -14;


        try {
            final FileOutputStream fOutStrm = new FileOutputStream(outputFile.getAbsolutePath());
            ZipOutputStream zipOut = new ZipOutputStream(fOutStrm);
            Stream<Path> stream = Files.walk(Paths.get(dir));
            Iterator<Path> it = stream.iterator();
            while (it.hasNext()) {
                Path fx = it.next();
                File fileToZip = new File(fx.toFile().getAbsolutePath());

                if (!Files.isDirectory(fx)) {
                    ZipEntry zipEntry = new ZipEntry(sourcePath.relativize(fx).toString());


                    zipOut.putNextEntry(zipEntry);

                    FileInputStream fInStrm = new FileInputStream(fileToZip);

                    byte[] bytes = new byte[BUFF_SIZE];
                    int length;
                    while ((length = fInStrm.read(bytes)) >= 0) {
                        zipOut.write(bytes, 0, length);
                    }
                    fInStrm.close();
                } else if (sourcePath.relativize(fx).toString().length() > 0) {
                    ZipEntry zipEntry = new ZipEntry(sourcePath.relativize(fx).toString() + "/"); //File.separator);
                    zipOut.putNextEntry(zipEntry);
                }
            }
            zipOut.close();
            fOutStrm.close();

            return Long.valueOf(Files.size(outputFile.toPath())).intValue();
        } catch (IOException e) {
            System.out.println("Exception:  " + e.getMessage());
        }
        return 0;


    }

    public void decompress(String filename, String dir) {

        if (dir == null || dir.length() == 0) return;
        if (filename == null || filename.length() == 0) return;
        Path destPath = Paths.get(dir);
        if (!Files.isDirectory(destPath)) return;
        File inputFile = new File(filename);
        if (!Files.exists(inputFile.toPath())) return;

        try {
            byte[] buffer = new byte[BUFF_SIZE];
            ZipInputStream zipInStrm = new ZipInputStream(new FileInputStream(inputFile));
            ZipEntry zipEntry = zipInStrm.getNextEntry();
            while (zipEntry != null) {
                System.out.println(zipEntry.getName());

                File fileToCreate = new File(destPath.toString(), zipEntry.getName());
                if (zipEntry.isDirectory()) {
                    try {
                        fileToCreate.mkdirs();
                    }
                    catch (SecurityException ex)
                    {
                        System.out.println("Issues with creating directory");
                    }
                } else {
                    FileOutputStream fileOutStrm = new FileOutputStream(fileToCreate);
                    int length;
                    while ((length = zipInStrm.read(buffer)) > 0) {
                        fileOutStrm.write(buffer, 0, length);
                    }
                    fileOutStrm.close();
                }

                zipEntry = zipInStrm.getNextEntry();
            }

            zipInStrm.closeEntry();
            zipInStrm.close();
        } catch (IOException e) {
            System.out.println("Exception:  " + e.getMessage());
        }
    }
}