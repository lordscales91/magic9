package io.github.lordscales91.magic9.cli;

import io.github.lordscales91.magic9.core.CallbackReceiver;
import io.github.lordscales91.magic9.core.HackingProcess;
import io.github.lordscales91.magic9.core.HackingResource;
import io.github.lordscales91.magic9.domain.HackingStep;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Alternative Application entrypoint for Command line interaction and testing
 */
public class Main implements CallbackReceiver {
    
    private int totalDownloads;
    private int completedDownloads;
    
    public static void main(String[] args) {
        File hackingdir = new File("/home/user/data/staging");
        File sdcarddir = new File("/home/user/data/sdcard");
        HackingProcess proc = HackingProcess.getInstance(HackingStep.BOOT9STRAP_BROWSER, hackingdir, sdcarddir);
        List<HackingResource> resources = proc.getRequiredResources();
        Main m = new Main();
        m.totalDownloads = resources.size();
        System.out.println("Downloading resources ");
        for (HackingResource res : resources) {
            res.getWorker(m).execute();
        }
        m.waitForCompletion();
        System.out.println(" DONE!");
        System.out.println("Preparing SD");
        
        try {
            proc.process();
            System.out.println("DONE!");
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    private void waitForCompletion() {
        while(completedDownloads < totalDownloads) {
            System.out.print(".");
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void receiveData(Object data, String tag) {
        completedDownloads++;
        if(data instanceof Exception) {
            System.err.println(tag +" download failed: "+((Exception)data).getMessage());
        }
    }
}