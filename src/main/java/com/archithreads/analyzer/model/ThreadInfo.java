package com.archithreads.analyzer.model;

import java.util.List;
import java.util.ArrayList;

public class ThreadInfo {
    private String threadName;
    private String threadId;
    private String threadState;
    private String priority;
    private String daemon;
    private String osPriority;
    private String tid;
    private String nid;
    private String nativeId;
    private String javaThreadId;
    private String stackTrace;
    private List<String> stackFrames;
    private String lockInfo;
    private String lockOwner;
    private String lockOwnerId;
    private boolean isBlocked;
    private boolean isWaiting;
    private boolean isParked;
    private String waitingOn;
    private String parkingToWaitFor;
    private long blockedTime;
    private long waitedTime;
    private String javaLangThreadState;
    private List<String> lockedMonitors;
    private List<String> lockedSynchronizers;

    public ThreadInfo() {
        this.stackFrames = new ArrayList<>();
        this.lockedMonitors = new ArrayList<>();
        this.lockedSynchronizers = new ArrayList<>();
    }

    // Getters and Setters
    public String getThreadName() { return threadName; }
    public void setThreadName(String threadName) { this.threadName = threadName; }

    public String getThreadId() { return threadId; }
    public void setThreadId(String threadId) { this.threadId = threadId; }

    public String getThreadState() { return threadState; }
    public void setThreadState(String threadState) { this.threadState = threadState; }

    public String getPriority() { return priority; }
    public void setPriority(String priority) { this.priority = priority; }

    public String getDaemon() { return daemon; }
    public void setDaemon(String daemon) { this.daemon = daemon; }

    public String getOsPriority() { return osPriority; }
    public void setOsPriority(String osPriority) { this.osPriority = osPriority; }

    public String getTid() { return tid; }
    public void setTid(String tid) { this.tid = tid; }

    public String getNid() { return nid; }
    public void setNid(String nid) { this.nid = nid; }

    public String getNativeId() { return nativeId; }
    public void setNativeId(String nativeId) { this.nativeId = nativeId; }

    public String getJavaThreadId() { return javaThreadId; }
    public void setJavaThreadId(String javaThreadId) { this.javaThreadId = javaThreadId; }

    public String getStackTrace() { return stackTrace; }
    public void setStackTrace(String stackTrace) { this.stackTrace = stackTrace; }

    public List<String> getStackFrames() { return stackFrames; }
    public void setStackFrames(List<String> stackFrames) { this.stackFrames = stackFrames; }

    public String getLockInfo() { return lockInfo; }
    public void setLockInfo(String lockInfo) { this.lockInfo = lockInfo; }

    public String getLockOwner() { return lockOwner; }
    public void setLockOwner(String lockOwner) { this.lockOwner = lockOwner; }

    public String getLockOwnerId() { return lockOwnerId; }
    public void setLockOwnerId(String lockOwnerId) { this.lockOwnerId = lockOwnerId; }

    public boolean isBlocked() { return isBlocked; }
    public void setBlocked(boolean blocked) { isBlocked = blocked; }

    public boolean isWaiting() { return isWaiting; }
    public void setWaiting(boolean waiting) { isWaiting = waiting; }

    public boolean isParked() { return isParked; }
    public void setParked(boolean parked) { isParked = parked; }

    public String getWaitingOn() { return waitingOn; }
    public void setWaitingOn(String waitingOn) { this.waitingOn = waitingOn; }

    public String getParkingToWaitFor() { return parkingToWaitFor; }
    public void setParkingToWaitFor(String parkingToWaitFor) { this.parkingToWaitFor = parkingToWaitFor; }

    public long getBlockedTime() { return blockedTime; }
    public void setBlockedTime(long blockedTime) { this.blockedTime = blockedTime; }

    public long getWaitedTime() { return waitedTime; }
    public void setWaitedTime(long waitedTime) { this.waitedTime = waitedTime; }

    public String getJavaLangThreadState() { return javaLangThreadState; }
    public void setJavaLangThreadState(String javaLangThreadState) { this.javaLangThreadState = javaLangThreadState; }

    public List<String> getLockedMonitors() { return lockedMonitors; }
    public void setLockedMonitors(List<String> lockedMonitors) { this.lockedMonitors = lockedMonitors; }

    public List<String> getLockedSynchronizers() { return lockedSynchronizers; }
    public void setLockedSynchronizers(List<String> lockedSynchronizers) { this.lockedSynchronizers = lockedSynchronizers; }
}
