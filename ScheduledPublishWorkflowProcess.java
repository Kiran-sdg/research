package com.demo.core.workflow;

import java.util.Calendar;

import javax.jcr.Node;
import javax.jcr.Property;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.granite.workflow.WorkflowException;
import com.adobe.granite.workflow.WorkflowSession;
import com.adobe.granite.workflow.exec.WorkItem;
import com.adobe.granite.workflow.exec.WorkflowProcess;
import com.adobe.granite.workflow.metadata.MetaDataMap;
import com.day.cq.commons.jcr.JcrConstants;

/**
 * Workflow process that reads the scheduledPublishDate from payload properties,
 * calculates the difference with current date, and sets the difference in milliseconds
 * as timeout for the Approve task and publish process.
 */
@Component(service = WorkflowProcess.class, property = {
        "process.label=Schedule Publish Process",
        "process.description=Sets timeout for publish process based on scheduledPublishDate"
})
public class ScheduledPublishWorkflowProcess implements WorkflowProcess {

    private static final Logger LOG = LoggerFactory.getLogger(ScheduledPublishWorkflowProcess.class);
    private static final String SCHEDULED_PUBLISH_DATE_PROPERTY = "scheduledPublishDate";
    private static final String TIMEOUT_PROPERTY = "absoluteTime";
    private static final String APPROVE_TASK_TITLE = "Wait";

    @Override
    public void execute(WorkItem workItem, WorkflowSession workflowSession, MetaDataMap metaDataMap) throws WorkflowException {
        LOG.trace("Inside ScheduledPublishWorkflowProcess:execute method \nworkItem = {} \nworkflowSession = {} \nmetaDataMap = {}",
                workItem, workflowSession, metaDataMap);

        try {
            // Get the payload path
            String payloadPath = workItem.getWorkflowData().getPayload().toString();
            LOG.debug("Payload path: {}", payloadPath);

            // Get JCR session
            Session session = workflowSession.adaptTo(Session.class);
            if (session == null) {
                LOG.error("Could not adapt workflow session to JCR session");
                return;
            }

            // Get the content node
            String contentPath = payloadPath + "/" + JcrConstants.JCR_CONTENT;
            if (!session.nodeExists(contentPath)) {
                LOG.error("Content node does not exist at path: {}", contentPath);
                return;
            }

            Node contentNode = session.getNode(contentPath);

            // Check if scheduledPublishDate property exists
            if (!contentNode.hasProperty(SCHEDULED_PUBLISH_DATE_PROPERTY)) {
            	setApproveTaskTimeout(workItem, Calendar.getInstance());
                LOG.warn("No scheduledPublishDate property found on node: {}", contentPath);
                return;
            }

            // Get the scheduled publish date
            Property scheduledPublishDateProp = contentNode.getProperty(SCHEDULED_PUBLISH_DATE_PROPERTY);
            Calendar scheduledPublishDate = scheduledPublishDateProp.getDate();
            LOG.debug("Got the publish date {}", scheduledPublishDate.getTimeInMillis());
            setApproveTaskTimeout(workItem, scheduledPublishDate);
            /*Date scheduledDate = scheduledPublishDate.getTime();
            
            // Get current date
            Date currentDate = new Date();
            
            // Calculate difference in milliseconds
            long differenceInMillis = scheduledDate.getTime() - currentDate.getTime();
            
            // If the scheduled date is in the past, set timeout to 0 (immediate execution)
            if (differenceInMillis < 0) {
                LOG.info("Scheduled publish date is in the past. Setting timeout to 0.");
                differenceInMillis = 0;
            }
            
            LOG.info("Setting timeout to {} milliseconds for payload: {}", differenceInMillis, payloadPath);
            
            // Set the timeout in the workflow metadata
            workItem.getWorkflow().getMetaDataMap().put(TIMEOUT_PROPERTY, differenceInMillis);*/
            
            // Find and set timeout for the Approve task
//            setApproveTaskTimeout(workflowSession, workItem.getWorkflow(), scheduledPublishDate);
            
            LOG.debug("Successfully set timeout for publish process and Approve task");
            
        } catch (RepositoryException e) {
            LOG.error("Repository exception while processing workflow", e);
            throw new WorkflowException("Error processing workflow", e);
        }
    }
    
    /**
     * Finds the "Approve" task in the workflow and sets its timeout
     * 
     * @param workflowSession The workflow session
     * @param workflow The current workflow
     * @param timeout The timeout value in milliseconds
     * @throws WorkflowException If an error occurs while setting the timeout
     */
    private void setApproveTaskTimeout(WorkItem workItem, Calendar timeout) throws WorkflowException {
        LOG.trace("Inside setApproveTaskTimeout method \nworkflow = {} \ntimeout = {}", workItem, timeout);
        
        try {
//        	workItem.getMetaDataMap().put(TIMEOUT_PROPERTY, timeout);
//        	workItem.getWorkflowData().getMetaDataMap().put(TIMEOUT_PROPERTY, timeout);
        	workItem.getWorkflow().getWorkflowData().getMetaDataMap().put(TIMEOUT_PROPERTY, timeout);
        } catch (Exception e) {
            LOG.error("Error setting timeout for Approve task", e);
            throw new WorkflowException("Error setting timeout for Approve task", e);
        }
    }
}
