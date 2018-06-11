package org.grails.formbuilder

import java.io.Serializable;
import com.oneapp.cloud.core.*
/**
 *
 * @author <a href='mailto:admin@yourdomain.com'>Nikkishi</a>
 *
 * @since 0.1
 */
class FormAdmin implements Serializable{
	String formType
	boolean published = true
	boolean specialCondition=false
	String formLogin
	String formPassword
	String formSubmitMessage
	Date dateCreated
	Date lastUpdated
	String query
	Field statusField
	Boolean openForEdit
	List<String> blockUserEditing
	Form form
	WebHookDetails webHookDetails
	String formSubmissionAction = "Email"
	List<User> formSubmissionTo = new ArrayList()
	List<User> publishedWith
	List<NotifyCondition> conditions = new ArrayList()
	MailChimpDetails mailChimpDetails
	String shortURL
	String fieldRulesData
	String pageRulesData
	String redirectUrl
	Boolean searchable=true
	List<Long> formEmailField = new ArrayList()
	boolean showStatusToUser
	boolean trackChanges=false
	
	Boolean notiOnCreate = true
	Boolean notiOnUpdate = true
	
	String formSubmissionToExternal
	Boolean extNotiOnCreate = false
	Boolean extNotiOnUpdate = false
	
	Boolean confOnCreate = false
	Boolean confOnUpdate = false
	
	static hasMany = [formEmailField:Long,blockUserEditing: String,publishedWith:User,formSubmissionTo:User,conditions:NotifyCondition]
	 
	static constraints = {
		form nullable:false, blank: false
		formType nullable:true, blank:true, inList:['Approval', 'Poll', 'Survey', 'Master', 'Sub' ]
		formLogin nullable:true, blank:true, inList:['NoOne','Login', 'Public', 'Password']
		formPassword nullable:true, blank:true
		formSubmitMessage nullable:true, blank:true
		published nullable:true, blank:true
		query nullable:true, blank:true
		statusField nullable:true, blank:true
		openForEdit nullable:true
		blockUserEditing nullable:true
		webHookDetails nullable:true
		formSubmissionAction nullable:true
		formSubmissionTo nullable:true
		publishedWith nullable:true
		mailChimpDetails nullable:true
		conditions nullable:true
		shortURL nullable:true, blank:true
		redirectUrl nullable:true, blank:true
		showStatusToUser nullable:true, blank:true
		trackChanges nullable:false, blank:false
		fieldRulesData nullable:true, blank:true
		pageRulesData nullable:true, blank:true
		formEmailField nullable:true
		searchable nullable:true
		formSubmissionToExternal nullable:true
		notiOnCreate nullable:true
		notiOnUpdate nullable:true
		extNotiOnCreate nullable:true
		extNotiOnUpdate nullable:true
		confOnCreate nullable:true
		confOnUpdate nullable:true
	}    
    static belongsTo = [form:Form]
	
	static mapping = {
		fieldRulesData type:'text'
		pageRulesData type:'text'
		formSubmissionToExternal type:'text'
	}
}