package com.oneapp.cloud.core


import java.io.Serializable;

import grails.plugin.multitenant.core.groovy.compiler.MultiTenant;

@MultiTenant
class UserAppAccessDetail implements Serializable{

   String ipAddress
   String accessType
   String accessMode
   String accessedClass
   String action
   String location
   Date accessTime
   User user
 
   public static final String BROWSER="Browser"
   public static final String DEVICE="Device"
   public static final String IPHONE="IPhone"
   public static final String IPAD="IPad"
   public static final String ANDROID="Android"
   public static final String IE="IE"
   public static final String MOZILLA="Mozilla"
   public static final String SAFARI="Safari"
   public static final String CHROME="Chrome"
   public static final String MACCHROME="Mac_Chrome"
   public static final String MACSAFARI="Mac_Safari"
   public static final String VIEW="View"
   public static final String CREATE="Create"
   
   static constraints = {
        ipAddress nullable: true, blank: true
        accessType nullable: true, blank: true,inList:[BROWSER,DEVICE]
        accessMode nullable: true, blank: true,inList:[IPHONE,IPAD,ANDROID,IE,MOZILLA,SAFARI,CHROME,MACCHROME,MACSAFARI]
		accessedClass nullable: true, blank: true
        action nullable: true, blank: true,inList:[VIEW,CREATE]
		location nullable: true, blank: true
		user nullable: true, blank: true
		accessTime nullable: true, blank: true
    }
   
}