package mybatisclone.models;

public class Job {
    public int jobId;
    public String jobTitle;
    public double minSalary;
    public double maxSalary;

    public Job(){}
    public Job(int jobId, String jobTitle, double minSalary, double maxSalary) {
        this.jobId = jobId;
        this.jobTitle = jobTitle;
        this.minSalary = minSalary;
        this.maxSalary = maxSalary;
    }

    @Override
    public String toString() {
        return "Job{" +
                "jobId=" + jobId +
                ", jobTitle='" + jobTitle + '\'' +
                ", minSalary=" + minSalary +
                ", maxSalary=" + maxSalary +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Job job)) return false;

        return jobId == job.jobId;
    }

    @Override
    public int hashCode() {
        return jobId;
    }
}
