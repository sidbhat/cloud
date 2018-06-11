package com.oneapp.cloud.core

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryPoolMXBean;

import org.quartz.SimpleTrigger;
import org.quartz.Trigger;
import org.quartz.impl.calendar.BaseCalendar;

class ApplicationConfController {

    def index = { redirect(action: "edit") }
	def quartzScheduler
	def domainClassService
	def sessionRegistry
    // the delete, save and update actions only accept POST requests
    static allowedMethods = [update: "POST"]

    def show = {
        redirect(action:'edit')
    }

    def edit = {
        def applicationConfInstance = ApplicationConf.get(1)
		def cnt = 0

        sessionRegistry.getAllPrincipals().each{
            cnt += sessionRegistry.getAllSessions(it, false).size()
        }  
		def activeSession = cnt  
		def permGenMem = ManagementFactory.getMemoryPoolMXBeans()
		def formsLoaded = domainClassService.formsLoaded.size()
        if (!applicationConfInstance) {
            flash.message = "applicationConf.not.found"
            flash.args = [1]
            flash.defaultMessage = "ApplicationConf not found"
            redirect(controller:'dashboard',action:'index')
        }
        else {
            return [applicationConfInstance: applicationConfInstance,formsLoaded:formsLoaded,permGenMem:permGenMem,activeSession:activeSession]
        }
    }

    def update = {
        def applicationConfInstance = ApplicationConf.get(1)
		def cnt = 0
		
		sessionRegistry.getAllPrincipals().each{
			cnt += sessionRegistry.getAllSessions(it, false).size()
		}
		def activeSession = cnt
		def permGenMem = ManagementFactory.getMemoryPoolMXBeans()
		def formsLoaded = domainClassService.formsLoaded.size()
        if (applicationConfInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (applicationConfInstance.version > version) {
                    
                    applicationConfInstance.errors.rejectValue("version", "applicationConf.optimistic.locking.failure", "Another user has updated this ApplicationConf while you were editing")
                    render(view: "edit", model: [applicationConfInstance: applicationConfInstance,formsLoaded:formsLoaded,permGenMem:permGenMem,activeSession:activeSession])
                    return
                }
            }
			applicationConfInstance.copyForms = []
			applicationConfInstance.copyFormsTrial = []
            applicationConfInstance.properties = params
            if (!applicationConfInstance.hasErrors() && applicationConfInstance.save()) {
				try{
					if(applicationConfInstance.asynEmailJobInterval){
						ArrayList<Object> triggers = quartzScheduler.getTriggersOfJob('SendAsyncEmailJob','SendAsyncEmailJobGroup')
						SimpleTrigger trigger = (SimpleTrigger)triggers.find{it.name == "simpleTriggerAsyncEmail"}
						trigger?.repeatInterval = applicationConfInstance.asynEmailJobInterval
						quartzScheduler.rescheduleJob(trigger.name, trigger.group, trigger)
					}
					if(applicationConfInstance.rulesJobInterval){
						ArrayList<Object> triggers = quartzScheduler.getTriggersOfJob('RulesJob','RulesJobGroup')
						SimpleTrigger trigger = (SimpleTrigger)triggers.find{it.name == "rulesJob"}
						trigger?.repeatInterval = applicationConfInstance.rulesJobInterval
						quartzScheduler.rescheduleJob(trigger.name, trigger.group, trigger)
					}
				}catch(Exception e){
					log.error e
				}
                flash.message = "applicationConf.updated"
                flash.args = ['']
                flash.defaultMessage = "ApplicationConf updated"
                redirect(action: "edit")
            }
            else {
                render(view: "edit", model: [applicationConfInstance: applicationConfInstance,formsLoaded:formsLoaded,permGenMem:permGenMem,activeSession:activeSession])
            }
        }
        else {
            flash.message = "applicationConf.not.found"
            flash.args = [1]
            flash.defaultMessage = "ApplicationConf not found"
            redirect(action: "edit")
        }
    }
}
