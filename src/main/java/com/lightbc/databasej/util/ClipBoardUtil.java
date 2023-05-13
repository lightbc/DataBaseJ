package com.lightbc.databasej.util;

import org.jetbrains.annotations.NotNull;

import java.awt.datatransfer.*;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * 剪贴板工具类，可以复制文件到剪贴板中
 */
public class ClipBoardUtil implements Transferable, ClipboardOwner {
    public DataFlavor[] flavors = new DataFlavor[]{DataFlavor.javaFileListFlavor};
    private List<File> files;

    public ClipBoardUtil(List<File> files) {
        this.files = files;
    }

    @Override
    public void lostOwnership(Clipboard clipboard, Transferable contents) {

    }

    @Override
    public DataFlavor[] getTransferDataFlavors() {
        return flavors;
    }

    @Override
    public boolean isDataFlavorSupported(DataFlavor flavor) {
        if (flavors[0].equals(flavor)) {
            return true;
        }
        return false;
    }

    @NotNull
    @Override
    public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
        return files;
    }
}
