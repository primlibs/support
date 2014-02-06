/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.prim.support;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author кот
 */
public class FileSearch {

    private EnumFileSearch result = EnumFileSearch.notRun;
    private String filePath = "";

    private FileSearch() {
    }

    public static FileSearch findInDir(String pathToStartDir, String fileName) throws Exception {
        FileSearch res = new FileSearch();
        if (MyString.NotNull(pathToStartDir)) {
            if (MyString.NotNull(fileName)) {
                File fl = new File(pathToStartDir);
                if (fl.exists()) {
                    if (fl.isDirectory()) {
                        res.result= EnumFileSearch.fileNotFound;
                        String list[] = fl.list();
                        if (list != null) {
                            for (int i = 0; i < list.length; i++) {
                                File fl1 = new File(pathToStartDir + "/" + list[i]);
                                if (fileName.equals(list[i])) {
                                    res.filePath = pathToStartDir + "/" + list[i];
                                    res.result = EnumFileSearch.success;
                                    return res;
                                } else if(fl1.isDirectory()){
                                    FileSearch fs= FileSearch.findInDir(pathToStartDir + "/" + list[i], fileName);
                                    if(fs.result.equals(EnumFileSearch.success)){
                                        return fs;
                                    }
                                }
                            }
                        }
                    }else{
                        res.result= EnumFileSearch.startIsNotDirectory;
                    }
                } else {
                    res.result = EnumFileSearch.notExistStartDir;
                }
            } else {
                res.result = EnumFileSearch.noGetFileName;
            }
        } else {
            res.result = EnumFileSearch.noGetStartDir;
        }
        return res;
    }

    public EnumFileSearch getResult() {
        return result;
    }

    public String getFilePath() {
        if (filePath == null) {
            filePath = "";
        }
        return filePath;
    }
}
