package chessGame.Util;

import java.util.Timer;
import java.util.TimerTask;

public class TimerUtil {

    private Timer gameTimer;
    private TimerTask task;
    private Runnable onTick;
    private Runnable onTimerComplete;
    private int countdown;

    // Constructor initializing the timer with the countdown time, tick action, and
    // completion action.
    public TimerUtil(int countdown, Runnable onTick, Runnable onTimerComplete) {
        this.countdown = countdown;
        this.onTick = onTick;
        this.onTimerComplete = onTimerComplete;
    }

    // Starts or restarts the timer.
    public void startTimer() {
        cancelTimer(); // Ensures any existing timer is cancelled before starting a new one.
        gameTimer = new Timer();
        task = new TimerTask() {
            @Override
            public void run() {
                countdown--;
                onTick.run();

                if (countdown <= 0) {
                    cancelTimer();
                    // Executes completion action when countdown reaches zero.
                    onTimerComplete.run();
                }
            }
        };
        gameTimer.scheduleAtFixedRate(task, 1000, 1000); // every 1 second excute one time
    }

    // Resets the timer with a new countdown value.
    public void resetTimer(int newCountdown) {
        this.countdown = newCountdown;
        startTimer(); // Restart Timer
    }

    // Cancels the current timer and cleans up resources.
    public void cancelTimer() {
        if (gameTimer != null) {
            gameTimer.cancel();
            gameTimer.purge();
            gameTimer = null;
        }
        if (task != null) {
            task.cancel();
            task = null;
        }
    }

    // Returns the current countdown time.
    public int getCurrentCountdown() {
        return countdown;
    }
}
