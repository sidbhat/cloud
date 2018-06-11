

package com.oneapp.cloud.core
import java.io.Serializable;

import com.oneapp.unibox.auth.*


class Subscription implements Serializable{

	User follower
	String followClass
	Long followId
	String followCategory
	User followUser
	
	User createdBy
	Date dateCreated
	Date lastUpdated
	String state
	
	public static final String INACTIVE="INACTIVE"
	public static final String ACTIVE="ACTIVE"
   
	
	static constraints = {
		createdBy nullable:false, blank:false
		follower nullable:false, blank:false
		followClass nullable:true, blank:true
		followId nullable:true, blank:true
		followCategory nullable:true, blank:true
		followUser nullable:true, blank:true
		state nullable:true, blank:true,inList:[INACTIVE, ACTIVE]
	}
	
	static hasMany   = [ruleSet: RuleSet]
	static mapping = {
		ruleSet lazy: false, cascade: "all"
	}
	
	def getFollowInstance() {
         getClass().classLoader.loadClass(followClass).get(followId)
    }
   
   	// Some named queries
    static namedQueries = {
     findByFollowerAndClass { fid, followClass ->
     		and {
     			eq 'follower', fid
     			eq 'followClass', followClass
     		}
     		order('lastUpdated','desc')
      }
      
      findByFollower{ fid ->
     		and {
     			eq 'follower', fid
     		}
     		order('lastUpdated','desc')
      }
      findByClass {  followClass ->
     		and {
     			eq 'followClass', followClass
     		}
     		order('lastUpdated','desc')
      }
      
  	}
   
}