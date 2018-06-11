package com.oneapp.cloud.core

import grails.plugin.multitenant.core.util.TenantUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import java.util.Properties;

import javax.mail.*;
import javax.mail.search.*
import javax.mail.internet.*
import org.codehaus.groovy.grails.commons.ConfigurationHolder
import com.sun.mail.imap.*
import com.sun.mail.imap.protocol.*
import com.oneapp.cloud.core.*
import com.macrobit.grails.plugins.attachmentable.services.*

//import com.sun.mail.imap.IMAPAddress
class InboxReaderService {

 static int attnum = 1;
 static transactional = false
 def utilService
 def attachmentableService
 def springSecurityService
 /*

  This method will retrieve 20 mails from gmail in 1 Minute
 * */
  def readMail(def search) {
 	Store store
    try {
      EmailSettings email = search.email
      Properties props = System.getProperties();
	  Session session;
      if(email.emailAddress == "Yahoo" || email.emailAddress == "Gmail"){
		  if(email.secureConnection){
			  props.setProperty("mail.imap.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
			  props.setProperty("mail.imap.socketFactory.fallback", "false");
			  props.setProperty("mail.imap.port", "993");
			  props.setProperty("mail.imap.socketFactory.port", "993");
			  props.setProperty("mail.imaps.class", "com.sun.mail.imap.IMAPSSLStore");
		  }
		  props.setProperty("mail.store.protocol", "imaps")
		  session = Session.getInstance(props, null);
		  store = session.getStore("imaps");
		  
	  }else{
		
		  props.setProperty("mail.store.protocol", "imap")
		  session = Session.getInstance(props, null);
		  store = session.getStore("imap");
	  }
	  store.connect("${email.popServerURL}", "${email.username}", "${email.password}");
      try {
          Folder inbox = store.getFolder("Inbox");
     	  inbox.open(Folder.READ_ONLY);
          return printAllMessageEnvelopes(inbox,true,search);
      } catch (IOException e) {
        println("InboxReaderService-readMail: "+e);
      }

    } catch (NoSuchProviderException e) {
      log.error e

    } catch (MessagingException e) {
      log.error e

    } catch (Exception e) {
      log.error e
    }
    finally {
    	disconnect(store)
    }
  }

	public void closeFolder(Folder folder) throws Exception {
        folder.close(false);
    }
    
    public int getMessageCount(Folder folder) throws Exception {
        return folder.getMessageCount();
    }
    
    public int getNewMessageCount(Folder folder) throws Exception {
        return folder.getNewMessageCount();
    }
    
    public void disconnect(Store store) throws Exception {
        store?.close();
    }


def printAllMessageEnvelopes(Folder folder,boolean inbox,def search) throws Exception {
        // Attributes & Flags for all messages ..
        
       
       ReceivedDateTerm term  = new ReceivedDateTerm(ComparisonTerm.GT,new Date()-search.daysPrior);
	   javax.mail.Message[] msgs = folder.search(term)
       HashMap<Sender> sender = new HashMap<Sender>()
        
        // Use a suitable FetchProfile
        FetchProfile fp = new FetchProfile();
        fp.add(FetchProfile.Item.ENVELOPE);        
        folder.fetch(msgs, fp);
        for (int i = 0; i < msgs?.length; i++) {
			if(msgs[i].envelope)
            	dumpEnvelope(msgs[i],sender,inbox);
        }
        return sender
        
    }

public static void dumpEnvelope(javax.mail.Message m, HashMap sender, boolean inbox) throws Exception {
	javax.mail.Address[] a;
	if ((a = m.getFrom()) != null) {
		for (int j = 0; j < a.length; j++){
			 String val
			 // Check if inbox or outbox results are summarized
			 if ( !inbox ){
			 // TO
				if ((a = m.getRecipients(javax.mail.Message.RecipientType.TO)) != null) {
					 val = a[0].toString() // use the first receipient
			  }
			  
			}else {
				val = a[j].toString()
			}
			String email = val
			def object = sender.get(email)
			if ( object == null ){
				def obj = new Sender()
				obj.name = email
				obj.email = email
				obj.emailCount=1
				obj.mostRecent = m.getSentDate()
				obj.totalSize = m.getSize()
				obj.largestSize = m.getSize()
				sender.put(email, obj)
			}else {
			
				object.emailCount= object.emailCount+1
				if ( object.mostRecent < m.getSentDate() ){
					object.mostRecent = m.getSentDate()
				}
				if ( object.largestSize < m.getSize() ){
					object.largestSize = m.getSize()
				}
				object.totalSize += m.getSize()
				sender.remove(email)
				sender.put(email, object)
			}
		}
	}
	
}
    
   
	
	def fetchEmailForAccount(def rule,def ruleSet){
		Store store
		try {
		  EmailSettings email = EmailSettings.get(Long.parseLong(rule.get(0).className))
		  Properties props = System.getProperties();
		  Session session;
	      if(email.emailAddress == "Yahoo" || email.emailAddress == "Gmail"){
			  props.setProperty("mail.store.protocol", "imaps")
			  session = Session.getInstance(props, null);
			  store = session.getStore("imaps");
		  }else{
			  props.setProperty("mail.store.protocol", "imap")
			  session = Session.getInstance(props, null);
			  store = session.getStore("imap");
		  }
		  store.connect("${email.popServerURL}", "${email.username}", "${email.password}");
		  try {
			  Folder inbox = store.getFolder("Inbox");
			   inbox.open(Folder.READ_ONLY);
			  return fetchAllEmailForSearch(inbox,rule,ruleSet);
		  } catch (IOException e) {
			println("InboxReaderService-fetchEmailForAccount: "+e);
		  }
	
		} catch (NoSuchProviderException e) {
		  log.error e
	
		} catch (MessagingException e) {
		  log.error e
	
		} catch (Exception e) {
		  log.error e
		}
		finally {
			disconnect(store)
		}
	}
	
	def fetchAllEmailForSearch(Folder folder,def rule,def ruleSet){
		def term = getTerm(0,rule)
		javax.mail.Message[] msgs = folder.search(term)
		FetchProfile fp = new FetchProfile();
		fp.add(FetchProfile.Item.ENVELOPE);
		folder.fetch(msgs, fp);
		counter = 0;
		for (int i = 0; i < msgs?.length; i++) {
			try{
				if(msgs[i].envelope)
					dumpPart(msgs[i], rule, ruleSet);
			}catch(Exception ex){
				log.error(ex)
			}
		}
	}
	
	def getTerm(int currentIndex,def rule){
		def currentSearchTerm
		def ruleInstance = rule.get(currentIndex)
		if(ruleInstance.attributeName == "Sender"){
			currentSearchTerm = new FromStringTerm(ruleInstance.value)
		}else if(ruleInstance.attributeName == "Subject"){
			currentSearchTerm = new SubjectTerm(ruleInstance.value)
		}else{
			def daysCount = (int)ruleInstance.value.toInteger()
			if(daysCount <= 0)
				daysCount = (int)ConfigurationHolder.config.reports.email.search.defaultDaysPrior
			else if(daysCount > 180)
				daysCount = (int)ConfigurationHolder.config.reports.email.search.maxDaysPrior
			currentSearchTerm = new ReceivedDateTerm(ComparisonTerm.GT,new Date()-daysCount)
		}
		if(currentIndex < rule.size()-1)
		{
			if(ruleInstance._condition == "AND"){
				return new AndTerm(currentSearchTerm,getTerm(currentIndex+1,rule))
			}
			else{
				return new OrTerm(currentSearchTerm,getTerm(currentIndex+1,rule))
			}
		}else{
			def dateRule = rule.find{it.attributeName == "Day Prior"}
			/*rule.each{
				if(it.attributeName == "Day Prior"){
					dateRule = true
				}
			}*/
			if(dateRule)
				return currentSearchTerm
			else
				return new AndTerm(currentSearchTerm,new ReceivedDateTerm(ComparisonTerm.GT,new Date()-(int)ConfigurationHolder.config.reports.email.search.defaultDaysPrior))
		}
		
	}
	def counter = 0;
	public void dumpPart(Part p,def rule,def ruleSet) throws Exception {
		def emailDetail = new EmailDetails()
		def msg = (javax.mail.Message)p
		def meesageId = msg.getMessageID().toString()
		def userInstance = User.findByUsername(ruleSet?.resultInstance)
		def currentTenantId = userInstance.userTenantId
		TenantUtils.doWithTenant(currentTenantId) {
			try{
				ActivityFeedConfig config = ActivityFeedConfig.findByConfigName(meesageId)
				if(config == null){
					def client = Client.get(currentTenantId)
					if(counter < client.maxEmailFetchCount){
						emailDetail.emailFrom = msg.getFrom()[0].toString()
						emailDetail.emailTo = (msg.getRecipients(javax.mail.Message.RecipientType.TO)).toString()
						emailDetail.subject = msg.getSubject()
						emailDetail.messageTime = msg.getReceivedDate()
						emailDetail.msgSize = msg.getSize()
						emailDetail.messageNumber = msg.getMessageNumber()
						emailDetail.ruleAccount = rule[0].className.toLong()
						Object content = p.getContent();
						if (content instanceof Multipart) {
							//emailDetail.attachmentName = getEmailFileName((Multipart)content)
							emailDetail.content = getEmailContent((Multipart)content)
						}
						else {
							//emailDetail.attachmentName = getFileNameInternal(p)
							emailDetail.content = getContentInternal(p)
						}
						if (!emailDetail.hasErrors() && emailDetail.save(flush:true)){
							def currentDate = new Date()
							def emailActivityFeedConfig = new ActivityFeedConfig()
							emailActivityFeedConfig = new ActivityFeedConfig(createdBy: ruleSet.createdBy, shareType: 'Email', configName: meesageId,className:meesageId, dateCreated: currentDate, lastUpdated: new Date())
							emailActivityFeedConfig.save(flush:true)
							def emailActivityFeed = new ActivityFeed()
							emailActivityFeed.config = emailActivityFeedConfig
							emailActivityFeed.shareId = emailDetail.id
							emailActivityFeed.createdBy = userInstance
							emailActivityFeed.feedState = ActivityFeed.ACTIVE
							emailActivityFeed.visibility = ActivityFeed.USER
							emailActivityFeed.dateCreated = currentDate
							emailActivityFeed.lastUpdated = currentDate
							emailActivityFeed.activityContent = "Email"
							if (!emailActivityFeed.hasErrors() && emailActivityFeed.save(flush:true)){
								emailActivityFeed.addTag(ruleSet.name)
								def fromAddress = msg.getFrom()[0].toString()
								def containsName = fromAddress.indexOf("<")
								def fromEmailId = null
								if(containsName == -1){
									fromEmailId = fromAddress
								}else{
								}
								if (content instanceof Multipart) {
									handleMultipart((Multipart)content,emailActivityFeed)
								}
								else {
									handlePart(p,emailActivityFeed)
								}
								def	infoMessage = "New email from "+msg.getFrom()[0].toString()
								def actionByPublic = User.findByUsername("publicuser@yourdomain.com")
								def activityNotification = new ActivityNotification(actionBy:actionByPublic,userFeedState:infoMessage,actionOnFeed:emailActivityFeed, actionTime:new Date())
								activityNotification.save(flush:true)
								
							}else{
								//Error in saving mailDetails
							}
							
						}
						
					}
				}
			}catch(Exception ex){
				log.error(ex)
			}
		}
	}
	
	
	/*def getFileNameInternal(Part part){
		String dposition = part.getDisposition();
		def fileName = null
		if (dposition == null) {
			
		}
		else if (dposition.equalsIgnoreCase(Part.ATTACHMENT)) {
			fileName = part.getFileName()
		}
		return fileName
	}
	
	def getEmailFileName(Multipart multipart) throws MessagingException, IOException {
		String fileName = null
		for (int i=0; i<multipart.getCount(); i++){
			def tempFileName = getFileNameInternal(multipart.getBodyPart(i))
			if(tempFileName)
				fileName = tempFileName
		}
		return fileName
	  }*/
	
	def getContentInternal(Part part){
		if (part.isMimeType("text/*")) {
            if(part.isMimeType("text/plain"))
			{
	            String s = (String)part.getContent();
				String newLineEmailContent = s.replaceAll("\n","</br>")
				String emailContent  = utilService.convertURLToLink(newLineEmailContent,true)
	            return URLEncoder.encode(emailContent,"UTF-8");
			}else{
				 String s = (String)part.getContent();
				 return URLEncoder.encode(s,"UTF-8");
			}
        }

        if (part.isMimeType("multipart/alternative")) {
            // prefer html text over plain text
            Multipart mp = (Multipart)part.getContent();
            String text = null;
            for (int i = 0; i < mp.getCount(); i++) {
                Part bp = mp.getBodyPart(i);
                if (bp.isMimeType("text/*")) {
                    if (text == null)
                        text = getContentInternal(bp);
                    continue;
                } else if (bp.isMimeType("text/html")) {
                    String s = getContentInternal(bp);
                    if (s != null)
                        return s;
                } else {
                    return getContentInternal(bp);
                }
            }
            return text;
        } else if (part.isMimeType("multipart/*")) {
            Multipart mp = (Multipart)part.getContent();
            for (int i = 0; i < mp.getCount(); i++) {
                String s = getContentInternal(mp.getBodyPart(i));
                if (s != null)
                    return s;
            }
        }

        return null;
	}
	
	def getEmailContent(Multipart multipart){
		String emailContent = null
		for (int i=0; i<multipart.getCount(); i++){
			def tempEmailContent = getContentInternal(multipart.getBodyPart(i))
			if(tempEmailContent != null){
				emailContent = tempEmailContent
				break;
			}
		}
		return emailContent
	}
	
	/*def downloadAttach(Long accountId,Long messageNumber){
		Store store
		try {
		  EmailSettings email = EmailSettings.get(accountId)
		  Properties props = System.getProperties();
		  Session session;
	      if(email.emailAddress == "Yahoo" || email.emailAddress == "Gmail"){
			  props.setProperty("mail.store.protocol", "imaps")
			  session = Session.getInstance(props, null);
			  store = session.getStore("imaps");
		  }else{
			  props.setProperty("mail.store.protocol", "imap")
			  session = Session.getInstance(props, null);
			  store = session.getStore("imap");
		  }
		  store.connect("${email.popServerURL}", "${email.username}", "${email.password}");
		  try {
			  Folder inbox = store.getFolder("Inbox");
			  inbox.open(Folder.READ_ONLY);
			  return emailAttachment(inbox,messageNumber);
		  } catch (IOException e) {
		  }
	
		} catch (NoSuchProviderException e) {
		  log.error e
	
		} catch (MessagingException e) {
		  log.error e
	
		} catch (Exception e) {
		  log.error e
		}
		finally {
			disconnect(store)
		}
	}
	
	def emailAttachment(Folder folder,Long messageNumber){
		javax.mail.Message[] msgs = folder.getMessages((int)messageNumber.toInteger())
		for (int i = 0; i < msgs?.length; i++) {
			Object content = msgs[i].getContent();
				if (content instanceof Multipart) {
					return handleMultipart((Multipart)content)
				}
				else {
					return handlePart(msgs[i])
				}
		}
	}*/
	
	def handleMultipart(Multipart multipart,def emailActivityFeed) throws MessagingException, IOException {
		def msgDetail = null
		for (int i=0; i<multipart.getCount(); i++){
			handlePart(multipart.getBodyPart(i),emailActivityFeed);
			/*def msgPart = handlePart(multipart.getBodyPart(i));
			if(msgPart)
				msgDetail = msgPart*/
		}
		//return msgDetail
	  }
	def handlePart(Part part,def emailActivityFeed)  throws MessagingException, IOException {
		def file = null
		String dposition = part.getDisposition();
		String cType = part.getContentType();
		if (dposition == null) {
			
		}
		else if (dposition.equalsIgnoreCase(Part.ATTACHMENT)) {
			attachmentableService.addEmailAttachment(ConfigurationHolder.config,springSecurityService.currentUser,emailActivityFeed,part.getFileName(),part.getInputStream(),cType)
			//file = saveFile(part.getFileName(),part.getInputStream());
		}
		//return file
		
	}
   
	/*def saveFile(String filename,InputStream input) throws IOException {
		 if (filename == null) {
            filename = File.createTempFile("MailAttacheFile", ".out").getName();
        }
        File file = new File(filename);
        for (int i=0; file.exists(); i++) {
            file = new File(filename+i);
        }
        FileOutputStream fos = new FileOutputStream(file);
        BufferedOutputStream bos = new BufferedOutputStream(fos);
        BufferedInputStream bis = new BufferedInputStream(input);
        int fByte;
        while ((fByte = bis.read()) != -1) {
            bos.write(fByte);
            }
        bos.flush();
        bos.close();
        bis.close();
        return file
	} */
}