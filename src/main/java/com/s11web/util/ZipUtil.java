package com.s11web.util;

import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.apache.xpath.SourceTree;

import java.io.*;
import java.util.List;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipUtil {

    private static final Logger log = Logger.getLogger(ZipUtil.class);

    public static String compress(String str) throws IOException {
        if (str == null || str.length() == 0) {
            return str;
        }

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        GZIPOutputStream gzip = new GZIPOutputStream(out);
        gzip.write(str.getBytes());
        gzip.close();

        return out.toString("ISO-8859-1");
    }

    public static String unCompress(String str) throws IOException {
        if (str == null || str.length() == 0) {
            return str;
        }

        try(ByteArrayOutputStream out = new ByteArrayOutputStream();
            GZIPInputStream gzipInputStream = new GZIPInputStream(
                    new ByteArrayInputStream(str.getBytes("ISO-8859-1")))) {
            IOUtils.copy(gzipInputStream, out);
            return  out.toString("UTF-8");
        }
    }

    public static void ZipMultiFile(List<InputStream> fileInputStreamList,
                                    List<String> fileNameList,
                                    OutputStream os) throws IOException {
        try (ZipOutputStream zipOut = new ZipOutputStream(os)) {
            if (fileInputStreamList.size() != fileNameList.size()) {
                log.error("文件流列表和文件名列表长度不一致!");
                return;
            }

            String fileName;
            for (int i = 0; i < fileNameList.size(); ++i) {
                try (InputStream inputStream = fileInputStreamList.get(i)) {
                    fileName = fileNameList.get(i);
                    zipOut.putNextEntry(new ZipEntry(fileName));
                    IOUtils.copy(inputStream, zipOut);
                }
            }
        }
    }

}
