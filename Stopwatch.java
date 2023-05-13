public class Stopwatch extends Thread{
    private int seconds;
    private int minutes;
    private Game game;
    public Stopwatch(Game game){
        this.game=game;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(1000); // sleep for 1 second
                if (seconds==59){
                    seconds=0;
                    minutes++;
                }else{
                    seconds++; // increment the seconds counter
                }
                setTextOnLabel();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    private void setTextOnLabel(){
        String minutesString;
        String secondsString;
        if (seconds>9)
            secondsString= String.valueOf(seconds);
        else
            secondsString="0"+String.valueOf(seconds);

        if (minutes>9)
            minutesString= String.valueOf(minutes);
        else
            minutesString="0"+String.valueOf(minutes);
        game.timerLabel.setText(minutesString+":"+secondsString);
    }

    public int getMinutes() {
        return minutes;
    }

    public int getSeconds() {
        return seconds;
    }

    public void reset() {
        seconds = 0;
        minutes=0;
    }
}
