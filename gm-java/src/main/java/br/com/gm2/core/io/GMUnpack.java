package br.com.gm2.core.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import br.com.gm2.core.content.Errors;
import br.com.gm2.core.element.Crumb;
import br.com.gm2.core.element.GMFileFormat;
import br.com.gm2.core.element.GlobalHeader;

public class GMUnpack {

    public void unCrumbIt(String file) {
        File src = new File(file);
        File dest = new File(src.getParent() + File.separator + extractFileType(src));
        try (InputStream is = new FileInputStream(src); OutputStream os = new FileOutputStream(dest)) {
            byte[] headerbuf = new byte[GlobalHeader.globalHeaderSize];
            is.read(headerbuf, 0, GlobalHeader.globalHeaderSize);
            GlobalHeader header = GlobalHeader.readBytes(headerbuf);
            byte[] readBuffer = new byte[Crumb.crumbSize];
            while ((is.read(readBuffer)) != -1) {
                byte[] writeBuffer = new Crumb(readBuffer).getBytes();
                os.write(writeBuffer, 0, writeBuffer.length);
            }
        } catch (Exception e) {
            System.err.println(Errors.ERROR_3.toString());
        }
    }

    public String extractFileType(File src) {
        return System.currentTimeMillis() + "-" + src.getName().replaceAll(GMFileFormat.gm2, "");

    }

}
