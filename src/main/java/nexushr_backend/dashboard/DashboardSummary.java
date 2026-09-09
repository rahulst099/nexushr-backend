package nexushr_backend.dashboard;

public class DashboardSummary {

    private long totalEmployees;
    private long activeEmployees;
    private long totalLeaves;
    private long pendingLeaves;
    private long approvedLeaves;
    private long rejectedLeaves;
    private long totalPayroll;
    private long pendingPayroll;
    private long paidPayroll;

    public DashboardSummary() {
    }

    public long getTotalEmployees() {
        return totalEmployees;
    }

    public void setTotalEmployees(long totalEmployees) {
        this.totalEmployees = totalEmployees;
    }

    public long getActiveEmployees() {
        return activeEmployees;
    }

    public void setActiveEmployees(long activeEmployees) {
        this.activeEmployees = activeEmployees;
    }

    public long getTotalLeaves() {
        return totalLeaves;
    }

    public void setTotalLeaves(long totalLeaves) {
        this.totalLeaves = totalLeaves;
    }

    public long getPendingLeaves() {
        return pendingLeaves;
    }

    public void setPendingLeaves(long pendingLeaves) {
        this.pendingLeaves = pendingLeaves;
    }

    public long getApprovedLeaves() {
        return approvedLeaves;
    }

    public void setApprovedLeaves(long approvedLeaves) {
        this.approvedLeaves = approvedLeaves;
    }

    public long getRejectedLeaves() {
        return rejectedLeaves;
    }

    public void setRejectedLeaves(long rejectedLeaves) {
        this.rejectedLeaves = rejectedLeaves;
    }

    public long getTotalPayroll() {
        return totalPayroll;
    }

    public void setTotalPayroll(long totalPayroll) {
        this.totalPayroll = totalPayroll;
    }

    public long getPendingPayroll() {
        return pendingPayroll;
    }

    public void setPendingPayroll(long pendingPayroll) {
        this.pendingPayroll = pendingPayroll;
    }

    public long getPaidPayroll() {
        return paidPayroll;
    }

    public void setPaidPayroll(long paidPayroll) {
        this.paidPayroll = paidPayroll;
    }
}