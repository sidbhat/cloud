
package com.oneapp.cloud.core

class LinkToURLTagLib {


  static namespace = 'feed'
  def utilService
  
  def convertLinkToURL = { attrs ->
	 if(attrs?.item)
	 	if ( attrs?.type == "social")
  	    	out << utilService.convertLinkToURL(ActivityFeed.get(attrs?.item).feedContent)
  	    else if ( attrs?.type == "company")
  	   		out << utilService.convertLinkToURL(CompanyActivity.get(attrs?.item).feedContent)
  }
}
