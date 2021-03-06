
package com.oneapp.cloud.core

import grails.plugins.springsecurity.Secured

@Secured("ROLE_USER")
class ProfileController {
	
	def springSecurityService

	def index = {
		
		def userprofile = UserProfile.findWhere (user:springSecurityService.currentUser.username)
		render template:'index', model: [userprofile:userprofile]		
	}
	
	def add = {

		def email = params['email']
		def homePhone = params['homephone']
		def mobile = params['mobile']
		def officePhone = params['officephone']
		def dob = params['dob']
		def hireDate = params['hiredate']
		def homeAddress = params['homeaddress']
		def officeAddress = params['officeaddress']
		def companyName = params['companyname']
		def designation = params['designation']
		
		//println 'email' + email + '      homePhone' + homePhone + '      mobile' + mobile + '     officePhone' + officePhone + '    dob' + dob + '    hireDate' + hireDate + '    homeAddress' + homeAddress + '    officeAddress' + officeAddress + '    companyName' + companyName + '	designation' + designation
		
		def userprofile = UserProfile.findWhere (user:springSecurityService.currentUser.username)

		if(userprofile == null){
			def userProfile = new UserProfile()
			userProfile.email = email
			userProfile.homePhone = homePhone
			userProfile.mobile = mobile
			userProfile.officePhone = officePhone
			userProfile.dateOfBirth = dob
			userProfile.hireDate = hireDate
			userProfile.homeAddress = homeAddress
			userProfile.officeAddress = officeAddress
			userProfile.companyName = companyName
			userProfile.designation = designation
			userProfile.user = springSecurityService.currentUser.username
			
			UserProfile.withTransaction {
				userProfile = userProfile.save( flush : true )
			}
			//println "Saved: " + userProfile
		}else {		
				UserProfile.executeUpdate("update UserProfile a set a.email= ?, a.homePhone = ?, a.mobile = ?, a.officePhone = ?, a.dateOfBirth = ?, a.hireDate = ?, a.homeAddress = ?, a.officeAddress = ?, a.companyName = ?, a.designation = ? where a.user = ?", [email,homePhone,mobile,officePhone,dob,hireDate,homeAddress,officeAddress,companyName,designation,springSecurityService.currentUser.username])
			}	
		redirect(action:'index')
	}
}
