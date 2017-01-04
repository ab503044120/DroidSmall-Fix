package com.sunfusheng.small.lib.framework.okhttp.callback;

import com.sunfusheng.small.lib.framework.okhttp.OkHttpProxy;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.Response;

public abstract class FileCallBack extends OkHttpCallBack<File> {

    private String dstFileDir; // 目标文件存储的文件夹路径
    private String dstFileName; // 目标文件存储的文件名
    private int downloadProgress = 0;

    public abstract void onProgress(int progress);

    public FileCallBack(String dstFileDir, String dstFileName) {
        this.dstFileDir = dstFileDir;
        this.dstFileName = dstFileName;
    }

    @Override
    public File parseResponse(Response response) throws Exception {
        return saveFile(response);
    }

    public File saveFile(Response response) throws IOException {
        downloadProgress = 0;
        InputStream is = null;
        byte[] buf = new byte[2048];
        int len = 0;
        FileOutputStream fos = null;
        try {
            is = response.body().byteStream();
            final long total = response.body().contentLength();
            long sum = 0;

            File dir = new File(dstFileDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            File file = new File(dir, dstFileName);
            fos = new FileOutputStream(file);
            while ((len = is.read(buf)) != -1) {
                sum += len;
                fos.write(buf, 0, len);
                final long finalSum = sum;
                OkHttpProxy.getInstance().getHandler().post(new Runnable() {
                    @Override
                    public void run() {
                        int p = (int) ((finalSum * 1.0f / total) * 100);
                        if (p > downloadProgress) {
                            downloadProgress = p;
                            onProgress(p);
                        }
                    }
                });
            }
            fos.flush();
            return file;
        } finally {
            try {
                if (is != null) is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if (fos != null) fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }


}
