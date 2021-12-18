package win233.demo.lock.fish;

public class Fish {
    public int count;
    public boolean note =false;

    /**
     * 0 无 1 tom 2 alice
     */
    public String noteFlag = "无";

    public void leaveNoteTom() {
        noteFlag = "通知tom";
    }
    public void leaveNoteAlice() {
        noteFlag = "通知alice";
    }

    /**
     * 是否没通知tom
     * @return
     */
    public boolean noNoteTom() {
        return noteFlag.equals("通知tom");
    }

    /**
     * 是否没通知alice
     * @return
     */
    public boolean noNoteAlice() {
        return noteFlag.equals("通知alice");
    }

    public void leaveANote() {
        note=true;
    }
    public void removeNote() {
        note=false;
    }

    public void feed() {
        count++;
    }


    public boolean noFeed() {
        if(count==0) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isLive() {
        return count==1;
    }

    public boolean isHungry() {
        return count==0;
    }
    @Override
    public String toString() {
        if(count==1) {
            return "Live";
        } else if(count == 0){
            return "Hungry";
        } else if(count > 1) {
            return "Dead";
        }
        return "Unknown";
    }

    public boolean isDead() {
        return count>1;
    }
}
