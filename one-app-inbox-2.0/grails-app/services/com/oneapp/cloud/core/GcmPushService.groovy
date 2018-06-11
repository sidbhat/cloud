package com.oneapp.cloud.core

import grails.plugin.multitenant.core.util.TenantUtils;
import groovy.lang.Closure;

import java.util.Map;

import com.google.android.gcm.server.Message
import com.google.android.gcm.server.MulticastResult
import com.google.android.gcm.server.Result
import com.google.android.gcm.server.Sender
class GcmPushService {

	static transactional = false
	/**
	 * 
	 * @param currentUser current Users GCM data
	 * @return map of the data
	 */
	def gcmConfig(User currentUser){
		return [apiKey:Client.get((currentUser.userTenantId).toLong())?.gcmApiKey, retries:2, delay:false , timeToGetLive:1200]
	}
	/**
	 * Add and Update user device
	 * @param deviceId A device deviceId 
	 * @param Token A device token 
	 * @param currentUser current User
	 * @return Device Object
	 */
	def addDevice(String deviceId,String token,User currentUser){
		TenantUtils.doWithTenant(currentUser.userTenantId){
			Device getDevice= Device.findByDeviceId(deviceId);
			if(!getDevice)
				getDevice=new Device(deviceId:deviceId)
			getDevice.token=token
			getDevice.deviceUser=currentUser
			if(!getDevice.hasErrors() && getDevice.save(flush:true))
				return getDevice;
			return null;
		}
	}
	/**
	 * delete all devices of the user
	 * 
	 * @param currentUser
	 * @return TRUE/False
	 */
	def removeDevice(User currentUser){
		TenantUtils.doWithTenant(currentUser.userTenantId){
			def currentUserDeviceList=currentUserDevices(currentUser);
			try{
				currentUserDeviceList?.each{Device device->
					device.delete()
				}
				return false
			}catch (Exception e) {
				print "Error e push noti removeDevice"+e.message
			}
		}
		return false;
	}
	/**
	 * users all devices
	 * @param currentUser
	 * @return
	 */
	def currentUserDevices(User currentUser){
		def currentUserDeviceList=[]
		currentUserDeviceList=Device.findAllByDeviceUser(currentUser);
	}
	def sendNotificationToUser(User currentUser,String mgsBody){
		def configr=gcmConfig(currentUser)
		if(configr && configr."apiKey"){
			try{
				String key=configr."apiKey"
				def toDevices=currentUserDevices(currentUser)
				toDevices?.each {
					sender(key)?.send(buildMessage(["note" : mgsBody],'',configr."timeToGetLive",configr."delay"), it.token , configr."retries")
				}
				return true;
			} catch (Exception e) {
				println "push send mgs error "+e.message 
			}
		}
		return false;
	}
	private sender(apiKey) {
		new Sender(apiKey)
	}
	private Message buildMessage(Map data, String collapseKey = '',def timeToGetLive,def delay=false) {
		withMessageBuilder(delay,data) { messageBuilder ->
			if (collapseKey) {
				messageBuilder.collapseKey(collapseKey).timeToLive(timeToGetLive)
			}
		}
	}

	private Message withMessageBuilder(def delay=false ,Map messageData, Closure builderConfigurator = null) {
		Message.Builder messageBuilder = new Message.Builder().delayWhileIdle(delay)
		if (builderConfigurator) {
			builderConfigurator(messageBuilder)
		}
		addData(messageData, messageBuilder).build()
	}
	private addData(Map data, Message.Builder messageBuilder) {
		data.each {
			messageBuilder.addData(it.key, it.value)
		}
		return messageBuilder
	}
}
