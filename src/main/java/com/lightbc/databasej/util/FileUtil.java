package com.lightbc.databasej.util;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;

/**
 * 文件处理工具类
 */
public class FileUtil {

    /**
     * 根据路径创建文件夹/文件
     *
     * @param path 路径信息
     * @return boolean TRUE-成功，FALSE-失败
     */
    public boolean create(String path) {
        boolean flag = false;
        try {
            if (path != null && !"".equals(path.trim())) {
                // 判断是否是文件是否保存在当前盘的根目录下面
                int count = StringUtils.getCharCount(path, '/');
                // 获取文件分隔符最后一次出现的位置
                int lastIndex = path.lastIndexOf("/");
                // 处理保存在文件夹下的文件创建
                if (count > 1) {
                    // 判断文件存放的上级文件目录是否存在，不存在则创建文件夹（多级判断）
                    String dirPath = path.substring(0, lastIndex);
                    File dirFile = new File(dirPath);
                    if (!dirFile.exists()) {
                        dirFile.mkdirs();
                    }
                    // 判断文件是否存在，不存在则创建文件
                    if (path.indexOf(".") != -1) {
                        File file = new File(path);
                        if (!file.exists()) {
                            file.createNewFile();
                        }
                    }
                } else {
                    // 处理保存在当前盘的根目录下的保存文件
                    File file = new File(path);
                    if (!file.exists()) {
                        file.createNewFile();
                    }
                }
            }
            flag = true;
        } catch (IOException e) {
        } finally {
            return flag;
        }
    }

    /**
     * 根据路径判断是否是有效的文件路径
     *
     * @param path 路径
     * @return Boolean TRUE-是文件，FALSE-不是文件
     */
    public boolean isFile(String path) {
        File file = new File(path);
        if (file.exists() && file.isFile()) {
            return true;
        }
        return false;
    }

    /**
     * 文件写入
     *
     * @param path    写入文件路径
     * @param content 写入内容
     * @return Boolean TRUE-写入成功，FALSE-写入失败
     */
    public boolean write(String path, String content) {
        boolean flag = false;
        if (isFile(path)) {
            File file = new File(path);
            FileOutputStream fos = null;
            PrintStream ps = null;
            try {
                fos = new FileOutputStream(file);
                ps = new PrintStream(fos, true, "UTF-8");
                ps.append(content);
                flag = true;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } finally {
                if (ps != null) {
                    ps.close();
                }
                if (fos != null) {
                    try {
                        fos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return flag;
    }

    /**
     * 文件读取
     *
     * @param path 读取文件路径
     * @return string 读取到的文件内容
     */
    public String read(String path) {
        if (isFile(path)) {
            File file = new File(path);
            FileInputStream fis = null;
            InputStreamReader isr = null;
            BufferedReader br = null;
            StringBuilder builder = new StringBuilder();
            try {
                fis = new FileInputStream(file);
                isr = new InputStreamReader(fis, "UTF-8");
                br = new BufferedReader(isr);
                String line;
                while ((line = br.readLine()) != null) {
                    builder.append(line).append("\n");
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (br != null) {
                        br.close();
                    }
                    if (isr != null) {
                        isr.close();
                    }
                    if (fis != null) {
                        fis.close();
                    }
                } catch (IOException e) {

                }
                return builder.toString();
            }
        }
        return null;
    }

    /**
     * 加载需要进行缓存的文件内容
     *
     * @param path 资源路径
     * @return string 缓存内容
     */
    public String loadCacheContent(String path) {
        StringBuilder builder = new StringBuilder();
        InputStream is = null;
        InputStreamReader isr = null;
        BufferedReader reader = null;
        try {
            is = FileUtil.class.getResourceAsStream(path);
            isr = new InputStreamReader(is, "UTF-8");
            reader = new BufferedReader(isr);
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line).append("\n");
            }
        } catch (IOException e) {
        } catch (Exception e) {
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
                if (isr != null) {
                    isr.close();
                }
                if (is != null) {
                    is.close();
                }
            } catch (IOException e) {
            }
            return builder.toString();
        }
    }

    /**
     * 缓存文件
     *
     * @param oPath     需要进行缓存的资源路径
     * @param cachePath 缓存路径
     */
    public void cacheFile(String oPath, String cachePath) {
        InputStream is = null;
        BufferedInputStream bis = null;
        FileOutputStream fos = null;
        try {
            create(cachePath);
            is = FileUtil.class.getResourceAsStream(oPath);
            bis = new BufferedInputStream(is);
            fos = new FileOutputStream(cachePath);
            byte[] buffer = new byte[1024];
            int len;
            while ((len = bis.read(buffer, 0, 1024)) > 0) {
                fos.write(buffer, 0, len);
            }
            fos.flush();
        } catch (FileNotFoundException e) {
        } catch (IOException e) {
        } finally {
            try {
                if (fos != null) {
                    fos.close();
                }
                if (bis != null) {
                    bis.close();
                }
                if (is != null) {
                    is.close();
                }
            } catch (IOException e) {
            }
        }
    }

    /**
     * 根据输入流读取内容
     *
     * @param is 输入流
     * @return string 读取内容
     */
    public String read(InputStream is) {
        StringBuilder builder = new StringBuilder();
        InputStreamReader isr = null;
        BufferedReader br = null;
        try {
            isr = new InputStreamReader(is, "UTF-8");
            br = new BufferedReader(isr);
            String line;
            while ((line = br.readLine()) != null) {
                builder.append(line).append("\n");
            }
        } catch (IOException e) {
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
                if (isr != null) {
                    isr.close();
                }
                if (is != null) {
                    is.close();
                }
            } catch (IOException e) {
            }
            return builder.toString();
        }
    }

    /**
     * 根据输出流写入内容
     *
     * @param os      输出流
     * @param content 写入内容
     */
    public void write(OutputStream os, String content) {
        OutputStreamWriter osw = null;
        BufferedWriter bw = null;
        try {
            osw = new OutputStreamWriter(os, "UTF-8");
            bw = new BufferedWriter(osw);
            bw.write(content);
            bw.flush();
            osw.flush();
            os.flush();
        } catch (UnsupportedEncodingException e) {
        } catch (IOException e) {
        } finally {
            try {
                if (bw != null) {
                    bw.close();
                }
                if (osw != null) {
                    osw.close();
                }
                if (os != null) {
                    os.close();
                }
            } catch (IOException e) {
            }
        }
    }

    /**
     * 获取图片对象
     *
     * @param relativePath 资源相对路径
     * @return BufferedImage
     * @throws IOException
     */
    public BufferedImage getImage(String relativePath) throws IOException {
        return ImageIO.read(FileUtil.class.getResourceAsStream(relativePath));
    }

}
