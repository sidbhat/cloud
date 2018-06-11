package com.oneapp.cloud.core

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import java.util.Properties;

import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.search.FlagTerm
import com.oneapp.cloud.time.TaskUpdate
import com.oneapp.cloud.time.Task
import javax.mail.internet.MimeMultipart
import javax.mail.Part
import javax.mail.Multipart;
import org.codehaus.groovy.grails.commons.ConfigurationHolder
import javax.mail.FetchProfile

import javax.mail.search.FromStringTerm
import javax.mail.search.SubjectTerm
import javax.mail.search.OrTerm
import javax.mail.search.SearchTerm
import javax.mail.search.SentDateTerm
import javax.mail.search.ComparisonTerm
import javax.mail.search.AndTerm
import grails.plugin.multitenant.core.util.TenantUtils

class MailRetrieveService /*implements Comparator*/ {
  /*

  This method will retrieve 20 mails from gmail in 1 Minute
 * */


  static transactional = false

  def retrieveMail() {

 
    try {

      Properties props = System.getProperties();
      props.setProperty("mail.store.protocol", "imaps")
      Session session = Session.getInstance(props, null);
      Store store = session.getStore("imaps");
      store.connect("imap.gmail.com", "${ConfigurationHolder.config.grails.mail.username}", "${ConfigurationHolder.config.grails.mail.password}");
      Folder inbox = store.getFolder("Inbox");
      inbox.open(Folder.READ_WRITE);

      try {
        showUnreadMails(inbox);
        store.close()
      } catch (IOException e) {
	  	log.error e
        println("MailRetrieveService-retrieveMail: "+e);
      }

    } catch (NoSuchProviderException e) {
      log.error e

    } catch (MessagingException e) {
      log.error e

    } catch (Exception e) {
      log.error e
    }
  }



  public void showUnreadMails(Folder inbox) {
    try {
      def content = null
      FlagTerm ft = new FlagTerm(new Flags(Flags.Flag.SEEN), false);
      SearchTerm st =
      new AndTerm(
              new SubjectTerm("Re:"), new FlagTerm(new Flags(Flags.Flag.SEEN), false));
      Message[] msg = inbox.search(st)

      println("MailRetrieveService-showUnreadMails: MAILS: " + msg.length);
      if (msg.length >= 1) {

        for (int i = (msg.length - 1); i >= (msg.length - 20); i--) {

          try {
            Message message = msg[i]
            if (!message.getContent() instanceof MimeMultipart) {

              content = message.getContent().toString()

            } else {

              MimeMultipart mimeMultipart = (MimeMultipart) message.getContent()
              Part bp = mimeMultipart.getBodyPart(1);

              content = getText(bp)

            }
            def task

            def taskName = message.getSubject().toString()
		    taskName = taskName.replaceFirst("Re:", "")
            taskName = taskName.split("#")[0]
            taskName = taskName.trim()
              TenantUtils.doWithTenant(1)  {
               task = Task.get(Long.parseLong(''+taskName))


            def from = message.getFrom()[0].toString().split("<")[1]
            
            from = from.split(">")[0]
            if (task) {
    		  content = content.replaceAll("<(.|\n)*?>", '');
              task.addComment(User.findByUsername(from), content)

              message.setFlag(Flags.Flag.DELETED, true)
            }
              }

          } catch (Exception e) {

				log.error e
          }
        }
      }
    } catch (MessagingException e) {
      	log.error e
    }

  }


  public boolean saveTaskUpdate(date, from, subject, content, taskName) {
    TaskUpdate taskUpdate = new TaskUpdate()
    Task task = Task.findById(taskName)
    // check to make sure that sender is either createdBy or assignedTo
    if ( task  && ( ( task.assignedTo?.username != null && task.assignedTo?.username == from ) || ( task.createdBy?.username != null && task.createdBy?.username == from ) ) ){
    taskUpdate.date = date
    taskUpdate.from1 = from
    taskUpdate.subject = subject
    taskUpdate.content = content
    taskUpdate.taskName = taskName
    if (!taskUpdate.save(flush: true)) {
      taskUpdate.errors.each {error ->
        println "MailRetrieveService-saveTaskUpdate: ${error}"
      }
    }
   		task.addToTaskUpdate(taskUpdate)
    	return true
    }else 
    	return false
  }


  private boolean textIsHtml = false;

  /**
   * Return the primary text content of the message.
   */
  public String getText(Part p) throws
          MessagingException, IOException {
    if (p.isMimeType("text/*")) {
      String s = (String) p.getContent();
      textIsHtml = p.isMimeType("text/html");
      return s;
    }

    if (p.isMimeType("multipart/alternative")) {
      // prefer html text over plain text
      Multipart mp = (Multipart) p.getContent();
      String text = null;
      for (int i = 0; i < mp.getCount(); i++) {
        Part bp = mp.getBodyPart(i);
        if (bp.isMimeType("text/plain")) {
          if (text == null)
            text = getText(bp);
          continue;
        } else if (bp.isMimeType("text/html")) {
          String s = getText(bp);
          if (s != null)
            return s;
        } else {
          return getText(bp);
        }
      }
      return text;
    } else if (p.isMimeType("multipart/*")) {
      Multipart mp = (Multipart) p.getContent();
      for (int i = 0; i < mp.getCount(); i++) {
        String s = getText(mp.getBodyPart(i));
        if (s != null)
          return s;
      }
    }

    return null;
  }


}