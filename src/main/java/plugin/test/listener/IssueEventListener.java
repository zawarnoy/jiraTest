package plugin.test.listener;

import com.atlassian.event.api.EventListener;
import com.atlassian.event.api.EventPublisher;
import com.atlassian.jira.event.issue.IssueEvent;
import com.atlassian.jira.event.type.EventType;
import com.atlassian.jira.issue.Issue;
import com.atlassian.plugin.spring.scanner.annotation.imports.JiraImport;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class IssueEventListener implements InitializingBean, DisposableBean {

    @JiraImport
    private final EventPublisher publisher;

    @Autowired
    public IssueEventListener(@JiraImport EventPublisher publisher) {
        this.publisher = publisher;
    }

    /**
     * Called when the plugin has been enabled.
     * @throws Exception
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        publisher.register(this);
    }

    /**
     * Called when the plugin is being disabled or removed.
     * @throws Exception
     */
    @Override
    public void destroy() throws Exception {
        publisher.unregister(this);
    }

    @EventListener
    public void onIssueEvent(IssueEvent issueEvent) throws Exception {
        Long eventTypeId = issueEvent.getEventTypeId();
        Issue issue = issueEvent.getIssue();

        if (eventTypeId.equals(EventType.ISSUE_CREATED_ID)) {
            System.out.println("created" + issue.getDescription());
        } else if (eventTypeId.equals(EventType.ISSUE_RESOLVED_ID)) {
            System.out.println("resolved" + issue.getDescription());
        } else if (eventTypeId.equals(EventType.ISSUE_CLOSED_ID)) {
            System.out.println("closed" + issue.getDescription());
        } else if(eventTypeId.equals(EventType.ISSUE_DELETED_ID)) {
            System.out.println("deleted" + issue.getDescription());
        } else if(eventTypeId.equals(EventType.ISSUE_COMMENT_DELETED_ID)) {
            System.out.println("comment deleted" + issue.getDescription());
        }
    }

}