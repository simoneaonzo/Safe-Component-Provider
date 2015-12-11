package com.uni.ailab.scp.instrument;

import brut.common.BrutException;
import org.jf.dexlib2.*;
import org.jf.dexlib2.iface.ClassDef;
import org.jf.dexlib2.iface.Method;
import org.jf.dexlib2.iface.MethodImplementation;
import org.jf.dexlib2.immutable.ImmutableClassDef;
import org.jf.dexlib2.immutable.ImmutableMethod;
import org.xml.sax.SAXException;


import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Main {

    private static IOutput out;
    private static Path origApk;
    private static Path tmpDir;
    private static Path AndroidManifestXML;

    private static class BadCommandLineException extends Exception {
        private BadCommandLineException () {
            super();
        }

        private BadCommandLineException (String message) {
            super(message);
        }
    }

    public static void main(String[] args) throws BadCommandLineException {
        origApk = Paths.get(args[0]);
        checkFileHasApkExtension(origApk);
        try {
            tmpDir = Files.createTempDirectory(Paths.get(System.getProperty("java.io.tmpdir")), "apktool_");
            String[] apktoolArgs={"d", origApk.toString(), "--no-src", "-f", "--output", tmpDir.toString()};
            brut.apktool.Main.main(apktoolArgs);
            AndroidManifestXML = Paths.get(tmpDir.toString(), "AndroidManifest.xml");
            new AndroidManifestExplorer(AndroidManifestXML);
        } catch (IOException|InterruptedException|BrutException|ParserConfigurationException|SAXException|XPathExpressionException e) {
            e.printStackTrace();
        }


    }

    private static void checkFileHasApkExtension(Path pathToFile) throws BadCommandLineException {
        String filePath = pathToFile.toString();
        if (filePath != null) {
            File file = new File(filePath);
            if (file.isDirectory()) {
                out.printf(IOutput.Level.ERROR, "This is a path of a directory '%s'. File needed.", filePath);
                throw new BadCommandLineException();
            }
            final int indexOfDot = filePath.lastIndexOf(".");
            if (indexOfDot==-1 || !filePath.substring(indexOfDot).equalsIgnoreCase(".apk")) {
                out.printf(IOutput.Level.ERROR, "The extension of the file '%s' must be '.apk'.", filePath);
                throw new BadCommandLineException();
            }
        }
    }

    private static void checkIsFolder(String folderPath) throws BadCommandLineException {
        if (folderPath != null) {
            File file = new File(folderPath);
            if (!file.isDirectory()) {
                out.printf(IOutput.Level.ERROR, "This is a path for a file '%s'. Folder needed.", folderPath);
                throw new BadCommandLineException();
            }
        }
    }

}
