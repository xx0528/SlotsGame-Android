package org.cocos2dx.javascript;

import java.util.TimerTask;

public class MiniGameTimer extends TimerTask {
    public final MiniGameActivity firstAct;

    public MiniGameTimer(MiniGameActivity firstActivity) {
        this.firstAct = firstActivity;
    }

    @Override
    public void run() {
        MiniGameActivity firstAct = this.firstAct;
        firstAct.runOnUiThread(new MiniGameRunnable(firstAct));
    }
}
