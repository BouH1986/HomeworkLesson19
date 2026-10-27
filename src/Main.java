import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class Main {
    public static void main(String[] args) {
        StringBuilder log = new StringBuilder();
        String parentFolder = "D:\\Programs\\Java\\Games\\";
        String[][] folderTree = {
                {"src", "main", "Main.java", "Utils.java"},
                {"res", "drawables", "vectors", "icons"},
                {"savegames"},
                {"temp", "temp.txt"}
        };
        StringBuilder pathTmp = new StringBuilder();
        for (int i = 0; i < folderTree.length; i++) {
            for (int j = 0; j < folderTree[i].length; j++) {
                try {
                    if (folderTree[i][j].contains(".")) {
                        File file = new File(parentFolder + pathTmp, folderTree[i][j]);
                        if (file.createNewFile()) {
                            log.append(java.time.LocalDateTime.now()).
                                    append(" - create: ").append(parentFolder).
                                    append(pathTmp).append(folderTree[i][j]).append("\n");
                        } else {
                            log.append(java.time.LocalDateTime.now()).
                                    append(" - not create: ").append(parentFolder).
                                    append(pathTmp).append(folderTree[i][j]).append("\n");
                        }
                    } else {
                        File file = new File(parentFolder + pathTmp, folderTree[i][j]);
                        if (file.mkdir()) {
                            log.append(java.time.LocalDateTime.now()).
                                    append(" - create: ").append(parentFolder).
                                    append(pathTmp).append(folderTree[i][j]).append("\n");
                        } else {
                            log.append(java.time.LocalDateTime.now()).
                                    append(" - not create: ").append(parentFolder).
                                    append(pathTmp).append(folderTree[i][j]).append("\n");
                        }
                        if (i == 1 && j > 0) {
                            pathTmp = new StringBuilder("res\\");
                        } else {
                            pathTmp.append(folderTree[i][j]).append("\\");
                        }
                    }
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
            pathTmp = new StringBuilder();
        }
        try (FileWriter writer =
                     new FileWriter(parentFolder + "temp\\temp.txt",
                             StandardCharsets.UTF_8)) {
            writer.write(log.toString());
            writer.flush();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        //------------------------------------------------------
        parentFolder += "savegames\\";
        GameProgress[] gameProgresses = {
                new GameProgress(95, 11, 3, 255.33),
                new GameProgress(96, 12, 4, 256.34),
                new GameProgress(97, 13, 5, 257.35)
        };
        List<String> pathList = new ArrayList<>();
        for (int i = 0; i < gameProgresses.length; i++) {
            String s = parentFolder + "save" + (i + 1) + ".dat";
            pathList.add(s);
            saveGame(s, gameProgresses[i]);
        }

        parentFolder += "zip.zip";
        zipFiles(parentFolder, pathList);

    }

    public static void saveGame(String filePath, GameProgress gameProgress) {
        try (FileOutputStream fos = new FileOutputStream(filePath);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(gameProgress);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static void zipFiles(String filePath, List<String> pathList) {
        try (
                FileOutputStream fos = new FileOutputStream(filePath);
                ZipOutputStream zos = new ZipOutputStream(fos)
        ) {
            for (String fileName : pathList) {
                File file = new File(fileName);
                try (FileInputStream fis = new FileInputStream(file)) {
                    ZipEntry entry = new ZipEntry(file.getName());
                    zos.putNextEntry(entry);

                    byte[] buffer = new byte[8192];
                    int len;
                    while ((len = fis.read(buffer)) != -1) {
                        zos.write(buffer, 0, len);
                    }

                    zos.closeEntry();
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        for (String name : pathList) {
            File fl = new File(name);
            fl.delete();
        }
    }
}
