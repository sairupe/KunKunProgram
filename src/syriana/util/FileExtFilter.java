/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package syriana.util;

import java.io.File;
import java.io.FilenameFilter;

/**
 *
 * @author Gust
 */
public class FileExtFilter implements FilenameFilter {
    //文件过滤器
    private String ext = null; //用于限定文件扩展名 ,比如 ".dat"

    public FileExtFilter(String s) {
        ext = s;
    }

    public boolean accept(File dir, String fname) {
        if (ext == null) {
            return true;
        }
        return fname.toLowerCase().endsWith(ext);
    }
}
