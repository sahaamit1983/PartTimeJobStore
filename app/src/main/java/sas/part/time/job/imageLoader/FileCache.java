package sas.part.time.job.imageLoader;

import java.io.File;

import sas.part.time.job.utils.PartTimeUtils;
import android.content.Context;
import android.webkit.MimeTypeMap;
import android.webkit.URLUtil;

public class FileCache {
    
    private File cacheDir;
    private PartTimeUtils appConfig = PartTimeUtils.getSingleInstance();
    
    public FileCache(Context context){
        //Find the dir to save cached images
       	if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED))
            cacheDir=new File(appConfig.getDirectory(context)+"Lazyloading");
        else
            cacheDir=context.getCacheDir();
       
        if(!cacheDir.exists())
            cacheDir.mkdirs();
    }
    
    public File getFile(String url){
        //I identify images by hashcode. Not a perfect solution, good for the demo.
        //String filename=String.valueOf(url.hashCode());
       
        String fileExtenstion = MimeTypeMap.getFileExtensionFromUrl(url);
		String filename = URLUtil.guessFileName(url, null, fileExtenstion);
       
        //Another possible solution (thanks to grantland)
        File f = new File(cacheDir, filename);
        return f;
        
    }
    
    public void clear(){
        File[] files=cacheDir.listFiles();
        if(files==null)
            return;
        for(File f:files)
            f.delete();
    }

}