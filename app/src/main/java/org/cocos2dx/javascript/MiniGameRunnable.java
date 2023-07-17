package org.cocos2dx.javascript;

public class MiniGameRunnable implements Runnable {
    public final MiniGameActivity firstAct;

    public MiniGameRunnable(MiniGameActivity firstActivity) {
        this.firstAct = firstActivity;
    }

    @Override
    public void run() {
        MiniGameActivity firstAct = this.firstAct;
        double random = Math.random();
        firstAct.num1 = (int) ((random * firstAct.factor1) + firstAct.factor2);
        double random2 = Math.random();
        firstAct.num2 = (int) ((random2 * firstAct.factor1) + firstAct.factor2);
        double random3 = Math.random();
        firstAct.num3 = (int) ((random3 * firstAct.factor1) + firstAct.factor2);
        this.firstAct.dialogSlot1.setText(this.firstAct.num1 + "");
        this.firstAct.dialogSlot2.setText(this.firstAct.num2 + "");
        this.firstAct.dialogSlot3.setText(this.firstAct.num3 + "");
    }
}
