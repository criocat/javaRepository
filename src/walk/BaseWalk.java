package info.kgeorgiy.ja.razinkov.walk;

import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.EnumSet;
import java.util.HexFormat;

public class BaseWalk {
    private static void writeHash(BufferedWriter out, byte[] hash, String fileName, int outLength) throws IOException {
        if (hash == null) {
            out.write(String.format("%0" + outLength + "x %s", 0, fileName));
        } else {
            out.write(HexFormat.of().formatHex(hash).substring(0, outLength));
            out.write(String.format(" %s", fileName));
        }
        out.newLine();
    }

    private static SimpleFileVisitor<Path> getFileVisitor(byte[] buffer, MessageDigest digst, BufferedWriter out, int outLength) {
        return new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                boolean success = true;
                // :NOTE: BufferedInputStream
                try (InputStream is = Files.newInputStream(file)) {
                    while (true) {
                        int count = is.read(buffer, 0, buffer.length);
                        if (count == -1) break;
                        digst.update(buffer, 0, count);
                    }
                } catch (IOException e) {
                    success = false;
                }
                byte[] result = null;
                if (success) {
                    result = digst.digest();
                }
                digst.reset();
                writeHash(out, result, file.toString(), outLength);
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
                writeHash(out, null, file.toString(), outLength);
                return FileVisitResult.CONTINUE;
            }
        };
    }

    public static void doWalk(String[] args, int maxDepth) {
        // :NOTE: write correct usage
        if (args == null || args.length < 2 || args.length > 3) {
            System.err.println("Invalid number of arguments");
            return;
        }
        if (args[0] == null || args[1] == null || (args.length == 3 && args[2] == null)) {
            System.err.println("null arguments");
            return;
        }

        try {
            Path parent = Paths.get(args[1]).getParent();
            if (parent != null) {
                Files.createDirectories(parent);
            }
        } catch (IOException | InvalidPathException e) {
            // :NOTE: return message
            System.err.println("cannot create output directory");
            return;
        }
        final int outLength;
        if (args.length == 3 && args[2].equals("md5")) {
            outLength = 32;
        } else {
            outLength = 16;
        }
        byte[] buffer = new byte[1024];
        try (BufferedReader fileList = Files.newBufferedReader(Paths.get(args[0]));
             BufferedWriter out = Files.newBufferedWriter(Paths.get(args[1]))) {
            final MessageDigest digst;
            if (args.length == 2) {
                digst = MessageDigest.getInstance("SHA-256");
            } else {
                digst = MessageDigest.getInstance(args[2]);
            }
            for (String fileName = fileList.readLine(); fileName != null; fileName = fileList.readLine()) {
                Path start;
                try {
                    start = Paths.get(fileName);
                } catch (InvalidPathException e) {
                    writeHash(out, null, fileName, outLength);
                    continue;
                }
                Files.walkFileTree(start, EnumSet.noneOf(FileVisitOption.class), maxDepth, getFileVisitor(buffer, digst, out, outLength));
            }
            // :NOTE: do not ignore messages
        } catch (FileNotFoundException e) {
            System.err.println("File not found");
        } catch (IOException e) {
            System.err.println("IO exception has occurred");
        } catch (NoSuchAlgorithmException e) {
            System.err.println("SHA-256 algorithm isn't available");
        } catch (InvalidPathException e) {
            System.err.println("Invalid input path");
        }
    }
}
