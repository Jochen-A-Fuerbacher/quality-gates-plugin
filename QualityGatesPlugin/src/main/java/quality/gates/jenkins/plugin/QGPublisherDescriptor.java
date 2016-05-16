package quality.gates.jenkins.plugin;

import hudson.Extension;
import hudson.model.AbstractProject;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.Publisher;
import hudson.util.ListBoxModel;
import net.sf.json.JSONObject;
import org.kohsuke.stapler.StaplerRequest;

import javax.inject.Inject;

@Extension
public final class QGPublisherDescriptor extends BuildStepDescriptor<Publisher> {

    @Inject
    private JobConfigurationService jobConfigurationService;

    @Inject
    private JobExecutionService jobExecutionService;

    public QGPublisherDescriptor() {
        super(QGPublisher.class);
        load();
    }

    public QGPublisherDescriptor(JobExecutionService jobExecutionService, JobConfigurationService jobConfigurationService) {
        super(QGPublisher.class);
        this.jobExecutionService = jobExecutionService;
        this.jobConfigurationService = jobConfigurationService;
    }

    public JobExecutionService getJobExecutionService() {
        return jobExecutionService;
    }

    public ListBoxModel doFillListOfGlobalConfigDataItems() {
        return jobConfigurationService.getListOfSonarInstanceNames(jobExecutionService.getGlobalConfigData());
    }

    @Override
    public boolean isApplicable(Class<? extends AbstractProject> jobType) {
        return true;
    }

    @Override
    public String getDisplayName() {
        return "Quality Gates";
    }

    @Override
    public boolean configure(StaplerRequest req, JSONObject json) throws FormException {
        save();
        return true;
    }

    @Override
    public QGPublisher newInstance(StaplerRequest req, JSONObject formData) throws QGException {
        JobConfigData firstInstanceJobConfigData = jobConfigurationService.createJobConfigData(formData, jobExecutionService.getGlobalConfigData());
        return new QGPublisher(firstInstanceJobConfigData);
    }
}
